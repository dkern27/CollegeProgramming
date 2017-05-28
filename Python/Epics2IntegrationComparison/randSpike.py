'''
Dylan Kern
Generates a random spike of noise in the range of -5 to 5
'''

import random as r


def randomSpike(noise, points):
    '''
    Generates random spike of noise
    '''
    place=r.randint(0,points) #chooses random spot to place spike in
    noise[place]=r.randrange(-5,5) #Generates random spike at place
    return noise