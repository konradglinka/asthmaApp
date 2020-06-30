package Repositories;

import Objects.Measure;

import java.util.ArrayList;

public class PefMeasuresRepository {
    private final ArrayList<Measure> pefMeasures;

    public PefMeasuresRepository(ArrayList<Measure> pefMeasures) {
        this.pefMeasures = pefMeasures;
    }

    public ArrayList<Measure> getMeasures() {
        return pefMeasures;
    }
}
