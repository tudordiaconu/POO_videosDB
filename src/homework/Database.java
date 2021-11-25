package homework;

import fileio.*;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Database {
    private Map<String, Actor> actorMap;
    private Map<String, Movie> movieMap;
    private Map<String, Serial> serialMap;
    private Map<String, User> userMap;

    public Input input;

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

    public Database(Input input) {
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
    
    public void command(Writer writer, JSONArray arrayResult) throws IOException {
        for (ActionInputData actionInputData : input.getCommands()) {
            if (actionInputData.getActionType().equals("command")) {

                if (actionInputData.getType().equals("view")) {
                    Command.view(actionInputData.getUsername(), actionInputData.getTitle(), this);
                    arrayResult.add(writer.writeFile(actionInputData.getActionId(), "", "success -> " +
                            actionInputData.getTitle() + " was viewed with total views of " +
                            this.userMap.get(actionInputData.getUsername()).
                                    getHistory().get(actionInputData.getTitle())));
                }

                if (actionInputData.getType().equals("favorite")) {
                    int alreadyExists = Command.favorite(actionInputData.getUsername(), actionInputData.getTitle(), this);

                    if (alreadyExists == 1) {
                        arrayResult.add(writer.writeFile(actionInputData.getActionId(), "", "success -> " +
                                actionInputData.getTitle() + " was added as favourite"));
                    } else if (alreadyExists == 2) {
                        arrayResult.add(writer.writeFile(actionInputData.getActionId(), "", "error -> " +
                                actionInputData.getTitle() + " is already in favourite list"));
                    } else {
                        arrayResult.add(writer.writeFile(actionInputData.getActionId(), "", "error -> " +
                                actionInputData.getTitle() + " is not seen"));
                    }
                }

                if (actionInputData.getType().equals("rating")) {
                    Command.rating(actionInputData.getUsername(), actionInputData.getTitle(), this, actionInputData);
                    arrayResult.add(writer.writeFile(actionInputData.getActionId(), "", "success -> " +
                            actionInputData.getTitle() + " was rated with " + actionInputData.getGrade() +
                            " by " + actionInputData.getUsername()));
                }
            }

            /*if(actionInputData.getActionType().equals("query")) {
                if(actionInputData.getObjectType().equals("users")) {
                    List<User> sortedUsers = Query.user(this, actionInputData);
                    int numberOfUsers = actionInputData.getNumber();
                }
            }*/
        }
    }

    public Map<String, Actor> getActorMap() {
        return actorMap;
    }

    public Map<String, Movie> getMovieMap() {
        return movieMap;
    }

    public Map<String, Serial> getSerialMap() {
        return serialMap;
    }

    public Map<String, User> getUserMap() {
        return userMap;
    }
}
