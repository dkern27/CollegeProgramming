'''
Dylan Kern
Generates both Gaussian and Uniform noise
'''

import numpy as np

def gaussianNoise(mean,stdev,numPoints):
    '''
    Generates gaussian noise using numpy
    Mean: Average value of noise
    Stdev: Standard Deviation of the points generated
    '''
    n=np.random.normal(mean,stdev,numPoints)
    return n
    
def uniformNoise(low,high,numPoints): 
    '''
    Generates uniform noise on a half-open interval using numpy
    Low: Low value of noise (included)
    High: High value of noise (not included)
    '''
    n=np.random.uniform(low,high,numPoints)
    return n
    
def N1(x):
    '''
    Specific gaussian noise generation using set values
    '''
    return gaussianNoise(0,.1,len(x))
    
def N2(x):
    '''
    Specific uniform noise generation using set values
    '''
    return uniformNoise(0,1,len(x))