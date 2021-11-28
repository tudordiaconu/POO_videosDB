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

    @SuppressWarnings("unchecked")
    private void commandView(final ActionInputData actionInputData,
                     final Writer writer, final JSONArray arrayResult) throws IOException {
        Command.view(actionInputData.getUsername(), actionInputData.getTitle(), this);
        arrayResult.add(writer.writeFile(actionInputData.getActionId(),
                "", "success -> "
                        + actionInputData.getTitle()
                        + " was viewed with total views of "
                        + this.userMap.get(actionInputData.getUsername()).
                        getHistory().get(actionInputData.getTitle())));
    }

    @SuppressWarnings("unchecked")
    private void commandFavorite(final ActionInputData actionInputData, final Writer writer,
                          final JSONArray arrayResult) throws IOException {
        int alreadyExists = Command.favorite(actionInputData.getUsername(),
                actionInputData.getTitle(), this);

        if (alreadyExists == 1) {
            arrayResult.add(writer.writeFile(actionInputData.getActionId(),
                    "", "success -> "
                            + actionInputData.getTitle() + " was added as favourite"));
        } else if (alreadyExists == 2) {
            arrayResult.add(writer.writeFile(actionInputData.getActionId(),
                    "", "error -> " + actionInputData.getTitle()
                            + " is already in favourite list"));
        } else {
            arrayResult.add(writer.writeFile(actionInputData.getActionId(),
                    "", "error -> " + actionInputData.getTitle() + " is not seen"));
        }
    }

    @SuppressWarnings("unchecked")
    private void commandRating(final ActionInputData actionInputData, final Writer writer,
                        final JSONArray arrayResult) throws IOException {
        int found = Command.rating(actionInputData.getUsername(),
                actionInputData.getTitle(), this, actionInputData);

        if (found == 1) {
            arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                    "success -> " + actionInputData.getTitle() + " was rated with "
                            + actionInputData.getGrade() + " by "
                            + actionInputData.getUsername()));
        } else if (found == 0) {
            arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                    "error -> " + actionInputData.getTitle() + " is not seen"));
        } else {
            arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                    "error -> " + actionInputData.getTitle()
                            + " has been already rated"));
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
                             final JSONArray arrayResult,
                             final String message) throws IOException {
        ArrayList<String> sortedMovies = new ArrayList<>();
        if (actionInputData.getCriteria().equals("longest")) {
            sortedMovies = Query.moviesLongest(
                    this, actionInputData);
        }

        if (actionInputData.getCriteria().equals("favorite")) {
            sortedMovies = Query.moviesFavorites(
                    this, actionInputData);
        }

        if (actionInputData.getCriteria().equals("most_viewed")) {
            sortedMovies = Query.moviesNumberViews(
                    this, actionInputData);
        }

        if (actionInputData.getCriteria().equals("ratings")) {
            sortedMovies = Query.moviesRatings(
                    this, actionInputData);
        }

        writeList(actionInputData, writer, arrayResult, message, sortedMovies);
    }

    private void showsQuery(final ActionInputData actionInputData, final Writer writer,
                            final JSONArray arrayResult,
                            final String message) throws IOException {
        ArrayList<String> sortedSerials = new ArrayList<>();
        if (actionInputData.getCriteria().equals("longest")) {
            sortedSerials = Query.serialsLongest(
                    this, actionInputData);
        }

        if (actionInputData.getCriteria().equals("favorite")) {
            sortedSerials = Query.serialsFavorites(
                    this, actionInputData);
        }

        if (actionInputData.getCriteria().equals("most_viewed")) {
            sortedSerials = Query.serialsNumberViews(
                    this, actionInputData);
        }

        if (actionInputData.getCriteria().equals("ratings")) {
            sortedSerials = Query.serialsRatings(
                    this, actionInputData);
        }

        writeList(actionInputData, writer, arrayResult, message, sortedSerials);
    }

    @SuppressWarnings("unchecked")
    private void recommendationMethod(final ActionInputData actionInputData, final Writer writer,
                                      final JSONArray arrayResult) throws IOException {
        if (actionInputData.getType().equals("standard")) {
            String videoName = Recommendation.standard(this,
                    actionInputData.getUsername(), this.input);
            if (!videoName.equals("")) {
                arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                        "StandardRecommendation result: " + videoName));
            } else {
                arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                        "StandardRecommendation cannot be applied!"));
            }
        }

        if (actionInputData.getType().equals("best_unseen")) {
            String videoName = Recommendation.bestUnseen(this,
                    actionInputData.getUsername(), this.input);
            if (!videoName.equals("")) {
                arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                        "BestRatedUnseenRecommendation result: " + videoName));
            } else {
                arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                        "BestRatedUnseenRecommendation cannot be applied!"));
            }
        }

        if (actionInputData.getType().equals("favorite")) {
            if (this.getUserMap().get(actionInputData.getUsername())
                    .getSubscriptionType().equals("PREMIUM")) {
                String videoName = Recommendation.favoriteRecommendation(this,
                        actionInputData.getUsername(), this.input);
                if (!videoName.equals("")) {
                    arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                            "FavoriteRecommendation result: " + videoName));
                } else {
                    arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                            "FavoriteRecommendation cannot be applied!"));
                }
            } else {
                arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                        "FavoriteRecommendation cannot be applied!"));
            }
        }

        if (actionInputData.getType().equals("search")) {
            if (this.getUserMap().get(actionInputData.getUsername())
                    .getSubscriptionType().equals("PREMIUM")) {
                ArrayList<String> sortedVideos = Recommendation.searchRecommendation(
                        this, actionInputData.getUsername(), actionInputData);
                if (sortedVideos.size() > 0) {
                    arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                            "SearchRecommendation result: " + sortedVideos));
                } else {
                    arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                            "SearchRecommendation cannot be applied!"));
                }
            } else {
                arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                        "SearchRecommendation cannot be applied!"));
            }
        }

        if (actionInputData.getType().equals("popular")) {
            if (this.getUserMap().get(actionInputData.getUsername())
                    .getSubscriptionType().equals("PREMIUM")) {
                String videoName = Recommendation.popularRecommendation(this,
                        actionInputData.getUsername(), this.input);
                if (!videoName.equals("")) {
                    arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                            "PopularRecommendation result: " + videoName));
                } else {
                    arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                            "PopularRecommendation cannot be applied!"));
                }
            } else {
                arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                        "PopularRecommendation cannot be applied!"));
            }
        }
    }

    /** method which does the commands given to the input */
    public void command(final Writer writer, final JSONArray arrayResult) throws IOException {
        for (ActionInputData actionInputData : input.getCommands()) {
            if (actionInputData.getActionType().equals("command")) {

                if (actionInputData.getType().equals("view")) {
                    commandView(actionInputData, writer, arrayResult);
                }

                if (actionInputData.getType().equals("favorite")) {
                    commandFavorite(actionInputData, writer, arrayResult);
                }

                if (actionInputData.getType().equals("rating")) {
                    commandRating(actionInputData, writer, arrayResult);
                }
            }

            if (actionInputData.getActionType().equals("query")) {
                String message = "Query result: ";
                if (actionInputData.getObjectType().equals("users")) {
                    ArrayList<String> sortedUsers = Query.user(this, actionInputData);
                    writeList(actionInputData, writer, arrayResult, message, sortedUsers);
                }

                if (actionInputData.getObjectType().equals("actors")) {
                    actorsQuery(actionInputData, writer, arrayResult, message);
                }

                if (actionInputData.getObjectType().equals("movies")) {
                    moviesQuery(actionInputData, writer, arrayResult, message);
                }

                if (actionInputData.getObjectType().equals("shows")) {
                    showsQuery(actionInputData, writer, arrayResult, message);
                }
            }

            if (actionInputData.getActionType().equals("recommendation")) {
                recommendationMethod(actionInputData, writer, arrayResult);
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
