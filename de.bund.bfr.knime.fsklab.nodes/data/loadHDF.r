grp <- file.h5[["output"]]

d_names <- names(grp)
for(d_name in d_names){
  ds_name <- paste("output",d_name,sep="/") # concatenate group with parameter name
  ds <- file.h5[[ds_name]];
  if(length(ds$dims) == 1){
    assign(d_name,file.h5[[ds_name]][]) #create variable of parameter with values
  } else {
    assign(d_name,file.h5[[ds_name]][,])
  }  
}
file.h5$close_all()