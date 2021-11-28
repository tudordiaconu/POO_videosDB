package homework;

import entertainment.Season;
import fileio.ActionInputData;

public final class Command {
    private Command() { }

    /** Function which does the view command */
    public static void view(final String username, final String title, final Database database) {
        User user = database.getUserMap().get(username);
        if (user.getHistory().containsKey(title)) {
            user.getHistory().put(title, user.getHistory().get(title) + 1);
        } else {
            user.getHistory().put(title, 1);
        }

        if (database.getMovieMap().containsKey(title)) {
            Movie movie = database.getMovieMap().get(title);
            for (String genre : movie.getGenres()) {
                if (!database.getGenresMap().containsKey(genre)) {
                    database.getGenresMap().put(genre, movie.getNumberOfViews(database));
                } else {
                    database.getGenresMap().put(genre, database.getGenresMap().get(genre)
                            + 1);
                }
            }
        } else if (database.getSerialMap().containsKey(title)) {
            Serial serial = database.getSerialMap().get(title);
            for (String genre : serial.getGenres()) {
                if (!database.getGenresMap().containsKey(genre)) {
                    database.getGenresMap().put(genre, serial.getNumberOfViews(database));
                } else {
                    database.getGenresMap().put(genre, database.getGenresMap().get(genre)
                            + 1);
                }
            }
        }
    }

    /** Function which does the favorite command */
    public static int favorite(final String username, final String title,
                               final Database database) {
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

    /** Function which does the rating command */
    public static int rating(final String username, final String title, final Database database,
                             final ActionInputData actionInputData) {
        User user = database.getUserMap().get(username);
        int found = 0;

        if (database.getMovieMap().containsKey(title)) {
            if (user.getHistory().containsKey(title)) {
                Movie movie = database.getMovieMap().get(title);
                if (!user.getMoviesGivenRatings().containsKey(title)) {
                    movie.getRatings().add(actionInputData.getGrade());
                    user.getMoviesGivenRatings().put(title, actionInputData.getGrade());
                    user.setNrGivenRatings(user.getNrGivenRatings() + 1);
                    found = 1;
                } else {
                    found = 2;
                }
            }
        } else if (database.getSerialMap().containsKey(title)) {
            if (user.getHistory().containsKey(title)) {
                Serial serial = database.getSerialMap().get(title);
                Season season = serial.getSeasons().get(actionInputData.getSeasonNumber() - 1);
                if (!user.getSeasonsGivenRatings().containsKey(season)) {
                    season.getRatings().add(actionInputData.getGrade());
                    user.getSeasonsGivenRatings().put(season, actionInputData.getGrade());
                    user.setNrGivenRatings(user.getNrGivenRatings() + 1);
                    found = 1;
                } else {
                    found = 2;
                }
            }
        }

        return found;
    }
}
