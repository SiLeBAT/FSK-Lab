/*
 * ------------------------------------------------------------------------ Copyright by KNIME GmbH,
 * Konstanz, Germany Website: http://www.knime.org; Email: contact@knime.org
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License, Version 3, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7:
 *
 * KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs. Hence, KNIME and ECLIPSE are
 * both independent programs and are not derived from each other. Should, however, the
 * interpretation of the GNU GPL Version 3 ("License") under any applicable laws result in KNIME and
 * ECLIPSE being a combined program, KNIME GMBH herewith grants you the additional permission to use
 * and propagate KNIME together with ECLIPSE with only the license terms in place for ECLIPSE
 * applying to ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the license terms of
 * ECLIPSE themselves allow for the respective use and propagation of ECLIPSE together with KNIME.
 *
 * Additional permission relating to nodes for KNIME that extend the Node Extension (and in
 * particular that are based on subclasses of NodeModel, NodeDialog, and NodeView) and that only
 * interoperate with KNIME through standard APIs ("Nodes"): Nodes are deemed to be separate and
 * independent programs and to not be covered works. Notwithstanding anything to the contrary in the
 * License, the License does not apply to Nodes, you are not required to license Nodes under the
 * License, and you are granted a license to prepare and propagate Nodes, in each case even if such
 * Nodes are propagated with or for interoperation with KNIME. The owner of a Node may freely choose
 * the license terms applicable to such Node, including when such Node is propagated with or for
 * interoperation with KNIME. ---------------------------------------------------------------------
 *
 * History 15.07.2013 (thor): created
 */
package de.bund.bfr.knime.fsklab.preferences;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.knime.core.node.NodeLogger;
import org.osgi.framework.Bundle;

/**
 * Utility class for getting various R installation paths.
 *
 * @author Thorsten Meinl, KNIME.com, Zurich, Switzerland
 * @author Miguel de Alba, BfR, Berlin.
 */
public final class RPathUtil {
  private RPathUtil() {}

  private static File packagedRExecutable;

  private static File packagedRHome;

  private static File systemRExecutable;

  private static File systemRHome;


  static {
    findPackagedR();
    if (Platform.OS_LINUX.equals(Platform.getOS()) || Platform.OS_MACOSX.equals(Platform.getOS())) {
      findSystemRUnix();
    } else if (Platform.OS_WIN32.equals(Platform.getOS())) {
      findSystemRWindows();
    }
  }


  private static void findPackagedR() {

    if (!Platform.OS_WIN32.equals(Platform.getOS()))
      return;

    if (!Platform.ARCH_X86_64.equals(Platform.getOSArch()))
      return;
    
    // On Windows (32 or 64 bit) look  for the optional plugin with packaged R.
    final String bundleName;
    if (Platform.ARCH_X86_64.equals(Platform.getOSArch())) {
      bundleName = "de.bund.bfr.binary.r.win32.x86_64";
    } else if (Platform.ARCH_X86.equals(Platform.getOSArch())) {
      bundleName = "de.bund.bfr.binary.r.win32.x86";
    } else {
      return;
    }
    
    Bundle bundle = Platform.getBundle(bundleName);
    if (bundle == null)
      return;

    // Look for the R.exe first in the /R-Inst/bin folder. If not found it
    // look for the it recursively.
    Enumeration<URL> enumeration = bundle.findEntries("/R-Inst/bin", "R.exe", false);
    URL rExe = null;
    if ((enumeration != null) && enumeration.hasMoreElements()) {
      rExe = enumeration.nextElement();
    } else {
      enumeration = bundle.findEntries("/R-Inst/bin", "R", true);
      if ((enumeration != null) && enumeration.hasMoreElements()) {
        rExe = enumeration.nextElement();
      }
    }

    if (rExe == null)
      return;

    try {

      // Look for R home directory included in the packaged installation which is
      // named `R-Inst`. It travels up from packagedExecutable.
      packagedRExecutable = new File(FileLocator.toFileURL(rExe).getFile());
      // parent is either /bin, /x64 (64-bit) or /i386 (32-bit)
      File RInstDir = packagedRExecutable.getParentFile();
      do {
        RInstDir = RInstDir.getParentFile();
      } while (!"R-Inst".equals(RInstDir.getName()));
      packagedRHome = RInstDir;
    } catch (IOException ex) {
      NodeLogger.getLogger(RPathUtil.class).info("Could not locate packaged R executable", ex);
    }
  }


  private static void findSystemRWindows() {
    FileFilter ff = new FileFilter() {
      @Override
      public boolean accept(final File pathname) {
        return pathname.isDirectory() && pathname.getName().startsWith("R-");
      }
    };

    File programFiles = new File(System.getenv("ProgramFiles"));
    for (File dir : programFiles.listFiles(ff)) {
      File binDir = new File(dir, "bin");
      if (binDir.isDirectory()) {
        File executable = new File(binDir, "R.exe");
        if (executable.isFile()) {
          systemRHome = dir;
          systemRExecutable = executable;
          break;
        }
      }
    }
  }


  private static void findSystemRUnix() {
    String[] searchPaths = {"/usr/bin/R", "/usr/local/bin/R"};
    for (String s : searchPaths) {
      File f = new File(s);
      if (f.canExecute()) {
        systemRExecutable = f;
        break;
      }
    }

    searchPaths = new String[] {"/usr/lib64/R", "/usr/lib/R", "/usr/local/lib64/R",
        "/usr/local/lib/R", "/Library/Frameworks/R.framework/Resources"};
    for (String s : searchPaths) {
      File f = new File(s, "bin");
      if (f.isDirectory()) {
        systemRHome = f.getParentFile();
        break;
      }
    }
  }


  /**
   * Returns the path to the executable of a packaged R installation if one exists.
   *
   * @return the R executable or <code>null</code> if no packaged executable was found
   */
  public static File getPackagedRExecutable() {
    return packagedRExecutable;
  }


  /**
   * Returns the path to a packaged R installation.
   *
   * @return the R installation directory or <code>null</code> if no packaged installation was found
   */
  public static File getPackagedRHome() {
    return packagedRHome;
  }


  /**
   * Returns the path to the executable of an R installation in the operating system if one exists.
   * The search is performed by looking at common places such as <tt>/usr/lib</tt> under Linux or
   * <tt>C:/Program Files/</tt> under Windows.
   *
   * @return the R executable or <code>null</code> if no system executable was found
   */
  public static File getSystemRExecutable() {
    return systemRExecutable;
  }


  /**
   * Returns the path to an R installation in the operating system if one exists. The search is
   * performed by looking at common places such as <tt>/usr/lib</tt> under Linux or
   * <tt>C:/Program Files/</tt> under Windows.
   *
   * @return the R installation directory or <code>null</code> if no system installation was found
   */
  public static File getSystemRHome() {
    return systemRHome;
  }
}
