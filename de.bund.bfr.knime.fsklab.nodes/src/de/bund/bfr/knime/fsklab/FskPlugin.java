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
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.emfjson.jackson.module.EMFModule;
import org.emfjson.jackson.resource.JsonResourceFactory;
import org.osgi.framework.BundleContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.threetenbp.ThreeTenModule;
import de.bund.bfr.knime.fsklab.rakip.RakipModule;

public class FskPlugin extends AbstractUIPlugin {

  private static FskPlugin plugin;

  /**
   * Jackson object mapper with {@link RakipModule}. Initialized with {@link #start(BundleContext)}
   * and assigned null with {@link #stop(BundleContext)}.
   */
  public ObjectMapper OBJECT_MAPPER;
  public ObjectMapper OLD_OBJECT_MAPPER ;
  public JsonResourceFactory FACTORY;

  public FskPlugin() {
    plugin = this;
  }

  @Override
  public void start(BundleContext context) throws Exception {
    super.start(context);

    OBJECT_MAPPER = EMFModule.setupDefaultMapper();
    OBJECT_MAPPER.registerModule(new ThreeTenModule());
    
    FACTORY = new JsonResourceFactory(OBJECT_MAPPER);
    OLD_OBJECT_MAPPER = new ObjectMapper().registerModule(new RakipModule());
  }

  @Override
  public void stop(BundleContext context) throws Exception {
    super.stop(context);
    ResourceBundle.clearCache();

    OBJECT_MAPPER = null;
    plugin = null;
  }

  /** @return Singleton instance of the plugin. */
  public static FskPlugin getDefault() {
    return plugin;
  }
}
