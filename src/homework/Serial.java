package homework;

import entertainment.Season;
import fileio.ActionInputData;
import fileio.SerialInputData;

import java.util.ArrayList;
import java.util.List;

public class Serial extends Video {
    private final int numberOfSeasons;
    private final ArrayList<Season> seasons;

    public Serial(final SerialInputData serialInputData) {
        super(serialInputData);
        this.numberOfSeasons = serialInputData.getNumberSeason();
        this.seasons = serialInputData.getSeasons();
    }

    /** sorts a list of serials by the duration */
    public static List<Serial> sortSerialListByDuration(final List<Serial> filteredSerials,
                                                      final ActionInputData actionInputData) {
        return filteredSerials.stream()
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
    }

    /** sorts a list of serials by the number of times it appears in the lists of favourites */
    public static List<Serial> sortSerialListByFavorites(final List<Serial> filteredSerials,
                                                       final Database database,
                                                       final ActionInputData actionInputData) {
        return filteredSerials.stream()
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
    }

    /** sorts a list of serials by the number of times it was viewed */
    public static List<Serial> sortSerialsListByViews(final List<Serial> filteredSerials,
                                                   final Database database,
                                                   final ActionInputData actionInputData) {
        return filteredSerials.stream()
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
    }

    /** sorts a list of serials by rating */
    public static List<Serial> sortSerialListByRating(final List<Serial> filteredSerials,
                                                       final ActionInputData actionInputData) {
        return filteredSerials.stream()
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
    }

    /** getter for the arraylist of seasons */
    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    /** calculates the total duration of a serial */
    public int calculateDuration() {
        int serialDuration = 0;

        for (Season season : this.seasons) {
            serialDuration += season.getDuration();
        }

        return serialDuration;
    }

    /** filters the serials by year */
    public static List<Serial> filterSerialsByYear(final Database database,
                                                   final ActionInputData actionInputData) {
        int year;
        List<Serial> filteredSerialsByYear;
        if (actionInputData.getFilters().get(0).get(0) != null) {
            year = Integer.parseInt(actionInputData.getFilters().get(0).get(0));
            filteredSerialsByYear = database.getSerialMap().values()
                    .stream()
                    .filter(serial -> serial.checkYear(year))
                    .toList();
        } else {
            filteredSerialsByYear = database.getSerialMap().values().stream().toList();
        }

        return filteredSerialsByYear;
    }

    /** filters the serials by genre*/
    public static List<Serial> filterSerialsByGenre(final Database database,
                                                    final ActionInputData actionInputData,
                                                    final List<Serial> filteredByYear) {
        List<Serial> filteredSerialsByGenre;

        if (actionInputData.getFilters().get(1).get(0) != null) {
            List<String> genres = new ArrayList<>(actionInputData.getFilters().get(1));

            filteredSerialsByGenre = filteredByYear
                    .stream()
                    .filter(serial -> serial.checkGenres(genres))
                    .toList();
        } else {
            filteredSerialsByGenre = database.getSerialMap().values().stream().toList();
        }

        return filteredSerialsByGenre;
    }

    /** calculates the rating of serial from a wanted database */
    @Override
    public double getRatingFromDatabase(final Database database) {
        Serial auxSerial = database.getSerialMap().get(this.getTitle());

        return auxSerial.getRating();
    }

    /** calculates the total rating of a serial */
    @Override
    public double getRating() {
        double ratingShow;
        double showSum = 0;

        for (Season season : this.seasons) {
            double seasonSum = 0;
            double seasonRating;
            int numberOfRatings = season.getRatings().size();
            for (int i = 0; i < season.getRatings().size(); i++) {
                seasonSum = seasonSum + season.getRatings().get(i);
            }

            if (numberOfRatings != 0) {
                seasonRating = seasonSum / numberOfRatings;
            } else {
                seasonRating = 0;
            }

            showSum = showSum + seasonRating;
        }

        ratingShow = showSum / this.numberOfSeasons;
        return ratingShow;
    }
}
