'''
Dylan Kern
Defines the four basic functions used for testing
'''


import math as m
import numpy as np


def f1(t): #sine function
    '''
    Returns the y values for y=sin(t)
    Ideal harmonic Oscillator
    '''
    return (np.sin(t))

def f2(t): #line function
    '''
    Returns the y-values for y=9.81
    gravity
    '''
    return [-9.81]*len(t)

def f3(t,freq=1,decay=2):
    '''
    Returns y-values for an exponentially decaying sinusoid
    Harmonic Oscillator
    '''
    return np.cos(2*m.pi*freq*t)*np.exp(-t*decay)

def f4(t):
    '''
    Returns y values of a step-function
    Cart on track, elevator
    '''
    y=[]
    i=0
    while(i<101): 
        if (i < 16 or (i>=84)):
            y.append(3)
        elif(i==16 or i==83):
            y.append(1.5)
        elif((i>=17 and i<33) or (i>=67 and i<83)):
            y.append(0)
        elif (i==33 or i==66):
            y.append(-1.5)
        elif (i>=34 and i <66):
            y.append(-3)
        i+=1
    return y

def time(start,stop,numPoints):
    '''
    Generates the range of time for the data
    '''
    t=np.linspace(start,stop,numPoints)
    return t


#t-values
def T1():
    return time(0,2.*np.pi,65)
    
def T2():
    return time(0,50, 51)
    
def T3():
    return time(0,4*m.pi,261)
    
def T4():
    return time(0,25,101)
