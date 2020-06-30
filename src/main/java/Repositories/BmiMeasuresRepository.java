package Repositories;

import Objects.Measure;

import java.util.ArrayList;

public class BmiMeasuresRepository {
    private final ArrayList<Measure> bmiMeasures;

    public BmiMeasuresRepository(ArrayList<Measure> bmiMeasures) {
        this.bmiMeasures = bmiMeasures;
    }

    public ArrayList<Measure> getMeasures() {
        return bmiMeasures;
    }
}
