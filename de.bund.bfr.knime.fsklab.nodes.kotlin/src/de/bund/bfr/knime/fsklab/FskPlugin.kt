package de.bund.bfr.knime.fsklab

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

object FskPlugin : AbstractUIPlugin() {
	
	/**
	 * This method is called upon plug-in activation.
     * @param context The OSGI bundle context
	 */
	override fun start(context: BundleContext) = super.start(context)
	
	/**
	 * This method is called when the plug-in is stopped.
	 * @param context The OSGI bundle context
	 */
	override fun stop(context: BundleContext) = super.stop(context)
}