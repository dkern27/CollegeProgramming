''' 
This code is specifically for evaluating finite integrals using the 4th
order Runge Kutta method. It will accept discrete points as the input rather
than a continuous function. It will find the first and second integrals of
those points. It will output the final values for both integrations as well
as graphs of the original points and both integrations.
Code by Jordan Schmerge.
'''
#import matplotlib.pyplot
#import numpy as np
#import math

#def f(x): #making a test function
#    return np.sin(x) #generates initial x values to integrate
'''
Basically the function described in the header comment.
'''
def rungeKutta(t,a): #takes in an intial array to be integrated as a parameter
    #t = np.linspace(0,2*math.pi,10002)
    h = t[1] - t[0] #(delta t)
    v_0 = 0 #setting initial conditions for first integral
    x_0 = 0 #setting initial conditions for second integral
    #a = f(t) #intitializing set of "a" values
    v = [0. for i in range(len(a)-1)]
    v[0] = v_0
    for i in range(len(a)-2): #4th order Runge Kutta itself
        k1 = ((a[i])*h) 
        i1 = i + 1
        k2 = (h*(a[i1]+(k1/2)))
        k3 = (h*(a[i1]+(k2/2)))
        i2 = i + 2
        k4 =  h*(a[i2] + k3)
        v[i+1] = (v[i] + 1/6.*(k1 + 2*k2 + 2*k3 + k4))
    x = [0. for i in range(len(v)-1)]
    x[0] = x_0
    for i in range(len(v)-2): #finding the second integral
        k1 = ((v[i])*h)
        i1 = i + 1
        k2 = (h*(v[i1]+(k1/2)))
        k3 = (h*(v[i1]+(k2/2)))
        i2 = i + 2
        k4 =  h*(v[i2] + k3)
        x[i+1] = (x[i] + 1/6.*(k1 + 2*k2 + 2*k3 + k4))
    #matplotlib.pyplot.plot(t, a,label='a') #these are the final graphs
    #matplotlib.pyplot.plot(t[:-1],v,label='v')
    #matplotlib.pyplot.plot(t[:-2],x,label='x')
    #matplotlib.pyplot.legend()
    #matplotlib.pyplot.show()
    #print v[-1] #printing the value of the first integral
    #print x[-1] #printing the value of the second integral
    
    return t[:-1], v, t[:-2], x
    

    
