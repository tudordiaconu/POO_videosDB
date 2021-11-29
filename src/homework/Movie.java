package homework;

import fileio.ActionInputData;
import fileio.MovieInputData;

import java.util.ArrayList;
import java.util.List;

public class Movie extends Video {
    private final int duration;

    public Movie(final MovieInputData movieInputData) {
        super(movieInputData);
        this.duration = movieInputData.getDuration();
    }

    /** sorts a list of movies by the duration */
    public static List<Movie> sortMovieListByDuration(final List<Movie> filteredMovies,
                                                    final ActionInputData actionInputData) {
        return filteredMovies.stream()
                .sorted((movie1, movie2) -> {
                    if (movie1.getDuration() == movie2.getDuration()) {
                        if (actionInputData.getSortType().equals("asc")) {
                            return movie1.getTitle().compareTo(movie2.getTitle());
                        } else {
                            return movie2.getTitle().compareTo(movie1.getTitle());
                        }
                    }

                    if (actionInputData.getSortType().equals("asc")) {
                        return movie1.getDuration() - movie2.getDuration();
                    } else {
                        return movie2.getDuration() - movie1.getDuration();
                    }
                }).toList();
    }

    /** sorts a list of movies by the number of times it appears in the lists of favourites */
    public static List<Movie> sortMovieListByFavorites(final List<Movie> filteredMovies,
                                                       final Database database,
                                                       final ActionInputData actionInputData) {
        return filteredMovies.stream()
                .sorted((movie1, movie2) -> {
                    if (movie1.getNumberOfFavored(database)
                            == movie2.getNumberOfFavored(database)) {
                        if (actionInputData.getSortType().equals("asc")) {
                            return movie1.getTitle().compareTo(movie2.getTitle());
                        } else {
                            return movie2.getTitle().compareTo(movie1.getTitle());
                        }
                    }

                    if (actionInputData.getSortType().equals("asc")) {
                        return movie1.getNumberOfFavored(database)
                                - movie2.getNumberOfFavored(database);
                    } else {
                        return movie2.getNumberOfFavored(database)
                                - movie1.getNumberOfFavored(database);
                    }
                }).toList();
}

    /** sorts a list of movies by the number of times it was viewed */
    public static List<Movie> sortMovieListByViews(final List<Movie> filteredMovies,
                                                   final Database database,
                                                   final ActionInputData actionInputData) {
        return filteredMovies.stream()
                .sorted((movie1, movie2) -> {
                    if (movie1.getNumberOfViews(database) == movie2.getNumberOfViews(database)) {
                        if (actionInputData.getSortType().equals("asc")) {
                            return movie1.getTitle().compareTo(movie2.getTitle());
                        } else {
                            return movie2.getTitle().compareTo(movie1.getTitle());
                        }
                    }

                    if (actionInputData.getSortType().equals("asc")) {
                        return movie1.getNumberOfViews(database)
                                - movie2.getNumberOfViews(database);
                    } else {
                        return movie2.getNumberOfViews(database)
                                - movie1.getNumberOfViews(database);
                    }
                }).toList();
    }

    /** sorts a list of movies by rating */
    public static List<Movie> sortMovieListByRating(final List<Movie> filteredMovies,
                                            final ActionInputData actionInputData) {
        return filteredMovies.stream()
                .sorted((movie1, movie2) -> {
                    if (movie1.getRating() == movie2.getRating()) {
                        if (actionInputData.getSortType().equals("asc")) {
                            return movie1.getTitle().compareTo(movie2.getTitle());
                        } else {
                            return movie2.getTitle().compareTo(movie1.getTitle());
                        }
                    }

                    if (actionInputData.getSortType().equals("asc")) {
                        return Double.compare(movie1.getRating(), movie2.getRating());
                    } else {
                        return Double.compare(movie2.getRating(), movie1.getRating());
                    }
                }).toList();
    }

    /** filters the movies by year */
    public static List<Movie> filterMoviesByYear(final Database database,
                                                 final ActionInputData actionInputData) {
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

    /** filters the movie by genre */
    public static List<Movie> filterMoviesByGenre(final Database database,
                                                  final ActionInputData actionInputData,
                                                  final List<Movie> filteredByYear) {
        List<Movie> filteredMoviesByGenre;

        if (actionInputData.getFilters().get(1).get(0) != null) {
            List<String> genres = new ArrayList<>(actionInputData.getFilters().get(1));

            filteredMoviesByGenre = filteredByYear
                    .stream()
                    .filter(movie -> movie.checkGenres(genres))
                    .toList();
        } else {
            filteredMoviesByGenre = filteredByYear;
        }

        return filteredMoviesByGenre;
    }

    /** calculates the rating of the movie */
    @Override
    public double getRating() {
        double rating;
        double movieSum = 0;
        int numberOfRatings = this.getRatings().size();
        for (int i = 0; i < numberOfRatings; i++) {
            movieSum = movieSum + this.getRatings().get(i);
        }
        if (numberOfRatings != 0) {
            rating = movieSum / numberOfRatings;
        } else {
            rating = 0;
        }
        return rating;
    }

    /** calculates the rating of movie from a wanted database */
    @Override
    public double getRatingFromDatabase(final Database database) {
        Movie auxMovie = database.getMovieMap().get(this.getTitle());

        return auxMovie.getRating();
    }

    /** getter for the duration */
    public int getDuration() {
        return duration;
    }
}
