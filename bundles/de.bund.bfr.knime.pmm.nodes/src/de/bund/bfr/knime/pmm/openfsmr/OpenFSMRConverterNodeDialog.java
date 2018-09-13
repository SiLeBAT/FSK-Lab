/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
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
 *******************************************************************************/
package de.bund.bfr.knime.pmm.openfsmr;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.nio.file.InvalidPathException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentStringListSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;

import de.bund.bfr.knime.pmm.common.KnimeUtils;

/**
 * <code>NodeDialog</code> for the OpenFSMR Converter node.
 * 
 * @author Miguel Alba
 */
public class OpenFSMRConverterNodeDialog extends DefaultNodeSettingsPane  {

  // models
  private SettingsModelString dir;
  private SettingsModelStringArray files;

  // GUI Widgets
  private final DialogComponentFileChooser dirChooser;
  private final DialogComponentStringListSelection filesChooser;

  // Constructor here
  public OpenFSMRConverterNodeDialog() {
    dir =
        new SettingsModelString(OpenFSMRConverterNodeModel.CFGKEY_DIR,
            OpenFSMRConverterNodeModel.DEFAULT_DIR);
    dir.setEnabled(true);

    files =
        new SettingsModelStringArray(OpenFSMRConverterNodeModel.CFGKEY_FILES,
            OpenFSMRConverterNodeModel.DEFAULT_FILES);
    files.setEnabled(false);

    // inits and adds GUI widgets
    dirChooser =
        new DialogComponentFileChooser(dir, "file-directory", JFileChooser.OPEN_DIALOG, true);
    dirChooser.addChangeListener(new DirChangeListener());
    addDialogComponent(dirChooser);

    filesChooser = new DialogComponentStringListSelection(files, "Selected files", Arrays.asList(""), true, 10);
    addDialogComponent(filesChooser);
  }
  
  private class DirChangeListener implements ChangeListener {

    private FileFilter filter = new FileFilter() {
      @Override
      public boolean accept(File pathname) {
        return new FileNameExtensionFilter("PMF files", "pmf", "pmfx").accept(pathname);
      }
    };

    public void stateChanged(ChangeEvent e) {
      try {

        File dirFile = KnimeUtils.getFile(dir.getStringValue());
        File[] filesInDir = dirFile.listFiles(filter);
        if (filesInDir.length > 0) {
          List<String> fnames =
              Arrays.stream(filesInDir).map(File::getName).collect(Collectors.toList());
          filesChooser.replaceListItems(fnames, (String[]) null);
        }
      } catch (InvalidPathException | MalformedURLException error) {
        error.printStackTrace();
      }
    }
  }
}
