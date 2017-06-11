package main.java.Token;

import java.util.LinkedList;

/**
 * Created by qiuzheng on 2017/5/17.
 */
public class Token {
    private TokenType type;
    private String literal;
    private int lineNum;
    private int position;
    public static LinkedList<Token> list = new LinkedList<>();

    public String getLiteral() {
        return literal;
    }

    public TokenType getType() {
        return type;
    }

    public int getLineNum() {
        return lineNum;
    }

    public int getPosition() {
        return position;
    }

    public Token(TokenType Type, String Literal, int LineNum, int Positon)
    {
        this.type = Type;
        this.literal = Literal;
        this.lineNum = LineNum;
        this.position = Positon;
        list.add(this);
    }

    public Token(){}

    public boolean addToken(Token Token)
    {
        boolean b = false;
        if(Token != null)
        {
            this.list.add(Token);
            b = true;
            return b;
        }
        return b;
    }

    public Token findToken(Token Token)
    {
        main.java.Token.Token target = null;
        TokenType type = Token.getType();
        String literal = Token.getLiteral();
        for (main.java.Token.Token currToken: list)
        {
            if(type.equals(currToken.getType()) && literal.equals(currToken.getLiteral()))
            {
                target = currToken;
                return target;
            }
        }
        return target;
    }

    @Override
    public String toString()
    {
        return this.getType()+"\t"+this.getLiteral()+"\t"+this.getLineNum()+"\t"+this.getPosition()+"\n";
    }
}
