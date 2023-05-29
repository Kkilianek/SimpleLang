

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {
    public static void main(String[] args) throws Exception {

        ANTLRFileStream input = new ANTLRFileStream(args[0]);

        SimpleLangLexer lexer = new SimpleLangLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SimpleLangParser parser = new SimpleLangParser(tokens);

        ParseTree tree = parser.program();

        //System.out.println(tree.toStringTree(parser));

        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new LLVMActions(), tree);

    }
}
