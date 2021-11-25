package homework;

import fileio.ActionInputData;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Query {
    public static List<User> user(Database database, ActionInputData actionInputData) {

        return database.getUserMap().values().stream()

                .sorted((o1, o2) -> {
                    int firstNumberRatings = o1.getMoviesGivenRatings().size() + o1.getSeasonsGivenRatings().size();
                    int secondNumberRatings = o2.getMoviesGivenRatings().size() + o2.getSeasonsGivenRatings().size();

                    if (firstNumberRatings >= secondNumberRatings) {
                        return 1;
                    } else {
                        return 0;
                    }
                })

                .toList();
    }
}
