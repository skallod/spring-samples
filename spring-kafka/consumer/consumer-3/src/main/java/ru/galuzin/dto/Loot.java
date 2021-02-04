package ru.galuzin.dto;

public class Loot {

    String name;

    byte[] data;

    CompressType compressType;

    public Loot() {
    }

    public Loot(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public CompressType getCompressType() {
        return compressType;
    }

    public void setCompressType(CompressType compressType) {
        this.compressType = compressType;
    }

    @Override
    public String toString() {
        return "Loot{" +
                "name='" + name + '\'' +
                '}';
    }
}
