package homework;

import entertainment.Season;
import fileio.UserInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private final String username;
    private final String subscriptionType;
    private final Map<String, Integer> history;
    private final ArrayList<String> favoriteMovies;
    private final Map<String, Double> moviesGivenRatings;
    private final Map<Season, Double> seasonsGivenRatings;
    private int nrGivenRatings;

    public User(final UserInputData userInputData) {
        this.username = userInputData.getUsername();
        this.subscriptionType = userInputData.getSubscriptionType();
        this.history = userInputData.getHistory();
        this.favoriteMovies = userInputData.getFavoriteMovies();
        this.moviesGivenRatings = new HashMap<>();
        this.seasonsGivenRatings = new HashMap<>();
        this.nrGivenRatings = 0;
    }

    /** getter for the number of ratings given */
    public int getNrGivenRatings() {
        return nrGivenRatings;
    }

    /** setter for the number of ratings given */
    public void setNrGivenRatings(final int nrGivenRatings) {
        this.nrGivenRatings = nrGivenRatings;
    }

    /** getter for the hashmap of the ratings given by the user for the movies seen*/
    public Map<String, Double> getMoviesGivenRatings() {
        return moviesGivenRatings;
    }

    /** getter for the hashmap of the rating given by the user for the seasons seen */
    public Map<Season, Double> getSeasonsGivenRatings() {
        return seasonsGivenRatings;
    }

    /** getter for the username */
    public String getUsername() {
        return username;
    }

    /** getter for the subscription type */
    public String getSubscriptionType() {
        return subscriptionType;
    }

    /** getter for the map with the movies seen by the user */
    public Map<String, Integer> getHistory() {
        return history;
    }

    /** getter for the list with the favorite movies of the user */
    public ArrayList<String> getFavoriteMovies() {
        return favoriteMovies;
    }
}
