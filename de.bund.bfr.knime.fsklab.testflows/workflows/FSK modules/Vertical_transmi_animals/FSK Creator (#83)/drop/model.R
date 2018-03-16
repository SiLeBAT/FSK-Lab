##########################################################################################
# vertical_transmi_animals = function(n.iter, P_initial, minimum, maximum, most_prob)
# 
# DESCRIPTION: 
# Chicks prevalence after the vertical transmission
# 
# INPUT:
# n.iter         integer       number of iterations
# P_inital       vector        initial prevalence among or within flocks  = prevalence at end of previous step in broiler production chain. 
# minimum        double        minimum value of triangle distribution
# maximum        double        maximum value of triangel distribution
# most_prob      double        moda or most probable value of triangle distribution
#
#
# then there is "elements"; it is not a part of the model it is a help varibale in order to write results in a file on disk, thus it does not appear in the list of independent variables below
# elements      string  can be "flocks" or "animals" and is used in the model script to create file names which store the end results to be used in subsequent model steps
#
# OUTPUT:
# Prevalence     vector       within chick flock prevalence after vertical transmission
#
# @author: Carolina Plaza-Rodriguez, Federal Institute for Risk Assessment, Germany, 2015
#########################################################################################


# model independent variable(s) - start
# n.iter, P_initial, minimum, maximum, most_prob
# model independent variable(s) - end

# model dependent variable(s) - start
# Prevalence
# model dependent variable(s) - end

library(triangle)
#source("vertical_transmi_animals_params.r")

# try to read data from previous step by trying to open a correspponding .dat file
# if there is no such file the error is handled by tryCatch and the default value in the parameter file is retained and used for calculation

out <- tryCatch({P_initial=scan("InitialValues.dat")},error=function(cond){message("Error with previfile")},warning=function(cond){message("no file warning")})

vertical_transmi_animals = function(n.iter, P_initial, minimum, maximum, most_prob){
  
  Prevalence = P_initial + (P_initial*(rtriangle(n.iter, minimum, maximum, most_prob)))
  return(Prevalence)
}

result <- vertical_transmi_animals(n.iter, P_initial, minimum, maximum, most_prob) 
