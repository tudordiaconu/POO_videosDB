package homework;

import fileio.ActionInputData;
import java.util.ArrayList;
import java.util.List;

public final class Query {
    private Query() { }

    private static void removeIfNecessary(final List<String> list,
                                    final ActionInputData actionInputData) {
        int n = actionInputData.getNumber();
        if (list.size() > n) {
            while (list.size() != n) {
                list.remove(list.size() - 1);
            }
        }
    }

    /** function for the user query */
    public static ArrayList<String> user(final Database database,
                                         final ActionInputData actionInputData) {
        ArrayList<String> usernames = new ArrayList<>();
        List<User> users = User.sortUsers(database, actionInputData);

        for (User user : users) {
            if (user.getNrGivenRatings() > 0) {
                usernames.add(user.getUsername());
            }
        }

        Query.removeIfNecessary(usernames, actionInputData);
        return usernames;
    }

    /** method which returns a list with actors sorted by average rating */
    public static ArrayList<String> actorsAverage(final Database database,
                                                  final ActionInputData actionInputData) {
        ArrayList<String> actorNames = new ArrayList<>();
        List<Actor> actors = Actor.sortActorsByRating(database, actionInputData);

        for (Actor actor : actors) {
            if (actor.getAverageRating(database) > 0) {
                actorNames.add(actor.getName());
            }
        }

        Query.removeIfNecessary(actorNames, actionInputData);
        return actorNames;
    }

    /** method which returns a list of actors sorted by the number of awards won */
    public static ArrayList<String> actorsAwards(final Database database,
                                                  final ActionInputData actionInputData) {
        ArrayList<String> actorNames = new ArrayList<>();

        List<String> awards = new ArrayList<>(actionInputData.getFilters().get(2 + 1));

        List<Actor> actors = database.getActorMap().values().stream()
                .filter(actor -> actor.checkAwards(awards)).toList();

        List<Actor> sortedActors = Actor.sortActorsByAwards(actors, actionInputData);
        for (Actor actor : sortedActors) {
            if (actor.getNumberAwards() > 0) {
                actorNames.add(actor.getName());
            }
        }
        return actorNames;
    }

    /** method which returns a list of actors filtered by words found in the
     *  career description and sorted alphabetically*/
    public static ArrayList<String> actorsFilterDescription(final Database database,
                                                            final ActionInputData actionInputData) {
        ArrayList<String> actorNames = new ArrayList<>();
        List<String> words = new ArrayList<>(actionInputData.getFilters().get(2));

        List<Actor> actors = database.getActorMap().values()
                .stream()
                .filter(actor -> actor.checkWords(words))
                .toList();

        List<Actor> sortedActors = actors
                .stream()
                .sorted((actor1, actor2) -> {
                    if (actionInputData.getSortType().equals("asc")) {
                        return actor1.getName().compareTo(actor2.getName());
                    } else {
                        return actor2.getName().compareTo(actor1.getName());
                    }
                }).toList();

        for (Actor actor : sortedActors) {
            actorNames.add(actor.getName());
        }
        return actorNames;
    }

    /** method which returns a list of movies sorted by length */
    public static ArrayList<String> moviesLongest(final Database database,
                                                  final ActionInputData actionInputData) {
        List<Movie> filteredMoviesListByYear = Movie.filterMoviesByYear(database, actionInputData);
        List<Movie> filteredMovies = Movie.filterMoviesByGenre(
                database, actionInputData, filteredMoviesListByYear);

        List<Movie> sortedMovies = Movie.sortMovieListByDuration(filteredMovies, actionInputData);

        ArrayList<String> moviesNames = new ArrayList<>();
        for (Movie movie : sortedMovies) {
            moviesNames.add(movie.getTitle());
        }

        Query.removeIfNecessary(moviesNames, actionInputData);
        return moviesNames;
    }

    /** method which returns list of serials sorted by duration */
    public static ArrayList<String> serialsLongest(final Database database,
                                                   final ActionInputData actionInputData) {
        List<Serial> filteredSerialsListByYear = Serial.filterSerialsByYear(
                database, actionInputData);
        List<Serial> filteredSerials = Serial.filterSerialsByGenre(
                    database, actionInputData, filteredSerialsListByYear);

        List<Serial> sortedSerials = Serial.sortSerialListByDuration(filteredSerials,
                actionInputData);
        ArrayList<String> serialsNames = new ArrayList<>();
        for (Serial serial : sortedSerials) {
            serialsNames.add(serial.getTitle());
        }

        Query.removeIfNecessary(serialsNames, actionInputData);
        return serialsNames;
    }

    /** method which returns a list of the movies sorted by the number of favorites */
    public static ArrayList<String> moviesFavorites(final Database database,
                                                    final ActionInputData actionInputData) {
        List<Movie> filteredMoviesListByYear = Movie.filterMoviesByYear(
                database, actionInputData);
        List<Movie> filteredMovies = Movie.filterMoviesByGenre(
            database, actionInputData, filteredMoviesListByYear);

        List<Movie> sortedMovies = Movie.sortMovieListByFavorites(filteredMovies, database,
                actionInputData);

        ArrayList<String> moviesNames = new ArrayList<>();
        for (Movie movie : sortedMovies) {
            if (movie.getNumberOfFavored(database) > 0) {
                moviesNames.add(movie.getTitle());
            }
        }

        Query.removeIfNecessary(moviesNames, actionInputData);
        return moviesNames;
    }

    /** method that returns a list of serials sorted by the number of favorites */
    public static ArrayList<String> serialsFavorites(final Database database,
                                                     final ActionInputData actionInputData) {
        List<Serial> filteredSerialsListByYear = Serial.filterSerialsByYear(
                database, actionInputData);
        List<Serial> filteredSerials = Serial.filterSerialsByGenre(
                database, actionInputData, filteredSerialsListByYear);

        List<Serial> sortedSerials = Serial.sortSerialListByFavorites(filteredSerials,
                database, actionInputData);
        ArrayList<String> serialsNames = new ArrayList<>();
        for (Serial serial : sortedSerials) {
            if (serial.getNumberOfFavored(database) > 0) {
                serialsNames.add(serial.getTitle());
            }
        }

        Query.removeIfNecessary(serialsNames, actionInputData);
        return serialsNames;
    }

    /** method that returns a list of movie names, sorted by the number of views */
    public static ArrayList<String> moviesNumberViews(final Database database,
                                                      final ActionInputData actionInputData) {
        List<Movie> filteredMoviesListByYear = Movie.filterMoviesByYear(
            database, actionInputData);
        List<Movie> filteredMovies = Movie.filterMoviesByGenre(
            database, actionInputData, filteredMoviesListByYear);

        List<Movie> sortedMovies = Movie.sortMovieListByViews(filteredMovies, database,
                actionInputData);

        ArrayList<String> moviesNames = new ArrayList<>();
        for (Movie movie : sortedMovies) {
            if (movie.getNumberOfViews(database) > 0) {
                moviesNames.add(movie.getTitle());
            }
        }

        Query.removeIfNecessary(moviesNames, actionInputData);
        return moviesNames;
    }

    /** method that returns a list of serials names sorted by the number of views */
    public static ArrayList<String> serialsNumberViews(final Database database,
                                                       final ActionInputData actionInputData) {
        List<Serial> filteredSerialsListByYear = Serial.filterSerialsByYear(
                database, actionInputData);
        List<Serial> filteredSerials = Serial.filterSerialsByGenre(
                database, actionInputData, filteredSerialsListByYear);

        List<Serial> sortedSerials = Serial.sortSerialsListByViews(filteredSerials,
                database, actionInputData);

        ArrayList<String> serialsNames = new ArrayList<>();

        for (Serial serial : sortedSerials) {
            if (serial.getNumberOfViews(database) > 0) {
                serialsNames.add(serial.getTitle());
            }
        }

        Query.removeIfNecessary(serialsNames, actionInputData);
        return serialsNames;
    }



    /** method that returns a list of movie names sorted by the ratings */
    public static ArrayList<String> moviesRatings(final Database database,
                                                  final ActionInputData actionInputData) {
        List<Movie> filteredMoviesListByYear = Movie.filterMoviesByYear(
                database, actionInputData);
        List<Movie> filteredMovies = Movie.filterMoviesByGenre(
                database, actionInputData, filteredMoviesListByYear);

        List<Movie> sortedMovies = Movie.sortMovieListByRating(filteredMovies, actionInputData);

        ArrayList<String> moviesNames = new ArrayList<>();

        for (Movie movie : sortedMovies) {
            if (movie.getRating() > 0) {
                moviesNames.add(movie.getTitle());
            }
        }

        Query.removeIfNecessary(moviesNames, actionInputData);
        return moviesNames;
    }

    /** method that returns a list of serials names sorted by rating */
    public static ArrayList<String> serialsRatings(final Database database,
                                                   final ActionInputData actionInputData) {
        List<Serial> filteredSerialsListByYear = Serial.filterSerialsByYear(
                database, actionInputData);
        List<Serial> filteredSerials = Serial.filterSerialsByGenre(
                database, actionInputData, filteredSerialsListByYear);

        List<Serial> sortedSerials = Serial.sortSerialListByRating(filteredSerials,
                actionInputData);
        ArrayList<String> serialsNames = new ArrayList<>();

        for (Serial serial : sortedSerials) {
            if (serial.getRating() > 0) {
                serialsNames.add(serial.getTitle());
            }
        }

        Query.removeIfNecessary(serialsNames, actionInputData);
        return serialsNames;
    }
}
