package com.alex09x.qsh.reader;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * User: alex
 * Date: 09.01.14
 * Time: 17:21
 */
public class QshReader<T> {

    public QshReader3<T> open(String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(path);
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
