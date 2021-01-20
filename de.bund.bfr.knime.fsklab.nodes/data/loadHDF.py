	data = f['output']
	for key in data.keys():
		vars()[key] = data[key][()]