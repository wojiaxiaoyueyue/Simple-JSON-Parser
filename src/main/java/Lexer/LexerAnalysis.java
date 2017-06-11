package main.java.Lexer;

import main.java.Exception.LexerException;
import main.java.Exception.SyntaxException;
import main.java.SourceRead.FileRead;
import main.java.Token.Token;
import main.java.Token.TokenType;

/**
 * Created by qiuzheng on 2017/6/6.
 */
public class LexerAnalysis {
    private FileRead source;
    public LexerAnalysis(FileRead source){this.source = source;}
    public boolean hasNext(){return source.findCharByOffset()!=FileRead.EOF;}
    public int beginPosition;
    public int beginLineNum;
    public StringBuilder literal = new StringBuilder();

    //对float点数的解析
    public boolean isFloat = false;
    //对科学技术法的数的解析
    public boolean isScientific = false;

    public char IgnoreWhiteSpace(FileRead source)
    {
        char ch = source.nextChar();
        //ignore the whitespace
        while(Character.isWhitespace(ch)){
            ch = source.nextChar();
        }
        return ch;
    }

    public boolean matchTrue(String content)
    {
        if(content.length() == 4)
        {
            if(content.charAt(0) == 't' && content.charAt(1) == 'r' && content.charAt(2) == 'u' && content.charAt(3) == 'e')
                return true;
            else
                return false;
        }
        else
        {
            return false;
        }
    }

    public boolean matchFalse(String content)
    {
        if(content.length() == 5)
        {
            if (content.charAt(0) == 'f' && content.charAt(1) == 'a' && content.charAt(2) == 'l' && content.charAt(3) == 's' && content.charAt(4) == 'e')
                return true;
            else
                return false;
        }
        else
            return false;
    }

    public boolean matchNull(String content)
    {
        if(content.length() == 4)
        {
            if(content.charAt(0) == 'n' && content.charAt(1) == 'u' && content.charAt(2) == 'l' && content.charAt(3) == 'l')
                return  true;
            else
                return false;
        }
        else
            return false;
    }

    public boolean matchNumber(String content)
    {
        isFloat = false;
        isScientific = false;
        //如果为负数
        if(content.charAt(0) == '-')
        {
            if(content.charAt(1) == '0')
            {
                //出现-0属于错误
                if(content.length() == 2)
                    return false;
                //0开头的float型或者scientific型数
                else if(content.length() > 3)
                {
                    if(content.charAt(2) == '.')
                    {
                        isFloat = true;
                        for(int i = 3; i<content.length(); i++)
                        {
                            if(content.charAt(i) == 'e' || content.charAt(i) == 'E')
                            {
                                if(isScientific)
                                    return false;
                                isScientific = true;
                                continue;
                            }
                            if (Character.isDigit(content.charAt(i)))
                            {
                                continue;
                            }
                            if(Character.isAlphabetic(content.charAt(i)))
                                return false;
                            if(content.charAt(i) == '.')
                                return false;
                        }
                    }
                    return true;
                }
            }
            //非0开头的负数
            else if(Character.isDigit(content.charAt(1)))
            {
                for(int j = 1; j < content.length(); j++)
                {
                    if(content.charAt(j) == '.')
                    {
                        if(isFloat)
                            return false;
                        isFloat = true;
                        continue;
                    }
                    if(content.charAt(j) == 'e' || content.charAt(j) == 'E')
                    {
                        if(isScientific)
                            return false;
                        isScientific = true;
                        continue;
                    }
                    if (Character.isDigit(content.charAt(j)))
                    {
                        continue;
                    }
                    if(Character.isAlphabetic(content.charAt(j)))
                        return false;
                }
                return true;
            }
            //其他默认错误
            else
                return false;
        }
        //如果为正数
        else
        {
            //如果为0开头
            if(content.charAt(0) == '0')
            {
                if(content.length() == 1)
                    return true;
                else if(content.length()>2)
                {
                    if (content.charAt(1) == '.')
                    {
                        isFloat = true;
                        for (int i = 2; i<content.length(); i++)
                        {
                            if(content.charAt(i) == 'e' || content.charAt(i) == 'E')
                            {
                                if(isScientific)
                                    return false;
                                isScientific = true;
                                continue;
                            }
                            if (Character.isDigit(content.charAt(i)))
                            {
                                continue;
                            }
                            if(Character.isAlphabetic(content.charAt(i)))
                                return false;
                            if(content.charAt(i) == '.')
                                return false;
                        }
                        return true;
                    }
                    else
                        return false;
                }
                else
                    return false;
            }
            //如果不为零开头
            else
            {
                for(int j = 0; j < content.length(); j++)
                {
                    if(content.charAt(j) == '.')
                    {
                        if (isFloat)
                            return false;
                        isFloat = true;
                        continue;
                    }
                    if(content.charAt(j) == 'e' || content.charAt(j) == 'E')
                    {
                        if(isScientific)
                            return false;
                        isScientific = true;
                        continue;
                    }
                    if (Character.isDigit(content.charAt(j)))
                    {
                        continue;
                    }
                    if(Character.isAlphabetic(content.charAt(j)))
                        return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Clear the cache of the StringBuilder
     * After searching some information on search Engines
     * I find that this method is the best one.
     * @param  sb
     */
    public void clearStringBuilder(StringBuilder sb)
    {
        if(sb.length()!=0)
            sb.setLength(0);
    }
    /*  look at this example
            {
                "users":
                [
                    {
                        "name":"Alice",
                        "sex":"female",
                        "age":20,
                        "married":false,
                        "vip":true,
                        "introduction":null,
                        "accounts":2.3333333
                    }
                ]
            }
    */

    public Token Analysis() throws LexerException, SyntaxException {
        char ch = IgnoreWhiteSpace(source);
        beginLineNum = source.getLineNum();
        beginPosition = source.getPostionOffset();

        //读取到文件末尾
        if(ch == FileRead.EOF)
        {
            return new Token(TokenType.EOF, "EOF", beginLineNum, beginPosition);
        }

        if(ch == '{')
        {
            return new Token(TokenType.LEFT_BRACE, "{", beginLineNum, beginPosition);

            //对应的BNF为<pair> ::= <string> : <value>
        }

        if(ch == '}')
        {
            return new Token(TokenType.RIGHT_BRACE, "}", beginLineNum, beginPosition);
        }

        if(ch == '\"')
        {
            //清空StringBuilder
            clearStringBuilder(literal);
            //截取字符串
            while((ch = source.nextChar())!= '\"')
            {
                if(ch == '}' || ch == ']' || ch == '{' || ch == '[' )
                    throw new SyntaxException("missing \" at line: " + source.getLineNum() + " Position: " + source.getPostionOffset());
                literal.append(ch);
            }
            return new Token(TokenType.STRING, literal.toString(), beginLineNum, beginPosition);
        }


        if(ch == ':')
        {
            return new Token(TokenType.COLON, ":", beginLineNum, beginPosition);
        }
        //解析true
        if(ch == 't')
        {
            //清空
            clearStringBuilder(literal);
            literal.append(ch);
            while((ch = source.nextChar())!= ',')
            {
                if(ch == '}')
                    break;
                if(Character.isWhitespace(ch))
                    continue;

                literal.append(ch);
            }
            //回退一个字符
            //source.findCharByOffset(-1);
            source.backPostion(1);
            if(matchTrue(literal.toString()) == true)
                return new Token(TokenType.TRUE, "true", beginLineNum, beginPosition);
            else {
                throw new LexerException("INVALID TokenType at line: " + beginLineNum + " Position: " + beginPosition + " value: " + literal.toString());
                //return new Token(TokenType.INVALID, literal.toString(), beginLineNum, beginPosition);

            }

        }

        //解析false
        if(ch == 'f')
        {
            clearStringBuilder(literal);
            literal.append(ch);
            while((ch = source.nextChar()) != ',')
            {
                if (ch == '}')
                    break;
                if(Character.isWhitespace(ch))
                    continue;
                literal.append(ch);
            }

            //source.findCharByOffset(-1);
            source.backPostion(1);
            if(matchFalse(literal.toString()) == true)
                return new Token(TokenType.FALSE, "false", beginLineNum, beginPosition);
            else
                throw new LexerException("INVALID TokenType at line: " + beginLineNum + " Position: " + beginPosition + " value: " + literal.toString());
                //return new Token(TokenType.INVALID, literal.toString(), beginLineNum, beginPosition);
        }

        //解析null

        if(ch == 'n')
        {
            clearStringBuilder(literal);
            literal.append(ch);
            while ((ch = source.nextChar()) != ',')
            {
                if(ch == '}')
                    break;
                if(Character.isWhitespace(ch))
                    continue;
                literal.append(ch);
            }

//            source.findCharByOffset(-1);
            source.backPostion(1);
            if(matchNull(literal.toString()) == true)
                return new Token(TokenType.NULL, "null", beginLineNum, beginPosition);
            else
                throw new LexerException("INVALID TokenType at line: " + beginLineNum + " Position: " + beginPosition + " value: " + literal.toString());
                //return new Token(TokenType.INVALID, literal.toString(), beginLineNum, beginPosition);

        }

        if(ch == ',')
        {
            return new Token(TokenType.COMMA, ",", beginLineNum, beginPosition);
        }

        if(ch == '[')
        {
            return new Token(TokenType.LEFT_BRACKET, "[", beginLineNum, beginPosition);
        }

        if(ch == ']')
        {
            return new Token(TokenType.RIGHT_BRACKET, "]", beginLineNum, beginPosition);
        }

        //解析数字
        if(Character.isDigit(ch) || ch == '-')
        {
            clearStringBuilder(literal);
            literal.append(ch);
            while ((ch = source.nextChar()) != ',')
            {
                if (ch == '}')
                    break;
                if(Character.isWhitespace(ch))
                    continue;
                literal.append(ch);
            }

            //指针回退
//            System.out.println(ch);
            source.backPostion(1);
//            System.out.println(source.nextChar());
            if(matchNumber(literal.toString()) == true && isScientific)
            {
                return new Token(TokenType.SCIENTIFIC, literal.toString(), beginLineNum, beginPosition);
            }
            else if(matchNumber(literal.toString()) == true && isFloat)
            {
                return new Token(TokenType.FLOAT, literal.toString(), beginLineNum, beginPosition);
            }
            else if(matchNumber(literal.toString()) == true)
            {
                return new Token(TokenType.INTEGER, literal.toString(), beginLineNum, beginPosition);
            }
            else {
                throw new LexerException("INVALID TokenType at line: " + beginLineNum + " Position: " + beginPosition + " value: " + literal.toString());
                //return new Token(TokenType.INVALID, literal.toString(), beginLineNum, beginPosition);
            }

        }
        else
            return null;

    }
}
