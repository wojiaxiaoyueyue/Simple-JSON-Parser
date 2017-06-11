package main.java.Token;

/**
 * Created by qiuzheng on 2017/5/17.
 */
public enum TokenType {
    /**
     * According to the BNF of this homework
     * List the value of <value>
     */
    STRING,
    INTEGER,
    FLOAT,
    SCIENTIFIC,
    TRUE,
    FALSE,
    NULL,
    /**
     * "{"
     */
    LEFT_BRACE,
    /**
     * "}"
     */
    RIGHT_BRACE,
    /**
     * "["
     */
    LEFT_BRACKET,
    /**
     * "]"
     */
    RIGHT_BRACKET,
    /**
     * ","
     */
    COMMA,
    /**
     * ":"
     */
    COLON,
    /**
     * END OF FILE
     */
    EOF,
    /**
     * INVALID VALUE
     */
    INVALID,

}

