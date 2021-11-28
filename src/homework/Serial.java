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

    @Override
    public double getRatingFromDatabase(Database database) {
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
