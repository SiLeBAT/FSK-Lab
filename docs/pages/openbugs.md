---
title: OpenBUGS
summay:
keyword: integration
sidebar: fsk_sidebar
permalink: openbugs.html
folder: home
---

OpenBUGS is a software application for the Bayesian analysis of complex statistical models using Markov chain Monte Carlo (MCMC) methods. OpenBUGS is the open source variant of WinBUGS (Bayesian inference Using Gibbs Sampling). It runs under Microsoft Windows and Linux, as well as from inside the R statistical package. Versions from v3.0.7 onwards have been designed to be at least as efficient and reliable as WinBUGS over a range of test applications. ([Wikipedia](https://en.wikipedia.org/wiki/OpenBUGS))

[OpenBUGS](http://www.openbugs.net/w/FrontPage) is supported in FSK-Lab through the R package [R2OpenBUGS](https://cran.r-project.org/web/packages/R2OpenBUGS/index.html). R models in FSK-Lab can make use of BUGS files as long as they are stored in plaintext files (.txt). This page includes a minimal example from the R2OpenBUGS vignettes implemented in FSK-Lab.

# School data

## OpenBUGS model: schools.txt

From the [R2OpenBugs vignettes](https://cran.r-project.org/web/packages/R2OpenBUGS/vignettes/R2OpenBUGS.pdf):
> For modeling these data, we use a hierarchical model as proposed by Gelman et al. (2003, Section 5.5). We assume a normal distribution for the observed estimate for each school with mean theta and inverse-variance tau.y. The inverse-variance is given as 1/sigma.y2 and its prior distribu- tion is uniform on (0,1000). For the mean theta, we employ another normal distribution with mean mu.theta and inverse-variance tau.theta.

```R
model {
  for (j in 1:J)
  {
    y[j] ~ dnorm (theta[j], tau.y[j])
    theta[j] ~ dnorm (mu.theta, tau.theta)
    tau.y[j] <- pow(sigma.y[j], -2)
  }
  mu.theta ~ dnorm (0.0, 1.0E-6)
  tau.theta <- pow(sigma.theta, -2)
  sigma.theta ~ dunif (0, 1000)
}
```

# Parameters
This model has two input parameters that have to be entered in the model metadata: *n_chains* and *n_iter*. These two parameters have the equivalent R code:
```R
n_chains <- 3
n_iter <- 1000
```

## Model script: model.R
The model script prepares the data inputs for the *bugs* function and run a MCMC simulation to get estimates for *theta*, *mu.theta* and *sigma.theta*.

```R
library(R2OpenBUGS)
data(schools)

J <- nrow(schools)
y <- schools$estimate
sigma.y <- schools$sd
data <- list("J", "y", "sigma.y")

inits <- function() {
  list(theta = rnorm(J, 0, 100),
       mu.theta = rnorm(1, 0, 100),
       sigma.theta = runif(1, 0, 100))
}

schools.sim <- bugs(data, inits,
                    model.file = "schools.txt",
                    parameters = c("theta", "mu.theta", "sigma.theta"),
                    n.chains = 3, n.iter = 1000)
```

## Visualization script: visualization.R
The visualization script is pretty straightforward and produces the following plot.
`plot(schools.sim)`

![](assets/openbugs/schooldata.png)
