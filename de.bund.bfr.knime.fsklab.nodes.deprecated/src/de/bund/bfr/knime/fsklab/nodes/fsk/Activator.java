package de.bund.bfr.knime.fsklab.nodes.fsk;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

  private static BundleContext context;

  static BundleContext getContext() {
    return context;
  }

  public void start(BundleContext bundleContext) throws Exception {
    Activator.context = bundleContext;
  }

  public void stop(BundleContext bundleContext) throws Exception {
    Activator.context = null;
  }
}