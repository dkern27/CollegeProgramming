import random

def main():
    random.seed()
    file = open("points.txt", 'w')
    file.write('1000\n10\n')
    i=0
    while i < 1001:
        x = random.randint(-1000, 1000)
        y = random.randint(-1000, 1000)
        z = random.randint(-1000, 1000)
        file.write(str(x) + ' ' + str(y) + ' ' + str(z) + '\n')
        i = i+1

main()
