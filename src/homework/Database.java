package homework;

import fileio.Input;
import fileio.MovieInputData;
import org.json.simple.JSONArray;
import fileio.Writer;
import fileio.ActionInputData;
import fileio.ActorInputData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import fileio.SerialInputData;
import fileio.UserInputData;


public class Database {
    private final Map<String, Actor> actorMap;
    private final Map<String, Movie> movieMap;
    private final Map<String, Serial> serialMap;
    private final Map<String, User> userMap;
    private final Input input;
    private final Map<String, Integer> genresMap;

    /** getter for hashmap of genres */
    public Map<String, Integer> getGenresMap() {
        return genresMap;
    }

    private void addActorsToDatabase() {
        for (ActorInputData actorInputData : input.getActors()) {
            Actor actor = new Actor(actorInputData);
            actorMap.put(actor.getName(), actor);
        }
    }

    private void addMoviesToDatabase() {
        for (MovieInputData movieInputData : input.getMovies()) {
            Movie movie = new Movie(movieInputData);
            movieMap.put(movie.getTitle(), movie);
        }
    }

    private void addSerialsToDatabase() {
        for (SerialInputData serialInputData : input.getSerials()) {
            Serial serial = new Serial(serialInputData);
            serialMap.put(serial.getTitle(), serial);
        }
    }

    private void addUsersToDatabase() {
        for (UserInputData userInputData : input.getUsers()) {
            User user = new User(userInputData);
            userMap.put(user.getUsername(), user);
        }
    }

    private void addGenresToDatabase() {
        for (Movie movie : movieMap.values()) {
            for (String genre : movie.getGenres()) {
                if (!genresMap.containsKey(genre)) {
                    genresMap.put(genre, movie.getNumberOfViews(this));
                } else {
                    genresMap.put(genre, genresMap.get(genre) + movie.getNumberOfViews(this));
                }
            }
        }
    }

    public Database(final Input input) {
        this.actorMap = new HashMap<>();
        this.movieMap = new HashMap<>();
        this.serialMap = new HashMap<>();
        this.userMap = new HashMap<>();
        this.genresMap = new HashMap<>();
        this.input = input;
        this.addActorsToDatabase();
        this.addMoviesToDatabase();
        this.addSerialsToDatabase();
        this.addUsersToDatabase();
        this.addGenresToDatabase();
    }

    private void commandView(final ActionInputData actionInputData,
                     final Writer writer, final JSONArray arrayResult) throws IOException {
        Command.view(this, actionInputData, writer, arrayResult);
    }

    @SuppressWarnings("unchecked")
    private void commandFavorite(final ActionInputData actionInputData, final Writer writer,
                          final JSONArray arrayResult) throws IOException {
        int alreadyExists = Command.favorite(actionInputData.getUsername(),
                actionInputData.getTitle(), this);

        switch (alreadyExists) {
            default -> arrayResult.add(writer.writeFile(actionInputData.getActionId(),
                    "", "error -> " + actionInputData.getTitle() + " is not seen"));

            case 1 -> arrayResult.add(writer.writeFile(actionInputData.getActionId(),
                    "", "success -> "
                            + actionInputData.getTitle() + " was added as favourite"));

            case 2 -> arrayResult.add(writer.writeFile(actionInputData.getActionId(),
                    "", "error -> " + actionInputData.getTitle()
                            + " is already in favourite list"));
        }
    }

    @SuppressWarnings("unchecked")
    private void commandRating(final ActionInputData actionInputData, final Writer writer,
                        final JSONArray arrayResult) throws IOException {
        int found = Command.rating(actionInputData.getUsername(),
                actionInputData.getTitle(), this, actionInputData);

        switch (found) {
            default -> arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                    "error -> " + actionInputData.getTitle() + " is not seen"));

            case 1 -> arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                    "success -> " + actionInputData.getTitle() + " was rated with "
                            + actionInputData.getGrade() + " by " + actionInputData.getUsername()));

            case 2 -> arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                    "error -> " + actionInputData.getTitle() + " has been already rated"));
        }
    }

    @SuppressWarnings("unchecked")
    private void writeList(final ActionInputData actionInputData, final Writer writer,
                           final JSONArray arrayResult, final String message,
                           final ArrayList<String> list) throws IOException {
        arrayResult.add(writer.writeFile(actionInputData.getActionId(), "", message + list));
    }

    private void actorsQuery(final ActionInputData actionInputData, final Writer writer,
                             final JSONArray arrayResult,
                             final String message) throws IOException {
        if (actionInputData.getCriteria().equals("awards")) {
            ArrayList<String> sortedActors = Query.actorsAwards(this, actionInputData);
            writeList(actionInputData, writer, arrayResult, message, sortedActors);
        }

        if (actionInputData.getCriteria().equals("average")) {
            ArrayList<String> sortedActors = Query.actorsAverage(
                    this, actionInputData);
            writeList(actionInputData, writer, arrayResult, message, sortedActors);
        }

        if (actionInputData.getCriteria().equals("filter_description")) {
            ArrayList<String> sortedActors = Query.actorsFilterDescription(
                    this, actionInputData);
            writeList(actionInputData, writer, arrayResult, message, sortedActors);
        }
    }

    private void moviesQuery(final ActionInputData actionInputData, final Writer writer,
                             final JSONArray arrayResult, final String text) throws IOException {
        ArrayList<String> sortedMovies = switch (actionInputData.getCriteria()) {
            default -> Query.moviesLongest(this, actionInputData);
            case "favorite" -> Query.moviesFavorites(this, actionInputData);
            case "most_viewed" -> Query.moviesNumberViews(this, actionInputData);
            case "ratings" -> Query.moviesRatings(this, actionInputData);
        };

        writeList(actionInputData, writer, arrayResult, text, sortedMovies);
    }

    private void showsQuery(final ActionInputData actionInputData, final Writer writer,
                            final JSONArray arrayResult, final String text) throws IOException {
        ArrayList<String> sortedSerials = switch (actionInputData.getCriteria()) {
            default -> Query.serialsLongest(this, actionInputData);
            case "favorite" -> Query.serialsFavorites(this, actionInputData);
            case "most_viewed" -> Query.serialsNumberViews(this, actionInputData);
            case "ratings" -> Query.serialsRatings(this, actionInputData);
        };

        writeList(actionInputData, writer, arrayResult, text, sortedSerials);
    }

    @SuppressWarnings("unchecked")
    private void writeStandard(final String title, final ActionInputData actionInputData,
                               final Writer writer, final JSONArray arrayResult,
                               final String text) throws IOException {
        if (!title.equals("")) {
            arrayResult.add(writer.writeFile(actionInputData.getActionId(), "", text
                    + "Recommendation result: " + title));
        } else {
            arrayResult.add(writer.writeFile(actionInputData.getActionId(), "", text
                    + "Recommendation cannot be applied!"));
        }
    }

    private void standardRecomm(final ActionInputData actionInputData, final Writer writer,
                                final JSONArray arrayResult) throws IOException {
        String videoName = Recommendation.standard(this,
                actionInputData.getUsername(), this.input);
        writeStandard(videoName, actionInputData, writer, arrayResult, "Standard");
    }

    private void unseenRecomm(final ActionInputData actionInputData, final Writer writer,
                                  final JSONArray arrayResult) throws IOException {
        String videoName = Recommendation.bestUnseen(this,
                actionInputData.getUsername(), this.input);
        writeStandard(videoName, actionInputData, writer, arrayResult, "BestRatedUnseen");
    }

    @SuppressWarnings("unchecked")
    private void favoriteRecomm(final ActionInputData actionInputData, final Writer writer,
                                final JSONArray arrayResult) throws IOException {
        if (this.getUserMap().get(actionInputData.getUsername())
                .getSubscriptionType().equals("PREMIUM")) {
            String videoName = Recommendation.favoriteRecommendation(this,
                    actionInputData.getUsername(), this.input);
            if (!videoName.equals("")) {
                arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                        "FavoriteRecommendation result: " + videoName));
                return;
            }
        }
        arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                "FavoriteRecommendation cannot be applied!"));
    }

    @SuppressWarnings("unchecked")
    private void searchRecomm(final ActionInputData actionInputData, final Writer writer,
                              final JSONArray arrayResult) throws IOException {
        if (this.getUserMap().get(actionInputData.getUsername())
                .getSubscriptionType().equals("PREMIUM")) {
            ArrayList<String> sortedVideos = Recommendation.searchRecommendation(
                    this, actionInputData.getUsername(), actionInputData);
            if (sortedVideos.size() > 0) {
                arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                        "SearchRecommendation result: " + sortedVideos));
                return;
            }
        }
        arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                "SearchRecommendation cannot be applied!"));
    }

    @SuppressWarnings("unchecked")
    private void popularRecomm(final ActionInputData actionInputData, final Writer writer,
                               final JSONArray arrayResult) throws IOException {
        if (this.getUserMap().get(actionInputData.getUsername())
                .getSubscriptionType().equals("PREMIUM")) {
            String videoName = Recommendation.popularRecommendation(this,
                    actionInputData.getUsername(), this.input);
            if (!videoName.equals("")) {
                arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                        "PopularRecommendation result: " + videoName));
                return;
            }
        }
        arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                "PopularRecommendation cannot be applied!"));
    }

    /** method which does the action given to the input */
    public void action(final Writer writer, final JSONArray arrayResult) throws IOException {
        for (ActionInputData actionInputData : input.getCommands()) {
            switch (actionInputData.getActionType()) {
                default -> {
                    switch (actionInputData.getType()) {
                        default -> commandView(actionInputData, writer, arrayResult);
                        case "favorite" -> commandFavorite(actionInputData, writer, arrayResult);
                        case "rating" -> commandRating(actionInputData, writer, arrayResult);
                    }
                }
                case "query" -> {
                    String text = "Query result: ";
                    switch (actionInputData.getObjectType()) {
                        default -> {
                            ArrayList<String> sortedUsers = Query.user(this, actionInputData);
                            writeList(actionInputData, writer, arrayResult, text, sortedUsers);
                        }
                        case "actors" -> actorsQuery(actionInputData, writer, arrayResult, text);
                        case "movies" -> moviesQuery(actionInputData, writer, arrayResult, text);
                        case "shows" -> showsQuery(actionInputData, writer, arrayResult, text);
                    }
                }
                case "recommendation" -> {
                    switch (actionInputData.getType()) {
                        default -> standardRecomm(actionInputData, writer, arrayResult);
                        case "best_unseen" -> unseenRecomm(actionInputData, writer, arrayResult);
                        case "favorite" -> favoriteRecomm(actionInputData, writer, arrayResult);
                        case "search" -> searchRecomm(actionInputData, writer, arrayResult);
                        case "popular" -> popularRecomm(actionInputData, writer, arrayResult);
                    }
                }
            }
        }
    }

    /** getter for hashmap of actors */
    public Map<String, Actor> getActorMap() {
        return actorMap;
    }

    /** getter for hashmap of movies */
    public Map<String, Movie> getMovieMap() {
        return movieMap;
    }

    /** getter for hashmap of serial */
    public Map<String, Serial> getSerialMap() {
        return serialMap;
    }

    /** getter for hashmap of users*/
    public Map<String, User> getUserMap() {
        return userMap;
    }
}
