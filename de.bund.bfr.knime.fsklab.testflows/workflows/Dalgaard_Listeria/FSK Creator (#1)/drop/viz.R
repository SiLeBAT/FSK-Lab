# To plot results from the R code "Dalgaard_L monocytogenes growth_parameter script"
# This code is based on the model published on: Mejlholm, O. and Dalgaard, P. (2009). Development and validation of 
#an extensive growth and growth boundary model for Listeria monocytogenes in lightly preserved and 
#ready-to-eat shrimp. J. Food Prot. 70 (10), 2132-2143. 

# Please, when applying the model, make reference to the paper above.

# This model is validated for Listeria monocytogenes growth in chilled seafood and meat products and can also be
#found in the user friendly software FSSP (Listeria monocytogenes in chilled seafood and meat products/Growth of
#L. monocytogenes).
#FSSP: http://fssp.food.dtu.dk/

plot(Temp,result, type = "l", col="red", xlab = "Temperature (°C)",
     ylab = "mmax (1/h)")
