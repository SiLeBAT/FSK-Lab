# load from file (all)

var_types <- json_params$var_types
script_language <- json_params$script_language
json_params <- within(json_params, rm(script_language)) 
json_params <- within(json_params, rm(var_types)) 

if(var_types[sourceParam] == 'DataFrame' | var_types[sourceParam] == 'data.frame') {
  if(script_language == "Python") {
    assign(targetParam,as.data.frame(lapply(fromJSON(parameters[sourceParam][[1]]),cbind)))
  }
}
else {
  assign(targetParam,parameters[sourceParam][[1]]) #create variable of parameter with values
}
  
rm(sourceParam)
rm(targetParam)
rm(script_language)
rm(var_types)
rm(json_params)
