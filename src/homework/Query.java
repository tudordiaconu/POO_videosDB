package homework;

import fileio.ActionInputData;

import javax.xml.crypto.Data;
import java.util.ArrayList;
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

        List<String> awards = new ArrayList<>();
        for (String award : actionInputData.getFilters().get(3)) {
            awards.add(award);
        }

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

        for (Actor actor : actors) {
            if (actor.getNumberAwards() > 0) {
                actorNames.add(actor.getName());
            }
        }

        return actorNames;
    }



    public static void actorsFilterDescription(Database database, ActionInputData actionInputData) {

    }

}