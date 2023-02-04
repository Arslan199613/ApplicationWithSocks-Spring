package com.example.applicationwithsocks.services;
import com.example.applicationwithsocks.model.Socks;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

public interface SocksService {
    long addSocks(Socks socks);

    Socks getSocksById(Long socksId);

    Socks editSocksById(Long socksId, Socks socks);

    void deleteAllSocks();

    boolean deleteSocks(Long socksId);

    Collection<Socks> getAllSocks();

    Path createReport() throws IOException;
}
