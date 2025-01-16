package ru.job4j.cinema.dto;

import java.util.Objects;

public class FilmDto {

//    private final int id;
//
//    private final String name;
//
//    private final String description;
//
//    private final int year;
//
//    private final int minimalAge;
//
//    private final int durationInMinutes;
//
//    private final String genre;

    private int id;

    private String name;

    private String description;

    private int year;

    private int minimalAge;

    private int durationInMinutes;

    private String genre;

//    public FilmDto(int id, String name, String description, int year, int minimalAge, int durationInMinutes, String genre) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.year = year;
//        this.minimalAge = minimalAge;
//        this.durationInMinutes = durationInMinutes;
//        this.genre = genre;
//    }

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FilmDto filmDto = (FilmDto) o;
        return id == filmDto.id && year == filmDto.year && minimalAge == filmDto.minimalAge && durationInMinutes == filmDto.durationInMinutes && Objects.equals(name, filmDto.name) && Objects.equals(description, filmDto.description) && Objects.equals(genre, filmDto.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, year, minimalAge, durationInMinutes, genre);
    }
}
