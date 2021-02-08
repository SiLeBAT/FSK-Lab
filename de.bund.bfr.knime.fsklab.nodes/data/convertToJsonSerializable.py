if isinstance(toSerializable, pandas.core.frame.DataFrame):
    toSerializable = toSerializable.to_json();
elif isinstance(toSerializable,numpy.ndarray):
    toSerializable = toSerializable.tolist();
elif isinstance(toSerializable,list):
    toSerializable = [item.tolist() if isinstance(item,numpy.ndarray) else item for item in toSerializable]
