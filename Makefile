# Kacper Kilianek | Agata Biernacka

ANTLR=/usr/local/lib/antlr-4.9.3-complete.jar

all: generate compile test
all-clean: all clean

generate:
	java -jar $(ANTLR) -o output SimpleLang.g4

compile:
	javac -cp $(ANTLR):output:. Main.java

test:
	java -cp $(ANTLR):output:. Main test_if.sl > test.ll
	lli-14 test.ll

clean:
	rm test.ll
	rm *.class
	rm -rf output

# Alternative
# generate:
# 	java -jar /Users/agatabiernacka/Documents/antlr-4.13.0-complete.jar -o output SimpleLang.g4

# compile:
# 	javac -cp /Users/agatabiernacka/Documents/antlr-4.13.0-complete.jar:output:. Main.java

# test:
# 	java -cp /Users/agatabiernacka/Documents/antlr-4.13.0-complete.jar:output:. Main test.sl > test.ll
# 	lli-14 test.ll

# clean:
# 	rm test.ll
# 	rm *.class
# 	rm -rf output
