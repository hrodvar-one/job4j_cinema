package ru.job4j.cinema.dto;

public class FilmDto {

    private int id;

    private String name;

    private String description;

    private int year;

    private int minimalAge;

    private int durationInMinutes;

    private String genre;

    private String posterPath;

    private int fileId;

    public FilmDto(int id, String name, String description, int year, int minimalAge, int durationInMinutes,
                   String genre, String posterPath, int fileId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.year = year;
        this.minimalAge = minimalAge;
        this.durationInMinutes = durationInMinutes;
        this.genre = genre;
        this.posterPath = posterPath;
        this.fileId = fileId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getYear() {
        return year;
    }

    public int getMinimalAge() {
        return minimalAge;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public String getGenre() {
        return genre;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public int getFileId() {
        return fileId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMinimalAge(int minimalAge) {
        this.minimalAge = minimalAge;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }
}
