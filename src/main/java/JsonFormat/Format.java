package main.java.JsonFormat;

import java.io.*;

/**
 * Created by qiuzheng on 2017/6/10.
 */
public class Format {
    private String currLine;
    private File file;
    private InputStreamReader inputStreamReader;
    private BufferedReader reader;
    private StringBuilder stringBuilder;
    private String path;

    public Format(String filePath) throws IOException {
        this.path = filePath;
        this.file = new File(filePath);
        this.inputStreamReader = new InputStreamReader(new FileInputStream(file));
        this.reader = new BufferedReader(inputStreamReader);
        this.stringBuilder = new StringBuilder();
        while ((currLine = reader.readLine()) != null)
        {
            stringBuilder.append(currLine);
        }
    }

    public String jsonFormat(StringBuilder sb)
    {
        StringBuilder outStringBuilder = new StringBuilder();
        String content = sb.toString();
        int size = content.length();
        //当前char
        int index = 0;
        //记录\t个数
        int count = 0;
        while(index < size)
        {
            char ch = content.charAt(index);
            if(ch == '{' || ch == '[')
            {
                outStringBuilder.append(ch);
                outStringBuilder.append('\n');
                count++;
                for(int i = 0; i < count; i++)
                {
                    outStringBuilder.append('\t');
                }
            }
            else if(ch == '}' || ch == ']')
            {
                outStringBuilder.append('\n');
                count--;
                for(int i = 0; i<count; i++)
                {
                    outStringBuilder.append('\t');
                }
                outStringBuilder.append(ch);
            }
            else if(ch == ',')
            {
                outStringBuilder.append(ch);
                outStringBuilder.append('\n');
                for(int i = 0; i<count; i++)
                {
                    outStringBuilder.append('\t');
                }
            }
            else
            {
                outStringBuilder.append(ch);
            }

            index++;
        }
        return outStringBuilder.toString();
    }

    public  void jsonFormat() throws IOException {
        String[] params = path.split("/");
        int size = params.length;
        String sourceFileName = params[size-1];
        String[] fileName = sourceFileName.split("\\.");
        StringBuilder outFileNameStringBuilder = new StringBuilder();
        outFileNameStringBuilder.append(fileName[0]);
        outFileNameStringBuilder.append(".pretty.");
        outFileNameStringBuilder.append(fileName[1]);
        String outFileName = outFileNameStringBuilder.toString();
        String outContent = jsonFormat(this.stringBuilder);
        RandomAccessFile raf = new RandomAccessFile(outFileName, "rw");
        raf.writeBytes(outContent);
    }
}
