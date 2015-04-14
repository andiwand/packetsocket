TRG := packetsocket
SRC := Library.cpp
INC := /usr/lib/jvm/java-7-openjdk-amd64/include /usr/lib/jvm/java-7-openjdk-amd64/include/linux

INC := $(addprefix -I,$(INC))
OBJ := $(TRG).o
LIB := lib$(TRG).so

all:
	g++ $(INC) -o $(LIB) -shared -lc -fPIC $(SRC)
clean:
	rm $(LIB)
