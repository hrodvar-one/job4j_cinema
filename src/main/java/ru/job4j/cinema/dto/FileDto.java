package ru.job4j.cinema.dto;

public class FileDto {

    private String name;

    private byte[] fileContent;

    public FileDto(String name, byte[] fileContent) {
        this.name = name;
        this.fileContent = fileContent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }
}
