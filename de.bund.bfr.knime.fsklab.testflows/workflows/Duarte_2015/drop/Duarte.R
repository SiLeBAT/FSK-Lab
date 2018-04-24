## Code from "Fitting a distribution to microbial counts: making sense of zeroes" ##
## Duarte, A.S.R.; Stockmarr, A.; Nauta, M.J., 2014 ##
## asrd@food.dtu.dk ##
#####################################################################################
# This method fits a zero-inflated Poisson lognormal distribution to in silico
# generated bacterial plate counts. It assumes a protocol with four 10-fold serial
# dilution steps, 3 replicates per dilution.
# Note that own input data can be used in place of simulated counts. In that case,
# parameters need to be adapted to the data collection protocol used.
#####################################################################################
########## define where to store outputs ##########
########## a) estimated parameters

mu.loop <- numeric () ; 
sd.loop <- numeric (); 
prev.loop <- numeric ()

########## b) expected frequency of positive and zero counts
obs.counts.loop <- numeric()
obs.counts1 <- numeric () ;obs.counts2 <- numeric ()
obs.counts3 <- numeric () ;obs.counts4 <- numeric ()
obs.counts5 <- numeric () ;obs.counts6 <- numeric ()
obs.counts7 <- numeric () ;obs.counts8 <- numeric ()
obs.counts9 <- numeric () ;obs.counts10 <- numeric ()
obs.counts11 <- numeric () ;obs.counts12 <- numeric ()
obs.counts13 <- numeric () ;obs.counts14 <- numeric ()
obs.counts15 <- numeric () ;obs.counts16 <- numeric ()
obs.counts17 <- numeric () ;obs.counts18 <- numeric ()
obs.counts19 <- numeric () ;obs.counts20 <- numeric ()
obs.zeroes.loop <- numeric ()

########## define number of loops (model simulations)
n <- 2   
for(l in 1:n){
 begTime <- Sys.time()

########## simulate and sample concentrations (500 iterations) ##########
########## (The user must define the values of mean and standard deviation that
########## define the initial distribution of concentrations (mu.init/sd.init) )
# mu.init <- 1
# sd.init <- 1
 z <- rnorm(500, mu_init, sd_init)
 x <- 10^z
 x <- rep(x,4)

########## create dilution levels
 dil <- c(rep(1/10,500),rep(1/100,500),rep(1/1000,500),rep(1/10000,500))

########## simulate counts (3 replicates/dilution)
 y <- rpois(2000,3*x*dil)

########## sum the counts from different dilutions for each sample unit
 y_sum <- numeric(500)
 for(i in 1:500){
    y_sum[i] = (y[i]+y[i+500]+y[i+1000]+y[i+1500])
 }

########## define true prevalence and inflate sample with true zeroes
 true.prevalence <- 0.1
 obs.true.zeroes <- ((1-true.prevalence) * 500) / true.prevalence
 y_sum <- c(y_sum,rep(0,obs.true.zeroes))
 y_sum

########## calulate initial prevalence (= true.prevalence)
 prev.init <- 500/(500+obs.true.zeroes)

########## calculate total dilution factor (4 dilutions; 3 replicates/dilution)
 total.dil <- ((1/10)*3)+((1/100)*3)+((1/1000)*3)+((1/10000)*3)

########## create lognormal density function for concentrations w in CFU/g
 f <- function(w,mu,sd){exp(-(log10(w)-mu)^2/(2*sd^2))/(w*log(10)*sd*sqrt(2*pi))}

########## create counts
 prop.temp <- (1- (length(y_sum[y_sum>0])/500))
 prop <- ifelse(length(y_sum[y_sum>0])/500 <= 0.10, 0.5, prop.temp+0.10)
 n.temp <- sort(y_sum[y_sum>0])[round(length(y_sum[y_sum>0])*prop)]
 counts <- numeric(n.temp+1)
 for(m in 1:n.temp){counts[m] <- length(y_sum[y_sum==m])}
 counts[n.temp+1] <- length(y_sum[y_sum>n.temp])
 zeroes <- length(y_sum[y_sum==0])

########## simulated observed frequencies of positive counts and zeroes
 counts ; zeroes

##########fitting procedure##########
 r2.temp <- array(dim=c(6,5,5))

########## initial guesses of mu, sd.
########## (Note: the user needs to provide an appropriate initial guess for the
########## parameters to be estimated (mean, standard deviation and prevalence).
########## In this study we define the initial guess in relation to the known values
########## of the parameters that were initially used to simulate the input data)
# mu.start <- mu.init - 0.3
# sd.start <- max (sd.init - 0.3,-0.05)
 for(i in 1:6){
  for(j in 1:5){
   mu.temp <- i/10 + mu_start
   sd.temp <- j/10 + sd_start

########## density with specific mu, sd
   f.temp <- function(w){f(w,mu.temp,sd.temp)}

########## calculate the probability of a positive count given infection
   q.pos <- (1-sum(exp(-(1:50000/100)*total.dil)*f.temp(1:50000/100))/100)

########## initial guess of prev
   prev.start <- prev.init - 0.125
   for(k in 1:5){
    prev.temp <- k/20 -0.5/20 + prev.start

########## vector of probabilities of positive counts
    probabilities.temp <- numeric(n.temp+1)

########## calculate probabilities
    for(m in 1:n.temp){
     phi1.temp<-function(w){dpois(m,w*total.dil)*f.temp(w)/q.pos}
     probabilities.temp[m]<-(sum(phi1.temp(1:50000/100))/100)
    }
    probabilities.temp[n.temp+1]<-1-sum(probabilities.temp[1:n.temp])

########## expected frequency of positive counts
    expected_temp <- probabilities.temp*length(y_sum)*q.pos*prev.temp

########## calculate the adjusted minimum SSD between observed and estimated positive
########## and zero counts
    r2.temp[i,j,k] <- sum((counts-expected_temp)^2/expected_temp)+(length(y_sum)*(1-prev.temp+prev.temp*(1-q.pos))-zeroes)^2/(length(y_sum)*(1-prev.temp+prev.temp*(1-q.pos)))

########## time1 = time for 25 iterations (i * 5j * 5k)
    runTime1 <- Sys.time()-begTime
    cat("i=",mu.temp,"j = ",sd.temp," k = ",prev.temp," ssd ",r2.temp[i,j,k],"\n")
   }
   cat("i=",i,"j = ",j," finished!!!!","\n")
  }
  cat("i = ",i,"finished!!!!! \n","time1=",runTime1,"\n")
 }
 index.r2.temp <- array(rep(0,6*5*5),dim=c(6,5,5))
 for(i in 1:6){index.r2.temp[i, ,]<-index.r2.temp[i, ,]+i-1}
 for(j in 1:5){index.r2.temp[ ,j,]<-index.r2.temp[,j,]+(j-1)*100}
 for(k in 1:5){index.r2.temp[ ,,k]<-index.r2.temp[, ,k]+(k-1)*10000}

########## minimum score of SSD
 index.r2.temp[r2.temp==min(r2.temp)]
 mu.estimate <-(index.r2.temp[r2.temp==min(r2.temp)]-floor(index.r2.temp[r2.temp==min(r2.temp)]/100)*100+1)/10 + mu_start
 sd.estimate <-(floor((index.r2.temp[r2.temp==min(r2.temp)]-floor(index.r2.temp[r2.temp==min(r2.temp)]/10000)*10000)/100)+1)/10 + sd_start
 prev.estimate <-(floor(index.r2.temp[r2.temp==min(r2.temp)]/10000)+1)/20 -0.5/20 + prev.start

########## Model outputs ##########
########## mu, sd and prev of the different simulations
 mu.loop[l] <- mu.estimate
 sd.loop[l] <- sd.estimate
 prev.loop[l] <- prev.estimate

########## expected positive and zero counts per simulation
 obs.counts1[l] <- counts[1];obs.counts2[l]<-counts[2]
 obs.counts3[l]<-counts[3];obs.counts4[l]<-counts[4]
 obs.counts5[l]<-counts[5];obs.counts6[l]<-counts[6]
 obs.counts7[l]<-counts[7];obs.counts8[l]<-counts[8]
 obs.counts9[l]<-counts[9];obs.counts10[l]<-counts[10]
 obs.counts11[l]<-counts[11];obs.counts12[l]<-counts[12]
 obs.counts13[l]<-counts[13];obs.counts14[l]<-counts[14]
 obs.counts15[l]<-counts[15];obs.counts16[l]<-counts[16]
 obs.counts17[l]<-counts[17];obs.counts18[l]<-counts[18]
 obs.counts19[l]<-counts[19];obs.counts20[l]<-counts[20]
 obs.counts.loop <- c( obs.counts1,obs.counts2,obs.counts3,obs.counts4,obs.counts5,obs.counts6,obs.counts7,obs.counts8,obs.counts9,obs.counts10,obs.counts11,obs.counts12,obs.counts13,obs.counts14, obs.counts15,obs.counts16,obs.counts17,obs.counts18,obs.counts19,obs.counts20)
 obs.counts <- matrix(obs.counts.loop, nrow = n, byrow = F)
 obs.zeroes.loop[l] <- zeroes
 obs.zeroes <- matrix(obs.zeroes.loop, nrow = n, byrow = F)

########## create a column for counts>20
 high.counts <- (500+obs.true.zeroes) - (rowSums(obs.counts)+obs.zeroes)
 obs.counts <- cbind(obs.counts, high.counts)

########## check if the total observations match the total simulated
 rowSums(obs.counts)+obs.zeroes

########## call outputs of each simulation (l loop)
########## mu, sd and prev
 mu.loop
 sd.loop
 prev.loop
 parameters <- data.frame (mu.loop,sd.loop,prev.loop)

########## expected counts
 obs.counts <- data.frame(obs.counts)
 obs.zeroes
 obs.true.zeroes
 obs.art.zeroes <- obs.zeroes - obs.true.zeroes
 obs.art.zeroes

########## final estimates of parameters
 mu.final <- c(round(mean(mu.loop),2), round(sd (mu.loop),2))
 sd.final <- c(round(mean(sd.loop),2),round( sd (sd.loop),2))
 prev.final <- c(round(mean(prev.loop),2), round(sd (prev.loop),2))

########## time 2 = time for 1 simulation l (6i * 5j * 5k)
 runTime2 <- Sys.time()-begTime
 cat("l = ",l,"finished!!!!!","time2=",runTime2,"\n")
}

########## call final estimates (mean and standard deviation of parameters obtained
########## in all simulations)
# mu.final
# sd.final
# prev.final