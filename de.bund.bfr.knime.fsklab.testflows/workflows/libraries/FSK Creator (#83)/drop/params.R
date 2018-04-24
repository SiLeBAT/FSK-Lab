# library command to test automatic installation of libraries from the parameters script
library(stringr)

# Parameters for prevalence among ANIMALS (within one flock)
# Npos:    number of positive flocks (i.e. flocks with at least one positive animal)
# Ntotal:  overall number of flocks
# elements: can be "flocks" or "animals" and is used in the model script to create file names which store the end results to be used in subsequent model steps

n.iter <- 200
Npos <- 20
Ntotal <- 100
