package main.java.SourceRead;

import java.io.*;

/**
 * Created by qiuzheng on 2017/5/17.
 */
public class FileRead implements AutoCloseable
{
    //end of file
    public static final char EOF = (char)0;
    //end of line
    public static final char EOL = '\n';
    //the reader
    private BufferedReader bufferedReader;
    //curr line number
    private int lineNum = 0;
    //the content of current line
    private String content;
    //the cursor of line
    private int postionOffset;

    private String path;

    public int getLineNum() {
        return lineNum;
    }

    public int getPostionOffset() {
        return postionOffset;
    }

    public FileRead(String path)
    {
        try
        {
            this.path = path;
            this.bufferedReader = new BufferedReader(new FileReader(path));
            content = readNextLine();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public String readNextLine()
    {
        String currLine = null;
        try
        {
            currLine=bufferedReader.readLine();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        postionOffset = -1;
        if(currLine != null)
        {
            lineNum = getLineNum() + 1;
            currLine += FileRead.EOL;
        }
        return currLine;
    }

    /**
     * update the private attribute refer to postionOffset
     * move the cursor of current line
     * @return  charAt(++postionOffset)
     */
    public char nextChar()
    {
        postionOffset++;
        if(content == null)
            return FileRead.EOF;
        else if(content.charAt(postionOffset) == FileRead.EOL || content.length() == 0)
        {
            content = readNextLine();
            return nextChar();
        }
        else
            return content.charAt(postionOffset);
    }

    /**
     *  Different from the func nextChar()
     *  Depending on the value of offset
     *  It will not change the postionOffset
     * @param offset
     * @return  charAt(postionOffset + offset)
     */
    public char findCharByOffset(int offset)
    {
        int index = postionOffset + offset;
        return content.charAt(index);
    }

    /**
     * the default value is 1
     * @return
     */
    public char findCharByOffset()
    {
        if(content!=null)
        return findCharByOffset(1);
        else
            return EOF;
    }

    public void backPostion(int offset)
    {
        postionOffset = postionOffset - offset;
    }

    @Override
    public void close()
    {
        if(this.bufferedReader!=null)
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
