# Module 2: Inactivation during carcass chilling - Carcass
modCarcassChilling <- function(Cinitial,Icc){
  Ccci_a <- Cinitial + Icc 
  MCcci <- log(1/2000)
  #Concentration on pig carcasses after inactivation during chilling
  Ccci <- ifelse(Ccci_a<MCcci,-1000,Ccci_a) 
  return(list(Ccci=Ccci))
}

# Module 3: Growth during carcass cold storage - Carcass
modCarcassColdStorage <- function(Lagccg,Timeccg,Ccci,Dccg){
  #Number of Y. enterocolitica after growth during cold storage  
  Nccg <- ifelse(Lagccg>Timeccg, 10^Ccci, (10^Ccci) * 2^((Timeccg-Lagccg)/Dccg)) 
  return(list(Nccg=Nccg))
}

# Module 4: Cutting and derinding - Belly cut
modCuttingDerinding <-function(Nccg,Sbc,Rbd){
  #Number of Y. enterocolitica per belly after cutting
  Nbc <- round(Nccg*Sbc,0) 
  # Number of Y. enterocolitica on belly cut after derinding
  Nbdr <- mcstoc(rbinom, size=Nbc, prob=Rbd)  
  return(list(Nbc=Nbc,Nbdr=Nbdr))
}

# Module 5: Mixing and grounding - Batch of minced meat
modMixingGrounding <-function(Wb,Bellies,Wbc,Pinitial,iterations,Nbdr){
  #Number of bellies per batch
  nbb <- round((Wb*Bellies)/Wbc,0) 
  #Number of positive bellies per batch
  npbb <- mcstoc(rbinom, type="V", size=nbb, prob=Pinitial) 
  
  #Randomlly choosing one number of positive bellies per batch
  npbb_sample <- rep(0,iterations)
  # Number of Y. enterocolitica on belly cut (i) after derinding
  Nbdri <- list(mode="vector",length=iterations)
  #Number of Y. enterocolitica in one minced meat batch
  Nmb <- rep(0,iterations)
  
 for(i in 1:iterations){
   #Randomlly choosing one number of positive bellies per batch
    npbb_sample[i] <- sample(npbb,1)
    # creating a vector with the concentration of the positive bellies
    Nbdri[[i]] <- sample(Nbdr,npbb_sample[i]) 
    # Creating a vector with Nmb
    Nmb[i] <- sum(Nbdri[[i]])
  }
  return(list(npbb=npbb, npbb_sample=npbb_sample,Nbdri=Nbdri,Nmb=Nmb))
}

# Module 6: Partitioning/packaging - Minced package
modPartioningPackaging <- function(Wmp,Wb,Nmb){
  #Number of minced meat packages per batch of minced meat
  Nmmp_batch <- Wmp/Wb 
  #Number of Y. enterocolitica in one minced meat package after packaging/partitioning
  Nmp <- mcstoc(rbinom, type="V", size=Nmb, prob=Nmmp_batch) 
  return(list(Nmp=Nmp))
}

# Module 7: Storage at retail - Minced package
modStorageRetail <- function(Temprg,Timerg,Nmp){
  #Maximum growth rate (MAP packaging = 30% CO2)
  mumax_rg <- 0.0003*Temprg^2 + 0.0005*Temprg + 0.0103 
  #Number of Y. enterocolitica in one package of minced meat after storage at retail
  Nrg <- Nmp*10^(mumax_rg*Timerg) 
  return(list(mumax_rg=mumax_rg,Nrg=Nrg))
}

# Module 8: Storage at consumer level - Minced package
modStorageConsumer <- function(Tempcg,Nrg,Timecg){
  #Maximum growth rate (MAP)
  mumax_cg <- ifelse(Tempcg<0,0,0.0003*(Tempcg^2)+0.0005*Tempcg+0.0103) 
  #Number of Y. enterocolitica in one package of minced meat at the end of storage 
  #(just before consumption or preparation)
  Ncg <- Nrg*10^(mumax_cg*Timecg*24)
  Ncg_log <- log(ifelse(Ncg==0,NA,Ncg))
  return(list(mumax_cg=mumax_cg,Ncg=Ncg,Ncg_log=Ncg_log))
}

modelYE <- mcmodel({
  
  # Step 1: Contamination of carcasses
  step1 <- modCarcassChilling(Cinitial, Icc)
  Ccci <- step1$Ccci
  
  # Step 2: Chilling room
  step2 <- modCarcassColdStorage(Lagccg, Timeccg, step1$Ccci, Dccg)
  Nccg <- step2$Nccg
  
  # Step 3: Cutting and derinding
  step3 <- modCuttingDerinding(step2$Nccg, Sbc, Rbd)
  Nbc <- step3$Nbc
  Nbdr <- step3$Nbdr
  
  # Step 4: Grinding and seasoning
  step4 <- modMixingGrounding(Wb, Bellies, Wbc, Pinitial, iterations,
                              step3$Nbdr)
  npbb <- step4$npbb
  Nbdri <- step4$Nbdri
  Nmb <- step4$Nmb
  
  # Step 5: Packaging
  step6 <- modPartioningPackaging(Wmp, Wb, step4$Nmb)
  Nmp <- step6$Nmp
  
  # Step 6: Storage (processing plant and retail)
  step7 <- modStorageRetail(Temprg, Timerg, step6$Nmp)
  mumax_rg <- step7$mumax_rg
  Nrg <- step7$Nrg
  
  # Step 7: Storage (consumer)
  step8 <- modStorageConsumer(Tempcg, step7$Nrg, Timecg)
  mumax_cg <- step8$mumax_cg
  Ncg <-step8$Ncg
  Ncg_log <- step8$Ncg_log
  
  mc(Cinitial, Ccci, Timeccg, Nccg, Nbc, Nbdr, npbb, Nmp, Nrg, Tempcg, Timecg,
     mumax_cg, Ncg, Ncg_log)
})

YE1 <- evalmcmod(modelYE, nsv = 100000)
