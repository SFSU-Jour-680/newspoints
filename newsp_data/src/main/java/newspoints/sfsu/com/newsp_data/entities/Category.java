package newspoints.sfsu.com.newsp_data.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains Shots, Sources and Questions for each type of Category. Used only during initial parsing of the Input XML file
 * <p/>
 * Created by Pavitra on 6/14/2016.
 */
public class Category {
    private ArrayList<Shot> shots;
    private ArrayList<Source> sources;
    private ArrayList<Question> questions;

    public Category(ArrayList<Shot> shots, ArrayList<Source> sources, ArrayList<Question> questions) {
        this.shots = shots;
        this.sources = sources;
        this.questions = questions;
    }

    public ArrayList<Shot> getShots() {
        return shots;
    }

    public void setShots(ArrayList<Shot> shots) {
        this.shots = shots;
    }

    public ArrayList<Source> getSources() {
        return sources;
    }

    public void setSources(ArrayList<Source> sources) {
        this.sources = sources;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
}
