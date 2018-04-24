##########################################################################################
# initialize_parents_animals = function(n.iter, Npos, Ntotal)
# 
# DESCRIPTION: 
# Initialize prevalence in parent flocks
# 
# INPUT:
# n. iter           integer      number of iterations
# Npos		    integer      number of positive animals in a flock
# Ntotal	    integer	 total number of animals in a flock	
#
# then there is "elements"; it is not a part of the model it is a help varibale in order to write results in a file on disk, thus it does not appear in the list of independent variables below
# elements          string       can be "flocks" or "animals" and is used in the model script to create file names which store the end results to be used in subsequent model steps
#
# OUTPUT:
# Prevalence             vector       prevalence of ESBL among or within flock (depending on the values of Npos and Ntotal)
#
# @author: Carolina Plaza-Rodriguez, Federal Institute for Risk Assessment, Germany, 2015
#          Alteration and modularisation by Guido correia Carreira, Federal Institute for Risk Assessment, Germany, 2016
#########################################################################################


#source("init_animal_params.r")

# model independent variable(s) - start
# n.iter, Npos, Ntotal
# model independent variable(s) - end

# model dependent variable(s) - start
# Prevalence
# model dependent variable(s) - end

initialize_parents_animals = function(n.iter, Npos, Ntotal){
  
  Prevalence = 100*(rbeta(n.iter, shape1 = Npos + 1, shape2 = Ntotal - Npos + 1 ))
  
  return(Prevalence)
}

result <- initialize_parents_animals(n.iter, Npos, Ntotal)
