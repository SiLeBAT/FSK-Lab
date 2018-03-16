# horizontal_transmi_animals = function(n.iter, K, nanim, r, t, n_i0)
# 
# DESCRIPTION: 
# Chicks prevalence after the horizontal transmission
# 
# INPUT:
# K		double  Carrying capacity of a flock
# nanim		integer number of animals in one flock
# r         	double  rate of growth for number of infected animals in one flock at a given stage (i.e. hatchery, truck, hen house)
# t         	double  time animals spend at a given stage given in days (i.e. hatchery, truck, hen house)
# n_i0  	vector  prevalence of flocks at the end of the previous stage in the production cycle (this is inital value which is going to be changed due to horizontal transmission
#
# then there is "elements"; it is not a part of the model it is a help varibale in order to write results in a file on disk, thus it does not appear in the list of independent variables below
# elements      string  can be "flocks" or "animals" and is used in the model script to create file names which store the end results to be used in subsequent model steps
#
#
# OUTPUT:
# Prevalence       vector       within chicks flock prevalence after horizontal transmission 
#
# @author: Carolina Plaza-Rodriguez, Federal Institute for Risk Assessment, Germany, 2015
#          modularized and commented by Guido Correia Carreira, Federal Institute for Risk Assessment, Germany, 2015
###########################################################################################

#source("horizontal_transmi_animals_params.R")

# model independent variable(s) - start
# n.iter, K, nanim, r, t, ni_0
# model independent variable(s) - end

# model dependent variable(s) - start
# Prevalence
# model dependent variable(s) - end


# try to read data from previous step by trying to open a correspponding .dat file
# if there is no such file the error is handled by tryCatch and the default value in the parameter file is retained and used for calculation

out <- tryCatch({n_i0=scan("InitialValues.dat")},error=function(cond){message("no file error")},warning=function(cond){message("no file warning")})


horizontal_transmi_animals = function(n_iter, K, nanim, r, t, n_i0){
  
  Prevalence = (K * nanim * n_i0)/(n_i0 + ((K * nanim - n_i0) * exp(-r * t)))
  return(Prevalence)
}

result <- horizontal_transmi_animals(n_iter, K, nanim, r, t, n_i0)
