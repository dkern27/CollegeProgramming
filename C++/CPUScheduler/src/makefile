# To build the program, simply type:
#   make
#
# To clean up and remove the compiled binary, type:
#   make clean

CC = g++
OBJS = *.cpp
HEADERS = *.hpp
NAME = simulator
VERSION = -std=c++11 -Wall

all: $(NAME)

$(NAME): $(OBJS) $(HEADERS)
	$(CC) $(VERSION) $(OBJS) -o $(NAME)

debug: $(OBJS) $(HEADERS)
	$(CC) $(VERSION) $(OBJS) -o $(NAME) -g

run: $(NAME)
	./$(NAME)

clean:
	rm $(NAME)*
