print(YE1)
# hist(YE1)
plot(YE1)

# To sumarize the model output with only modules outputs and percentiles
# sYE1 <- mc(Ccci=YE1$Ccci, Nccg=YE1$Nccg, Nbdr=YE1$Nbdr, Nmp=YE1$Nmp,
#            Nrg=YE1$Nrg, Ncg=YE1$Ncg, Ncg_log=YE1$Ncg_log)
# summary(sYE1, probs = c(0, 0.5, 0.75, 0.85, 0.95, 1))

## To analyze the main model output: Number of Y. enterocolitica in one package
## of minced meat at the end of storage 
# print(YE1$Ncg)
# hist(YE1$Ncg)
# plot(YE1$Ncg)

# To analize the main model output extracting the probabilities of having 1,
# 100, 1000 and 10000 cells of YE in one minced packaged
# Fn <- ecdf(YE1$Ncg)
# plot(Fn,verticals=T, do.points=F, col.01line = NULL)
# print((1 - Fn(1)) * 100)
# print((1 - Fn(100)) * 100)
# print((1 - Fn(1000)) * 100)
# print((1 - Fn(10000)) * 100)
