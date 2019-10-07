---
title: Installation
summary: Installation instructions
keywords: installation
sidebar: home_sidebar
permalink: installation.html
folder: pages
---

KNIME 3.7.2 must be installed.

| KNIME Analytics Platform version 3.7.2 for Windows | [64bit](http://download.knime.org/analytics-platform/win/KNIME%203.7.2%20Installer%20%2864bit%29.exe) |
| KNIME Analytics Platform version 3.7.2 for Windows | [32bit](http://download.knime.org/analytics-platform/win/knime_3.7.2.win32.win32.x86_64.exe) |
| KNIME Analytics Platform version 3.7.2 for Linux | [64bit](http://download.knime.org/analytics-platform/linux/knime_3.7.2.linux.gtk.x86_64.tar.gz) |
| KNIME Analytics Platform version 3.7.2 for Mac OS X	| [64bit](http://download.knime.org/analytics-platform/macosx/knime_3.7.2.app.macosx.cocoa.x86_64.dmg) |

* Help with KNIME installation [https://www.knime.com/installation](https://www.knime.com/installation)

# Installing plugins
The FSK-Lab and PMM-Lab plugins are available at the KNIME update site:
[https://dl.bintray.com/silebat/fsklab_icpmf](https://dl.bintray.com/silebat/fsklab_icpmf).

<video width="706" height="700" controls="controls">
  <source src="assets/update_site.mp4" type="video/mp4">
</video>

Steps:

1. Download our Eclipse bookmarks from <a href="assets/fsk_bookmarks.xml" download>here</a>. This file contains the configuration to our KNIME update site.
2. Import this file from KNIME in Help, Install new Software and Manage.
3. In the *Available Software Sites* window click on *Import* and browse the file you downloaded in step 1. After importing successfully you should be back to the *Available Software Sites* and a new site called *FSK-Lab* should aoppear.
4. If you click *Apply and Close* you will return to the *Install* dialog you opened in step 1. Now you should be able to pick the *FSK-Lab* software site in the *Work with:* entry by clicking the blue arrow.
5. When the *FSK-Lab* software site is selected all the plugins published there including PMM-Lab and FSK-Lab are listed there. They can be checked and install by clicking on Next or Finish.

# Installing PMM-Lab
![](assets/pmmlab_installation.png)

1. Configure the software site as explained in <a href="#installing-plugins">Installing plugins</a>.
2. Check PMM-Nodes and click *Next*.

# Installing FSK-Lab
FSK-Lab is based on R 3.4, [https://r-project.org](https://r-project.org), which is an essential component for it to function. FSK-Lab is supported on Windows, Mac and Ubuntu.

<div class="col-lg-12">
  <ul id="myTab" class="nav nav-tabs nav-justified">
    <li class="active"><a href="#fsk-windows" data-toggle="tab"><i class="fa fa-tree"></i>Windows</a></li>
    <li class=""><a href="#fsk-mac" data-toggle="tab"><i class="fa fa-car"></i>Mac</a></li>
    <li class=""><a href="#fsk-ubuntu" data-toggle="tab"><i class="fa fa-support"></i>Ubuntu</a></li>
  </ul>

  <div id="myTabContent" class="tab-content">
    <div class="tab-pane fade active in" id="fsk-windows">
      <h4>Installing R with bundles</h4>
      <p>For convenience some bundles with preconfigured R are included in the software site. You can pick either the 32 or 64 bit version depending of your operating system. If you already have R installed on your system and you prefere to use that version, do not check either of the options. The bundles are named:
        <ul>
          <li>R 3.4.4 for 64-bit Windows</li>
          <li>R 3.4.4 for 32-bit Windows</li>
        </ul>
      </p>
      <h4>Installing R manually</h4>
      <p>If R is already installed it can be configured to be used in FSK-Lab. First two R packages need to be installed. These can be easily installed in an R console with the command: <code>install.packages(c('Rserve', 'miniCRAN')).</code>
      </p>
      <p>The path to R needs to be set in Preferences, FSK-Lab settings.
      <img src="assets/fsk_preferences.png" alt="FSK settings">
      </p>
    </div>
    <div class="tab-pane fade" id="fsk-mac">
      <p>On Mac it is necessary to install R 3.4.x from <a href="https://cran.r-project.org/bin/macosx/el-capitan/base/">https://cran.r-project.org/bin/macosx/el-capitan/base/</a>. FSK-Lab on Mac requires three packages: Rserve, miniCRAN and Cairo. These may be installed in the R console with: <code>install.packages(c('Rserve', 'miniCRAN', 'Cairo'))</code>. Besides these three packages, MacOS also requires the XQuartz software which can be obtained at <a href="http://xquartz.macos-forge.org">http://xquartz.macos-forge.org</a>.
      </p>
      <p>
        The path to the R folder to be entered in KNIME (as described for Windows) should be /Library/Frameworks/R.framework/Resources/.
        <img src="assets/fsk_preferences.png" alt="FSK settings">
      </p>
    </div>
    <div class="tab-pane fade" id="fsk-ubuntu">
      <p>
        First, Ubuntu requires some development libraries for the R packages that can be installed with apt-get in the terminal:
        <code>sudo apt-get install libcurl4-openssl-dev libssl-dev libxml2-dev</code>
      </p>
      <p>
        On Mac it is necessary to install the packages Rserve and miniCRAN that can be installed in the R terminal with:
        <code>install.packages(c('Rserve', 'miniCRAN'))</code>.
      </p>
      <p>
        The path to the R folder to be entered in KNIME should be: /usr/lib/R.
      </p>
    </div>
  </div>
</div>

# Recommended optimization of KNIME
{% include warning.html title="Warning" content="A bad configuration can break KNIME and this is only recommended for expert users" %}

The configuration file *KNIME.ini* allows to optimize KNIME. To apply the changes KNIME must be started after saving the changes to this file.

1. The amount of RAM available in KNIME might be extended by changing the value of `-Xmx`. For example `-Xmx4g` indicates that 4GB RAM should be used.
2. If the connection to a KNIME server through KNIME fails, it might help to increase the response time. Simply add the line `-Dcom.knime.enterprise.client.connect-timeout=10000`.
