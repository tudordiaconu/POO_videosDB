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

    public Video(final ShowInput showInput) {
        this.title = showInput.getTitle();
        this.year = showInput.getYear();
        this.cast = showInput.getCast();
        this.genres = showInput.getGenres();
        this.ratings = new ArrayList<>();
    }

    /** checks if the video's year is the one required in the filter */
    public boolean checkYear(final int filterYear) {
        return this.getYear() == filterYear;
    }

    /** checks if the video's genres are the one required in the filter */
    public boolean checkGenres(final List<String> filterGenres) {
        Map<String, Integer> mapOfGenres = new HashMap<>();

        for (String genre : this.getGenres()) {
            mapOfGenres.put(genre, 0);
        }

        for (String genre : filterGenres) {
            if (!mapOfGenres.containsKey(genre)) {
                return false;
            }
        }

        return true;
    }

    /** does nothing in this class*/
    public abstract double getRating();

    /** getter for title */
    public String getTitle() {
        return title;
    }

    /** getter for year */
    public int getYear() {
        return year;
    }

    /** getter for cast */
    public ArrayList<String> getCast() {
        return cast;
    }

    /** getter for genres */
    public ArrayList<String> getGenres() {
        return genres;
    }

    /** getter for ratings */
    public ArrayList<Double> getRatings() {
        return ratings;
    }
}
