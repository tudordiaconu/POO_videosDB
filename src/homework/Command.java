package homework;

import entertainment.Season;
import fileio.ActionInputData;

public class Command {
    public static void view(String username, String title, Database database) {
        User user = database.getUserMap().get(username);
        if (user.getHistory().containsKey(title)) {
            user.getHistory().put(title, user.getHistory().get(title) + 1);
        } else {
            user.getHistory().put(title, 1);
        }
    }

    public static int favorite(String username, String title, Database database) {
        int alreadyExists = 0;

        User user = database.getUserMap().get(username);

        if (user.getHistory().containsKey(title)) {
            if (!user.getFavoriteMovies().contains(title)) {
                user.getFavoriteMovies().add(title);
                alreadyExists = 1;
            } else {
                alreadyExists = 2;
            }
        }

        return alreadyExists;
    }

    public static int rating(String username, String title, Database database, ActionInputData actionInputData) {
        User user = database.getUserMap().get(username);
        int found = 0;

        if (database.getMovieMap().containsKey(title)) {
            if (user.getHistory().containsKey(title)) {
                Movie movie = database.getMovieMap().get(title);
                movie.getRatings().add(actionInputData.getGrade());
                user.getMoviesGivenRatings().put(title, actionInputData.getGrade());
                user.setNrGivenRatings(user.getNrGivenRatings() + 1);
                found = 1;
            }
        } else if (database.getSerialMap().containsKey(title)) {
            if (user.getHistory().containsKey(title)) {
                Serial serial = database.getSerialMap().get(title);
                Season season = serial.getSeasons().get(actionInputData.getSeasonNumber() - 1);
                //serial.getRatings().add(actionInputData.getGrade());
                season.getRatings().add(actionInputData.getGrade());
                user.getSeasonsGivenRatings().put(season, actionInputData.getGrade());
                user.setNrGivenRatings(user.getNrGivenRatings() + 1);
                found = 1;
            }
        }

        return found;
    }
}
