# Kacper Kilianek | Agata Biernacka

ANTLR=/usr/local/lib/antlr-4.9.3-complete.jar

all: generate compile test
all-clean: all clean

generate:
	java -jar $(ANTLR) -o output SimpleLang.g4

compile:
	javac -cp $(ANTLR):output:. Main.java

test:
	java -cp $(ANTLR):output:. Main test_bool.sl > test.ll
	lli-14 test.ll

clean:
	rm test.ll
	rm *.class
	rm -rf output

