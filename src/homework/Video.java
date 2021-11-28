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

    /** returns the number of times a video is found in the users' favourites list */
    public int getNumberOfFavored(final Database database) {
        int number = 0;
        for (User user : database.getUserMap().values()) {
            for (String favoriteTitle : user.getFavoriteMovies()) {
                if (this.title.equals(favoriteTitle)) {
                    number++;
                }
            }
        }

        return number;
    }

    /** method that returns number of total views of a video */
    public int getNumberOfViews(final Database database) {
        int number = 0;
        for (User user : database.getUserMap().values()) {
            for (String titleInHistory : user.getHistory().keySet()) {
                if (this.title.equals(titleInHistory)) {
                    number += user.getHistory().get(titleInHistory);
                }
            }
        }

        return number;
    }

    /** checks if the video's year is the one required in the filter */
    public boolean checkYear(final int filterYear) {
        return this.getYear() == filterYear;
    }

    /** checks if the video contains the required genre among its genres */
    public boolean checkGenre(final String filterGenre) {
        Map<String, Integer>  mapOfGenres = new HashMap<>();

        for (String genre : this.getGenres()) {
            mapOfGenres.put(genre, 0);
        }

        return mapOfGenres.containsKey(filterGenre);
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

    public abstract double getRatingFromDatabase(Database database);

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
