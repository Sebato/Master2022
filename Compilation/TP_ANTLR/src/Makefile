#Makefile for ANTLR

ANTLR=java -jar /home/e20170009949/Téléchargements/lib/antlr-4.11.1-complete.jar
JAVAC= javac
G4=PPgram.g4

bin:
	for i in $(G4); do $(ANTLR) $$i; done; \
        for i in $(G4); do tmp=`basename $$i .g4`; $(JAVAC) $$tmp*.java; done

clean:
	rm -f *~ *.class *.tokens *Lexer.java *Parser.java *Listener.java