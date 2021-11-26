package homework;

import fileio.ActionInputData;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Query {
    public static ArrayList<String> user(Database database, ActionInputData actionInputData) {
        ArrayList<String> usernames = new ArrayList<>();
        List<User> users = database.getUserMap().values()
                .stream()
                .sorted((user1, user2) -> {
                    int firstNumberRatings = user1.getNrGivenRatings();
                    int secondNumberRatings = user2.getNrGivenRatings();

                    if (firstNumberRatings == secondNumberRatings) {
                        return user1.getUsername().compareTo(user2.getUsername());
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

    public static ArrayList<String> actorsAverage (Database database, ActionInputData actionInputData) {
        ArrayList<String> actorNames = new ArrayList<>();
        int n = actionInputData.getNumber();

        List<Actor> actors = database.getActorMap().values()
                .stream()
                .sorted((actor1, actor2) -> {
                    actor1.getAverageRating(database);
                    actor2.getAverageRating(database);

                    if (actor1.averageRating == actor2.averageRating) {
                        return actor1.getName().compareTo(actor2.getName());
                    }

                    if (actionInputData.getSortType().equals("asc")) {
                        return Double.compare(actor1.averageRating, actor2.averageRating);
                    } else {
                        return Double.compare(actor2.averageRating, actor1.averageRating);
                    }
                }).toList();

        for (Actor actor : actors) {
            if (actor.averageRating > 0) {
                actorNames.add(actor.getName());
            }
        }

        if (actorNames.size() > n) {
            while(actorNames.size() != n) {
                actorNames.remove(actorNames.size() - 1);
            }
        }

        return actorNames;
    }

    public static ArrayList<String> actorsAwards (Database database, ActionInputData actionInputData) {
        ArrayList<String> actorNames = new ArrayList<>();

        List<String> awards = new ArrayList<>(actionInputData.getFilters().get(3));

        List<Actor> actors = database.getActorMap().values().stream()
                .filter(actor -> actor.checkAwards(awards)).toList();

        List<Actor> sortedActors = actors
                .stream()
                .sorted((actor1, actor2) -> {
                    int firstNumberAwards = actor1.getNumberAwards();
                    int secondNumberAwards = actor2.getNumberAwards();

                    if (firstNumberAwards == secondNumberAwards) {
                        return actor1.getName().compareTo(actor2.getName());
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



    public static ArrayList<String> actorsFilterDescription(Database database, ActionInputData actionInputData) {
        ArrayList<String> actorNames = new ArrayList<>();
        List<String> words = new ArrayList<>(actionInputData.getFilters().get(2));

        List<Actor> actors = database.getActorMap().values()
                .stream()
                .filter(actor -> actor.checkWords(words))
                .toList();

        List<Actor> sortedActors = actors
                .stream()
                .sorted(Comparator.comparing(Actor::getName)).toList();

        for (Actor actor : sortedActors) {
            actorNames.add(actor.getName());
        }

        return actorNames;
    }

    public static ArrayList<String> moviesLongest(Database database, ActionInputData actionInputData) {
        List<Movie> filteredMoviesListByYear = Movie.filterMoviesByYear(database, actionInputData);
        List<Movie> filteredMovies = Movie.filterMoviesByGenre(database, actionInputData, filteredMoviesListByYear);

        List<Movie> sortedMovies = filteredMovies
                .stream()
                .sorted((movie1, movie2) -> {
                    if (movie1.getDuration() == movie2.getDuration()) {
                        return movie1.getTitle().compareTo(movie2.getTitle());
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
            while(moviesNames.size() != n) {
                moviesNames.remove(moviesNames.size() - 1);
            }
        }

        return moviesNames;
    }

    public static ArrayList<String> serialsLongest(Database database, ActionInputData actionInputData) {
        List<Serial> filteredSerialsListByYear = Serial.filterSerialsByYear(database, actionInputData);
        List<Serial> filteredSerials = Serial.filterSerialsByGenre(database, actionInputData, filteredSerialsListByYear);

        List<Serial> sortedSerials = filteredSerials
                .stream()
                .sorted((serial1, serial2) -> {
                    if (serial1.calculateDuration() == serial2.calculateDuration()) {
                        return serial1.getTitle().compareTo(serial2.getTitle());
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
            while(serialsNames.size() != n) {
                serialsNames.remove(serialsNames.size() - 1);
            }
        }

        return serialsNames;
    }

}