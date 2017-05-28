#GUI.py

'''
    Harry Krantz
    11/19/14
    
    This file contains the functions and framework for the GUI. The main function
    builGUI() launches the window through which the program is run. The other
    functions serve as commands for each button and link to the mathematial functions
    contained in other files.
'''


import Tkinter as Tk                                                             #Import the various frameworks
import tkFileDialog

import matplotlib
matplotlib.use('TkAgg')

from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
from matplotlib.figure import Figure



import fileInput as fI                                                           #Import the other functions for the program
import fileOutput as fO
import functions as func
import noise as noise
import randSpike as rs
import fourier as ft
import rungeKutta as rk
import trapezoid as trap
import verlet as ver
import beeman as bee


recent = ()                                                                      #Initialize some global variables
root = 0
P = 0
canvas = 0
fileIn = 0


###############################################################################
'''
    The plot function controls the canvas and graph display. When called it checks a
    bool, if true it will add a subplot with the data provided. If false it will remove
    the subplot with the given name.
'''

def plot(var,x,y,name):
    global P, canvas, recent
    if( var.get() ):
        print "Displaying " + name
        P.plot(x,y,label=name)
        canvas.show()
        recent = (name,x,y)
    if( not var.get() ):
        print "Hiding " + name
        line = [line for line in P.lines if line.get_label()==name][0]
        P.lines.remove(line)
        canvas.show()
        
###############################################################################
'''
    Opens a file dialog window for the user to select a file, then calls the fileInput
    function to open the file. A new layer is created with the given data ready for display.
'''

def fileImport():
    filename = tkFileDialog.askopenfilename()
    data = fI.fileInput(filename)
    var = Tk.BooleanVar()
    x = data[0]
    y = data[1]
    name = filename.split( "/" )[-1]
    imported = Tk.Checkbutton( root, text=name, variable=var, command=lambda: plot(var,x,y,name) )
    imported.pack( side=Tk.TOP, anchor=Tk.W )

###############################################################################
'''
    The following functions are all for the simulated data. They will call the
    corresponding function which will calculate the data. A new layer will be 
    created with the data.
'''

def showConstant():
    print "Adding Constant"
    var = Tk.BooleanVar()
    x=func.T2()
    y=func.f2(func.T2())
    constant = Tk.Checkbutton( root, text="Constant", variable=var, command=lambda: plot(var,x,y,"Constant") )
    constant.pack( side=Tk.TOP, anchor=Tk.W )
    
def showSine():
    print "Adding Sine Curve"
    var = Tk.BooleanVar()
    x=func.T1()
    y=func.f1(func.T1())
    sine = Tk.Checkbutton( root, text="Sine Curve", variable=var, command=lambda: plot(var,x,y,"Sine") )
    sine.pack( side=Tk.TOP, anchor=Tk.W )
    
def showCosine():
    print "Adding Decaying Cosine Curve"
    var = Tk.BooleanVar()
    x=func.T3()
    y=func.f3(func.T3(), 1, 1)
    cosine = Tk.Checkbutton( root, text="Decaying Cosine Curve", variable=var, command=lambda: plot(var,x,y,"Cosine") )
    cosine.pack( side=Tk.TOP, anchor=Tk.W )
    
def showStep():
    print "Adding Step Function"
    var = Tk.BooleanVar()
    x=func.T4()
    y=func.f4(func.T4())
    step = Tk.Checkbutton( root, text="Step Function", variable=var, command=lambda: plot(var,x,y,"Step") )
    step.pack( side=Tk.TOP, anchor=Tk.W )

###############################################################################
'''
    The following functions are methods for adding simulated noise. The most 
    recently selected data-layer will be used and the corresponding noise function
    will calculate the simulated noise and add it to the data. A new layer will
    be created with the modified data.
'''

def addGauss():
    print "Adding Gauss " + recent[0]
    var = Tk.BooleanVar()
    x=recent[1]
    y=recent[2] + noise.N1(x)
    name = recent[0] + " with Gauss"
    gauss = Tk.Checkbutton( root, text=name, variable=var, command=lambda: plot(var, x, y, name) )
    gauss.pack( side=Tk.TOP, anchor=Tk.W )
    
def addUnif():
    print "Adding Uniform " + recent[0]
    var = Tk.BooleanVar()
    x=recent[1]
    y=recent[2] + noise.N2(x)
    name = recent[0] + " with Uniform"
    gauss = Tk.Checkbutton( root, text=name, variable=var, command=lambda: plot(var, x, y, name) )
    gauss.pack( side=Tk.TOP, anchor=Tk.W )
    
def addSpike():
    print "Adding Spike " + recent[0]
    var = Tk.BooleanVar()
    x=recent[1]
    y=rs.randomSpike(recent[2],len(recent[1]))
    name = recent[0] + " with Spike"
    spike = Tk.Checkbutton( root, text=name, variable=var, command=lambda: plot(var, x, y, name) )
    spike.pack( side=Tk.TOP, anchor=Tk.W )

###############################################################################
'''
    The following functions are all methods for manipulating the data. The most
    recently selected data-layer will be used the the corresponding method function
    will calculate the new data. A new layer will be created with the data.
'''

def addFourier():
    print "Adding Fourier " + recent[0]
    var = Tk.BooleanVar()
    x=recent[1]
    y=ft.fourier( len(recent[2]), recent[1][-1] , recent[2], 10)
    name = recent[0] + " with Fourier"
    fourierT = Tk.Checkbutton( root, text=name, variable=var, command=lambda: plot(var, x, y, name) )
    fourierT.pack( side=Tk.TOP, anchor=Tk.W )

def addRunga():
    print "Adding 1st Runge " + recent[0]
    var1 = Tk.BooleanVar()
    x1=rk.rungeKutta(recent[1], recent[2])[0]
    y1=rk.rungeKutta(recent[1], recent[2])[1]
    name1 = recent[0] + " with 1st Runge"
    runge1 = Tk.Checkbutton( root, text=name1, variable=var1, command=lambda: plot(var1, x1 , y1 , name1) )
    runge1.pack( side=Tk.TOP, anchor=Tk.W )

    print "Adding 2nd Runge " + recent[0]
    var2 = Tk.BooleanVar()
    x2=rk.rungeKutta(recent[1], recent[2])[2]
    y2=rk.rungeKutta(recent[1], recent[2])[3] 
    name2 = recent[0] + " with 2nd Runge"
    runge2 = Tk.Checkbutton( root, text=name2, variable=var2, command=lambda: plot(var2, x2, y2, name2) )
    runge2.pack( side=Tk.TOP, anchor=Tk.W )

def addTrap():
    print "Adding 1st Trapezoid " + recent[0]
    var1 = Tk.BooleanVar()
    x1=trap.trapezoidal(recent[1], recent[2])[0]
    y1=trap.trapezoidal(recent[1], recent[2])[1]
    name1 = recent[0] + " with 1st Trapezoid"
    trap1 = Tk.Checkbutton( root, text=name1, variable=var1, command=lambda: plot(var1, x1 , y1 , name1) )
    trap1.pack( side=Tk.TOP, anchor=Tk.W )

    print "Adding 2nd Trapezoid " + recent[0]
    var2 = Tk.BooleanVar()
    x2=trap.trapezoidal(recent[1], recent[2])[2]
    y2=trap.trapezoidal(recent[1], recent[2])[3] 
    name2 = recent[0] + " with 2nd Trapezoid"
    trap2 = Tk.Checkbutton( root, text=name2, variable=var2, command=lambda: plot(var2, x2, y2, name2) )
    trap2.pack( side=Tk.TOP, anchor=Tk.W )
    
def addVer():
    print "Adding 1st Verlet " + recent[0]
    var1 = Tk.BooleanVar()
    x1=ver.verlet(recent[1], recent[2])[0]
    y1=ver.verlet(recent[1], recent[2])[1]
    name1 = recent[0] + " with 1st Verlet"
    ver1 = Tk.Checkbutton( root, text=name1, variable=var1, command=lambda: plot(var1, x1 , y1 , name1) )
    ver1.pack( side=Tk.TOP, anchor=Tk.W )

    print "Adding 2nd Verlet " + recent[0]
    var2 = Tk.BooleanVar()
    x2=ver.verlet(recent[1], recent[2])[2]
    y2=ver.verlet(recent[1], recent[2])[3] 
    name2 = recent[0] + " with 2nd Verlet"
    ver2 = Tk.Checkbutton( root, text=name2, variable=var2, command=lambda: plot(var2, x2, y2, name2) )
    ver2.pack( side=Tk.TOP, anchor=Tk.W )
    
def addBee():
    print "Adding 1st Beeman " + recent[0]
    var1 = Tk.BooleanVar()
    x1=bee.beeman(recent[1], recent[2])[0]
    y1=bee.beeman(recent[1], recent[2])[1]
    name1 = recent[0] + " with 1st Beeman"
    bee1 = Tk.Checkbutton( root, text=name1, variable=var1, command=lambda: plot(var1, x1 , y1 , name1) )
    bee1.pack( side=Tk.TOP, anchor=Tk.W )

    print "Adding 2nd Beeman " + recent[0]
    var2 = Tk.BooleanVar()
    x2=bee.beeman(recent[1], recent[2])[2]
    y2=bee.beeman(recent[1], recent[2])[3] 
    name2 = recent[0] + " with 2nd Beeman"
    bee2 = Tk.Checkbutton( root, text=name2, variable=var2, command=lambda: plot(var2, x2, y2, name2) )
    bee2.pack( side=Tk.TOP, anchor=Tk.W )

###############################################################################
'''
    The following functions export data as either a .txt or .pdf. Both will open
    a file dialog window for the user to create a new file. The corresponding 
    output functions will be called and the file saved.
'''

def saveGraph():
    global canvas
    print "Saving Graph"
    path = tkFileDialog.asksaveasfilename()
    fO.graphOutput( path, canvas )


def saveData():
    filename = tkFileDialog.asksaveasfilename()
    x=recent[1]
    y=recent[2]
    fO.fileOutput( filename, x, y )

###############################################################################
'''
    The follwing functions serve to manage the entire program. The reset function
    will reinitialize everything and relaunch the GUI. The quit function simply
    ends the loop and closes the GUI, thus ending the program.
'''

def reset():
    print "Resetting... "
    _quit()
    global recent, root, P, canvas, fileIn
    recent = ()
    root = 0
    P = 0
    canvas = 0
    fileIn = 0
    buildGUI()

def _quit():
    root.quit()     # stops mainloop
    root.destroy()  # this is necessary on Windows to prevent
                    # Fatal Python Error: PyEval_RestoreThread: NULL tstate

###############################################################################
'''
    The buildGUI function opens the window, creates a new drawing canvas and creates
    all the buttons within the window.
'''

def buildGUI():
    ###########################################################################
    global root, P, canvas, fileIn
    root = Tk.Tk()
    root.wm_title("Numerical Integration With Noise")
    
    f = Figure(figsize=(8,6), dpi=100)
    P = f.add_subplot(111)
    
    # a tk.DrawingArea
    canvas = FigureCanvasTkAgg(f, master=root)
    canvas.show()
    canvas.get_tk_widget().pack(side=Tk.RIGHT, fill=Tk.BOTH, expand=1)
    
    ###########################################################################
    
    importData = Tk.Label( root, text="Import Data:" )
    importData.pack( side=Tk.TOP, anchor=Tk.W )
    
    importButton = Tk.Button( root,text="Open...", command=fileImport )
    importButton.pack( side=Tk.TOP, anchor=Tk.CENTER )
    
    emptySpace1 = Tk.Label( root, text="" )
    emptySpace1.pack( side=Tk.TOP, anchor=Tk.CENTER )
    
    ###########################################################################
    
    simData = Tk.Label( root, text="Simulated Data:" )
    simData.pack( side=Tk.TOP, anchor=Tk.W )
    
    constantButton = Tk.Button( root, text="Constant", command=showConstant )
    constantButton.pack( side=Tk.TOP, anchor=Tk.CENTER )
    
    sinButton = Tk.Button( root, text="Sine Curve", command=showSine )
    sinButton.pack( side=Tk.TOP, anchor=Tk.CENTER )
    
    cosButton = Tk.Button( root, text="Decaying Cosine", command=showCosine )
    cosButton.pack( side=Tk.TOP, anchor=Tk.CENTER )
    
    stepButton = Tk.Button( root, text="Step Function", command=showStep )
    stepButton.pack( side=Tk.TOP, anchor=Tk.CENTER )
    
    emptySpace2 = Tk.Label( root, text="" )
    emptySpace2.pack( side=Tk.TOP, anchor=Tk.CENTER )
    
    ###########################################################################
    
    addNoise = Tk.Label( root, text="Add Noise:" )
    addNoise.pack( side=Tk.TOP, anchor=Tk.W )
    
    gaussButton = Tk.Button( root, text="Gaussian Noise", command=addGauss )
    gaussButton.pack( side=Tk.TOP, anchor=Tk.CENTER )
    
    unifButton = Tk.Button( root, text="Uniform Noise", command=addUnif )
    unifButton.pack( side=Tk.TOP, anchor=Tk.CENTER )
    
    spikeButton = Tk.Button( root, text="Random Spike", command=addSpike )
    spikeButton.pack( side=Tk.TOP, anchor=Tk.CENTER )
    
    emptySpace3 = Tk.Label( root, text="" )
    emptySpace3.pack( side=Tk.TOP, anchor=Tk.CENTER )
    
    ###########################################################################
    
    methods = Tk.Label( root, text="Methods:" )
    methods.pack( side=Tk.TOP, anchor=Tk.W )
    
    fourierButton = Tk.Button( root, text="Fourier Transform", command=addFourier )
    fourierButton.pack( side=Tk.TOP, anchor=Tk.CENTER )
    
    rungaButton = Tk.Button( root, text="Runge Kutta", command=addRunga )
    rungaButton.pack( side=Tk.TOP, anchor=Tk.CENTER )

    trapButton = Tk.Button( root, text="Trapezoidal Integration", command=addTrap )
    trapButton.pack( side=Tk.TOP, anchor=Tk.CENTER )
    
    verButton = Tk.Button( root, text="Verlet Integration", command=addVer )
    verButton.pack( side=Tk.TOP, anchor=Tk.CENTER )
    
    beeButton = Tk.Button( root, text="Beeman Integration", command=addBee )
    beeButton.pack( side=Tk.TOP, anchor=Tk.CENTER )
    
    emptySpace4 = Tk.Label( root, text="" )
    emptySpace4.pack( side=Tk.TOP, anchor=Tk.CENTER )
    
    ###########################################################################
    
    display = Tk.Label( root, text="Display:" )
    display.pack( side=Tk.TOP, anchor=Tk.W )
    
    ###########################################################################
    
    quitButton = Tk.Button( root, text="Quit", command=_quit )
    quitButton.pack( side=Tk.BOTTOM, anchor=Tk.CENTER, fill=Tk.X)
    
    resetButton = Tk.Button( root, text="Reset", command=reset )
    resetButton.pack( side=Tk.BOTTOM, anchor=Tk.CENTER, fill=Tk.X )
    
    ###########################################################################
    
    emptySpace5 = Tk.Label( root, text="" )
    emptySpace5.pack( side=Tk.BOTTOM, anchor=Tk.W )
    
    graphButton = Tk.Button( root, text="Graph", command=saveGraph )
    graphButton.pack( side=Tk.BOTTOM, anchor=Tk.CENTER )
    
    dataButton = Tk.Button( root, text="Data Points", command=saveData )
    dataButton.pack( side=Tk.BOTTOM, anchor=Tk.CENTER )
    
    export = Tk.Label( root, text="Export:" )
    export.pack( side=Tk.BOTTOM, anchor=Tk.W )
    
    emptySpace6 = Tk.Label( root, text="" )
    emptySpace6.pack( side=Tk.BOTTOM, anchor=Tk.W )
    
    ###########################################################################

    Tk.mainloop()
    
###############################################################################
