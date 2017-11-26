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
package de.bund.bfr.knime.fsklab.nodes.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;
import org.knime.core.util.FileUtil;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPString;
import com.sun.jna.Platform;
import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException;

/**
 * Singleton!! There can only be one.
 * 
 * @author Miguel Alba
 */
public class LibRegistry {

  private static LibRegistry instance;

  /** Installation path. */
  private final Path installPath = FileUtil.createTempDir("install.path").toPath();

  /** miniCRAN repository path. */
  private final Path repoPath = FileUtil.createTempDir("repo").toPath();

  /** Utility set to keep count of installed libraries. */
  private final Set<String> installedLibs = new HashSet<>();

  /** Utility RController for running R commands. */
  private final RController controller = new RController();

  private String type;
  private RWrapper rWrapper;

  private LibRegistry() throws IOException, RException {

    if (Platform.isWindows()) {
      type = "win.binary";
    } else if (Platform.isMac()) {
      type = "mac.binary";
    } else {
      type = "source";
    }

    rWrapper = new RWrapper();
    rWrapper.library("miniCRAN");
    rWrapper.makeRepo(repoPath, "http://cran.us.r-project.org");
  }

  public static LibRegistry instance() throws IOException, RException {
    if (instance == null) {
      instance = new LibRegistry();
    }
    return instance;
  }

  /**
   * Returns whether an R library is installed.
   * 
   * @param libraryName name of the R library
   * @return whether an R library is installed
   */
  public boolean isInstalled(final String libraryName) {

    // Look in libraries installed within LibRegistry
    if (installedLibs.contains(libraryName))
      return true;

    // Look for libraries installed in the R distribution.
    // Make a library(<libname>) call. If an RException is return the library is missing.
    try {
      rWrapper.library(libraryName);
      return true;
    } catch (RException exception) {
      // the library is not included in .Library or .Library.site
      return false;
    }
  }

  /**
   * Install a list of libraries into the repository.
   * 
   * @param libs list of names of R libraries
   * @throws RException
   * @throws REXPMismatchException
   */
  public void installLibs(final List<String> libs) throws RException, REXPMismatchException {

    /*
     * Gets list of R dependencies of libs. pkgDep returns dependencies for the required libs of
     * which some may already be installed.
     */
    final List<String> deps =
        rWrapper.pkgDep(libs).stream().filter(it -> !isInstalled(it)).collect(Collectors.toList());

    // Adds the dependencies to the miniCRAN repository
    rWrapper.addPackage(deps, repoPath, "http://cran.us.r-project.org");

    // Gets the paths to the binaries of these dependencies
    List<Path> paths = rWrapper.checkVersions(deps, repoPath);

    // Install binaries
    rWrapper.installPackages(paths, installPath);

    // Adds names of installed libraries to utility set
    installedLibs.addAll(deps);
  }

  /**
   * Gets list of paths to the binaries of the desired libraries.
   * 
   * @param libs
   * @return list of paths to the binaries of the desired libraries
   * @throws RException
   * @throws REXPMismatchException
   */
  public Set<Path> getPaths(List<String> libs) throws RException, REXPMismatchException {
    // Gets list of R dependencies of libs
    List<String> deps = rWrapper.pkgDep(libs);

    // Gets the paths to the binaries of these dependencies
    List<Path> paths = rWrapper.checkVersions(deps, repoPath);
    return new HashSet<>(paths);
  }

  public Path getInstallationPath() {
    return installPath;
  }

  public Path getRepositoryPath() {
    return repoPath;
  }

  private class RWrapper {

    // R commands
    /**
     * Load and attach add-on packages.
     * 
     * @param pkg The name of a package.
     * @throws RException
     * 
     * @see <a href= "https://stat.ethz.ch/R-manual/R-devel/library/base/html/library.html"> R
     *      documentation</a>
     */
    void library(final String pkg) throws RException {
      String cmd = "library(" + pkg + ")";
      controller.eval(cmd, false);
    }

    /**
     * Install packages from local files.
     * 
     * @param pkgs List of package files. The files can be source distributions (.tar.gz) or binary
     *        distributions (.zip for Windows and .tgz for Mac).
     * @param lib Directory where packages are installed.
     * @throws RException
     * 
     * @see <a href=
     *      "https://stat.ethz.ch/R-manual/R-devel/library/utils/html/install.packages.html"> R
     *      documentation</a>
     */
    void installPackages(final List<Path> pkgs, final Path lib) throws RException {

      String pkgsAsString = pkgs.stream().map(Path::toString).map(FilenameUtils::separatorsToUnix)
          .map(str -> "'" + str + "'").collect(Collectors.joining(", "));

      String pkgList = "c(" + pkgsAsString + ")";
      String cmd = "install.packages(" + pkgList + ", repos = NULL, lib = '" + _path2String(lib)
          + "', type = '" + type + "')";
      controller.eval(cmd, false);
    }

    // miniCRAN commands
    /**
     * Add packages to a miniCRAN repository.
     * 
     * @param pkgs List of names of packages to be downloaded.
     * @param path Destination download path. This path is the root folder of the repository.
     * @param repos URL of the 'contrib' sections of the repository, e.g.
     *        "http://cran.us.r-project.org".
     * @throws RException
     * 
     * @see <a href= "https://cran.r-project.org/web/packages/miniCRAN/miniCRAN.pdf"> miniCRAN
     *      documentation</a>
     */
    void addPackage(final List<String> pkgs, final Path path, final String repos)
        throws RException {
      String cmd = "addPackage(" + _pkgList(pkgs) + ", '" + _path2String(path) + "', repos = '"
          + repos + "', type = '" + type + "', Rversion = '3.0')";
      controller.eval(cmd, false);
    }

    /**
     * Returns the file paths for the specified packages.
     * 
     * @param pkgs List of names of packages.
     * @param path The local path to the directory where the miniCRAN repo resides.
     * @return the file paths for the specified packages
     * @throws REXPMismatchException
     * @throws RException
     * 
     * @see <a href= "https://cran.r-project.org/web/packages/miniCRAN/miniCRAN.pdf"> miniCRAN
     *      documentation</a>
     */
    List<Path> checkVersions(final List<String> pkgs, final Path path)
        throws REXPMismatchException, RException {
      String cmd = "checkVersions(" + _pkgList(pkgs) + ", '" + _path2String(path) + "', type = '"
          + type + "', Rversion = '3.0')";

      REXP rexp = controller.eval(cmd, true);
      @SuppressWarnings("unchecked")
      Collection<REXPString> values = rexp.asList().values();

      return values.stream().map(it -> it.asStrings()[0]).map(Paths::get)
          .collect(Collectors.toList());
    }

    /**
     * Creates a local repository in the specified path.
     * <p>
     * Creates a CRAN folder structure in the specified destination folder and then creates the
     * PACKAGES index file. Since the folder structure mimics the required structure and files of a
     * CRAN repository, it supports functions like <i>install.packages()</i>.
     * 
     * @param path Destination download path. This path is the root folder of the repository.
     * @param repos URL of the 'contrib' sections of the repository, e.g.
     *        "http://cran.us.r-project.org".
     * @throws RException
     * 
     * @see <a href= "https://cran.r-project.org/web/packages/miniCRAN/miniCRAN.pdf"> miniCRAN
     *      documentation</a>
     */
    void makeRepo(final Path path, final String repos) throws RException {
      String cmd = "makeRepo(c(), '" + _path2String(path) + "', repos = '" + repos + "', type = '"
          + type + "')";
      controller.eval(cmd, false);
    }

    /**
     * Retrieve package dependencies.
     * <p>
     * Perform recursive retrieve for Depends, Imports and LinkLibrary. Performs non-recursive
     * retrieve for Suggests.
     * 
     * @param pkgs List of names of packages.
     * @return the dependencies of the specified packages
     * @throws RException
     * @throws REXPMismatchException
     * 
     * @see <a href= "https://cran.r-project.org/web/packages/miniCRAN/miniCRAN.pdf"> miniCRAN
     *      documentation</a>
     */
    List<String> pkgDep(final List<String> pkgs) throws RException, REXPMismatchException {
      String cmd =
          "pkgDep(" + _pkgList(pkgs) + ", availPkgs = cranJuly2014, type = '" + type + "')";
      REXP rexp = controller.eval(cmd, true);
      return Arrays.asList(rexp.asStrings());
    }

    // Utility method. Should not be used outside of RCommandBuilder.
    String _pkgList(final List<String> pkgs) {
      return "c(" + pkgs.stream().map(pkg -> "'" + pkg + "'").collect(Collectors.joining(", "))
          + ")";
    }

    // Utility method. Should not be used outside of RCommandBuilder.
    String _path2String(final Path path) {
      return FilenameUtils.separatorsToUnix(path.toString());
    }
  }
}
