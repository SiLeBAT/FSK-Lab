par(mfrow=c(2,2))

## both "out" are the same 
print(out <- as.data.frame(rk4(x, time, logistB, parms)))
print(out <- as.data.frame(ode(x, time, logistB, parms, method = rkMethod("rk4"))))

## To plot the results 
plot((out$time)/24, log10(out$N), ylim = c(0, 10), xlim = c(0, 60),
     type = "l", col = "red", lwd = 2)
plot((out$time)/24, out$mmax, ylim = c(0, .05), xlim = c(0, 60),
     type = "l", col = "red", lwd = 2)
plot((out$time)/24, out$Temp, ylim = c(0, 15), xlim = c(0, 60),
     type = "l", col = "red", lwd = 2)