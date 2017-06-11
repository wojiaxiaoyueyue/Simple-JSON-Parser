package main.java.Parser;

import main.java.Exception.LexerException;
import main.java.Exception.SyntaxException;
import main.java.Lexer.LexerAnalysis;
import main.java.Token.Token;
import main.java.Token.TokenType;
import java.util.LinkedList;

/**
 * Created by qiuzheng on 2017/6/8.
 * Syntax Analysis to examine the source file's syntax format
 * using the tokens information previously processed by LexerAnalysis
 */
public class SyntaxAnalysis {
    //current Token
    private Token cursor;
    //store tokens
    private LinkedList<Token> tokens;

    private LexerAnalysis lexerAnalysis;

    public SyntaxAnalysis(LexerAnalysis lexerAnalysis)
    {
        this.tokens = new LinkedList<>();
        this.lexerAnalysis = lexerAnalysis;
    }

    public void initParse() throws LexerException, SyntaxException {
        while (lexerAnalysis.hasNext()){
            tokens.add(lexerAnalysis.Analysis());
        }
        cursor = tokens.poll();

    }

    private void syntaxError(Token token) throws SyntaxException
    {
        throw new SyntaxException("SyntaxException Found at line: " + token.getLineNum() +
                " Position: " + token.getPosition() +
                " TokenType: " + token.getType() +
                " Value: " + token.getLiteral());
    }

    private void missingError(TokenType tokenType) throws SyntaxException
    {
        throw new SyntaxException("Missing " + tokenType + " at line: " + cursor.getLineNum() + " Position: " + cursor.getPosition());
    }

    private void match(TokenType tokenType) throws SyntaxException
    {
        if(!cursor.getType().equals(tokenType))
        {
            missingError(tokenType);
        }
        else
        {
            cursor = tokens.poll();
        }
    }

    /**
     * <json> ::= <object> | <array>
     */
    public void json() throws SyntaxException
    {
        switch (cursor.getType())
        {
            case LEFT_BRACE:
            {
                cursor = tokens.poll();
                object();
                break;
            }
            case LEFT_BRACKET:
            {
                cursor = tokens.poll();
                array();
                break;
            }
        }

    }

    /**
     * <object> ::= "{" "}" | "{" <members> "}"
     * @throws SyntaxException
     */
    public void object() throws SyntaxException
    {
        if(cursor.getType().equals(TokenType.RIGHT_BRACE))
            return;
        else {
            members();
            match(TokenType.RIGHT_BRACE);
        }
    }

    /**
     * <members> ::= <pair> | <pair> "," <members>
     * @throws SyntaxException
     */
    public void members() throws SyntaxException
    {
        pair();
        if(cursor.getType().equals(TokenType.COMMA))
        {
            match(TokenType.COMMA);
            members();
        }
    }

    /**
     * <pair> ::= <string> ":" <value>
     * @throws SyntaxException
     */
    public void pair() throws SyntaxException
    {
        match(TokenType.STRING);
        match(TokenType.COLON);
        value();
    }

    /**
     * <value> ::= <string> | <number> | <object> | <array> | "true" | "false" | "null"
     * @throws SyntaxException
     */

    public void value() throws SyntaxException
    {
        switch (cursor.getType())
        {
            case STRING:
            case INTEGER:
            case FLOAT:
            case SCIENTIFIC:
            case NULL:
            case TRUE:
            case FALSE:
                cursor = tokens.poll();
                return;
            case LEFT_BRACE:
                cursor = tokens.poll();
                object();
                return;
            case LEFT_BRACKET:
                cursor = tokens.poll();
                array();
                return;
            default:
                syntaxError(cursor);

        }
    }

    /**
     * <array> ::= "[" "]" | "[" <elements> "]"
     * @throws SyntaxException
     */
    public void array() throws  SyntaxException{
        if(cursor.getType().equals(TokenType.RIGHT_BRACKET))
            return;
        else
        {
            elements();
            match(TokenType.RIGHT_BRACKET);
        }
    }

    /**
     * <elements> ::= <value> | <value> "," <elements>
     * @param
     * @throws SyntaxException
     */
    public void elements() throws SyntaxException{
        value();
        if(cursor.getType().equals(TokenType.COMMA))
        {
            match(TokenType.COMMA);
            elements();
        }
    }
}
