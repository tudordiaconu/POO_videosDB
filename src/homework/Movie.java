package homework;

import fileio.MovieInputData;
import fileio.ShowInput;

public class Movie extends Video{
    private final int duration;
    public Movie(MovieInputData movieInputData) {
        super(movieInputData);
        this.duration = movieInputData.getDuration();
    }

    @Override
    public double getRating() {
        double rating;
        double movieSum = 0;
        int numberofRatings = this.getRatings().size();
        for(int i = 0; i < numberofRatings; i++) {
            movieSum = movieSum + this.getRatings().get(i);
        }

        rating = movieSum / numberofRatings;
        return rating;
    }


    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "Movie{" + "title= "
                + super.getTitle() + "year= "
                + super.getYear() + "duration= "
                + duration + "cast {"
                + super.getCast() + " }\n"
                + "genres {" + super.getGenres() + " }\n";
    }
}
