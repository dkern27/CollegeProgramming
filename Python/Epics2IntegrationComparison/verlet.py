'''
Integrating using discrete points and the Verlet integration method.
It accepts 2 data arrays as inputs (a set of x and of acceleration values).
It outputs the first and second integrals and can output complete arrays
that show a "running total" as the integral is summed up. It can display
graphs and simple error analysis as required.
Code by Jordan Schmerge.
'''

def verlet(t,a): #takes in an intial array to be integrated as a parameter   
    #exactfirst = 0.00000000001
    #exactsecond = 2*math.pi
    #t = np.linspace(0,2*math.pi,1002)
    h = t[1] - t[0] #(delta t)
    v_0 = 0 #setting initial conditions for first integral
    x_0 = 0 #setting initial conditions for second integral
    #a = f(t) + N1(t) #intitializing set of "a" values
    v = [0. for i in range(len(a)-1)]
    v[0] = v_0
    for i in range(len(a)-2): #4th order Runge Kutta itself
        v[i+1] = (v[i] + 0.5*(a[i+1] + a[i])*h)
    x = [0. for i in range(len(v)-1)]
    x[0] = x_0
    for i in range(len(v)-2): #finding the second integral
        x[i+1] = (x[i] + (v[i]*h) + (0.5*a[i]*h*h))
    #matplotlib.pyplot.plot(t, a,label='a') #these are the final graphs
    #matplotlib.pyplot.plot(t[:-1],v,label='v')
    #matplotlib.pyplot.plot(t[:-2],x,label='x')
    #matplotlib.pyplot.legend(loc='best')
    #matplotlib.pyplot.show()
    #error1 = ((exactfirst - v[-1])/exactfirst)*100
    #error2 = ((exactsecond - x[-1])/exactsecond)*100
    #print " "
    #print "Exact First Integral: ", exactfirst
    #print "Exact Second Integral: ", exactsecond
    #print "Verlet says first integral is: ",v[-1]
    #print "And second integal is: ",x[-1]
    #print "Error for first integral is: ",error1,"%"
    #print "Error for the second is: ",error2,"%."
    
    return t[:-1], v, t[:-2], x
