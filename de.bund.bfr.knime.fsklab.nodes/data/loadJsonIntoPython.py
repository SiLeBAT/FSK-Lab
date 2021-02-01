with open(JSON_FILE_NAME) as f:
	json_params = json.load(f)

var_types = json_params['var_types']
script_language = json_params['script_language']
del json_params['var_types']
del json_params['script_language']


if var_types[sourceParam] == 'DataFrame' or var_types[sourceParam] == 'data.frame' :
    if script_language == 'Python':
    	vars()[targetParam] = pandas.read_json(json_params[sourceParam])
    else:
        vars()[targetParam] = pandas.DataFrame.from_records(json_params[sourceParam])
else:
    vars()[targetParam] = json_params[sourceParam]

del var_types
del script_language
del json_params
del sourceParam
del targetParam
del JSON_FILE_NAME