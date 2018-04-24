rm(list = ls())

library(mc2d)

# Number of Monte Carlo iterations 
iterations <- 1e5

### Module 1: Input - Carcass ###

#Prevalence of Y. enterocolitica on pig carcasses (sternal region) after evisceration
P.initial <- 0.1639 

#Concentration (Log10 CFU/cm2) of Y. enterocolitica on pig carcasses (sternal region) after evisceration (positive carcasses only)
#The concentration is considered normally distributed with the minimal possible value correspondent
#to the limit of detection of the most sensitive detection method.
mu.c.initial <- -2.565 #Mean value of the concentration in log(CFU/cm2)
sigma.c.initial <- 0.736 #Standard deviation of the mean value
lower.c.inital <- -1.85 #Minimal possible concentration 
#C.initial <- rtnorm(n.bb, mu.c.initial, sigma.c.initial, lower.c.inital)

### Module 2: Inactivation during carcass chilling - Carcass ###

#Inactivation constant (Log10 reduction)
I.cc <- -0.6 

# Module 3: Growth during carcass cold storage - Carcass

#Cold storage time (h) of carcasses and all head meat and tonsils applied in the same batch
Time.ccg.x <- c(20,68) #values
Time.ccg.p <- c(4/5,1/5) #probabilities
#Time.ccg <- sample(Time.ccg.x, 1, prob=Time.ccg.p, replace=TRUE)

#Lag phase during carcass cold storage (h)
lambda.ccg <- 24 

#Doubling time during cold storage (h)
D.ccg <- 9.978 

### Module 4: Cutting and derinding - Belly cut ###

#Surface of belly cut (cm2)
S.bc <- 2000 

#Proportion of Y. enterocolitica that remain on the belly cut after derinding
R.bd <- 0.50

### Module 5: Mixing and grounding - Batch of minced meat ###

#Weight of a batch of minced meat (kg)
W.b <- 900 

#Proportion of bellies per batch (w:w)
P.bellies <-0.34 

#Weight of a belly cut (kg)
W.bc <- 7.5

### Module 6: Partitioning/packaging - Minced package ###

#Weight per minced meat package (kg)
W.mp <- 0.5 

### Module 7: Storage at retail - Minced package ###

#Temperature during storage in meat processing plant and at retail (°C)
Temp.rg <- 4

#Time between packaging and selling at retail (h)
Time.rg <- 48 

### Module 8: Storage at consumer level - Minced package ###

#Temperature of home refrigerators (°C)
mode.Temp.cg <- 7
min.Temp.cg <- -0.114
max.Temp.cg <- 14.114
#Temp.cg <- rpert(1, mode=mode.Temp.cg, min=min.Temp.cg, max=max.Temp.cg)

#Time between purchase and consumption/preparation (h)
mode.Time.cg <- 1
min.Time.cg <- 0
max.Time.cg <- 4
#Time.cg <- rpert(1, mode=mode.Time.cg, min=min.Time.cg, max=max.Time.cg) 

### Final output ###
# Choose a prevalence (CFU/packaged) to be shown as output
prev.thrs.1 <- 1
prev.thrs.2 <- 1e5


