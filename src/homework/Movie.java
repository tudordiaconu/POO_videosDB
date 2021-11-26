package homework;

import fileio.ActionInputData;
import fileio.MovieInputData;
import fileio.ShowInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Movie extends Video{
    private final int duration;
    public Movie(MovieInputData movieInputData) {
        super(movieInputData);
        this.duration = movieInputData.getDuration();
    }

    public static List<Movie> filterMoviesByYear(Database database, ActionInputData actionInputData) {
        int year;
        List<Movie> filteredMoviesByYear;
        if (actionInputData.getFilters().get(0).get(0) != null) {
            year = Integer.parseInt(actionInputData.getFilters().get(0).get(0));
            filteredMoviesByYear = database.getMovieMap().values()
                    .stream()
                    .filter(movie -> movie.checkYear(year))
                    .toList();
        } else {
            filteredMoviesByYear = database.getMovieMap().values().stream().toList();
        }

        return filteredMoviesByYear;
    }

    public static List<Movie> filterMoviesByGenre(Database database, ActionInputData actionInputData,
                                                  List<Movie> filteredByYear) {
        List<Movie> filteredMoviesByGenre;

        if (actionInputData.getFilters().get(1).get(0) != null) {
            List<String> genres = new ArrayList<>(actionInputData.getFilters().get(1));

            filteredMoviesByGenre = filteredByYear
                    .stream()
                    .filter(movie -> movie.checkGenres(genres))
                    .toList();
        } else {
            filteredMoviesByGenre = database.getMovieMap().values().stream().toList();
        }

        return filteredMoviesByGenre;
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
