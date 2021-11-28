package homework;

import fileio.ActionInputData;
import fileio.Input;
import fileio.MovieInputData;
import fileio.SerialInputData;

import java.util.ArrayList;
import java.util.List;

public final class Recommendation {
    private Recommendation() {
    }

    /** method that returns name of the video for the standard recommendation */
    public static String standard(final Database database,
                                  final String username, final Input input) {
        User user = database.getUserMap().get(username);

        for (MovieInputData movie : input.getMovies()) {
            if (!user.getHistory().containsKey(movie.getTitle())) {
                return movie.getTitle();
            }
        }

        for (SerialInputData serial : input.getSerials()) {
            if (!user.getHistory().containsKey(serial.getTitle())) {
                return serial.getTitle();
            }
        }

        return "";
    }

    /** method that returns name of the best unseen video */
    public static String bestUnseen(final Database database,
                                    final String username, final Input input) {
        User user = database.getUserMap().get(username);

        List<Video> videoList = new ArrayList<>();

        for (MovieInputData movie : input.getMovies()) {
            videoList.add(new Movie(movie));
        }

        for (SerialInputData serial : input.getSerials()) {
            videoList.add(new Serial(serial));
        }

        List<Video> sortedVideos = videoList.stream()
                .sorted((video1, video2) -> Double.compare(video2.getRatingFromDatabase(database),
                        video1.getRatingFromDatabase(database))).toList();

        for (Video video : sortedVideos) {
            if (!user.getHistory().containsKey(video.getTitle())) {
                return video.getTitle();
            }
        }

        return "";
    }

    /** method that returns the name of a video after the favorite recommendation */
    public static String favoriteRecommendation(final Database database,
                                                final String username, final Input input) {
        User user = database.getUserMap().get(username);
        List<Video> videoList = new ArrayList<>();

        for (MovieInputData movieInputData : input.getMovies()) {
            Movie movie = new Movie(movieInputData);

            if (movie.getNumberOfFavored(database) != 0) {
                videoList.add(movie);
            }
        }

        for (SerialInputData serialInputData : input.getSerials()) {
            Serial serial = new Serial(serialInputData);
            if (serial.getNumberOfFavored(database) != 0) {
                videoList.add(serial);
            }
        }

        List<Video> sortedVideos = videoList.stream()
                .sorted((video1, video2) -> Double.compare(video2.getNumberOfFavored(database),
                        video1.getNumberOfFavored(database))).toList();

        for (Video video : sortedVideos) {
            if (!user.getHistory().containsKey(video.getTitle())) {
                return video.getTitle();
            }
        }

        return "";
    }

    /** method that returns the names of the videos from the search recommendation */
    public static ArrayList<String> searchRecommendation(final Database database,
                                                         final String username,
                                                         final ActionInputData actionInputData) {
        User user = database.getUserMap().get(username);

        List<Video> videoList = new ArrayList<>();
        List<Movie> filteredMoviesByGenre;
        List<Serial> filteredSerialsByGenre;
        ArrayList<String> videosName = new ArrayList<>();

        filteredMoviesByGenre = database.getMovieMap().values().stream()
                .filter(movie -> movie.checkGenre(actionInputData.getGenre()))
                .toList();

        filteredSerialsByGenre = database.getSerialMap().values().stream()
                .filter(serial -> serial.checkGenre(actionInputData.getGenre()))
                .toList();

        videoList.addAll(filteredMoviesByGenre);

        videoList.addAll(filteredSerialsByGenre);

        List<Video> sortedVideos = videoList.stream()
                .sorted((video1, video2) -> {
                    if (video1.getRating() == video2.getRating()) {
                        return video1.getTitle().compareTo(video2.getTitle());
                    }

                    return Double.compare(video1.getRatingFromDatabase(database),
                            video2.getRatingFromDatabase(database));
                }).toList();

        for (Video video : sortedVideos) {
            if (!user.getHistory().containsKey(video.getTitle())) {
                videosName.add(video.getTitle());
            }
        }

        return videosName;
    }

    /** method that returns the name of a video returned after the popular recommendation */
    public static String popularRecommendation(final Database database, final String username,
                                               final Input input) {
        User user = database.getUserMap().get(username);
        List<Video> videoList = new ArrayList<>();

        for (MovieInputData movieInputData : input.getMovies()) {
            Movie movie = new Movie(movieInputData);
            videoList.add(movie);
        }

        for (SerialInputData serialInputData : input.getSerials()) {
            Serial serial = new Serial(serialInputData);
            videoList.add(serial);
        }


        List<String> genresList = database.getGenresMap().keySet().stream()
                .sorted((genre1, genre2) -> {
                    Integer numberGenre1 = database.getGenresMap().get(genre1);
                    Integer numberGenre2 = database.getGenresMap().get(genre2);

                    return numberGenre2 - numberGenre1;
                }).toList();

        for (String genre : genresList) {
            List<Video> filteredVideoList = videoList.stream()
                    .filter(video -> video.checkGenre(genre)).toList();

            for (Video video : filteredVideoList) {
                if (!user.getHistory().containsKey(video.getTitle())) {
                    return video.getTitle();
                }
            }
        }

        return "";
    }
}
