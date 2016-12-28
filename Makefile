JFLAGS = -cp ./lib/\*: ./src/DatabaseAdaptor.java -d ./bin
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $*.java $(JFLAGS)

CLASSES = ./src/DatabaseAdaptor.java ./src/SpellsGUI.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	rm -f ./src/*.class
