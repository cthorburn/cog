package com.cog.lang;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Interpreter {
	
	public static void main(String[] args) throws Exception {
        MuLexer lexer = new MuLexer(new ANTLRFileStream("src/main/resources/test.mu"));
        MuParser parser = new MuParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.parse();
        MuBaseVisitor<Value> visitor = new MuBaseVisitor<Value>();
        visitor.visit(tree);
    }
}
