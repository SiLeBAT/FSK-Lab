---
title: Writing R Code
summary: Writing R code compatible with FSK-Lab
keywords: r code
sidebar: writing_r_code
permalink: writing_r_code.html
folder: pages
---

# Functions not allowed
* `source()`. All code has to be compiled in one script file.
* `rm(list=ls())`
* `setwd`, `read` and `write`
* No plotting functions in model script

# Visualization script
* Only one plot is allowed

# Importing data
* Only use csv and RData to import data

# Parameter annotation
* Parameters are defined in the „Model Annotation Excel template“
* A “data type” and “default value” has to be provided for each input/constant parameter
* Vectors / matrices/strings can also be defined as input parameters
* Parameters can also be defined as function when parameter “data type” is string
* File inputs must be between quotes in “Parameter value” field. 
* File inputs are case sensitive (workspace.Rdata/Workspace.RData)
* You must use'.' and not',' for decimal numbers
* Parameter names and IDs should not contain a dot
* Parameter defined as input or constant must have a value.
* Parameters referenced in the default values of other parmaterers must be defined before being referenced.

# Output variables
Output variables must be defined in the global environment or otherwise will not be available for other models to be joined.

```R
Out1 <- "Hello"		# global variable created
test <- function () {
   Out2 <- "World"	# local variable created
}
test()
print(Out1)	# no problem
print(Out2)  	# NOT FOUND because this variable is NOT in the workspace anymore
```
