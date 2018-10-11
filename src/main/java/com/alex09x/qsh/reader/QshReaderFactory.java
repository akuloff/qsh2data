package com.alex09x.qsh.reader;

import com.alex09x.qsh.reader.v3.QshReader3;
import com.alex09x.qsh.reader.v4.QshReader4;

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
public class QshReaderFactory<T> {

    public Iterator<T> openStream(InputStream inputStream) throws IOException {
        return getQshReader(inputStream);
    }

    public Iterator<T> openPath(String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(path);
        return getQshReader(fileInputStream);
    }

    private Iterator<T> getQshReader(InputStream fileInputStream) throws IOException {
        DataInputStream dataInputStream = DataFile.getReader(fileInputStream);

        int version = dataInputStream.readByte();

        switch (version) {
            case 3:
                return new QshReader3<>(dataInputStream);
            case 4:
                return new QshReader4<>(dataInputStream);
            default:
                throw new IOException("Unsupported file version (" + version + ")");
        }
    }
}
