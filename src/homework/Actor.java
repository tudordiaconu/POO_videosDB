package homework;

import java.util.List;
import actor.ActorsAwards;
import fileio.ActorInputData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class Actor {
    private String name;
    private final String careerDescription;
    private ArrayList<String> filmography;

    private final Map<String, Integer> awards;

    public Actor(final ActorInputData actorInputData) {
        this.name = actorInputData.getName();
        this.careerDescription = actorInputData.getCareerDescription();
        this.filmography = actorInputData.getFilmography();
        this.awards = new HashMap<>();
        for (Map.Entry<ActorsAwards, Integer> actorsAwards
                : actorInputData.getAwards().entrySet()) {
            String award = actorsAwards.getKey().toString();
            this.awards.put(award, actorsAwards.getValue());
        }
    }

    /** calculates average rating of an actor*/
    public double getAverageRating(final Database database) {
        double rating = 0;
        int numberOfMoviesInMap = 0;

        for (String video : filmography) {
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

        return rating / numberOfMoviesInMap;
    }

    /** calculate total number of awards won*/
    public int getNumberAwards() {
        int number = 0;
        Collection<Integer> numAwards = awards.values();
        for (Integer i : numAwards) {
            number = number + i;
        }

        return number;
    }

    /**Getter for name*/
    public String getName() {
        return name;
    }

    /** Getter for carrer description */
    public String getCareerDescription() {
        return careerDescription;
    }

    /** Getter for filmography */
    public ArrayList<String> getFilmography() {
        return filmography;
    }

    /** Getter for the map of awards*/
    public Map<String, Integer> getAwards() {
        return awards;
    }

    /** Checks if the actor has the awards required by filter */
    public boolean checkAwards(final List<String> filterAwards) {
        for (String award : filterAwards) {
            if (!this.getAwards().containsKey(award)) {
                return false;
            }
        }

        return true;
    }

    /** Checks if the actor's career description contains the words required by filter */
    public boolean checkWords(final List<String> words) {
        String lowerCareerDescription = this.getCareerDescription().toLowerCase();
        String[] wordsCareerDescription = lowerCareerDescription.split("\\W+");

        Map<String, Integer> mapOfWords = new HashMap<>();

        for (String word : wordsCareerDescription) {
            mapOfWords.put(word, 0);
        }

        for (String word : words) {
            if (!mapOfWords.containsKey(word)) {
                return false;
            }
        }

        return true;
    }
}
