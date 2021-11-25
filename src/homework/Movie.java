package homework;

import fileio.MovieInputData;
import fileio.ShowInput;

public class Movie extends Video{
    private final int duration;
    public Movie(MovieInputData movieInputData) {
        super(movieInputData);
        this.duration = movieInputData.getDuration();
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
