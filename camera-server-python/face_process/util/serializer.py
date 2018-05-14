import json
import numpy as np

def serialize(arr):
    s = json.dumps(arr.tolist(), sort_keys=True)
    return s
def deserialize(s):
    arr = json.loads(s)
    return np.array(arr)

