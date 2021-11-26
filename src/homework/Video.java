package homework;

import fileio.ShowInput;

import java.util.ArrayList;

public abstract class Video {
    private final String title;
    private final int year;
    private final ArrayList<String> cast;
    private final ArrayList<String> genres;
    private final ArrayList<Double> ratings;

    public Video(ShowInput showInput) {
        this.title = showInput.getTitle();
        this.year = showInput.getYear();
        this.cast = showInput.getCast();
        this.genres = showInput.getGenres();
        this.ratings = new ArrayList<>();
    }

    public abstract double getRating();

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public ArrayList<String> getCast() {
        return cast;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public ArrayList<Double> getRatings() {
        return ratings;
    }
}
