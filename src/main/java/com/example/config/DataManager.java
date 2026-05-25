package com.example.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class DataManager {

    private static final ThreadLocal<List<Map<String, Object>>> dataList = new ThreadLocal<>();
    private static final ThreadLocal<Integer> currentIndex = new ThreadLocal<>();
    private static final ThreadLocal<Map<String, Object>> currentEntry = new ThreadLocal<>();

    public static void loadDataFile(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = DataManager.class.getClassLoader().getResourceAsStream(filePath)) {
            if (is == null) {
                throw new RuntimeException("Data file not found: " + filePath);
            }
            List<Map<String, Object>> data = mapper.readValue(is, new TypeReference<List<Map<String, Object>>>() {});
            dataList.set(data);
            Integer index = currentIndex.get();
            if (index != null && index < data.size()) {
                currentEntry.set(data.get(index));
            } else if (!data.isEmpty()) {
                currentEntry.set(data.get(0));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading data file: " + filePath, e);
        }
    }

    public static void setCurrentDataIndex(int index) {
        currentIndex.set(index);
        List<Map<String, Object>> data = dataList.get();
        if (data != null && index < data.size()) {
            currentEntry.set(data.get(index));
        }
    }

    public static String resolveValue(String raw) {
        if (raw != null && !raw.isEmpty()) {
            return raw;
        }
        Map<String, Object> entry = currentEntry.get();
        if (entry != null && entry.containsKey("username")) {
            return (String) entry.get("username");
        }
        return raw;
    }

    public static Object getValue(String key) {
        Map<String, Object> entry = currentEntry.get();
        if (entry != null) {
            return entry.get(key);
        }
        return null;
    }

    public static void clearData() {
        dataList.remove();
        currentIndex.remove();
        currentEntry.remove();
    }
}
