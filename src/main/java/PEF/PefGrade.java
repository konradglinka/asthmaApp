package PEF;

import Repositories.PefMeasuresRepository;
import Repositories.UserSettingsRepository;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class PefGrade {
    StringBuilder sb=new StringBuilder();
    PefMeasuresRepository pefMeasuresRepository;
    UserSettingsRepository userSettingsRepository;
    private TextField actualMeasuredPefTextField;
    private TextField normalPefTextField;
    private TextField measuredNormalPefDifferenceTextField;
    private TextField measuredNormalPefPercentTextField;
    private TextField lastPefTextField;
    private TextField measuredPefLastPefDiffernceTextField;
    private TextField dailyPefDifferncePercent;
    private TextField sfereColorTextField;
    private TextArea resultsAreaTextArea;
    private double normalValuePEF;
    private double measuredPEF;
    private double percentPEFNormalValue;
    private double lastPef;

    public PefGrade(PefMeasuresRepository pefMeasuresRepository, UserSettingsRepository userSettingsRepository, TextField actualMeasuredPefTextField, TextField normalPefTextField, TextField measuredNormalPefDifferenceTextField, TextField measuredNormalPefPercentTextField, TextField lastPefTextField, TextField measuredPefLastPefDiffernceTextField, TextField dailyPefDifferncePercent, TextField sfereColorTextField,TextArea resultsAreaTextArea) {
        this.pefMeasuresRepository = pefMeasuresRepository;
        this.userSettingsRepository = userSettingsRepository;
        this.actualMeasuredPefTextField = actualMeasuredPefTextField;
        this.normalPefTextField = normalPefTextField;
        this.measuredNormalPefDifferenceTextField = measuredNormalPefDifferenceTextField;
        this.measuredNormalPefPercentTextField = measuredNormalPefPercentTextField;
        this.lastPefTextField = lastPefTextField;
        this.measuredPefLastPefDiffernceTextField = measuredPefLastPefDiffernceTextField;
        this.dailyPefDifferncePercent = dailyPefDifferncePercent;
        this.sfereColorTextField = sfereColorTextField;
        this.resultsAreaTextArea=resultsAreaTextArea;
        NormalPEF normalPEF=new NormalPEF();
        this.normalValuePEF =normalPEF.calculateNormalPEFValue(userSettingsRepository.getUserSettings().getAge(),userSettingsRepository.getUserSettings().getGrowth(),userSettingsRepository.getUserSettings().isWhomen());
        this.measuredPEF=pefMeasuresRepository.getMeasures().get(pefMeasuresRepository.getMeasures().size()-1).getValue();
try {
    this.lastPef = pefMeasuresRepository.getMeasures().get(pefMeasuresRepository.getMeasures().size() - 2).getValue();
}
catch (Exception e){
    this.lastPef=-1.0;
}
        this.percentPEFNormalValue= (measuredPEF/normalValuePEF)*100.00;
        showMeasuredPef();
        showNormalPef();
        showLastPefTextField();
        showDailyDiffenrcePercent();
        showDifferenceBeetwenNormalPEF();
        showDiffernceBeetenLastPef();
        showPercentBeetwenNormalPEF();
        showSfereColor();
        showResultArea();
    }

    private void showMeasuredPef(){
        actualMeasuredPefTextField.setText(String.valueOf(measuredPEF));
    }
    private void showNormalPef(){
        normalPefTextField.setText(String.valueOf(normalValuePEF));
        if(measuredPEF>normalValuePEF){
            sb.append("Wartość twojego pomiaru jest wyższa od wartości należytej. Jeśli pomiar został wykonany prawidłowo to doskonały wynik. \n");
        }
    }
    public void showPercentBeetwenNormalPEF(){

        Long result2=Math.round(percentPEFNormalValue); //Wartosc po zaokragleniu
        measuredNormalPefPercentTextField.setText(result2.toString()+"%");
    }
    public void showDifferenceBeetwenNormalPEF(){
        if(measuredPEF-normalValuePEF>0) {
            measuredNormalPefDifferenceTextField.setText("+" + String.valueOf(Math.round(measuredPEF - normalValuePEF)));
        }
        else{
            measuredNormalPefDifferenceTextField.setText(String.valueOf(Math.round(measuredPEF - normalValuePEF)));

        }
    }
    public void showLastPefTextField(){
        if(lastPef>0) {
            lastPefTextField.setText(String.valueOf(lastPef));
        }
        if(measuredPEF>lastPef&&lastPef!=-1){
            sb.append("Uzyskano lepszy wynik niż w poprzednim pomiarze \n");
        }
        else if(lastPef>measuredPEF)
        {
            sb.append("Uzyskano gorszy wynik niż w poprzednim pomiarze \n");
        }
    }
    public void showDiffernceBeetenLastPef(){
        if(lastPef>0) {
            if (measuredPEF - lastPef > 0) {
                measuredPefLastPefDiffernceTextField.setText("+" + String.valueOf(Math.round(measuredPEF - lastPef)));
            } else {
                measuredPefLastPefDiffernceTextField.setText(String.valueOf(Math.round(measuredPEF - lastPef)));
            }
        }
        else{
            measuredPefLastPefDiffernceTextField.setText("");
        }
    }
    public void showSfereColor(){
        if (percentPEFNormalValue > 80.0) {
            sfereColorTextField.setStyle("-fx-background-color:#22ff00");
            sb.append("Wartość twojego pomiaru należy do zielonej strefy co oznacza, że twoja astma jest kontrolowana w sposób prawidłowy. \n");
        }
        else if(percentPEFNormalValue<80.0 && percentPEFNormalValue>50.0){
            sfereColorTextField.setStyle("-fx-background-color:yellow");
            sb.append("Wartość twojego pomiaru należy do żóltej strefy co oznacza, że twoja astma jest słabo kontrolowana i powinieneś stosować się do zaleceń lekarza oraz korzystać z innych funkcji aplikacji by lepiej kontrolować chorobe. \n");
        }
        else if(percentPEFNormalValue<50.0){
            sfereColorTextField.setStyle("-fx-background-color:red");
            sb.append("Wartość twojego pomiaru należy do czerwonej strefy co oznacza, że twoja astma jest niekontrolowana i powinieneś skonsultować się z lekarzem \n");

        }
    }
    public void showDailyDiffenrcePercent() {
        String actualDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        System.out.println(actualDate);
        ArrayList<Double> sameDataValues = new ArrayList<>();
        System.out.println(pefMeasuresRepository.getMeasures().size());
        for (int i = pefMeasuresRepository.getMeasures().size()-1; i >= 0; i--) {
            System.out.println(pefMeasuresRepository.getMeasures().get(i).getDate());

            if (pefMeasuresRepository.getMeasures().get(i).getDate().startsWith(actualDate)) {
                sameDataValues.add(pefMeasuresRepository.getMeasures().get(i).getValue());
            } else {
                break;
            }
        }
        Collections.sort(sameDataValues);
        if (sameDataValues.size() > 1) {
            double result = ((sameDataValues.get(sameDataValues.size() - 1) - sameDataValues.get(0)) / sameDataValues.get(sameDataValues.size() - 1)) * 100.00;
            if(result>20.00){
                sb.append("Twoja zmienność dobowa przekracza 20% co może wskazywać na nieprawidłową kontrole choroby \n");
            }
            else {
                sb.append("Twoja zmienność dobowa jest prawidłowa ponieważ nie przekracza 20% \n");
            }
            dailyPefDifferncePercent.setText(String.valueOf(Math.round(result))+"%");
        }
        else {
            dailyPefDifferncePercent.setText("");
        }

    }
    void showResultArea(){
        resultsAreaTextArea.setText(sb.toString());
    }
}
