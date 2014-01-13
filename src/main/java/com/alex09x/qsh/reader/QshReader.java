package com.alex09x.qsh.reader;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * User: alex
 * Date: 09.01.14
 * Time: 17:21
 */
public class QshReader<T> {

    public Iterator<T> openStream(InputStream inputStream) throws IOException {
        return getQshReader3(inputStream);
    }

    public Iterator<T> openPath(String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(path);
        return getQshReader3(fileInputStream);
    }

    private Iterator<T> getQshReader3(InputStream fileInputStream) throws IOException {
        DataInputStream dataInputStream = DataFile.getReader(fileInputStream);

        int version = dataInputStream.readByte();

        switch (version) {
            case 3:
                return new QshReader3(dataInputStream);
            default:
                throw new IOException("Unsupported file version (" + version + ")");
        }
    }
}
