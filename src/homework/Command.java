package homework;

import fileio.ActionInputData;

public class Command {
    private final int actionId;
    private final String actionType;
    private final String type;
    private final String username;
    private final String title;

    public Command(ActionInputData actionInputData) {
        this.actionId = actionInputData.getActionId();
        this.actionType = actionInputData.getActionType();
        this.type = actionInputData.getType();
        this.username = actionInputData.getUsername();
        this.title = actionInputData.getTitle();
    }

    public int getActionId() {
        return actionId;
    }

    public String getActionType() {
        return actionType;
    }

    public String getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Command{" +
                "actionId=" + actionId +
                ", actionType='" + actionType + '\'' +
                ", type='" + type + '\'' +
                ", username='" + username + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public static void view(String username, String title, Database database) {
        User user = database.getUserMap().get(username);
        if (user.getHistory().containsKey(title)) {
            user.getHistory().put(title, user.getHistory().get(title) + 1);
        } else {
            user.getHistory().put(title, 1);
        }
    }

    public static void favorite(String username, String title, Database database) {
        User user = database.getUserMap().get(username);
        if (user.getHistory().containsKey(title)) {
            if (!user.getFavoriteMovies().contains(title)) {
                user.getFavoriteMovies().add(title);
            }
        }
    }

    public static void rating(String username, String title, Database database) {
        User user = database.getUserMap().get(username);
        if (database.getMovieMap().containsKey(title)) {
            Movie movie = database.getMovieMap().get(title);
            for (ActionInputData actionInputData : database.input.getCommands()) {
                movie.getRatings().add(actionInputData.getGrade());
            }
        }

    }
}
