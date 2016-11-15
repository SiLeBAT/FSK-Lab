/***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 * <p>
 * Contributors: Department Biological Safety - BfR
 **************************************************************************************************/

package de.bund.bfr.knime.fsklab.nodes.controller;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.Rserve.RConnection;

/**
 * Interface for RController.
 *
 * @author Miguel Alba
 */
public interface IRController extends AutoCloseable {

    /**
     * Marker class for exceptions during R configuration or execution.
     */
    class RException extends Exception {
        /** Generated serialVersionUID */
        private static final long serialVersionUID = 2815440774381106769L;

        /** Message constants */
        static String MSG_EVAL_FAILED = "R evaluation failed";

        /** Constructor */
        public RException(final String msg) {
            super(msg);
        }

        /** Constructor */
        public RException(final String msg, final Throwable cause) {
            super(msg, cause);
        }
    }


    /** Marker class for exception occurring when trying to use RController when not initialized. */
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

    /** @return The underlying REngine (usually an {@link RConnection}) */
    RConnection getREngine();

    /**
     * Evaluate R code. This may have side effects on the workspace of the RController.
     *
     * @param expr the R expression to evaluate.
     * @return result of evaluation
     * @throws RException
     */
    REXP eval(String expr) throws RException;

    /**
     * Evaluate R code in a separate thread to be able to cancel it.
     *
     * @param cmd
     * @param exec only used for checking if execution is cancelled.
     * @return
     * @throws CanceledExecutionException
     * @see #eval(String)
     */
    REXP monitoredEval(String cmd, ExecutionMonitor exec)
            throws RException, CanceledExecutionException, InterruptedException;
}

