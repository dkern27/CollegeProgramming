''' 
This code is specifically for evaluating finite integrals using the trapezoidal
rule. It will accept discrete points as the input rather than a continuous
function. It will find the first and second integrals of those points.
It will output the final values for both integrations as well as graphs
of the original points and both integrations.
Code by Jordan Schmerge.
'''
#import numpy as np
#import matplotlib.pyplot

#def f(x): #making a test function
#    return x**2
'''
Basically the function described in the header comment.
'''
def trapezoidal( t, y1): #takes in array of points to integrate as a parameter
    #t = []
    y2, y3 = [], [] #initialize empty arrays
    #t = np.linspace(0,2,10000)
    z = 0
    #y1 = f(t)
    for i in range(len(t)-1): #actually using trapezoidal rule (v)
        integral = ((t[1]-t[0])/2)*(y1[i]+y1[i+1])
        z+=integral
        y2.append(float(z))
    #print y2[-1] #value of the first integral
    z2 = 0
    for i in range(len(t)-2): #finding the second integral (x)
        integral2 = ((t[1]-t[0])/2)*(y2[i]+y2[i+1])
        z2+=integral2
        y3.append(float(z2))
    #print y3[-1] #value of the second integral
    #matplotlib.pyplot.plot(t, y1,label='a') #making all the output graphs
    #matplotlib.pyplot.plot(t[:-1],y2,label='v')
    #matplotlib.pyplot.plot(t[:-2],y3,label='x')
    #matplotlib.pyplot.legend()
    #matplotlib.pyplot.show()
   
    return t[:-1], y2, t[:-2], y3
