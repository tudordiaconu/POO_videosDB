package homework;

import fileio.ActionInputData;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Query {
    public static ArrayList<String> user(Database database, ActionInputData actionInputData) {
        ArrayList<String> usernames = new ArrayList<>();
        List<User> users = database.getUserMap().values().stream()

                .sorted((user1, user2) -> {
                    int firstNumberRatings = user1.getNrGivenRatings();
                    int secondNumberRatings = user2.getNrGivenRatings();

                    if (actionInputData.getSortType().equals("asc")) {
                        return firstNumberRatings - secondNumberRatings;
                    } else {
                        return secondNumberRatings - firstNumberRatings;
                    }
                })

                .toList();

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
}