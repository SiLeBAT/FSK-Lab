# This code is based on the predictive microbial model published on: Mejlholm, O. and Dalgaard, P. (2009). Development and validation of 
#an extensive growth and growth boundary model for Listeria monocytogenes in lightly preserved and 
#ready-to-eat shrimp. J. Food Prot. 70 (10), 2132-2143. 

# Please, when applying the model, make reference to the paper above.

# This model is validated for Listeria monocytogenes growth in chilled seafood and meat products and can also be
#found in the user friendly software FSSP (Listeria monocytogenes in chilled seafood and meat products/Growth of
#L. monocytogenes).
#FSSP: http://fssp.food.dtu.dk/

fun <-function(Temp,NaCl,pH,P,CO2equilibrium,NIT,
               AA_wph,BA_wph,CA_wph,DA_wph,LA_wph,SA_wph){
  
Tmin <- 2.83 # ºC
Tref <- 25 # ºC
awmin <- 0.923 
pHmin <- 4.97 
P_max <- 32 # ppm 
NITmax <- 350 # ppm 
CO2max <- 3140 # ppm 
MIC_LACu <- 3.79 # MICs of concentrations (mM) of undissociated lactic acid
MIC_DACu <- 4.8 # MICs of concentrations (mM) of undissociated diacetate
MIC_AACu <- 10.3 # MICs of concentrations (mM) of undissociated acetic acid
MIC_BACu <- 0.349 # MICs of concentrations (mM) of undissociated benzoic acid
MIC_CACu <- 2.119 # MICs of concentrations (mM) of undissociated citric acid
MIC_SACu <- 1.896 # MICs of concentrations (mM) of undissociated sorbic acid
mmaxref <- 0.417 # 1/h 
Nmax <- 3.18E8 # cfu/g
RLT <- 4.5 # Relative lag time (days)
b <- 0.41899729
Tmin2 <- -2.83

## Conversion of factor's units to the ones used in the equations
#CO2
H1 <- exp(-6.8346 + 12817/(Temp+273.15)-3766800/(Temp+273.15)^2+2.997e8/(Temp+273.15)^3) #Henrys's constant
H2 <- 101323*2.4429/H1 #Henrys's constant
CO2 <- CO2equilibrium*H2/100
#water activity 
aw <- 0.999489-0.005179*NaCl-0.0001272*NaCl^2
#Organic acids
AA_total_mM <- AA_wph/60.05
AACu <- AA_total_mM/(1+10^(pH-4.76))
BA_total_mM <- BA_wph/122.12
BACu <- BA_total_mM/(1+10^(pH-4.19))
CA_total_mM <- CA_wph/192.13
CACu <- CA_total_mM/(1+10^(pH-3.13))
DA_total_mM <- DA_wph/119.1
DACu <- DA_total_mM/(1+10^(pH-4.76))
LA_total_mM <- LA_wph/90.08
LACu <- LA_total_mM/(1+10^(pH-3.86))
SA_total_mM <- SA_wph/112.1
SACu <- SA_total_mM/(1+10^(pH-4.76))

##Equations for phi terms for each environmental parameter
phi_temp <- (1-((Temp+Tmin)/(Tref+Tmin)))^2
phi_aw <- (1-sqrt((aw-awmin)/(1-awmin)))^2
phi_pH <- (1-sqrt(1-10^(pHmin-pH)))^2
phi_phenol <- (1-sqrt((P_max-P)/P_max))^2
phi_NIT <- (1-((NITmax-NIT)/NITmax))^2
phi_CO2 <- (1-sqrt((CO2max-CO2)/CO2max))^2
phi_acids <- (1-((1-sqrt(LACu/MIC_LACu))*(1-sqrt(DACu/MIC_DACu))*(1-sqrt(AACu/MIC_AACu))*(1-(BACu/MIC_BACu))*
                   (1-(CACu/MIC_CACu))*(1-(SACu/MIC_SACu))))^2

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

psi <- (psi_temp + psi_aw + psi_pH + psi_phenol + psi_NIT +  psi_CO2 + psi_acids)/2
psi <- ifelse(psi<0,"",psi)

#Equation for xi value
xi <- ifelse(psi>=1, 0, ifelse(psi<=0.5, 1, ((1-psi)*2)))

#Equation for mmax  
#mmax <- mmaxref*(((Temp+Tmin)/(Tref+Tmin))^2)*((aw-awmin)/(1-awmin))*(1-10^(pHmin-pH))*(1-(LACu/MIC_LACu))*
#  ((P_max-P)/P_max)*((NITmax-NIT)/NITmax)^2*((CO2max-CO2)/CO2max)*(1-sqrt(DACu/MIC_DACu))*
#  (1-sqrt(AACu/MIC_AACu))*(1-(BACu/MIC_BACu))*(1-(CACu/MIC_CACu))*(1-(SACu/MIC_SACu))*xi

mmax <- b*((Temp-Tmin2)/(Tref-Tmin2))^2*((aw-awmin)/(1-awmin))*(1-10^(pHmin-pH))*(1-(LACu/MIC_LACu))*
        ((P_max-P)/P_max)*((NITmax-NIT)/NITmax)^2*((CO2max-CO2)/CO2max)*(1-sqrt(DACu/MIC_DACu))*
       (1-sqrt(AACu/MIC_AACu))*(1-(BACu/MIC_BACu))*(1-(CACu/MIC_CACu))*(1-(SACu/MIC_SACu))*xi

mmax <- ifelse(mmax<0,0,mmax)

   return(mmax)
}

result <- fun(Temp,NaCl,pH,P,CO2equilibrium,NIT,AA_wph,BA_wph,CA_wph,DA_wph,LA_wph,SA_wph)


