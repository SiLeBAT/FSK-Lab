package de.bund.bfr.knime.fsklab.nodes;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.NodeLogger;
import org.knime.core.util.FileUtil;
import de.bund.bfr.knime.fsklab.nodes.plot.ModelPlotter;
import de.bund.bfr.knime.fsklab.v1_9.FskPortObject;
import de.bund.bfr.knime.fsklab.v1_9.FskSimulation;
import de.bund.bfr.knime.fsklab.v1_9.runner.RunnerNodeInternalSettings;
import de.bund.bfr.knime.fsklab.v1_9.runner.RunnerNodeModel;
import de.bund.bfr.knime.fsklab.v1_9.runner.RunnerNodeSettings;
import de.bund.bfr.metadata.swagger.Parameter;
import de.bund.bfr.metadata.swagger.Parameter.ClassificationEnum;
import metadata.SwaggerUtil;

public abstract class ScriptHandler implements AutoCloseable {

  protected ModelPlotter plotter;

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
   * @param LOGGER A {@link NodeLogger} that keeps track of the progress during the execu-tion of
   *        the node
   * @param internalSettings internal settings to store the image file of the plot
   * @param nodeSettings settings of the node containing the dimensions of the output im-age
   * @throws Exception
   */
  public final void runSnippet(final FskPortObject fskObj, final FskSimulation simulation,
      final ExecutionContext exec, NodeLogger LOGGER, File imageFile) throws Exception {
    
    // Sets up working directory with resource files. This directory needs to be deleted.
    exec.setProgress(0.05, "Add resource files");
    Optional<Path> workingDirectory;
    if (fskObj.getEnvironmentManager().isPresent()) {
      workingDirectory = fskObj.getEnvironmentManager().get().getEnvironment();
    } else {
      // @Miguel: this is a workaround. But we ALWAYS need a working directory, so I suggest to 
      // change the environment manager to always create some sort of default directory.
         //workingDirectory = Optional.of(FileUtil.createTempDir("defaultWorkingDirectory").toPath());
      workingDirectory = Optional.empty();
    }

    if (workingDirectory.isPresent()) {
      setWorkingDirectory(workingDirectory.get(), exec);
    }else {
      workingDirectory = Optional.of(Files.createTempDirectory("workingDirectory"));
      setWorkingDirectory(workingDirectory.get(), exec);
    }
    

    // START RUNNING MODEL
    exec.setProgress(0.1, "Setting up output capturing");
    setupOutputCapturing(exec);

    // Install needed libraries
    if (!fskObj.packages.isEmpty()) {
      installLibs(fskObj, exec, LOGGER);
    }

    exec.setProgress(0.72, "Set parameter values");
    LOGGER.info(" Running with '" + simulation.getName() + "' simulation!");

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

    exec.setProgress(0.75, "Run models script");
    runScript(fskObj.getModel(), exec, false);

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
    // convertToKnimeDataTable(fskObj,exec);

    try {
      plotter.plotSvg(imageFile, fskObj.getViz());

      // Save path of generated plot
      fskObj.setPlot(imageFile.getAbsolutePath());
    } catch (final Exception exception) {
      LOGGER.warn("Visualization script failed", exception);
    }

    exec.setProgress(0.96, "Restore library paths");
    restoreDefaultLibrary();

    exec.setProgress(0.98, "Collecting captured output");
    finishOutputCapturing(exec);

    saveWorkspace(fskObj, exec);
    
  
    // Save generated resources
    if (workingDirectory.isPresent()) {
      File workingDirectoryFile = workingDirectory.get().toFile();
      saveGeneratedResources(fskObj, workingDirectoryFile, exec.createSubExecutionContext(1));
    }
    
    // delete working directory
    if (fskObj.getEnvironmentManager().isPresent() && workingDirectory.isPresent()) {
      fskObj.getEnvironmentManager().get().deleteEnvironment(workingDirectory.get());
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
        handler = new PythonScriptHandler();
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
  public abstract void setWorkingDirectory(Path workingDirectory, ExecutionContext exec) throws Exception;

  /**
   * Needed if the interpreter requires specific code necessary for starting output captur-ing.
   * 
   * @param exec KNIME way of managing storage and information output during the current NodeModels
   *        execution
   * @throws Exception
   */
  public abstract void setupOutputCapturing(ExecutionContext exec) throws Exception;

  /**
   * Needed if interpreter requires specific code for retrieving captured output
   * 
   * @param exec KNIME way of managing storage and information output during the current NodeModels
   *        execution
   * @throws Exception
   */
  public abstract void finishOutputCapturing(ExecutionContext exec) throws Exception;

  /**
   * Set up the way in which Eclipse and the script interpreter communicate.* @param exec KNIME way
   * of managing storage and information output during the current NodeModels execution
   * 
   * @throws Exception
   */
  // public abstract void setController(ExecutionContext exec) throws Exception;


  public abstract String[] runScript(String script, ExecutionContext exec, Boolean showErrors)
      throws Exception;

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
  public abstract void installLibs(final FskPortObject fskObj, ExecutionContext exec, NodeLogger LOGGER)
      throws Exception;

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
  
  private void saveGeneratedResources(FskPortObject fskPortObject, File workingDirectory, ExecutionContext exec) {

    // Delete previous resources if they exist
    fskPortObject.getGeneratedResourcesDirectory().ifPresent(directory -> {
      if (directory.exists()) {
        FileUtil.deleteRecursively(directory);
      }
    });

    List<Parameter> parameterMetadata = SwaggerUtil.getParameter(fskPortObject.modelMetadata);

    if (parameterMetadata == null) {
      return;
    }

    List<String> outputFileParameterIds = parameterMetadata.stream()
        .filter(currentParameter -> currentParameter.getClassification() == Parameter.ClassificationEnum.OUTPUT
        && currentParameter.getDataType() == Parameter.DataTypeEnum.FILE)
        .map(Parameter::getId).collect(Collectors.toList());

    // Get filenames out of the output file parameter values
    String command = "c(" +  String.join(", ", outputFileParameterIds) + ")";

    try {
      String[] filenames = runScript(command, exec, true);

      // Copy every resource from the working directory
      File newResourcesDirectory = FileUtil.createTempDir("generatedResources");
      for (String filename : filenames) {
        File sourceFile = new File(workingDirectory, filename);
        File targetFile = new File(newResourcesDirectory, filename);
        FileUtil.copy(sourceFile, targetFile, exec);
      }
      
      fskPortObject.setGeneratedResourcesDirectory(newResourcesDirectory);
    } catch (Exception e) {
    }
  }
}
