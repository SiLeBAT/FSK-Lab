package de.bund.bfr.knime.fsklab.nodes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.NodeLogger;
import org.knime.core.util.FileUtil;
import org.rosuda.REngine.REXPMismatchException;
import de.bund.bfr.knime.fsklab.FskErrorMessages;
import de.bund.bfr.knime.fsklab.JsonFileNotFoundException;
import de.bund.bfr.knime.fsklab.ModelScriptException;
import de.bund.bfr.knime.fsklab.ResourceFileNotFoundException;
import de.bund.bfr.knime.fsklab.VariableNotGlobalException;
import de.bund.bfr.knime.fsklab.nodes.plot.ModelPlotter;
import de.bund.bfr.knime.fsklab.r.client.ScriptExecutor;
import de.bund.bfr.knime.fsklab.preferences.PreferenceInitializer;
import de.bund.bfr.knime.fsklab.python2.PythonVersion;
import de.bund.bfr.knime.fsklab.r.client.IRController.RException;
import de.bund.bfr.knime.fsklab.v2_0.FskPortObject;
import de.bund.bfr.knime.fsklab.v2_0.FskSimulation;
import de.bund.bfr.knime.fsklab.v2_0.JoinRelationAdvanced;
import de.bund.bfr.knime.fsklab.v2_0.runner.RunnerNodeInternalSettings;
import de.bund.bfr.knime.fsklab.v2_0.runner.RunnerNodeModel;
import de.bund.bfr.knime.fsklab.v2_0.runner.RunnerNodeSettings;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum;
import metadata.SwaggerUtil;
public abstract class ScriptHandler implements AutoCloseable {

  protected ModelPlotter plotter;
  protected JsonHandler jsonHandler;
  private boolean saveToJsonChecked = false;
  /**
   * Setter method to decide if parameters should be written to JSON 
   * @param saveToJsonChecked the value from the Runner "save to 
   * json file" checkbox.
   */
  public final void setSaveToJsonChecked(boolean saveToJsonChecked) {
    this.saveToJsonChecked = saveToJsonChecked;
  }
  /**
   * This template method runs a snippet of script code. It does not save the stdOutput or the
   * stdErrOutput. After running this method, "cleanup" has to be called in order to close the
   * communication with the script-interpreter.
   *
   *
   * @param fskObj A {@link FSKPortObject} containing the model scripts
   * @param simulation Part of {@link FSKPortObject} which contains the parameter list of the chosen
   *        simulation
   * @param exec KNIME way of managing storage and information output during the current NodeModels
   *        execution
   * @param logger A {@link NodeLogger} that keeps track of the progress during the execu-tion of
   *        the node
   * @param internalSettings internal settings to store the image file of the plot
   * @param nodeSettings settings of the node containing the dimensions of the output im-age
   * @throws Exception
   */
  public final void runSnippet(final FskPortObject fskObj, final FskSimulation simulation,
      final ExecutionContext exec, NodeLogger logger, File imageFile,
      List<JoinRelationAdvanced> joinRelationList, String suffix) throws Exception {

    // Sets up working directory with resource files. This directory needs to be deleted.
    exec.setProgress(0.05, "Add resource files");
    Path workingDirectory;
    if (fskObj.getEnvironmentManager().isPresent()) {
      workingDirectory = fskObj.getEnvironmentManager().get().getEnvironment().get();
    } else {
      workingDirectory = FileUtil.createTempDir("tempResourceFiles",new File(PreferenceInitializer.getFSKWorkingDirectory())).toPath();

    }
    setWorkingDirectory(workingDirectory, exec);

    // START RUNNING MODEL
    exec.setProgress(0.1, "Setting up output capturing");
    setupOutputCapturing(exec);

    // Install needed libraries & set path to .fsk folder
    installLibs(fskObj, exec, logger);
    
    exec.setProgress(0.3, "create JsonHandler");
    jsonHandler = JsonHandler.createHandler(this, exec);
    exec.setProgress(0.4, "apply Join Relations");

    // Performance Test: how long to load json data into workspace
    Instant start = Instant.now();
    jsonHandler.applyJoinRelation(fskObj, joinRelationList, suffix);
    Instant end = Instant.now();
    Duration timeElapsed = Duration.between(start, end);
    if(saveToJsonChecked) {
      logger.info("time to load json file into workspace: "+timeElapsed.getSeconds() + "s\n" );  
    }
    

   

    exec.setProgress(0.72, "Set parameter values");
    if(logger.isInfoEnabled()) {
      logger.info("Running with '" + simulation.getName() + "' simulation! " + simulation.getParameters().toString());
    } else {
      logger.info("Running with '" + simulation.getName() + "' simulation! " + simulation.getParameters().toString());  
    }
    

    // load libraries before (python) parameters are evaluated

    // Dirty workaround. Only execute simulation if there are parameters configured.
    if (!simulation.getParameters().isEmpty()) {
      String paramScript = buildParameterScript(simulation);
      Arrays.stream(fskObj.getModel().split("\\r?\\n")).filter(id -> id.startsWith("import"))
          .forEach(line -> {
            try {
              runScript(line, exec, false);

            } catch (Exception e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
          });
      runScript(paramScript, exec, false);
    }


    // JsonHandler stores all input parameters before model execution

    if(saveToJsonChecked) {
      jsonHandler.saveInputParameters(fskObj, workingDirectory);
    }
      

    exec.setProgress(0.75, "Run models script");
    try {
      runScript(fskObj.getModel(), exec, false);
      finishOutputCapturing(exec);
      setupOutputCapturing(exec);
    } catch (RException | IOException e) {
      throw new ModelScriptException(e.getMessage());
    } finally {
      if(!getStdErr().isEmpty()) {
        if(getStdErr().contains(ScriptExecutor.ERROR_PREFIX)) { // needed because warnings should be ignored
          throw new ModelScriptException(getStdErr());  
        }
      }
    }
    
    if (RunnerNodeModel.isTest) {
      List<Parameter> parameters = SwaggerUtil.getParameter(fskObj.modelMetadata);
      for (Parameter param : parameters) {
        if (param.getClassification() == ClassificationEnum.OUTPUT) {
          String[] value = runScript("eval(" + param.getId() + ")", exec, true);
          param.setValue(value[0]);
        }
      }
    }

    exec.setProgress(0.9, "Run visualization script");


    try {
      plotter.plotSvg(imageFile, fskObj.getViz());

      // Save path of generated plot
      fskObj.setPlot(imageFile.getAbsolutePath());
    } catch (final Exception exception) {
      logger.warn("Visualization script failed", exception);
    }
    
    // HDFHandler stores all ouput parameters in HDF file
    if(saveToJsonChecked) {
      exec.setProgress(0.92, "Save output parameters");
      jsonHandler.saveOutputParameters(fskObj, workingDirectory);
    }
      

    // Save generated resources
    exec.setProgress(0.92, "Save generated resources");
    saveGeneratedResources(fskObj, workingDirectory.toFile(), exec.createSubExecutionContext(1));

    exec.setProgress(0.96, "Restore library paths");
    restoreDefaultLibrary();

    exec.setProgress(0.98, "Collecting captured output");
    finishOutputCapturing(exec);

    saveWorkspace(fskObj, exec);


  

    // delete environment directory (workingDirectory is always present)
    if (fskObj.getEnvironmentManager().isPresent()) {
      fskObj.getEnvironmentManager().get().deleteEnvironment(workingDirectory);
    } else {
      // delete temporary working directory
      FileUtil.deleteRecursively(workingDirectory.toFile());
    }

  }

  public abstract void convertToKnimeDataTable(FskPortObject fskObj, ExecutionContext exec)
      throws Exception;

  public static ScriptHandler createHandler(String script_type, List<String> packages)
      throws Exception {

    final ScriptHandler handler;

    if (script_type == null) {
      handler = new RScriptHandler(packages);
    } else {
      final String type = script_type.toLowerCase();
      if (type.startsWith("r")) {
        handler = new RScriptHandler(packages);
      } else if (type.startsWith("py")) {
        if (type.startsWith("python 2")) {
          handler = new PythonScriptHandler(PythonVersion.PYTHON2);
        } else if (type.startsWith("python 3")) {
          handler = new PythonScriptHandler(PythonVersion.PYTHON3);
        } else {
          handler = new PythonScriptHandler(); // use version from KNIME preference page
        }
      } else {
        handler = new RScriptHandler();
      }
    }

    return handler;
  }


  /**
   * Set the directory in which the interpreter can temporarily save data while executing the script
   * 
   * @param workingDirectory The directory in which the script code is executed
   * @throws Exception if an error occurs accessing the directory
   */
  public abstract void setWorkingDirectory(Path workingDirectory, ExecutionContext exec)
      throws Exception;

  /**
   * Needed if the interpreter requires specific code necessary for starting output captur-ing.
   * 
   * @param exec KNIME way of managing storage and information output during the current NodeModels
   *        execution
   * @throws Exception
   */
  public abstract void setupOutputCapturing(ExecutionContext exec) throws RException, CanceledExecutionException, InterruptedException;

  /**
   * Needed if interpreter requires specific code for retrieving captured output
   * 
   * @param exec KNIME way of managing storage and information output during the current NodeModels
   *        execution
   * @throws Exception
   */
  public abstract void finishOutputCapturing(ExecutionContext exec) throws RException, CanceledExecutionException, InterruptedException;

  /**
   * Set up the way in which Eclipse and the script interpreter communicate.* @param exec KNIME way
   * of managing storage and information output during the current NodeModels execution
   * 
   * @throws Exception
   */
  // public abstract void setController(ExecutionContext exec) throws Exception;


  public abstract String[] runScript(String script, ExecutionContext exec, Boolean showErrors)
      throws RException, CanceledExecutionException, InterruptedException, REXPMismatchException, IOException;

  /**
   * install library packages necessary for running the scripts
   * 
   * @param fskObj A {@link FSKPortObject} containing the model scripts
   * @param exec KNIME way of managing storage and information output during the current NodeModels
   *        execution
   * @param LOGGER A {@link NodeLogger} that keeps track of the progress during the execu-tion of
   *        the node
   * @throws Exception
   */
  public abstract void installLibs(final FskPortObject fskObj, ExecutionContext exec,
      NodeLogger LOGGER) throws Exception;

  /**
   *
   * @param simulation Part of {@link FSKPortObject} which contains the parameter list of the chosen
   *        simulation
   * @return A script in the concrete language containing a list of parameters and their values
   *         (e.g. "x <- 1.0 \n id <- 'model1'")
   */
  public abstract String buildParameterScript(final FskSimulation simulation);

  /**
   *
   * @param internalSettings internal settings to store the image file of the plot
   * @param nodeSettings settings of the node containing the dimensions of the output im-age
   * @param fskObj A {@link FSKPortObject} containing the model scripts
   * @param exec KNIME way of managing storage and information output during the current NodeModels
   *        execution
   * @throws Exception
   */
  public abstract void plotToImageFile(final RunnerNodeInternalSettings internalSettings,
      RunnerNodeSettings nodeSettings, final FskPortObject fskObj, ExecutionContext exec)
      throws Exception;

  /**
   * Restore library trees to the default library.
   * 
   * @throws Exception
   */
  public abstract void restoreDefaultLibrary() throws Exception;

  /**
   * Save the workspace in the session to a file linked to by a path in the
   * {@link FSKPor-tObject.workspace}.
   *
   * @param fskObj A {@link FSKPortObject} containing the model scripts
   * @param exec KNIME way of managing storage and information output during the current NodeModels
   *        execution
   * @throws Exception
   */
  public abstract void saveWorkspace(final FskPortObject fskObj, ExecutionContext exec)
      throws Exception;

  /**
   ** @return The output generated by the last {@link #runScript(String, ExecutionContext)} call.
   */
  public abstract String getStdOut();

  /**
   *
   * @return The error output generated by the last {@link #runScript(String, ExecutionCon-text)}
   *         call.
   */
  public abstract String getStdErr();

  /**
   * Cleanup temporary variables, which were created during output capturing and execution
   * 
   * @param exec KNIME way of managing storage and information output during the current NodeModels
   *        execution
   * @throws Exception
   */
  public abstract void cleanup(ExecutionContext exec) throws Exception;

  /**
   * Returns the script code to have the interpreter acquire the version number of a pack-age
   * 
   * @param pkg_name The name of the package
   * @return The command have the interpreter return the version of the package (e.g. in R:
   *         "packageDescription(\"RServe")$Version"
   */
  public abstract String getPackageVersionCommand(String pkg_name);

  /**
   * Returns the script code to have the interpreter acquire the version number of all available
   * packages (e.g. in R: available.packages(...))
   * 
   * @param pkg_names a list of packages whose version need to be determined
   * @return the command to have the interpreter return a list of available packages
   */
  public abstract String getPackageVersionCommand(List<String> pkg_names);

  /**
   * @return The file extension of script files of the specific language (e.g. in R: return value
   *         would be "r" )
   */
  public abstract String getFileExtension();

  /**
   * Creates the string command in R or Python to return a java String array with the values of the
   * passed variables. For example:
   * <ul>
   * <li>c(0, 1, 2) should return [0, 1, 2] when called in R.
   * <li>[0, 1, 2] should return [0, 1, 2] when called in Python.
   * </ul>
   * 
   * @param items List of strings
   * @return Command in the script language to retrieve a vector with the values of the passed
   *         variables.
   */
  protected abstract String createVectorQuery(List<String> variableNames);

  private void saveGeneratedResources(FskPortObject fskPortObject, File workingDirectory,
      ExecutionContext exec)
      throws ResourceFileNotFoundException, JsonFileNotFoundException, VariableNotGlobalException {



    List<Parameter> parameterMetadata = SwaggerUtil.getParameter(fskPortObject.modelMetadata);

    if (parameterMetadata == null) {
      return;
    }

    // save all FILE Parameters since they might be joined with another model later
    List<String> outputFileParameterIds = parameterMetadata.stream()
        .filter(currentParameter -> currentParameter.getDataType() == Parameter.DataTypeEnum.FILE)
        .map(Parameter::getId).collect(Collectors.toList());

    // Get filenames out of the output file parameter values
    String command = createVectorQuery(outputFileParameterIds);

    try {
      File newResourcesDirectory = FileUtil.createTempDir("generatedResources",new File(PreferenceInitializer.getFSKWorkingDirectory()));

      if (!command.equals("c()") && !command.equals("print([])")) {
        try {
          String[] filenames = runScript(command, exec, true);

          // Copy every resource from the working directory
          for (String filename : filenames) {
            try {
              // file parameters can come from another generatedResourceDirectory
              // from another model; in that case we use that path since it is
              // not present in the current workingDirectory
              Path sourceDir = workingDirectory.toPath();
              Path source = Paths.get(filename.trim());
              // if a file variable has a path already attached, use that instead
              File sourceFile = sourceDir.resolve(source).toFile();
              File targetFile = new File(newResourcesDirectory, source.getFileName().toString());
              FileUtil.copy(sourceFile, targetFile, exec);
            } catch (CanceledExecutionException | IOException e) {
              // we only warn the user that a resource file was not found
              // if the file is actually needed later, we will throw an exception then
              FskErrorMessages.resourceFileNotFoundWarning(filename);
            }
          }
        } catch (REXPMismatchException | IOException | RException | InterruptedException e) {

          FskErrorMessages.variableAccessWarning(command, 
              SwaggerUtil.getModelId(fskPortObject.modelMetadata));

          if (saveToJsonChecked) {
            throw new VariableNotGlobalException(command,
                SwaggerUtil.getModelId(fskPortObject.modelMetadata));  
          } 
        }
      }

      // Save JSON file
      if (saveToJsonChecked) {
        File sourceFile = new File(workingDirectory, JsonHandler.JSON_FILE_NAME);
        File targetFile = new File(newResourcesDirectory, JsonHandler.JSON_FILE_NAME);
        FileUtil.copy(sourceFile, targetFile, exec);
      }
      fskPortObject.setGeneratedResourcesDirectory(newResourcesDirectory);

    } catch (CanceledExecutionException | IOException e) {
      throw new JsonFileNotFoundException(workingDirectory.toString());
    }

    // Save Plot in generatedResouces Folder (SubPlot for Combined Models)    
    try {
      if(!fskPortObject.getPlot().isBlank() && fskPortObject.getGeneratedResourcesDirectory().isPresent()) {
        File sourcePlot = new File(fskPortObject.getPlot());
        String targetPlotName =  (SwaggerUtil.getModelId(fskPortObject.modelMetadata) != null) 
            ? SwaggerUtil.getModelId(fskPortObject.modelMetadata) + ".svg" 
                : "subModelPlot.svg";
        File targetPlot = new File(fskPortObject.getGeneratedResourcesDirectory().get(), targetPlotName);
        FileUtil.copy(sourcePlot, targetPlot, exec);  
      }
    } catch(IOException | CanceledExecutionException e) {
      throw new ResourceFileNotFoundException(fskPortObject.getPlot());
    }
  }
}
