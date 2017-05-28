#   fileInput.py

'''
    Harry Krantz

    When called this function will take in a file path. The file
    will be read and the data sorted into two float arrays. The function then
    returns the two arrays.
    
    The data within the file must be sorted into two columns separated by a tab.
    Any non-numerical entries will be ignored.
'''


def fileInput(path):   
         
    print "\nOpening " + path + "...\n"                         #Notify the user that the file is being located
    infile = open( path, 'r' )   

    
    x = []                                                      #Create two empty arrays
    y = []
    
    print "File found. Reading " + path + "...\n"               #Notify user that the file is open and being read
    
    for line in infile:                                         #Loop over every line in the file
        temp = line.split( "\t" )                               #Split each line by tabs
        try:
            x.append( float(temp[0]) )                          #Add each element to the corresponding array
            y.append( float(temp[1]) )
        except: ValueError                                      #Error exception if the entry cannot be converted to a float; skip and continue the loop
        
    infile.close()                                              #Close the file
    
    print "Data imported successfully."                          #Notify the user that the data has been read succesfully
    
    return x, y                                                 #Return the two arrays for use by other functions