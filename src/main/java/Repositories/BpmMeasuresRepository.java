package Repositories;

import Objects.Measure;

import java.util.ArrayList;

public class BpmMeasuresRepository {
    private final ArrayList<Measure> bpmMeasures;

    public BpmMeasuresRepository(ArrayList<Measure> bpmMeasures) {
        this.bpmMeasures = bpmMeasures;
    }

    public ArrayList<Measure> getMeasures() {
        return bpmMeasures;
    }
}
