########## (The user must define the values of mean and ########## standard deviation that define the initial ########## distribution of concentrations (mu.init/sd.init) )
mu_init <- 1
sd_init <- 1


########## initial guesses of mu, sd.
########## (Note: the user needs to provide an appropriate
########## initial guess for the parameters to be estimated 
########## (mean, standard deviation and prevalence).
########## In this study we define the initial guess in 
########## relation to the known values of the parameters that 
########## were initially used to simulate the input data)
mu_start <- 0.7 # mu.init - 0.3
sd_start <- 0.7 # ax (sd.init - 0.3,-0.05)
