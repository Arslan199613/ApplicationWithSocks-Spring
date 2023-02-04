package com.example.applicationwithsocks.services;
import java.io.File;
import java.nio.file.Path;

public interface FileSocksService {
    boolean saveToFile(String json);

    Path createTempFile(String suffix);

    String readFromFile();

    File getDataFile();

    boolean cleanDataFile();
}