package BMI.CalculateMethods;



public class CPMCalculate extends PpmCalculate { //Klasa zajmuje sie wyliczaniem wartosci CPM na podstawie danych podanych przez uzytkownika

    public double calculateCpm(double ppm, double phisicalActivity) { //Metoda wylicza CPM
        return Math.round(ppm * phisicalActivity * 100.00) / 100.00;
    }
}


