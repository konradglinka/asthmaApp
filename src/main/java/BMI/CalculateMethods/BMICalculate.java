package BMI.CalculateMethods;

import Repositories.UserSettingsRepository;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class BMICalculate { //Klasa zajmuje sie wyliczaniem wartosci BMI na podstawie danych podanych przez uzytkownika

    public double calculateBMI(UserSettingsRepository userSettingsRepository,double bodyMass){ // Metoda wylicza BMI i zwraca obiekt BMI zawierajacy date i wartosc pomiaru
               return Math.round((bodyMass/(userSettingsRepository.getUserSettings().getGrowth()*userSettingsRepository.getUserSettings().getGrowth())*100.00))/100.00;

    }
    public void ShowAboutBMI(Label BMIResultLabel, Label AboutBMILabel){
        double BMI= Double.parseDouble(BMIResultLabel.getText());
        if(BMI<16){
            AboutBMILabel.setText("Twoj wskaznik BMI wskazuje na wyglodzenie");
        }
        if(BMI>16&&BMI<16.99){
            AboutBMILabel.setText("Twoj wskaznik BMI wskazuje na wychudzenie");
        }
        if(BMI>17&&BMI<18.49){
            AboutBMILabel.setText("Twoj wskaznik BMI wskazuje na, ze masz niedowage");
        }
        if(BMI>18.5&&BMI<24.99){
            AboutBMILabel.setText("Twoj wskaznik BMI wskazuje na to, ze twoja waga jest prawidlowa");
        }
        if(BMI>25&&BMI<29.99){
            AboutBMILabel.setText("Twoj wskaznik BMI wskazuje na to, ze masz nadwage ");
        }
        if(BMI>30&BMI<34.99){
            AboutBMILabel.setText("Twoj wskaznik BMI wskazuje na to, ze masz otylosc 1 stopnia");
            if(BMI>35&BMI<39.99){
                AboutBMILabel.setText("Twoj wskaznik BMI wskazuje na to, ze masz otylosc 2 stopnia");        }}
        if(BMI>40){
            AboutBMILabel.setText("Twoj wskaznik BMI wskazuje na to, ze masz skrajna otylosc");        }
    }
}
