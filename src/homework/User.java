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

    public User(UserInputData userInputData) {
        this.username = userInputData.getUsername();
        this.subscriptionType = userInputData.getSubscriptionType();
        this.history = userInputData.getHistory();
        this.favoriteMovies = userInputData.getFavoriteMovies();
        this.moviesGivenRatings = new HashMap<>();
        this.seasonsGivenRatings = new HashMap<>();
        this.nrGivenRatings = 0;
    }

    public int getNrGivenRatings() {
        return nrGivenRatings;
    }

    public void setNrGivenRatings(int nrGivenRatings) {
        this.nrGivenRatings = nrGivenRatings;
    }

    public Map<String, Double> getMoviesGivenRatings() {
        return moviesGivenRatings;
    }

    public Map<Season, Double> getSeasonsGivenRatings() {
        return seasonsGivenRatings;
    }

    public String getUsername() {
        return username;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    public ArrayList<String> getFavoriteMovies() {
        return favoriteMovies;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", subscriptionType='" + subscriptionType + '\'' +
                ", history=" + history +
                ", favoriteMovies=" + favoriteMovies +
                '}';
    }
}
