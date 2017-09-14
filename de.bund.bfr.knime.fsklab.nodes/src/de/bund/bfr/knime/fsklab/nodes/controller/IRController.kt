/***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http:></http:>//www.gnu.org/licenses/>.
 *
 *
 * Contributors: Department Biological Safety - BfR
 */

package de.bund.bfr.knime.fsklab.nodes.controller

import org.knime.core.node.CanceledExecutionException
import org.knime.core.node.ExecutionMonitor
import org.rosuda.REngine.REXP
import org.rosuda.REngine.Rserve.RConnection

/**
 * Interface for RController.
 *
 * @author Miguel Alba
 */
interface IRController : AutoCloseable {

    /**
     * Marker class for exceptions during R configuration or execution.
     */
    class RException : Exception {

        /** Constructor  */
        constructor(msg: String) : super(msg)

        /** Constructor  */
        constructor(msg: String, cause: Throwable) : super(msg, cause)

        companion object {
            /** Generated serialVersionUID  */
            private val serialVersionUID = 2815440774381106769L

            /** Message constants  */
            internal var MSG_EVAL_FAILED = "R evaluation failed"
        }
    }


    /** Marker class for exception occurring when trying to use RController when not initialized.  */
    class RControllerNotInitializedException
    /** Constructor  */
    internal constructor() : RuntimeException("CODING PROBLEM\tRController was not initialized at this point.") {
        companion object {
            /** Generated serialVersionUID  */
            private val serialVersionUID = 7059728194387457225L
        }
    }


    /**
     * Initialize everything which may fail. This includes starting up servers/checking for libraries,
     * etc.
     *
     * @throws RException
     */
    @Throws(RException::class)
    fun initialize()

    /**
     * @return `true` if [.initialize] was called on this instance and [.close] has not been called since.
     */
    fun isInitialized(): Boolean

    /** @return The underlying REngine (usually an [RConnection])
     */
    val rEngine: RConnection?

    /**
     * Evaluate R code. This may have side effects on the workspace of the RController.
     *
     * @param expr the R expression to evaluate.
     * @return result of evaluation
     * @throws RException
     */
    @Throws(RException::class)
    fun eval(expr: String): REXP

    /**
     * Evaluate R code in a separate thread to be able to cancel it.
     *
     * @param cmd
     * @param exec only used for checking if execution is cancelled.
     * @return
     * @throws CanceledExecutionException
     * @see .eval
     */
    @Throws(RException::class, CanceledExecutionException::class, InterruptedException::class)
    fun monitoredEval(cmd: String, exec: ExecutionMonitor): REXP
}

