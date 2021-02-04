package ru.galuzin.dto;

import java.util.Arrays;

public class Loot {
    String name;

    CompressType compressType;

    byte[] data;

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
                ", compressType='" + compressType + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
