prev <- matrix(NA, 2, 1)
prev[1] <- sum(N.cg.batches>prev.thrs.1)/iterations
prev[2] <- sum(N.cg.batches>prev.thrs.2)/iterations

layout(rbind(1,2), heights=c(9,1))
plot(ecdf(N.cg.batches.log), xlab='log CFU/package', ylab='Probability', main='')
par(mar=c(0, 0, 0, 0)) #c(bottom, left, top, right)
plot.new()
legend('center', ncol=1, bty ="n",
       legend=c(paste("Prevalence of ",prev.thrs.1,'CFU/package =',prev[1]), 
                paste("Prevalence of ",prev.thrs.2,'CFU/package =',prev[2])))
