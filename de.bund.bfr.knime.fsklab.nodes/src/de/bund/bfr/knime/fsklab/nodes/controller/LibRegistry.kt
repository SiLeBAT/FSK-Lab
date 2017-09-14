package de.bund.bfr.knime.fsklab.nodes.controller

import com.sun.jna.Platform
import de.bund.bfr.knime.fsklab.nodes.controller.IRController.RException
import org.knime.core.util.FileUtil
import org.rosuda.REngine.REXPMismatchException
import java.io.IOException
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

/**
 * Singleton!! There can only be one.
 *
 * @author Miguel Alba
 */
class LibRegistry @Throws(IOException::class, RException::class)
private constructor() {

    /** Installation path.  */
    val installationPath: Path = FileUtil.createTempDir("install").toPath()

    /** miniCRAN repository path.  */
    val repositoryPath: Path = FileUtil.createTempDir("repo").toPath()

    /** Utility set to keep count of installed libraries.  */
    private val installedLibs = HashSet<String>()

    /** Utility RController for running R commands.  */
    private val controller: RController = RController()

    private var type: String = when {
        Platform.isWindows() -> "win.binary"
        Platform.isMac() -> "mac.binary"
        else -> "source"
    }
    private val rWrapper: RWrapper

    init {
        rWrapper = RWrapper()
        rWrapper.library("miniCRAN")
        rWrapper.makeRepo(repositoryPath, "http://cran.us.r-project.org")
    }

    /**
     * Returns whether an R library is installed.
     *
     * @param libraryName name of the R library
     * @return whether an R library is installed
     */
    fun isInstalled(libraryName: String): Boolean {
        return installedLibs.contains(libraryName)
    }

    /**
     * Install a list of libraries into the repository.
     *
     * @param libs list of names of R libraries
     * @throws RException
     * @throws REXPMismatchException
     */
    @Throws(RException::class, REXPMismatchException::class)
    fun installLibs(libs: List<String>) {

        // Gets list of R dependencies of libs
        val deps = rWrapper.pkgDep(libs)

        // Adds the dependencies to the miniCRAN repository
        rWrapper.addPackage(deps, repositoryPath, "http://cran.us.r-project.org")

        // Gets the paths to the binaries of these dependencies
        val paths = rWrapper.checkVersions(deps, repositoryPath)

        // Install binaries
        rWrapper.installPackages(paths, installationPath)

        // Adds names of installed libraries to utility set
        installedLibs.addAll(deps)
    }

    /**
     * Gets list of paths to the binaries of the desired libraries.
     *
     * @param libs
     * @return list of paths to the binaries of the desired libraries
     * @throws RException
     * @throws REXPMismatchException
     */
    @Throws(RException::class, REXPMismatchException::class)
    fun getPaths(libs: List<String>): Set<Path> {
        // Gets list of R dependencies of libs
        val deps = rWrapper.pkgDep(libs)

        // Gets the paths to the binaries of these dependencies
        val paths = rWrapper.checkVersions(deps, repositoryPath)
        return HashSet(paths)
    }

    private inner class RWrapper {

        // R commands
        /**
         * Load and attach add-on packages.
         *
         * @param pkg The name of a package.
         * @throws RException
         *
         * @see [R documentation](https://stat.ethz.ch/R-manual/R-devel/library/base/html/library.html)
         */
        @Throws(RException::class)
        internal fun library(pkg: String) {
            val cmd = "library($pkg)"
            controller.eval(cmd)
        }

        /**
         * Install packages from local files.
         *
         * @param pkgs List of package files. The files can be source distributions (.tar.gz) or binary distributions
         * (.zip for Windows and .tgz for Mac).
         * @param lib Directory where packages are installed.
         * @throws RException
         *
         * @see [R documentation](https://stat.ethz.ch/R-manual/R-devel/library/utils/html/install.packages.html)
         */
        @Throws(RException::class)
        internal fun installPackages(pkgs: List<Path>, lib: Path) {
            val pkgsAsString = pkgs.joinToString(separator = ", ", prefix = "'", postfix = "'") { it.toString().replace("\\", "/") }
            val cmd = "install.packages(c($pkgsAsString), repos = NULL, lib = '${_path2String(lib)}', type = '$type')"
            controller.eval(cmd)
        }

        // miniCRAN commands
        /**
         * Add packages to a miniCRAN repository.
         *
         * @param pkgs List of names of packages to be downloaded.
         * @param path Destination download path. This path is the root folder of the repository.
         * @param repos URL of the 'contrib' sections of the repository, e.g. "http://cran.us.r-project.org".
         * @throws RException
         *
         * @see [miniCRAN documentation](https://cran.r-project.org/web/packages/miniCRAN/miniCRAN.pdf)
         */
        @Throws(RException::class)
        internal fun addPackage(pkgs: List<String>, path: Path, repos: String) {
            val cmd = "addPackage(${_pkgList(pkgs)}, '${_path2String(path)}', repos = '$repos', type = '$type', Rversion = '3.0')"
            controller.eval(cmd)
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
         * @see [miniCRAN documentation](https://cran.r-project.org/web/packages/miniCRAN/miniCRAN.pdf)
         */
        @Throws(REXPMismatchException::class, RException::class)
        internal fun checkVersions(pkgs: List<String>, path: Path): List<Path> {
            val cmd = "checkVersions(${_pkgList(pkgs)}, '${_path2String(path)}, type = '$type', Rversion = '3.0')"
            val pathsArray = controller.eval(cmd).asStrings()
            return pathsArray.map { it -> Paths.get(it) }.toList()
        }

        /**
         * Creates a local repository in the specified path.
         *
         * Creates a CRAN folder structure in the specified destination folder
         * and then creates the PACKAGES index file. Since the folder structure
         * mimics the required structure and files of a CRAN repository, it
         * supports functions like *install.packages()*.
         *
         * @param path Destination download path. This path is the root folder of the repository.
         * @param repos URL of the 'contrib' sections of the repository, e.g. "http://cran.us.r-project.org".
         * @throws RException
         *
         * @see [miniCRAN documentation](https://cran.r-project.org/web/packages/miniCRAN/miniCRAN.pdf)
         */
        @Throws(RException::class)
        internal fun makeRepo(path: Path, repos: String) {
            val cmd = "makeRepo(c(), '${_path2String(path)}', repos = '$repos', type = '$type')"
            controller.eval(cmd)
        }

        /**
         * Retrieve package dependencies.
         *
         * Perform recursive retrieve for Depends, Imports and LinkLibrary.
         * Performs non-recursive retrieve for Suggests.
         *
         * @param pkgs List of names of packages.
         * @return the dependencies of the specified packages
         * @throws RException
         * @throws REXPMismatchException
         *
         * @see [miniCRAN documentation](https://cran.r-project.org/web/packages/miniCRAN/miniCRAN.pdf)
         */
        @Throws(RException::class, REXPMismatchException::class)
        internal fun pkgDep(pkgs: List<String>): List<String> {
            val cmd = "pkgDep(${_pkgList(pkgs)}, availPkgs = cranJuly2014, type = '$type')"
            val rexp = controller.eval(cmd)
            return Arrays.asList(*rexp.asStrings())
        }

        // Utility method. Should not be used outside of RCommandBuilder.
        internal fun _pkgList(pkgs: List<String>): String {
            return "c(${pkgs.joinToString(separator = ", ", prefix = "'", postfix = "'")})"
        }

        // Utility method. Should not be used outside of RCommandBuilder.
        internal fun _path2String(path: Path): String {
            return path.toString().replace("\\", "/")
        }
    }

    companion object {
        val instance: LibRegistry by lazy { LibRegistry() }
    }
}