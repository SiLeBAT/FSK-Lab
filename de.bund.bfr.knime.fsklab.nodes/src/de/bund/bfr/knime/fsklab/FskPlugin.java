/*
 ***************************************************************************************************
 * Copyright (c) 2017 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *************************************************************************************************
 */
package de.bund.bfr.knime.fsklab;

import java.util.ResourceBundle;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.emfjson.jackson.module.EMFModule;
import org.emfjson.jackson.resource.JsonResourceFactory;
import org.osgi.framework.BundleContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bund.bfr.knime.fsklab.rakip.RakipModule;

public class FskPlugin extends AbstractUIPlugin {

  private static FskPlugin plugin;

  /**
   * Jackson object mapper with {@link RakipModule}. Initialized with {@link #start(BundleContext)}
   * and assigned null with {@link #stop(BundleContext)}.
   */
  public ObjectMapper OBJECT_MAPPER;
  public JsonResourceFactory FACTORY;

  private RegistryFix registryFix;

  public FskPlugin() {
    plugin = this;

    // registryFix = new RegistryFix();
  }

  @Override
  public void start(BundleContext context) throws Exception {
    super.start(context);

    // registryFix.fix();
    OBJECT_MAPPER = EMFModule.setupDefaultMapper();
    FACTORY = new JsonResourceFactory(OBJECT_MAPPER);
  }

  @Override
  public void stop(BundleContext context) throws Exception {
    super.stop(context);
    ResourceBundle.clearCache();

    // registryFix.restore();
    OBJECT_MAPPER = null;

    plugin = null;
  }

  /** @return Singleton instance of the plugin. */
  public static FskPlugin getDefault() {
    return plugin;
  }

  /**
   * Fix {@link EPackage.Registry} so that {@link FskPlugin#OBJECT_MAPPER} can be used with EMF
   * models.
   * 
   * Certain packages are removed from {@link EPackage.Registry} so that the mapper works with EMF
   * classes in KNIME.
   * 
   * <ul>
   * <li>{@link #fix()} removes certain packages from {@link EPackage.Registry} so that the mapper
   * works with EMF classes in KNIME.
   * <li>{@link #restore()} restores the {@link EPackage.Registry}.
   * 
   * @author Miguel de Alba
   */
  private class RegistryFix {

    private Object change;
    private Object namespace;
    private Object ui;
    private Object metadata;

    RegistryFix() {
      EPackage.Registry reg = EPackage.Registry.INSTANCE;
      change = reg.get("http://www.eclipse.org/emf/2003/Change");
      System.out.println("Change: " + reg.containsKey("http://www.eclipse.org/emf/2003/Change"));

      namespace = reg.get("http://www.w3.org/XML/1998/namespace");
      System.out.println("Change: " + reg.containsKey("http://www.w3.org/XML/1998/namespace"));

      ui = reg.get("http://www.eclipse.org/ui/2010/UIModel/application/ui");
      System.out.println(
          "Change: " + reg.containsKey("http://www.eclipse.org/ui/2010/UIModel/application/ui"));

      metadata = reg.getEPackage("http://www.example.org/metadata");
      System.out.println("metadata: " + reg.containsKey("http://www.example.org/metadata"));
    }

    void fix() {
      EPackage.Registry reg = EPackage.Registry.INSTANCE;
      reg.remove("http://www.eclipse.org/emf/2003/Change");
      System.out.println("Change: " + reg.containsKey("http://www.eclipse.org/emf/2003/Change"));

      reg.remove("http://www.w3.org/XML/1998/namespace");
      System.out.println("Change: " + reg.containsKey("http://www.w3.org/XML/1998/namespace"));

      reg.remove("http://www.eclipse.org/ui/2010/UIModel/application/ui");
      System.out.println(
          "Change: " + reg.containsKey("http://www.eclipse.org/ui/2010/UIModel/application/ui"));

      reg.remove("http://www.example.org/metadata");
      System.out.println("metadata: " + reg.containsKey("http://www.example.org/metadata"));

    }

    void restore() {
      EPackage.Registry reg = EPackage.Registry.INSTANCE;
      reg.put("http://www.eclipse.org/emf/2003/Change", change);
      reg.put("http://www.w3.org/XML/1998/namespace", namespace);
      reg.put("http://www.eclipse.org/ui/2010/UIModel/application/ui", ui);
      reg.put("http://www.example.org/metadata", metadata);
    }
  }
}
