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
    private Map<String, Actor> actorMap;
    private Map<String, Movie> movieMap;
    private Map<String, Serial> serialMap;
    private Map<String, User> userMap;

    private final Input input;

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

    public Database(final Input input) {
        this.actorMap = new HashMap<>();
        this.movieMap = new HashMap<>();
        this.serialMap = new HashMap<>();
        this.userMap = new HashMap<>();
        this.input = input;
        this.addActorsToDatabase();
        this.addMoviesToDatabase();
        this.addSerialsToDatabase();
        this.addUsersToDatabase();
    }

    /** method which does the commands given to the input */
    @SuppressWarnings("unchecked")
    public void command(final Writer writer, final JSONArray arrayResult) throws IOException {
        for (ActionInputData actionInputData : input.getCommands()) {
            if (actionInputData.getActionType().equals("command")) {

                if (actionInputData.getType().equals("view")) {
                    Command.view(actionInputData.getUsername(), actionInputData.getTitle(), this);
                    arrayResult.add(writer.writeFile(actionInputData.getActionId(),
                            "", "success -> "
                                    + actionInputData.getTitle()
                                    + " was viewed with total views of "
                                    + this.userMap.get(actionInputData.getUsername()).
                                    getHistory().get(actionInputData.getTitle())));
                }

                if (actionInputData.getType().equals("favorite")) {
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

                if (actionInputData.getType().equals("rating")) {
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
            }

            if (actionInputData.getActionType().equals("query")) {
                if (actionInputData.getObjectType().equals("users")) {
                    ArrayList<String> sortedUsers = Query.user(this, actionInputData);
                    arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                            "Query result: " + sortedUsers));
                }

                if (actionInputData.getObjectType().equals("actors")) {
                    if (actionInputData.getCriteria().equals("awards")) {
                        ArrayList<String> sortedActors = Query.actorsAwards(this, actionInputData);
                        arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                                "Query result: " + sortedActors));
                    }

                    if (actionInputData.getCriteria().equals("average")) {
                        ArrayList<String> sortedActors = Query.actorsAverage(
                                this, actionInputData);
                        arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                                "Query result: " + sortedActors));
                    }

                    if (actionInputData.getCriteria().equals("filter_description")) {
                        ArrayList<String> sortedActors = Query.actorsFilterDescription(
                                this, actionInputData);
                        arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                                "Query result: " + sortedActors));
                    }
                }

                if (actionInputData.getObjectType().equals("movies")) {
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

                    arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                            "Query result: " + sortedMovies));
                }

                if (actionInputData.getObjectType().equals("shows")) {
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

                    arrayResult.add(writer.writeFile(actionInputData.getActionId(), "",
                            "Query result: " + sortedSerials));
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
