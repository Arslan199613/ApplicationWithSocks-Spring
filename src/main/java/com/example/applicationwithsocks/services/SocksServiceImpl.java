package com.example.applicationwithsocks.services;
import com.example.applicationwithsocks.model.Socks;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

@Service
public class SocksServiceImpl implements SocksService {
    Map<Long, Socks> socksMap = new TreeMap<>();
    private static long socksId = 0;

    private FileSocksService fileSocksService;

    public SocksServiceImpl(FileSocksService fileSocksService) {
        this.fileSocksService = fileSocksService;
    }
    @PostConstruct
    private void init() {
        try {
            readFromFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public long addSocks(Socks socks) {
        socksMap.put(socksId, socks);
        saveToFile();
        return socksId++;
    }
    @Override
    public Socks getSocksById(Long socksId) {
        Socks socks = socksMap.get(socksId);
        if (socks != null) {
            return socks;
        }
        return null;
    }

    @Override
    public Socks editSocksById(Long socksId,Socks socks) {
        if (socksMap.containsKey(socksId)) {
            socksMap.put(socksId, socks);
            saveToFile();
            return socks;

        }
        return null;
    }
    @Override
    public void deleteAllSocks() {
        socksMap.clear();
    }

    @Override
    public boolean deleteSocks(Long socksId) {
        var removed = socksMap.remove(socksId);
        return removed != null;
    }
    @Override
    public Collection<Socks> getAllSocks() {
        return socksMap.values();
    }

    @Override
    public Path createReport() throws IOException {
        socksMap.getOrDefault(socksId, null);
        Path socksText = fileSocksService.createTempFile("Socks_text");
        for (Socks socks : socksMap.values()) {
            try (Writer writer = Files.newBufferedWriter(socksText, StandardOpenOption.APPEND)) {
                writer.append("Тип(ADD,REMOVE,GET): "+ socks.getType()+", цвет -  "+ socks.getColor() + " ,размер   -   " + socks.getSize() + " , количество  -  "
                        + socks.getQuantity()+" ,процентное содержание хлопка   -   " + socks.getCottonPartAsAPercentage());
                writer.append("\n");
            }
        }
        return socksText;
    }
    private void saveToFile() {
        try {
            DataFile dataFile = new DataFile(socksId + 1,socksMap);
            String json = new ObjectMapper().writeValueAsString(dataFile);
            fileSocksService.saveToFile(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void readFromFile() {
        try {
            String json = fileSocksService.readFromFile();
            DataFile dataFile=new ObjectMapper().readValue(json, new TypeReference<DataFile>() {
            });
            socksId = dataFile.socksId;
            socksMap = dataFile.getSocksMap();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class DataFile {
        private long socksId;
        private Map<Long, Socks> socksMap;
    }
}