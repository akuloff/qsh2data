package com.alex09x.qsh.reader;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;

/**
 * User: alex
 * Date: 09.01.14
 * Time: 17:23
 */
public class DataFile {
    static byte[] prefix = "QScalp History Data".getBytes(Charset.forName("US-ASCII"));

    static boolean checkPrefix(DataInput stream, byte[] buffer) throws IOException {
        stream.readFully(buffer, 0, buffer.length);
        for (int i = 0; i < buffer.length; i++)
            if (buffer[i] != prefix[i])
                return false;
        return true;
    }

    public static DataInputStream getReader(FileInputStream fs) throws IOException {
        byte[] buffer = new byte[prefix.length];

        InputStream stream = new GZIPInputStream(fs);
        DataInputStream in = new DataInputStream(new BufferedInputStream(stream));

        if (checkPrefix(in, buffer))
            return in;

        throw new IOException("Incorrect file format.");
    }
}
