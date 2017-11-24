/***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Contributors: Department Biological Safety - BfR
 **************************************************************************************************/

package de.bund.bfr.knime.fsklab.nodes.controller;

import java.io.File;
import java.util.Collection;
import java.util.List;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.workflow.FlowVariable;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.Rserve.RConnection;

/**
 * Interface for RController.
 *
 * @author Jonathan Hale
 */
public interface IRController extends AutoCloseable {

  /**
   * Marker class for exceptions during R configuration or execution.
   * 
   * @author Jonathan Hale, KNIME, Konstanz, Germany
   */
  class RException extends Exception {
    /** Generated serialVersionUID */
    private static final long serialVersionUID = 2815440774381106769L;

    /** Message constants */
    static String MSG_EVAL_FAILED = "R evaluation failed";

    /** Constructor */
    public RException(final String msg, final Throwable cause) {
      super(msg, cause);
    }
  }

  /**
   * Marker class for exception occurring when trying to use RController when not initialized.
   */
  class RControllerNotInitializedException extends RuntimeException {
    /** Generated serialVersionUID */
    private static final long serialVersionUID = 7059728194387457225L;

    /** Constructor */
    RControllerNotInitializedException() {
      super("CODING PROBLEM\tRController was not initialized at this point.");
    }
  }

  /**
   * Initialize everything which may fail. This includes starting up servers/checking for libraries,
   * etc.
   *
   * @throws RException
   */
  void initialize() throws RException;

  /**
   * @return <code>true</code> if {@link #initialize()} was called on this instance and
   *         {@link #close()} has not been called since.
   */
  boolean isInitialized();

  /**
   * Set whether to use {@link NodeContext}s for Threads.
   *
   * @param useNodeContext Whether to use node contexts
   * @see ThreadUtils#threadWithContext(Runnable)
   * @see ThreadUtils#threadWithContext(Runnable, String)
   * @see IRController#isUsingNodeContext()
   */
  void setUseNodeContext(boolean useNodeContext);

  /**
   * @return <code>true</code> if currently using NodeContexts in threads.
   * @see IRController#setUseNodeContext(boolean)
   */
  boolean isUsingNodeContext();

  /** @return The underlying REngine (usually an {@link RConnection}) */
  RConnection getREngine();

  /**
   * Evaluate R code. This may have side effects on the workspace of the RController.
   *
   * @param expr the R expression to evaluate
   * @param cmd the R expression to evaluate
   * @param resolve Whether to resolve the resulting reference, if <code>false</code> null returned
   *        (unless exception is thrown)
   * @return result of evaluation
   * @throws RException
   */
  REXP eval(String expr, boolean resolve) throws RException;

  /**
   * Evaluate R code in a separate thread to be able to cancel it.
   *
   * @param cmd The R command
   * @param exec only used for checking if execution is cancelled
   * @param resolve Whether to resolve the resulting reference
   * @return Result of the evaluation, either a reference (if resolve is false) or the resolved
   *         value.
   * @throws RException
   * @throws CanceledExecutionException
   * @throws InterruptedException
   */
  REXP monitoredEval(String cmd, ExecutionMonitor exec, boolean resolve)
      throws RException, CanceledExecutionException, InterruptedException;

  /**
   * Assign an integer into an R variable.
   * 
   * @param symbol symbol name
   * @param value value to assign
   * @throws RException
   */
  void assign(String symbol, int value) throws RException;

  /**
   * Assign a double into an R variable.
   * 
   * @param symbol symbol name
   * @param value value to assign
   * @throws RException
   */
  void assign(String symbol, double value) throws RException;

  /**
   * Assign an REXP an R variable.
   * 
   * @param expr Expression to assign the value to. Usually a variable name
   * @param value Value to assign
   * @throws RException
   */
  void assign(String expr, String value) throws RException;

  /**
   * Assign an REXP an R variable.
   *
   * @param expr Expression to assign the value to. Usually a variable name
   * @param value Value to assign
   * @throws RException
   */
  void assign(String expr, REXP value) throws RException;

  /**
   * Assign an R variable in a separate thread to be able to cancel it.
   *
   * @param symbol R variable name
   * @param value REXP value to assign to the variable
   * @param exec Execution Monitor
   * @throws RException
   * @throws CanceledExecutionException
   * @throws InterruptedException
   * @see #monitoredEval(String, ExecutionMonitor, boolean)
   * @see #assign(String, REXP)
   */
  void monitoredAssign(String symbol, REXP value, ExecutionMonitor exec)
      throws RException, CanceledExecutionException, InterruptedException;

  /**
   * Clear the R workspace (remove all variables and imported packages).
   *
   * @param exec Execution Monitor
   * @throws RException
   * @throws CanceledExecutionException
   */
  void clearWorkspace(ExecutionMonitor exec) throws RException, CanceledExecutionException;

  /**
   * @param workspaceFile R workspace file to read
   * @param tempWorkspaceFile the workspace file
   * @param exec execution monitor to report progress on
   * @return List of libraries which were previously imported in the workspace. See
   *         {@link #importListOfLibrariesAndDelete()}.
   * @throws RException
   * @throws CanceledExecutionException
   */
  List<String> clearAndReadWorkspace(final File workspaceFile, final ExecutionMonitor exec)
      throws RException, CanceledExecutionException;

  /**
   * Write R variables into a R variable in the current workspace
   *
   * @param inFlowVariables Flow variables to export into the R workspace
   * @param name Name for the R variable to contain the flow variables
   * @param exec Execution monitor
   * @throws RException
   * @throws CanceledExecutionException
   */
  void exportFlowVariables(Collection<FlowVariable> inFlowVariables, String name,
      ExecutionMonitor exec) throws RException, CanceledExecutionException;

  /**
   * Get flow variables from a R variable.
   *
   * @param variableName Name of the variable to get the {@link FlowVariable}s from.
   * @return The extracted flow variables.
   * @throws RException If an R related error occurred during execution.
   */
  Collection<FlowVariable> importFlowVariables(String variableName) throws RException;

  /**
   * Assign a {@link BufferedDataTable} to a R variable in the current workspace.
   *
   * @param symbol R variable to assign to.
   * @param value The table to assign to the variable.
   * @param exec For monitoring the progress.
   * @param batchSize max number of rows to send to R per batch.
   * @param type R type for "symbol" to provide the table data as
   * @param sendRowNames Whether to send names of rows to R with the input table
   * @throws RException If an R related error occurred during execution.
   * @throws CanceledExecutionException If execution was cancelled.
   * @throws InterruptedException If a thread was interrupted.
   */
  void monitoredAssign(String symbol, BufferedDataTable value, ExecutionMonitor exec, int batchSize,
      String type, boolean sendRowNames)
      throws RException, CanceledExecutionException, InterruptedException;

  /**
   * Import a BufferedDataTable from the R expression <code>string</code>.
   *
   * @param string R expression (variable for e.g.) to retrieve a data.frame which is then converted
   *        into a BufferedDataTable.
   * @param nonNumbersAsMissing Convert NaN and Infinity to {@link MissingCell}.
   * @param exec Execution context for creating the table and monitoring execution.
   * @return The created BufferedDataTable.
   * @throws RException
   * @throws CanceledExecutionException
   */
  BufferedDataTable importBufferedDataTable(String string, boolean nonNumbersAsMissing,
      ExecutionContext exec) throws RException, CanceledExecutionException;

  /**
   * Get list of libraries imported in the current session and then delete those imports.
   *
   * @return The list of deleted imports
   * @throws RException
   */
  List<String> importListOfLibrariesAndDelete() throws RException;

  /**
   * Save the workspace in the current R session to the specified file.
   *
   * @param workspaceFile File to save the workspace to.
   * @param exec For monitoring the progress.
   * @throws RException If an R related error occurred during execution.
   * @throws CanceledExecutionException If execution was cancelled.
   */
  void saveWorkspace(File workspaceFile, ExecutionMonitor exec)
      throws RException, CanceledExecutionException;

  /**
   * Import RInputPorts and BufferedDataTables into the current R workspace.
   *
   * @param inData ports to import
   * @param exec For monitoring the progess.
   * @param batchSize max number of rows to send to R per batch.
   * @param rType R type for "symbol" to provide the table data as
   * @param sendRowNames Whether to send row names of input tables to R
   * @throws RException
   * @throws CanceledExecutionException
   */
  void importDataFromPorts(PortObject[] inData, ExecutionMonitor exec, final int batchSize,
      final String rType, final boolean sendRowNames) throws RException, CanceledExecutionException;
}
