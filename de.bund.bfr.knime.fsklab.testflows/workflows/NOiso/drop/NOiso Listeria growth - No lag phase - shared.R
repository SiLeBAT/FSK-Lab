library(deSolve)

logistB <- function(t, x, parms) {
  with(as.list(parms), {
    Temp <- Tempprofile(t)
    
    H1 <- exp(-6.8346 + 12817 / (Temp + 273.15) - 3766800 / (Temp + 273.15) ^ 2 + 2.997e8 / (Temp + 273.15) ^ 3)  # Henrys's constant
    H2 <- 101323 * 2.4429 / H1  # Henrys's constant
    CO2 <- CO2equilibrium * H2 / 100
    
    ## Conversion of factor's units to the ones used in the equations
    # CO2
    H1 <- exp(-6.8346 + 12817 / (Temp + 273.15) - 3766800 / (Temp + 273.15) ^ 2 + 2.997e8 / (Temp + 273.15) ^ 3)  # Henrys's constant
    H2 <- 101323 * 2.4429 / H1  # Henrys's constant
    CO2 <- CO2equilibrium * H2 / 100

    # water activity 
    aw <- 0.999489 - 0.005179 * NaCl - 0.0001272 * NaCl ^ 2
    
    # Organic acids
    AA_total_mM <- AA_wph / 60.05
    AACu <- AA_total_mM / (1 + 10 ^ (pH - 4.76))
    BA_total_mM <- BA_wph / 122.12
    BACu <- BA_total_mM / (1 + 10 ^ (pH - 4.19))
    CA_total_mM <- CA_wph / 192.13
    CACu <- CA_total_mM / (1 + 10 ^ (pH - 3.13))
    DA_total_mM <- DA_wph / 119.1
    DACu <- DA_total_mM / (1 + 10 ^ (pH - 4.76))
    LA_total_mM <- LA_wph / 90.08
    LACu <- LA_total_mM / (1 + 10 ^ (pH - 3.86))
    SA_total_mM <- SA_wph / 112.1
    SACu <- SA_total_mM / (1 + 10 ^ (pH - 4.76))
    
    ## Equations for phi terms for each environmental parameter
    phi_temp <- (1 - ((Temp + Tmin) / (Tref + Tmin))) ^ 2
    phi_aw <- (1 - sqrt((aw - awmin) / (1 - awmin))) ^ 2
    phi_pH <- (1 - sqrt(1 - 10 ^ (pHmin - pH))) ^ 2
    phi_phenol <- (1 - sqrt((P_max - P) / P_max)) ^ 2
    phi_NIT <- (1 - ((NITmax - NIT) / NITmax)) ^ 2
    phi_CO2 <- (1 - sqrt((CO2max - CO2) / CO2max)) ^ 2
    phi_acids <- (1 - ((1-sqrt(LACu/MIC_LACu)) * (1-sqrt(DACu/MIC_DACu)) * (1-sqrt(AACu/MIC_AACu)) *
                         (1-(BACu/MIC_BACu)) * (1-(CACu/MIC_CACu)) * (1-(SACu/MIC_SACu)))) ^ 2
    
    ##Equations for phi terms for each environmental parameter
    # cartesian product with all environmental parameters: (total 7 phi calculated)
    # (1-phi_temp)*(1-phi_aw)*(1-phi_pH)*(1-phi_phenol)*(1-phi_NIT)*(1-phi_CO2)*(1-phi_acids)
    psi_temp <- phi_temp/((1-phi_aw)*(1-phi_pH)*(1-phi_phenol)*(1-phi_NIT)*(1-phi_CO2)*(1-phi_acids))
    psi_aw <- phi_aw/((1-phi_temp)*(1-phi_pH)*(1-phi_phenol)*(1-phi_NIT)*(1-phi_CO2)*(1-phi_acids))
    psi_pH <- phi_pH/((1-phi_temp)*(1-phi_aw)*(1-phi_phenol)*(1-phi_NIT)*(1-phi_CO2)*(1-phi_acids))
    psi_phenol <- phi_phenol/((1-phi_temp)*(1-phi_aw)*(1-phi_pH)*(1-phi_NIT)*(1-phi_CO2)*(1-phi_acids))
    psi_NIT <- phi_NIT/((1-phi_temp)*(1-phi_aw)*(1-phi_pH)*(1-phi_phenol)*(1-phi_CO2)*(1-phi_acids))
    psi_CO2 <- phi_CO2/((1-phi_temp)*(1-phi_aw)*(1-phi_pH)*(1-phi_phenol)*(1-phi_NIT)*(1-phi_acids))
    psi_acids <- phi_acids/((1-phi_temp)*(1-phi_aw)*(1-phi_pH)*(1-phi_phenol)*(1-phi_NIT)*(1-phi_CO2))
    
    psi <- (psi_temp + psi_aw + psi_pH + psi_phenol + psi_NIT +  psi_CO2 + psi_acids) / 2
    
    xi <- ifelse(psi>=1, 0, ifelse(psi<=0.5, 1, ((1-psi)*2)))
    
    mmax <- b*((Temp-Tmin2)/(Tref-Tmin2))^2*((aw-awmin)/(1-awmin))*(1-10^(pHmin-pH))*(1-(LACu/MIC_LACu))*
      ((P_max-P)/P_max)*((NITmax-NIT)/NITmax)^2*((CO2max-CO2)/CO2max)*(1-sqrt(DACu/MIC_DACu))*
      (1-sqrt(AACu/MIC_AACu))*(1-(BACu/MIC_BACu))*(1-(CACu/MIC_CACu))*(1-(SACu/MIC_SACu))*xi
    
    dx <- mmax * x[1] * (1 - x[1]/Nmax)
    list(dx,mmax=mmax, Temp=Temp)
  })
}


#setwd("~/FSSP/Listeria growth model non-isothermical conditions")
 
# This are different temperature/time profiles that I'm testing (the files are send as an attachment to the email)

# You have to choose one of them to continue 

# Temperature/time profiel 1
Temp_profile <- read.table("Temp_profile.txt", header=TRUE)

time <- seq(from = 0, to = 316, by = 2)
Tempprofile <- approxfun(Temp_profile) 

# Temperature/time profiel 2
Temp_profile <- read.table("Temp_profileFSSP.txt", header=TRUE)
time <- seq(from = 0, to = 1270, by = 5)
Tempprofile <- approxfun(Temp_profile) 

parms <- c(Ni = N0, NaCl = NaCl, pH = pH, P = P, CO2equilibrium = CO2equilibrium, 
           NIT = NIT, AA_wph = AA_wph, BA_wph = BA_wph, CA_wph = CA_wph, DA_wph = DA_wph,
           LA_wph = LA_wph, SA_wph = SA_wph)
x <- c(N = N0)
