n_iter <- 200


#params_horizontal <- data.frame("h_same"=0.0062, "h_prev"=0.7, "ho"=0.1, "n_f"=100, "t_f"=0.29, "p_pcf"=0.7, "elements"="flocks", row.names="BaselineESBLEcoli")

h_same <- 0.0062
h_prev <- 0.7
ho <- 0.1
n_f <- 100
t_f <- 0.29
p_pf <- 0.7

# The following values are default values for n_i0 produced for n.iter=200 and 100 flocks and running vertical transmission with triangle distrib. ith 0,1,0.25 and using the seed 19
n_i0 <- c(20.13533,20.18208,14.64824,24.00037,24.77051,22.27986,25.39517,15.05366,10.17508,13.35199,22.12452,22.67484,13.28911,7.009042,25.84986,7.759795,18.47647,18.83664,17.58743,13.55704,11.93086,5.34843,13.76488,8.214135,15.1736,17.62031,29.4717,19.09481,17.27915,17.76378,22.52632,16.72649,26.14947,6.823643,6.701237,10.1493,15.05184,12.97573,18.8189,21.95561,7.193089,23.57109,28.2189,23.2649,19.17024,20.64555,8.829339,20.92484,23.27599,24.1471,11.57289,15.86647,15.7221,23.0617,3.931043,25.68556,25.05375,10.35879,19.56079,20.34687,6.24409,12.69776,17.66054,3.063431,14.76037,18.54085,19.31375,8.761153,11.93218,6.730912,31.71509,11.75325,1.141946,7.770602,30.07501,25.23397,24.17267,28.72469,24.79693,14.8093,19.87193,8.072654,2.924765,18.79444,19.19628,14.21841,24.13575,11.14231,23.95048,24.5611,20.95048,11.93536,16.81621,20.47042,11.66786,20.6199,6.452878,22.56183,18.24643,17.63884,5.193775,24.153,27.43118,13.74649,21.96672,11.9193,22.01244,24.64472,4.022668,6.089249,20.91747,30.97648,17.38381,12.29622,12.27896,22.32347,21.62734,13.36411,18.92174,20.24135,12.68753,10.65981,21.34372,17.15313,23.84702,13.61339,13.62944,17.20962,22.49093,10.02783,10.67894,23.07947,16.45667,15.01205,17.78071,18.65794,31.41837,18.018,19.74941,15.74405,21.71563,28.26036,15.88737,27.11026,15.50644,18.29944,15.11078,10.41599,31.10347,16.78198,14.41265,14.04983,23.05631,31.73748,10.16711,20.2555,32.59428,15.49806,17.60216,21.26766,19.83445,19.15243,23.90692,14.08382,21.12462,13.3945,23.06772,14.42595,11.01964,4.402711,17.87011,8.324741,4.469319,17.95253,16.37921,20.0536,15.59909,5.982001,13.18757,15.31358,25.43679,10.34191,9.492794,18.39629,20.39416,18.94999,32.45602,18.39077,26.52064,2.611357,9.657757,18.2024,19.38815,29.75094,7.382067,19.10389,23.94239,1.359055,13.47996,30.16983)



#  h_same = dimensionless parameter describing horizontal transfer between flocks from different houses/incubators at the same place (farm/hatchery) and in thesame production cycle 
#  h_prev = dimensionless parameter describing horizontal transfer between a flock in the current production cycle and the flock in the previous production cycle in the same #house/incubator 
#  ho =     dimensionless parameter which describes all other sources of bacterial introduction into the house/incubator 
#  n_f =    number of flocks (= number of houses/incubatory) at farm
#  t_f =    duration of current production step (in days, in hatchery t_f is typically 0.29, i.e. one considers that a couple of hours pass between hatching and transport to the broiler farm, at the broiler farm the duration is 42 days)

#  p_pf = prevalence of infected flocks in previous production cycle
#  n_i0  = ni_bf or ni_cf = can take values between 0 and 100 which is given by the former step in the production process
# elements: can be "flocks" or "animals" and is used in the model script to create file names which store the end results to be used in subsequent model steps