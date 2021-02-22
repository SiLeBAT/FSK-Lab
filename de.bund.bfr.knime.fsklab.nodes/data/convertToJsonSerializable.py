if isinstance(toSerializable, pandas.core.frame.DataFrame):
    toSerializable = pandas.DataFrame.to_json(toSerializable)
#    toSerializable = toSerializable.to_json();
elif isinstance(toSerializable,numpy.ndarray):
    toSerializable = json.dumps(toSerializable.tolist());
elif isinstance(toSerializable,list):
    toSerializable = [item.tolist() if isinstance(item,numpy.ndarray) else item for item in toSerializable]
    toSerializable = json.dumps(toSerializable)
else:
    toSerializable = json.dumps(toSerializable)
