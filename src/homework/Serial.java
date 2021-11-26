package homework;

import entertainment.Season;
import fileio.ActionInputData;
import fileio.SerialInputData;

import java.util.ArrayList;
import java.util.List;

public class Serial extends Video{
    private final int numberOfSeasons;
    private final ArrayList<Season> seasons;

    public Serial(SerialInputData serialInputData) {
        super(serialInputData);
        this.numberOfSeasons = serialInputData.getNumberSeason();
        this.seasons = serialInputData.getSeasons();
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    public int calculateDuration() {
        int serialDuration = 0;

        for (Season season : this.seasons) {
            serialDuration += season.getDuration();
        }

        return serialDuration;
    }

    public static List<Serial> filterSerialsByYear(Database database, ActionInputData actionInputData) {
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

    public static List<Serial> filterSerialsByGenre(Database database, ActionInputData actionInputData,
                                                    List<Serial> filteredByYear) {
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
    public double getRating(){
        double ratingShow;
        double showSum = 0;

        for(Season season : this.seasons) {
            double seasonSum = 0;
            if(season.getRatings().size() != 0) {
                for (int i = 0; i < season.getRatings().size(); i++) {
                    seasonSum = seasonSum + season.getRatings().get(i);
                }
                double seasonRating = seasonSum / season.getRatings().size();
                showSum = showSum + seasonRating;
            }
        }

        ratingShow = showSum / this.numberOfSeasons;
        return ratingShow;
    }

    @Override
    public String toString() {
        return "Serial{" + " title= "
                + super.getTitle() + " " + " year= "
                + super.getYear() + " cast {"
                + super.getCast() + " }\n" + " genres {"
                + super.getGenres() + " }\n "
                + " numberSeason= " + numberOfSeasons
                + ", seasons=" + seasons + "\n\n" + '}';
    }
}
