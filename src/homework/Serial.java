package homework;

import entertainment.Season;
import fileio.SerialInputData;

import java.util.ArrayList;

public class Serial extends Video{
    private final int numberOfSeasons;
    private final ArrayList<Season> seasons;

    public Serial(SerialInputData serialInputData) {
        super(serialInputData);
        this.numberOfSeasons = serialInputData.getNumberSeason();
        this.seasons = serialInputData.getSeasons();
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
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
