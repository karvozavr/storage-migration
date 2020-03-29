package ru.karvozavr.data;

public class FileData {

    public FileData(String name, byte[] bytes) {
        this.name = name;
        this.bytes = bytes;
    }

    private String name;
    private byte[] bytes;

    public String getName() {
        return name;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
