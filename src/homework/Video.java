package homework;

import fileio.ShowInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public boolean checkYear(int year) {
        return this.getYear() == year;
    }

    public boolean checkGenres(List<String> genres) {
        Map<String, Integer> mapOfGenres = new HashMap<>();

        for (String genre : this.getGenres()) {
            mapOfGenres.put(genre, 0);
        }

        for (String genre : genres) {
            if (!mapOfGenres.containsKey(genre)) {
                return false;
            }
        }

        return true;
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
