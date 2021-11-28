package homework;

import fileio.ActionInputData;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class Query {
    private Query() { }

    /** function for the user query */
    public static ArrayList<String> user(final Database database,
                                         final ActionInputData actionInputData) {
        ArrayList<String> usernames = new ArrayList<>();
        List<User> users = database.getUserMap().values()
                .stream()
                .sorted((user1, user2) -> {
                    int firstNumberRatings = user1.getNrGivenRatings();
                    int secondNumberRatings = user2.getNrGivenRatings();

                    if (firstNumberRatings == secondNumberRatings) {
                        if (actionInputData.getSortType().equals("asc")) {
                            return user1.getUsername().compareTo(user2.getUsername());
                        } else {
                            return user2.getUsername().compareTo(user1.getUsername());
                        }
                    }

                    if (actionInputData.getSortType().equals("asc")) {
                        return firstNumberRatings - secondNumberRatings;
                    } else {
                        return secondNumberRatings - firstNumberRatings;
                    }
                }).toList();

        for (User user : users) {
            if (user.getNrGivenRatings() > 0) {
                usernames.add(user.getUsername());
            }
        }

        int n = actionInputData.getNumber();
        if (usernames.size() > n) {
            while (usernames.size() != n) {
                usernames.remove(usernames.size() - 1);
            }
        }

        return usernames;
    }

    /** method which returns a list with actors sorted by average rating */
    public static ArrayList<String> actorsAverage(final Database database,
                                                  final ActionInputData actionInputData) {
        ArrayList<String> actorNames = new ArrayList<>();
        int n = actionInputData.getNumber();

        List<Actor> actors = database.getActorMap().values()
                .stream()
                .sorted((actor1, actor2) -> {
                    actor1.getAverageRating(database);
                    actor2.getAverageRating(database);

                    if (actor1.getAverageRating(database) == actor2.getAverageRating(database)) {
                        if (actionInputData.getSortType().equals("asc")) {
                            return actor1.getName().compareTo(actor2.getName());
                        } else {
                            return actor2.getName().compareTo(actor1.getName());
                        }
                    }

                    if (actionInputData.getSortType().equals("asc")) {
                        return Double.compare(actor1.getAverageRating(database),
                                actor2.getAverageRating(database));
                    } else {
                        return Double.compare(actor2.getAverageRating(database),
                                actor1.getAverageRating(database));
                    }
                }).toList();

        for (Actor actor : actors) {
            if (actor.getAverageRating(database) > 0) {
                actorNames.add(actor.getName());
            }
        }

        if (actorNames.size() > n) {
            while (actorNames.size() != n) {
                actorNames.remove(actorNames.size() - 1);
            }
        }

        return actorNames;
    }

    /** method which returns a list of actors sorted by the number of awards won */
    public static ArrayList<String> actorsAwards(final Database database,
                                                  final ActionInputData actionInputData) {
        ArrayList<String> actorNames = new ArrayList<>();

        List<String> awards = new ArrayList<>(actionInputData.getFilters().get(2 + 1));

        List<Actor> actors = database.getActorMap().values().stream()
                .filter(actor -> actor.checkAwards(awards)).toList();

        List<Actor> sortedActors = actors.stream()
                .sorted((actor1, actor2) -> {
                    int firstNumberAwards = actor1.getNumberAwards();
                    int secondNumberAwards = actor2.getNumberAwards();

                    if (firstNumberAwards == secondNumberAwards) {
                        if (actionInputData.getSortType().equals("asc")) {
                            return actor1.getName().compareTo(actor2.getName());
                        } else {
                            return actor2.getName().compareTo(actor1.getName());
                        }
                    }

                    if (actionInputData.getSortType().equals("asc")) {
                        return firstNumberAwards - secondNumberAwards;
                    } else {
                        return secondNumberAwards - firstNumberAwards;
                    }
                }).toList();

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

        List<Movie> sortedMovies = filteredMovies
                .stream()
                .sorted((movie1, movie2) -> {
                    if (movie1.getDuration() == movie2.getDuration()) {
                        if (actionInputData.getSortType().equals("asc")) {
                            return movie1.getTitle().compareTo(movie2.getTitle());
                        } else {
                            return movie2.getTitle().compareTo(movie1.getTitle());
                        }
                    }

                    if (actionInputData.getSortType().equals("asc")) {
                        return movie1.getDuration() - movie2.getDuration();
                    } else {
                        return movie2.getDuration() - movie1.getDuration();
                    }
                }).toList();

        ArrayList<String> moviesNames = new ArrayList<>();

        for (Movie movie : sortedMovies) {
            moviesNames.add(movie.getTitle());
        }

        int n = actionInputData.getNumber();

        if (moviesNames.size() > n) {
            while (moviesNames.size() != n) {
                moviesNames.remove(moviesNames.size() - 1);
            }
        }

        return moviesNames;
    }

    /** method which returns list of serials sorted by duration */
    public static ArrayList<String> serialsLongest(final Database database,
                                                   final ActionInputData actionInputData) {
        List<Serial> filteredSerialsListByYear = Serial.filterSerialsByYear(
                database, actionInputData);
        List<Serial> filteredSerials = Serial.filterSerialsByGenre(
                    database, actionInputData, filteredSerialsListByYear);

        List<Serial> sortedSerials = filteredSerials
                .stream()
                .sorted((serial1, serial2) -> {
                    if (serial1.calculateDuration() == serial2.calculateDuration()) {
                        if (actionInputData.getSortType().equals("asc")) {
                            return serial1.getTitle().compareTo(serial2.getTitle());
                        } else {
                            return serial2.getTitle().compareTo(serial1.getTitle());
                        }
                    }

                    if (actionInputData.getSortType().equals("asc")) {
                        return serial1.calculateDuration() - serial2.calculateDuration();
                    } else {
                        return serial2.calculateDuration() - serial1.calculateDuration();
                    }
                }).toList();

        ArrayList<String> serialsNames = new ArrayList<>();

        for (Serial serial : sortedSerials) {
            serialsNames.add(serial.getTitle());
        }

        int n = actionInputData.getNumber();

        if (serialsNames.size() > n) {
            while (serialsNames.size() != n) {
                serialsNames.remove(serialsNames.size() - 1);
            }
        }

        return serialsNames;
    }

    /** method which returns a list of the movies sorted by the number of favorites */
    public static ArrayList<String> moviesFavorites(final Database database,
                                                    final ActionInputData actionInputData) {
        List<Movie> filteredMoviesListByYear = Movie.filterMoviesByYear(
                database, actionInputData);
        List<Movie> filteredMovies = Movie.filterMoviesByGenre(
            database, actionInputData, filteredMoviesListByYear);

        List<Movie> sortedMovies = filteredMovies
                .stream()
                .sorted((movie1, movie2) -> {
                    if (movie1.getNumberOfFavored(database)
                            == movie2.getNumberOfFavored(database)) {
                        if (actionInputData.getSortType().equals("asc")) {
                            return movie1.getTitle().compareTo(movie2.getTitle());
                        } else {
                            return movie2.getTitle().compareTo(movie1.getTitle());
                        }
                    }

                    if (actionInputData.getSortType().equals("asc")) {
                        return movie1.getNumberOfFavored(database)
                                - movie2.getNumberOfFavored(database);
                    } else {
                        return movie2.getNumberOfFavored(database)
                                - movie1.getNumberOfFavored(database);
                    }
                }).toList();

        ArrayList<String> moviesNames = new ArrayList<>();

        for (Movie movie : sortedMovies) {
            if (movie.getNumberOfFavored(database) > 0) {
                moviesNames.add(movie.getTitle());
            }
        }

        int n = actionInputData.getNumber();
        if (moviesNames.size() > n) {
            while (moviesNames.size() != n) {
                moviesNames.remove(moviesNames.size() - 1);
            }
        }

        return moviesNames;
    }

    /** method that returns a list of serials sorted by the number of favorites */
    public static ArrayList<String> serialsFavorites(final Database database,
                                                     final ActionInputData actionInputData) {
        List<Serial> filteredSerialsListByYear = Serial.filterSerialsByYear(
                database, actionInputData);
        List<Serial> filteredSerials = Serial.filterSerialsByGenre(
                database, actionInputData, filteredSerialsListByYear);

        List<Serial> sortedSerials = filteredSerials
                .stream()
                .sorted((serial1, serial2) -> {
                    if (serial1.getNumberOfFavored(database)
                            == serial2.getNumberOfFavored(database)) {
                        if (actionInputData.getSortType().equals("asc")) {
                            return serial1.getTitle().compareTo(serial2.getTitle());
                        } else {
                            return serial2.getTitle().compareTo(serial1.getTitle());
                        }
                    }

                    if (actionInputData.getSortType().equals("asc")) {
                        return serial1.getNumberOfFavored(database)
                                - serial2.getNumberOfFavored(database);
                    } else {
                        return serial2.getNumberOfFavored(database)
                                - serial1.getNumberOfFavored(database);
                    }
                }).toList();

        ArrayList<String> serialsNames = new ArrayList<>();

        for (Serial serial : sortedSerials) {
            if (serial.getNumberOfFavored(database) > 0) {
                serialsNames.add(serial.getTitle());
            }
        }

        int n = actionInputData.getNumber();
        if (serialsNames.size() > n) {
            while (serialsNames.size() != n) {
                serialsNames.remove(serialsNames.size() - 1);
            }
        }

        return serialsNames;
    }

    public static ArrayList<String> moviesNumberViews(final Database database,
                                                      final ActionInputData actionInputData) {
        List<Movie> filteredMoviesListByYear = Movie.filterMoviesByYear(
            database, actionInputData);
        List<Movie> filteredMovies = Movie.filterMoviesByGenre(
            database, actionInputData, filteredMoviesListByYear);

        List<Movie> sortedMovies = filteredMovies.stream()
                .sorted((movie1, movie2) -> {
                    if (movie1.getNumberOfViews(database) == movie2.getNumberOfViews(database)) {
                        if (actionInputData.getSortType().equals("asc")) {
                            return movie1.getTitle().compareTo(movie2.getTitle());
                        } else {
                            return movie2.getTitle().compareTo(movie1.getTitle());
                        }
                    }

                    if (actionInputData.getSortType().equals("asc")) {
                        return movie1.getNumberOfViews(database)
                                - movie2.getNumberOfViews(database);
                    } else {
                        return movie2.getNumberOfViews(database)
                                - movie1.getNumberOfViews(database);
                    }
                }).toList();

        ArrayList<String> moviesNames = new ArrayList<>();

        for (Movie movie : sortedMovies) {
            if (movie.getNumberOfViews(database) > 0) {
                moviesNames.add(movie.getTitle());
            }
        }

        int n = actionInputData.getNumber();
        if (moviesNames.size() > n) {
            while (moviesNames.size() != n) {
                moviesNames.remove(moviesNames.size() - 1);
            }
        }

        return moviesNames;
    }

    public static ArrayList<String> serialsNumberViews(final Database database,
                                                       final ActionInputData actionInputData) {
        List<Serial> filteredSerialsListByYear = Serial.filterSerialsByYear(
                database, actionInputData);
        List<Serial> filteredSerials = Serial.filterSerialsByGenre(
                database, actionInputData, filteredSerialsListByYear);

        List<Serial> sortedSerials = filteredSerials.stream()
                .sorted((serial1, serial2) -> {
                    if (serial1.getNumberOfViews(database)
                            == serial2.getNumberOfViews(database)) {
                        if (actionInputData.getSortType().equals("asc")) {
                            return serial1.getTitle().compareTo(serial2.getTitle());
                        } else {
                            return serial2.getTitle().compareTo(serial1.getTitle());
                        }
                    }

                    if (actionInputData.getSortType().equals("asc")) {
                        return serial1.getNumberOfViews(database)
                                - serial2.getNumberOfViews(database);
                    } else {
                        return serial2.getNumberOfViews(database)
                                - serial1.getNumberOfViews(database);
                    }
                }).toList();

        ArrayList<String> serialsNames = new ArrayList<>();

        for (Serial serial : sortedSerials) {
            if (serial.getNumberOfViews(database) > 0) {
                serialsNames.add(serial.getTitle());
            }
        }

        int n = actionInputData.getNumber();
        if (serialsNames.size() > n) {
            while (serialsNames.size() != n) {
                serialsNames.remove(serialsNames.size() - 1);
            }
        }

        return serialsNames;
    }

    public static ArrayList<String> moviesRatings(final Database database,
                                                  final ActionInputData actionInputData) {
        List<Movie> filteredMoviesListByYear = Movie.filterMoviesByYear(
                database, actionInputData);
        List<Movie> filteredMovies = Movie.filterMoviesByGenre(
                database, actionInputData, filteredMoviesListByYear);

        List<Movie> sortedMovies = filteredMovies.stream()
                .sorted((movie1, movie2) -> {
                    if (movie1.getRating() == movie2.getRating()) {
                        if (actionInputData.getSortType().equals("asc")) {
                            return movie1.getTitle().compareTo(movie2.getTitle());
                        } else {
                            return movie2.getTitle().compareTo(movie1.getTitle());
                        }
                    }

                    if (actionInputData.getSortType().equals("asc")) {
                        return Double.compare(movie1.getRating(), movie2.getRating());
                    } else {
                        return Double.compare(movie2.getRating(), movie1.getRating());
                    }
                }).toList();

        ArrayList<String> moviesNames = new ArrayList<>();

        for (Movie movie : sortedMovies) {
            if (movie.getRating() > 0) {
                moviesNames.add(movie.getTitle());
            }
        }

        int n = actionInputData.getNumber();
        if (moviesNames.size() > n) {
            while (moviesNames.size() != n) {
                moviesNames.remove(moviesNames.size() - 1);
            }
        }

        return moviesNames;
    }

    public static ArrayList<String> serialsRatings(final Database database,
                                                   final ActionInputData actionInputData) {
        List<Serial> filteredSerialsListByYear = Serial.filterSerialsByYear(
                database, actionInputData);
        List<Serial> filteredSerials = Serial.filterSerialsByGenre(
                database, actionInputData, filteredSerialsListByYear);

        List<Serial> sortedSerials = filteredSerials.stream()
                .sorted((serial1, serial2) -> {
                    if (serial1.getRating() == serial2.getRating()) {
                        if (actionInputData.getSortType().equals("asc")) {
                            return serial1.getTitle().compareTo(serial2.getTitle());
                        } else {
                            return serial2.getTitle().compareTo(serial1.getTitle());
                        }
                    }

                    if (actionInputData.getSortType().equals("asc")) {
                        return Double.compare(serial1.getRating(), serial2.getRating());
                    } else {
                        return Double.compare(serial2.getRating(), serial1.getRating());
                    }
                }).toList();

        ArrayList<String> serialsNames = new ArrayList<>();

        for (Serial serial : sortedSerials) {
            if (serial.getRating() > 0) {
                serialsNames.add(serial.getTitle());
            }
        }

        int n = actionInputData.getNumber();
        if (serialsNames.size() > n) {
            while (serialsNames.size() != n) {
                serialsNames.remove(serialsNames.size() - 1);
            }
        }

        return serialsNames;
    }
}
