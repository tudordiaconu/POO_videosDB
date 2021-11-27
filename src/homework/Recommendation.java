package homework;

import fileio.ActionInputData;
import fileio.Input;
import fileio.MovieInputData;
import fileio.SerialInputData;

import javax.sql.rowset.serial.SQLInputImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class Recommendation {
    private Recommendation() {
    }

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
                .sorted((video1, video2) -> Double.compare(video2.getRating(),
                        video1.getRating())).toList();

        for (Video video : sortedVideos) {
            if (!user.getHistory().containsKey(video.getTitle())) {
                return video.getTitle();
            }
        }

        return "";
    }

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
                .sorted((video1, video2) -> Double.compare(video2.getRating(),
                        video1.getRating())).toList();

        for (Video video : sortedVideos) {
            if (!user.getHistory().containsKey(video.getTitle())) {
                return video.getTitle();
            }
        }

        return "";
    }

    public static ArrayList<String> searchRecommendation(final Database database, final String username,
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

                    return Double.compare(video1.getRating(), video2.getRating());
                }).toList();

        for (Video video : sortedVideos) {
            if (!user.getHistory().containsKey(video.getTitle())) {
                videosName.add(video.getTitle());
            }
        }

        return videosName;
    }
}
