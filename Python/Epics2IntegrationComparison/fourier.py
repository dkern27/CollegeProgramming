import numpy as np
import math as m
#import pylab as py


def sine(amp, f, t):
    return amp*np.sin(2*m.pi*f*t)

def cosine(amp, f, t):
    return amp*np.cos(2*m.pi*f*t)

def fourier(N,T, func,threshold) :
    '''
    This function smooths the noise filled discrete data using a fourier transform and 
    a filtering term based on the magnitude spectrum of the transformed function
    the function takes in 4 arguements, N is the number of data points, T is the range of the data
    func is an array of the data points. and threshold is a double that sets the threshold of the
    filtering function. this returns a new array of data points this time of the smoothed function
    '''
    
    t = np.linspace(0, T,N)
    
    #py.subplot(3,1,1)
    #py.plot (t, func, color = '#996699', label = 'signal')
    #py.legend(loc = 'best')
    
    
    transform = np.fft.fft(func)
    n = transform.size
    freq = np.fft.fftfreq(n, d = .1)
    
    #py.subplot(3,1,2)
    #py.plot(freq, transform.real, color = 'b', label = 'real')
    #py.plot(freq, transform.imag, color = 'r', label = 'imaginary')
    #py.legend(loc='best')
    
    SmoothR = []
    SmoothI= []
    countR = 0
    countI = 0
    Real = [[0 for x in xrange(2)] for x in xrange(N)]
    for i in range (N) :
        Real[i][0]=freq[i] 
        Real[i][1]=transform[i].real
    for i in range(N) :
        if abs(Real[i][1]) > threshold: #This runs through the amplitude spectrum checking which frequencies are most important
            SmoothR.append(Real[i])
            countR += 1
    Imaginary = [[0 for x in xrange(2)] for x in xrange(N)]
    for i in range (N) :
        Imaginary[i][0]=freq[i] 
        Imaginary[i][1]=transform[i].imag
    for i in range(N) :
        if abs(Imaginary[i][1]) > threshold: #This runs through the amplitude spectrum checking which frequencies are most important
            SmoothI.append(Imaginary[i])
            countI += 1
    c=0
    for i in range (countR) :
        c= c+cosine(SmoothR[i][1],SmoothR[i][0],t)
    for i in range (countI) :
        c= c-sine(SmoothI[i][1],SmoothI[i][0],t)
    c = (c/N)
    
    #py.subplot(3,1,3)
    #py.plot(t, c)
    
    #py.show()
    return c #c is the smoothed function in array form