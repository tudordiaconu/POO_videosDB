package homework;

import actor.ActorsAwards;
import fileio.ActorInputData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Actor {
    private String name;
    private String careerDescription;
    private ArrayList<String> filmography;

    private Map<ActorsAwards, Integer> awards;
    public double averageRating;

    public Actor(ActorInputData actorInputData) {
        this.name = actorInputData.getName();
        this.careerDescription = actorInputData.getCareerDescription();
        this.filmography = actorInputData.getFilmography();
        this.awards = actorInputData.getAwards();
    }

    public void getAverageRating(Database database) {
        double rating = 0;
        int numberOfMoviesInMap = 0;

        for(String video : filmography) {
            if (database.getSerialMap().containsKey(video)) {
                if (database.getSerialMap().get(video).getRating() != 0) {
                    rating += database.getSerialMap().get(video).getRating();
                    numberOfMoviesInMap++;
                }
            } else if (database.getMovieMap().containsKey(video)) {
                if (database.getMovieMap().get(video).getRating() != 0) {
                    rating += database.getMovieMap().get(video).getRating();
                    numberOfMoviesInMap++;
                }
            }
        }

        this.averageRating = rating/numberOfMoviesInMap;
    }

    public int getNumberAwards() {
        int number = 0;
        Collection<Integer> numAwards = awards.values();
        for (Integer i : numAwards) {
            number = number + i;
        }

        return number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public void setCareerDescription(String careerDescription) {
        this.careerDescription = careerDescription;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public void setFilmography(ArrayList<String> filmography) {
        this.filmography = filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public void setAwards(Map<ActorsAwards, Integer> awards) {
        this.awards = awards;
    }

    public boolean checkAwards(List<String> awards) {
        for (String award : awards) {
            if(!this.getAwards().containsKey(award)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "name='" + name + '\'' +
                ", careerDescription='" + careerDescription + '\'' +
                ", filmography=" + filmography +
                ", awards=" + awards +
                '}';
    }
}
