package de.bund.bfr.knime.fsklab.v2_0.fskenvironmentcreator;


public class EnvironmentYAML {

    public static String getPython2EnvContent(String envName) {
        return "name: "+envName+"\n"
               + "channels:\n"
               + "  - defaults\n"
               + "dependencies:\n"
               + "  - python=2.7\n";
    }

    public static String getPython3EnvContent(String envName) {
        return "name: "+envName+"\n"
               + "channels:\n"
               + "  - defaults\n"
               + "dependencies:\n"
               + "  - python=3.9\n";
    }

    public static String getR3EnvContent(String envName) {
        return "name: "+envName+"\n"
               + "channels:\n"
               + "  - defaults\n"
               + "  - conda-forge\n"
               + "dependencies:\n"
               + "  - r-base=3.6\n"
               + "  - r-rserve\n"
               + "  - r-svglite\n"
               + "  - r-jsonlite\n"
               + "  - r-tidyverse\n";
    }

    public static String getR4EnvContent(String envName) {
        return "name: "+envName+"\n"
               + "channels:\n"
               + "  - conda-forge\n"
               + "  - defaults\n"
               + "dependencies:\n"
               + "  - r-base=4.2.2\n"
               + "  - r-rserve\n"
               + "  - r-svglite\n"
               + "  - r-jsonlite\n"
               + "  - r-tidyverse\n";
    }
}
