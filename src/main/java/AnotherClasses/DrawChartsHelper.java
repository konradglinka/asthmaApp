package AnotherClasses;

import Objects.Measure;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;

import java.util.ArrayList;


public class DrawChartsHelper { // Klasa zajmuje sie tworzeniem wykresow na podstawie rekordow zawartych w bazie danych
    private BarChart barChart;
    private LineChart lineChart;
    private XYChart.Series series=new XYChart.Series(); //Dane na wykresie
    private int howManyMeasuresOnChart; //Maksymalna ilość ostatnich danych na wykresie
    public DrawChartsHelper(TextField howManyMeasuresOnChart, BarChart barChart,LineChart lineChart) {
        this.howManyMeasuresOnChart =Integer.parseInt(howManyMeasuresOnChart.getText());
        this.barChart=barChart;
        this.lineChart=lineChart;
        lineChart.setAnimated(false);
        barChart.setAnimated(false);
    }

    private void refreshData(ArrayList<Measure> list)  { //Metoda aktualizuje dane znajdujace sie na wykresie
        ArrayList<Measure> listOfResulsts= list;
        int i=0;
        if(listOfResulsts.size()>= howManyMeasuresOnChart){
            i=listOfResulsts.size()- howManyMeasuresOnChart;  //Jeżeli mamy wiecej pomiarów dostępnych niż chcemy otrzymać to zaczynamy od i  pozycji
        }
        for(; i<listOfResulsts.size();i++) {
            series.getData().add(new XYChart.Data<>(listOfResulsts.get(i).getDate(), listOfResulsts.get(i).getValue()));
        }
        }
    public void drawLineChart(ArrayList<Measure>list)  { //Metoda rysuje wykres liniowy
        lineChart.getData().clear();
        refreshData(list);
        lineChart.getData().addAll(series);
    }
    public void drawBarChart(ArrayList<Measure>list) { //Metoda rysuje wykres slupkowy
        refreshData(list);
        barChart.getData().clear();
        barChart.getData().addAll(series);
    }
}
