   #fileOutput.py

'''
    Harry Krantz

    This function will accept two float arrays and print them to a file. An
    output file will be created using the current date and time.
    
    The data will be printed to the file as two columns of corresponding data points.
    
    The two arrays must be the same size.
'''

import matplotlib as mpl

def fileOutput( path, x, y ):                                                 #Takes a file path and two arrrays as arguments


    print "\nCreating " + path + ".txt\n"                             #Notify the user that the file is being created
    outfile = open( path + ".txt", 'w' ) 

    print "Writing to " + path + ".txt...\n"                          #Notify the user that the file is opened and about to be written
    
    for i in range( 0, len(x) ):                                        #For each entry write the data to the file separated by a tab
        outfile.write( str(x[i]) + "\t" + str(y[i]) + "\n" )
        
    outfile.close()                                                     #Close the file
    
    print "Data saved."                                                 #Notify the user that the data has been successfully saved
    
    return                                                              #Return nothing
    
    
def graphOutput( path, canvas ):
    print "\nSaving graph in " + path + "...\n"                     
    canvas.print_pdf( path + ".pdf" )                                    
    print "Saved as " + path + ".pdf\n"
    return