# load from file (all)

var_types <- json_params$var_types
script_language <- json_params$script_language
json_params <- within(json_params, rm(script_language)) 
json_params <- within(json_params, rm(var_types)) 
classes<-length(unique(lapply(json_params[sourceParam][[1]],class)))
sizes<-length(unique(lapply(json_params[sourceParam][[1]],length)))
if(var_types[sourceParam] == 'DataFrame' | var_types[sourceParam] == 'data.frame') {
  if(script_language == "Python") {
    #unlist columns to restore their original structure
    assign(targetParam,as.data.frame(lapply(lapply(fromJSON(json_params[sourceParam][[1]]),cbind),unlist)))
  } else {
    assign(targetParam, do.call(rbind.data.frame, json_params[sourceParam][[1]]))
  }
} else if ((var_types[sourceParam] =='ndarray'| var_types[sourceParam] =='list' ) & classes == 1 & sizes==1) {
    assign(targetParam,matrix(unlist(json_params[sourceParam][[1]]), nrow=length(json_params[sourceParam][[1]])))
} else {
  assign(targetParam,json_params[sourceParam][[1]]) #create variable of parameter with values
}
  
rm(sourceParam)
rm(targetParam)
rm(script_language)
rm(var_types)
rm(json_params)
