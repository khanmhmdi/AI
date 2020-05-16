data = ['[Burglary, [], [0.999, 0.001]]', '[Alarm, [Burglary, Earthquake], [0.95, 0.94, 0.29, 0.001]]', '[[Burglary], [MaryCalls, JohnCalls], [1, 1]]']

from ast import  literal_eval
import re


lst = [literal_eval(re.sub("([a-zA-Z]+)", r'"\1"', lst)) for lst in data]
lst = [[[x[0]]] + x[1:] if not isinstance(x[0], list) else x for x in lst ] 

for x in lst:
    print(x)
