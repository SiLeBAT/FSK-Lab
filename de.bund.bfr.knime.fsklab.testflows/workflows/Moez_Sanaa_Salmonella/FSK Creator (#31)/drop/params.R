#############################################################
#                                                           #
#     QMRA for Salmonella Enteridis in eggs                 #
#                 (parameters) - Moez Sanaa                 #
#                                                           #
#############################################################



#############################
# Time (hours):
#############################

# Farm:
minD1<-2
modeD1<-6
maxD1<-13

# Grading:
minD4<-1
modeD4<-2
maxD4<-4

# Transport:
minD5<-7
modeD5<-48
maxD5<-72

# Storage (wholesaler):
minD6<-1
modeD6<-5
maxD6<-24

# Retail:
minD8<-1
modeD8<-24
maxD8<-72

# Household: 
minD10<-1
modeD10<-24
maxD10<-336


#############################
# Temperature (°C):
#############################

# Farm: 
minT1<-29
modeT1<-30
maxT1<-35

# Grading:
minT4<-25
modeT4<-27
maxT4<-30

# Transport:
minT5<-28
modeT5<-30
maxT5<-33

# Storage: 
minT6<-25
modeT6<-27
maxT6<-30

# Retail:
minT8<-20
modeT8<-25
maxT8<-30

# Household:
minT10<-6
modeT10<-15
maxT10<-30

#############################
# simulation parameters:
#############################

# Iteration number
sim<-2000 
# min<-1000,max<-10000,step<-1000

# Prevalence of egg contamination
prevalence<-0.1 
# min<-0,max<-1

# Frequency of well-cooked eggs
freqcuisson<-0.9 
# min<-0,max<-1
