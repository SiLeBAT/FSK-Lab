#############################################################
#                                                           #
#     QMRA for Salmonella Enteridis in eggs                 #
#                   (model) - Moez Sanaa                    #
#                                                           #
#############################################################



# The risk to consumers posed by Salmonella Enteritidis, the bacteria responsible for causing the
#highest number of egg-borne outbreaks in EU.

# This program allows us to estimate :
#   - Number of illness per million servings of egg (lightly cooked or weel-cooked)
#   - Probability of illness when ingesting a random serving


library(mvtnorm)
library(mc2d)

# Final parameters:
minD<-c(minD1,minD4,minD5,minD6,minD8,minD10)
maxD<-c(maxD1,maxD4,maxD5,maxD6,maxD8,maxD10)
modeD<-c(modeD1,modeD4,modeD5,modeD6,modeD8,modeD10)
minT<-c(minT1,minT4,minT5,minT6,minT8,minT10)
maxT<-c(maxT1,maxT4,maxT5,maxT6,maxT8,maxT10)
modeT<-c(modeT1,modeT4,modeT5,modeT6,modeT8,modeT10)

parm=cbind(minD,modeD,maxD,minT,modeT,maxT)

res=matrix(rep(0,3*sim),ncol=3)

for (j in 1:sim) {
  T=41.2 # temperature
  for (i in 1:nrow(parm)) {
    Ti=T[length(T)]
    Ts=rpert(1,min=minT[i],mode=modeT[i],max=maxT[i])
    times=floor(rpert(1,min=minD[i],mode=modeD[i],max=maxD[i]))
    krs<-ifelse(i==1,rpert(1,min=0.8,mode=0.9,max=1),
                ifelse(i==2,rpert(1,min=25,mode=27,max=30),
                       rpert(1,min=0.066,mode=0.1,max=0.134)))
    # reduction of the internal temperature of egg
    T=c(T,Ts+(Ti-Ts)*exp(-krs*(1:times)))
  } # end of for de i
  a=2.0872
  b=-0.042579
  Tmin=6.29
  Topt=40.11
  Tmax=43.46
  kopt=1.6
  ft=cumsum(1/((10^(a+b*T))*24)) 
  gammaT = ((T - Tmax) * (T - Tmin) ^ 2) / 
    ((Topt - Tmin) * ((Topt - Tmin) * (T - Topt) - (Topt - Tmax) * (Topt + Tmin - 2 * T)))
  NCD=sum((ft>=1)*gammaT*kopt) # growth rate (log10 CFU/g per hour)
  TRMV=which(ft>=1)[1] # give TRVM=1 if breakdown (ft>=1)
  matres=cbind(min(NCD,9),TRMV,round(length(T)/24,1))
  res[j,]<-matres
  
} #end of for de j
#c0 : initial contamination
c0=1+rpois(sim,7)
dosecru=c0*(10^res[,1])
doselcuit=c0*(10^res[,1])*(10^(-rnorm(sim,2,0.5)))
dosebcuit=c0*(10^res[,1])*(10^(-rnorm(sim,12,1)))
beta=53.33348955
alpha=0.134586035
probacru=1-(1+dosecru/beta)^(-alpha)
probalcuit=1-(1+doselcuit/beta)^(-alpha)
probabcuit=1-(1+dosebcuit/beta)^(-alpha)
data=data.frame(res,c0,dosecru,probacru,probalcuit,probabcuit)
names(data)<-c("NCD","TRMV","AGE","CI","CC","Pcru","Plcuit","Pbcuit")

# NCD: growth rate (log10 CFU/g per hour)
# TRMV: yolk membrane breakdown time (days)
# AGE : age of eggs (day)
# CI: Initial contamination: number of S. Enteredis (log10 CFU)
# CC: Dose in uncooked eggs (log10 CFU)
# Pcru: Probability of illness per S. Enteritidis-contaminated serving of uncooked egg
# Plcuit: Probability of illness per S. Enteritidis-contaminated serving of lightly cooked egg
# Pbcuit: Probability of illness per S. Enteritidis-contaminated serving of well-cooked egg


out=data
#head(out)
#tail(out)

# barplot
Rlcuit=(1-freqcuisson)*prevalence*round(1E6*(mean(out[,7])),2) # Rlcuit: Number of illness per million servings of lightly cooked egg
Rbcuit=freqcuisson*prevalence*round(1E6*(mean(out[,8])),2)  # Rbcuit: Number of illness per million servings of well-cooked egg

# plot
Rlcuit_1=prevalence*out[,7] # Rlcuit_1: probability of illness when ingesting a serving of lightly cooked egg
Rbcuit_1=prevalence*out[,8] # Rbcuit_1: probability of illness when ingesting a serving of well-cooked egg
Risque=freqcuisson*Rbcuit_1+(1-freqcuisson)*Rlcuit_1 # Probability of illness when ingesting a random serving of egg

