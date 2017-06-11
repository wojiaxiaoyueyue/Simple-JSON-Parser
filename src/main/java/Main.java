package main.java;

import main.java.Exception.LexerException;
import main.java.Exception.SyntaxException;
import main.java.JsonFormat.Format;
import main.java.Lexer.LexerAnalysis;
import main.java.Parser.SyntaxAnalysis;
import main.java.SourceRead.FileRead;
import main.java.Token.Token;
import main.java.Token.TokenType;

import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by qiuzheng on 2017/5/17.
 */
public class Main {
    public static void main(String[] args) throws LexerException, SyntaxException, IOException {


        if(args.length == 1) {

            String FilePath = args[0];

            FileRead source = new FileRead(FilePath);
            LexerAnalysis lex = new LexerAnalysis(source);
            LinkedList<Token> tokens = Token.list;
            boolean isValid = true;
            while (!(lex.Analysis().getLiteral().equals("EOF"))) {
            }
            //词法分析
            for (int i = 0; i < tokens.size(); i++) {
               // System.out.println(tokens.get(i).toString());
                if (tokens.get(i).getType() == TokenType.INVALID) {
                    isValid = false;
                    break;
                }
            }

            if (isValid)
                System.out.println("Valid");
            else
                System.out.println("INValid");

            //语法分析
            FileRead source_s = new FileRead(FilePath);
            lex = new LexerAnalysis(source_s);
            SyntaxAnalysis syntaxAnalysis = new SyntaxAnalysis(lex);
            syntaxAnalysis.initParse();
            syntaxAnalysis.json();
        }

        if(args.length == 2 && args[0].equals("-pretty"))
        {
            String FilePath = args[1];
            //格式化json
            Format format = new Format(FilePath);
            format.jsonFormat();
        }

    }
}
