### Module 1: Input - Carcass ### 

modCarcassInput <- function(n.bb, mu.c.initial, sigma.c.initial, lower.c.inital){
  C.initial <- rtnorm(n.bb, mu.c.initial, sigma.c.initial, lower.c.inital)
  return(list(C.initial=C.initial))
}

### Module 2: Inactivation during carcass chilling - Carcass ###

modCarcassChilling <- function(C.initial, I.cc, S.bc){
  C.cci.a <- C.initial + I.cc
  MC.cci <- log(1/S.bc)
  C.cci <- ifelse(C.cci.a>MC.cci, C.cci.a, -1000) 
  return(list(C.cci=C.cci))
}

### Module 3: Growth during carcass cold storage - Carcass ###

modCarcassColdStorage <- function(Time.ccg.x, Time.ccg.p, lambda.ccg, D.ccg, C.cci){
  Time.ccg <- sample(Time.ccg.x, 1, prob=Time.ccg.p, replace=TRUE) 
  
  if (Time.ccg<lambda.ccg){
    N.ccg <- 10^(C.cci)
  } else {
    N.ccg <- 10^(C.cci)*2^((Time.ccg-lambda.ccg)/D.ccg)
  }
  
  return(list(Time.ccg=Time.ccg, N.ccg=N.ccg))
}

### Module 4: Cutting and derinding - Belly cut ###

modCuttingDerinding <- function(N.ccg, S.bc, R.bd, n.bb){
  N.bc <- round(N.ccg*S.bc,0)
  
  N.bdr <- array(NA, n.bb)
  for (i in 1:n.bb) N.bdr[i] <- rbinom(1, N.bc[i], prob=R.bd)
  
  return(list(N.bc=N.bc, N.bdr=N.bdr))
}

### Module 5: Mixing and grounding - Batch of minced meat ###

modMixingGrounding <- function(P.initial, n.bb, N.bdr, W.b, P.bellies, W.bc){
  n.pbb <- rbinom(1, n.bb, prob=P.initial)
  I.pbb <- sort(sample(seq(1,n.bb,by=1), n.pbb)) #Indicator of the positive bellies
  N.mb <- sum(N.bdr[I.pbb])
  return(list(n.pbb=n.pbb, N.mb=N.mb))
}

### Module 6: Partitioning/packaging - Minced package ###

modPartioningPackaging <- function(W.mp, W.b, N.mb){
  N.mp <- rbinom(1, N.mb, prob=W.mp/W.b)
  return(list(N.mp=N.mp))
}

### Module 7: Storage at retail - Minced package ###

modStorageRetail <- function(Temp.rg, Time.rg, N.mp){
  mu.max.rg <- 0.0003*Temp.rg^2 + 0.0005*Temp.rg + 0.0103
  N.rg <- N.mp*10^(mu.max.rg*Time.rg)
  return(list(mu.max.rg=mu.max.rg, N.rg=N.rg))
}

### Module 8: Storage at consumer level - Minced package ###

modStorageConsumer <- function(mode.Temp.cg, min.Temp.cg, max.Temp.cg, mode.Time.cg, min.Time.cg, max.Time.cg, N.rg){
  Temp.cg <- rpert(1, mode=mode.Temp.cg, min=min.Temp.cg, max=max.Temp.cg)
  Time.cg <- rpert(1, mode=mode.Time.cg, min=min.Time.cg, max=max.Time.cg) 
  mu.max.cg <- ifelse(Temp.cg<0, 0, 0.0003*Temp.cg^2 + 0.0005*Temp.cg + 0.0103)
  N.cg <- N.rg*10^(mu.max.cg*Time.cg*24)
  
  N.cg.log <- log(ifelse(N.cg==0,NA,N.cg))
  
  return(list(mu.max.cg=mu.max.cg, N.cg=N.cg, N.cg.log=N.cg.log))
}

# Truncated normal distribution random number generator
rtnorm <- function(n, mu, sd, lower){
  lower.std = (lower-mu)/sd
  U = runif(n)
  sample = qnorm(pnorm(lower.std) + U*(1-pnorm(lower.std)))*sd + mu
  return(sample)
}

n.bb <- round((W.b*P.bellies)/W.bc) #Number of bellies per batch

N.cg.batches <- array(NA, iterations)
N.cg.batches.log <- array(NA, iterations)

ptm <- proc.time() #set aclock

for (k in 1:iterations){
  step.1 <- modCarcassInput(n.bb, mu.c.initial, sigma.c.initial, lower.c.inital)
  C.initial <- step.1$C.initial
  
  step.2 <- modCarcassChilling(C.initial, I.cc, S.bc)
  C.cci <- step.2$C.cci
  
  step.3 <- modCarcassColdStorage(Time.ccg.x, Time.ccg.p, lambda.ccg, D.ccg, C.cci)
  N.ccg <- step.3$N.ccg
  
  step.4 <- modCuttingDerinding(N.ccg, S.bc, R.bd, n.bb)
  N.bc <- step.4$N.bc
  N.bdr <- step.4$N.bdr
  
  step.5 <- modMixingGrounding(P.initial, n.bb, N.bdr, W.b, P.bellies, W.bc)
  n.pbb <- step.5$n.pbb
  N.mb <- step.5$N.mb
  
  step.6 <- modPartioningPackaging(W.mp, W.b, N.mb)
  N.mp <- step.6$N.mp
  
  step.7 <- modStorageRetail(Temp.rg, Time.rg, N.mp)
  mu.max.rg <- step.7$mu.max.rg
  N.rg <- step.7$N.rg
  
  step.8 <- modStorageConsumer(mode.Temp.cg, min.Temp.cg, max.Temp.cg, mode.Time.cg, min.Time.cg, max.Time.cg, N.rg)
  mu.max.cg <- step.8$mu.max.cg
  N.cg <- step.8$N.cg
  N.cg.log <- step.8$N.cg.log
  
  N.cg.batches[k] <- N.cg
  N.cg.batches.log[k] <- N.cg.log
}

proc.time() - ptm #stop the clock