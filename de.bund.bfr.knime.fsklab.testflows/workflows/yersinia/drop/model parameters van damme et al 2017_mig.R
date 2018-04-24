library(mvtnorm)
library(mc2d)
library(fitdistrplus)
ndvar(100000)

# Same value as ndvar
iterations <- 100000

# Module 1: Input - Carcass
## Prevalence of Y. enterocolitica on pig carcasses (sternal region) after
## evisceration
Pinitial <- mcdata(0.1639, type="0") 
## Concentration of Y. enterocolitica on pig carcasses (sternal region) after
##evisceration (positive carcasses only)
Cinitial <- mcstoc(rnorm, type="V", mean=-2.565, sd=0.736, rtrunc=TRUE,
                   linf=-1.85) 

# Module 2: Inactivation during carcass chilling - Carcass
Icc <- mcdata(-0.6, type="0")

# Module 3: Growth during carcass cold storage - Carcass
## Cold storage time of carcasses and all head meat and tonsils applied in the
## same batch
Timeccg <- mcstoc(rempiricalD, type="V", values=c(20, 68), prob=c(4, 1)) 
Lagccg <- mcdata(24, type="0") #Lag phase during carcass cold storage
Dccg <- mcdata(9.978, type="0") #Doubling time during cold storage

# Module 4: Cutting and derinding - Belly cut
Sbc <- mcdata(2000, type="0") # Surface of belly cut
## Proportion of Y. enterocolitica that remain on the belly cut after derinding
Rbd <- mcdata(0.50, type="0") 

# Module 5: Mixing and grounding - Batch of minced meat
Wb <- mcdata(900, type="0") # Weight of a batch of minced meat
Bellies <- mcdata(0.34, type="0") # Proportion of bellies per batch (w:w)
Wbc <- mcdata(7.5, type="0") # Weight of a belly cut

# Module 6: Partitioning/packaging - Minced package
Wmp <- mcdata(0.5, type="0") # Weight per minced meat package

# Module 7: Storage at retail - Minced package
## Temperature during storage in meat processing plant and at retail
Temprg <- mcdata(4, type="0") 
Timerg <- mcdata(48, type="0") # Time between packaging and selling at retail

# Module 8: Storage at consumer level - Minced package
## Temperature of home refrigerators
Tempcg <- mcstoc(rpert, type="V", mode=7, max=14.114, min =-0.114) 
## Time between purchase and consumption/preparation
Timecg <- mcstoc(rpert, type="V", mode=1, max=4, min =0) 
