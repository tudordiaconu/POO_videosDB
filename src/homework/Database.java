package homework;

import fileio.*;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.util.HashMap;
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
            if (Objects.equals(actionInputData.getActionType(), "command")) {
                if (Objects.equals(actionInputData.getType(), "view")) {
                    Command.view(actionInputData.getUsername(), actionInputData.getTitle(), this);
                        arrayResult.add(writer.writeFile(actionInputData.getActionId(), "", "success -> " +
                                            actionInputData.getTitle() + " was viewed with total views of " +
                                            this.userMap.get(actionInputData.getUsername()).
                                                    getHistory().get(actionInputData.getTitle())));
                }

                if (Objects.equals(actionInputData.getType(), "favorite")) {
                    Command.favorite(actionInputData.getUsername(), actionInputData.getTitle(), this);
                        arrayResult.add(writer.writeFile(actionInputData.getActionId(), "", "success -> " +
                                            actionInputData.getTitle() + " was added as favourite"));
                }
            }
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
