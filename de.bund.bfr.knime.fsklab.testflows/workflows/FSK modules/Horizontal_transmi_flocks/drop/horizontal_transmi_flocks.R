###########################################################################################################################
# horizontal_transmi_flocks = function(n.iter, h_same, h_prev, ho, n_f, t_f, p_pf, n_i0)
# 
# DESCRIPTION: 
# Calculates flock prevalence in the hatchery or broiler farm after the horizontal transmission of ESBL E.Coli
# 
# INPUT:
# n.iter	integer Number of iterations
# h_same 	double 	dimensionless parameter describing horizontal transfer between flocks from different houses/incubators at the same place (farm/hatchery) and in thesame production cycle 
# h_prev 	double  dimensionless parameter describing horizontal transfer between a flock in the current production cycle and the flock in the previous production cycle in the same #house/incubator/truck 
# ho		double  dimensionless parameter which describes all other sources of bacterial introduction into the house/incubator/truck 
# n_f		integer number of flocks (= number of houses/incubatory) at farm
# t_f 		double  duration of current production step given in days
# p_pf 		double  prevalence of infected flocks in previous production cycle
# n_i0  	vector  prevalence of flocks at the end of the previous stage in the production cycle (this is inital value which is going to be changed due to horizontal transmission)
#
# then there is "elements"; it is not a part of the model it is a help varibale in order to write results in a file on disk, thus it does not appear in the list of independent variables below
# elements      string  can be "flocks" or "animals" and is used in the model script to create file names which store the end results to be used in subsequent model steps
#
# OUTPUT:
# Prevalence            vector  flock prevalence in the hatchery or broiler farm or transport after horizontal transmission
#
# @author: Carolina Plaza-Rodriguez, Federal Institute for Risk Assessment, Germany, 2015
# modularized and commented by Guido Correia Carreira, Federal Institute for Risk Assessment, Germany, 2016
###########################################################################################################################



#source("horizontal_transmi_flock_params.R")

# model independent variable(s) - start
# n.iter, h_same, h_prev, ho, n_f, t_f, p_pf, n_i0
# model independent variable(s) - end

# model dependent variable(s) - start
# Prevalence
# model dependent variable(s) - end


# try to read data from previous step by trying to open a correspponding .dat file
# if there is no such file the error is handled by tryCatch and the default value in the parameter file is retained and used for calculation

out <- tryCatch({n_i0=scan("InitialValues.dat")},error=function(cond){message("no file error")},warning=function(cond){message("no file warning")})


horizontal_transmi_flocks = function(n.iter, h_same, h_prev, ho, n_f, t_f, p_pf, n_i0){
   


    # determine n_icf_prev = prevalence of infected flock in the previous production cycle

    prev <- runif(n.iter)
    n_icf_prev <-(prev <= p_pf)*1

    # this was a two step process here.

    # first: the array "prev" with "n.iter" array elements is constructed by drawing "n.iter" times from the uniform distribution 
    # (each draw gives a value between 0 and 1. Each value between 0 and 1 is equally likely).

    # second: every element of array "prev" is checked if it is smaller or equal than a user set value for the prevalence of infected 
    # flocks at the same place (truck, incubator, broiler house) in the previous production cycle (i.e. back in time). If the array 
    # element is smaller or equal than p_pcf then
    # "prev <= params_horizontal$p_pcf" has the value "TRUE". If this logical variable type is multiplied by a number(i.e.numerical data type), here the number 1, the result will 
    # be coerced into the numerical type thus changing the logical value "TRUE" to the numerical value "1".If the array element is larger than p_pcf then
    # "prev<=params_horizontal$p_pcf" gets a value of FALSE which is by multiplication with unity coerced into the value "0".
    # Since we have drawn the "prev" elements from a uniform distribution all values are equally likely and the two steps lead to an array with n.iter elements
    # of which a proportion of p_pcf elements are infected. 

    # This is ok to do since the iteration of 200 corresponds to 200 independent broiler production chains which are considered in this model.
    # Furthermore this model is set up as one-time-run-model. This means that even though the production cycle hat a time component there is only
    # one process cycle considered (more precisely n.iter cylces). Yes, there might be n.iter = 200 production cycles under consideration, but
    # they are independent insofar as each stand for itself and it is NOT the case that the first cycle is followed by the second one up to 200th cycle.
    # The production cycle has no evolutionary history, knows nothing about its past and one wants to come up with a past since the past should influence the 
    # development of the prevalence in a certain step of the current production cycle (if the prevalence of infeciton among flocks was huge in the 
    # previous production cycle then the model should account for that by having the prevalence in the current cycle be higher).
    



# apply formula of horizontal transfer model for flocks

  Prevalence = ((((1/(1+exp(-((h_same*(n_i0/n_f))+(h_prev*n_icf_prev)+ho))))/42)*t_f)*(100-(n_i0)))+(n_i0)

  return(Prevalence)
}

result <- horizontal_transmi_flocks(n.iter, h_same, h_prev, ho, n_f, t_f, p_pf, n_i0)
