package cz.nkp.differ.cmdline;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * User: Jonatan Svensson <jonatansve@gmail.com>
 * Date: 2013-07-23
 * Time: 11:57
 */
public class TestHelper {

    public static byte[] readFile(String string) throws IOException {
        RandomAccessFile f = new RandomAccessFile(new File(string), "r");

        try {
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        }catch(IOException e){
            e.printStackTrace();
        }
        finally {
            f.close();
        }
        return null;
    }
}
