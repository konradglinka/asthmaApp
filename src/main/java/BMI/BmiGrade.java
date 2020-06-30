package BMI;

import BMI.CalculateMethods.*;
import Repositories.BmiMeasuresRepository;
import Repositories.UserSettingsRepository;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class BmiGrade {
    StringBuilder sb=new StringBuilder();
    BmiMeasuresRepository bmiMeasuresRepository;
    UserSettingsRepository userSettingsRepository;
    RadioButton noActivity;
    RadioButton mediumActivity;
    RadioButton lowActivity;
    RadioButton highActivity;
    RadioButton veryHighActivity;
    private TextField actualMeasuredBodyMassTextField;
    private TextField normalBodyMassTextField;
    private TextField actualNormalBodyMassDiffernce;
    private TextField bmiTextField;
    private TextField bmrTextField;
    private TextField cpmTextField;
    private TextField watherTextField;
    private TextField lastBodyMassTextField;
    private TextField bodyMassDiffernce;
    private TextArea resultsAreaTextArea;
    private double measuredBodyMass;
    private double lastBmi;
    private double BMI;
    private  double PPM;
    private double CPM;
    private double phisicalActivity;
    private double normalBodyMass;
    private double wather;

    public BmiGrade(BmiMeasuresRepository bmiMeasuresRepository, UserSettingsRepository userSettingsRepository, RadioButton noActivity, RadioButton mediumActivity, RadioButton lowActivity, RadioButton highActivity, RadioButton veryHighActivity, TextField actualMeasuredBodyMassTextField, TextField normalBodyMassTextField, TextField actualNormalBodyMassDiffernce, TextField bmiTextField, TextField bmrTextField, TextField cpmTextField, TextField watherTextField, TextField lastBodyMassTextField, TextField bodyMassDiffernce, TextField bodyMass, TextArea resultsAreaTextArea) {
        this.bmiMeasuresRepository = bmiMeasuresRepository;
        this.userSettingsRepository = userSettingsRepository;
        this.noActivity = noActivity;
        this.mediumActivity = mediumActivity;
        this.lowActivity = lowActivity;
        this.highActivity = highActivity;
        this.veryHighActivity = veryHighActivity;
        this.actualMeasuredBodyMassTextField = actualMeasuredBodyMassTextField;
        this.normalBodyMassTextField = normalBodyMassTextField;
        this.actualNormalBodyMassDiffernce = actualNormalBodyMassDiffernce;
        this.bmiTextField = bmiTextField;
        this.bmrTextField = bmrTextField;
        this.cpmTextField = cpmTextField;
        this.watherTextField = watherTextField;
        this.lastBodyMassTextField = lastBodyMassTextField;
        this.bodyMassDiffernce = bodyMassDiffernce;
        this.resultsAreaTextArea = resultsAreaTextArea;
        this.BMI= bmiMeasuresRepository.getMeasures().get(bmiMeasuresRepository.getMeasures().size() - 1).getValue();
        setPhisicalActivity();
        try {
            this.lastBmi = bmiMeasuresRepository.getMeasures().get(bmiMeasuresRepository.getMeasures().size() - 2).getValue();
        }
        catch (Exception e){
            this.lastBmi =-1.0;
        }
        actualMeasuredBodyMassTextField.setText(bodyMass.getText());
        PpmCalculate ppmCalculate =new PpmCalculate();
        CPMCalculate cpmCalculate=new CPMCalculate();
        WatherCalculate watherCalculate=new WatherCalculate();
        NormalBodyMass normalbodyMass=new NormalBodyMass();
        this.measuredBodyMass=Double.parseDouble(bodyMass.getText());
        this.PPM = ppmCalculate.calculatePpm(measuredBodyMass,userSettingsRepository);
        this.CPM = cpmCalculate.calculateCpm(PPM,phisicalActivity);
        this.normalBodyMass=normalbodyMass.calculateNBM(userSettingsRepository);
        this.wather=watherCalculate.calculateWather(measuredBodyMass);
        showBMI();
        showPPM();
        showCPM();
        showLastBmiTextField();
        showNormalBodyMass();
        showDifferenceBodyMassNormalMass();
        showDifferceneBmi();
        showWather();
        showResultArea();

    }


    private void showBMI(){
        bmiTextField.setText(String.valueOf(BMI));
        if(BMI<16){
           sb.append("Twoj wskaznik BMI wskazuje na wyglodzenie");
        }
        if(BMI>16&&BMI<16.99){
            sb.append("Twoj wskaznik BMI wskazuje na wychudzenie");
        }
        if(BMI>17&&BMI<18.49){
            sb.append("Twoj wskaznik BMI wskazuje na, ze masz niedowage");
        }
        if(BMI>18.5&&BMI<24.99){
            sb.append("Twoj wskaznik BMI wskazuje na to, ze twoja waga jest prawidlowa");
        }
        if(BMI>25&&BMI<29.99){
            sb.append("Twoj wskaznik BMI wskazuje na to, ze masz nadwage ");
        }
        if(BMI>30&BMI<34.99){
            sb.append("Twoj wskaznik BMI wskazuje na to, ze masz otylosc 1 stopnia");
            if(BMI>35&BMI<39.99){
                sb.append("Twoj wskaznik BMI wskazuje na to, ze masz otylosc 2 stopnia");        }}
        if(BMI>40){
            sb.append("Twoj wskaznik BMI wskazuje na to, ze masz skrajna otylosc");        }


    }
    public void showPPM(){
        bmrTextField.setText(String.valueOf(PPM));
    }
    public void showCPM(){
        cpmTextField.setText(String.valueOf(CPM));
    }
    public void showLastBmiTextField(){
      lastBodyMassTextField.setText(String.valueOf(lastBmi));
    }
    public void showDifferceneBmi(){
        if(BMI- lastBmi >0) {
            bodyMassDiffernce.setText(String.valueOf("+" + (Math.round((BMI - lastBmi)* 100.00)  ) / 100.00));
        }
        else {
            bodyMassDiffernce.setText(String.valueOf((Math.round((BMI - lastBmi)* 100.00) ) / 100.00));
        }
    }
    public void showNormalBodyMass(){
        normalBodyMassTextField.setText(String.valueOf(normalBodyMass));
    }
    private void showDifferenceBodyMassNormalMass(){
        if(measuredBodyMass-normalBodyMass>0) {
            actualNormalBodyMassDiffernce.setText("+"+String.valueOf((Math.round((measuredBodyMass - normalBodyMass)* 100.00) ) / 100.00));
        }
        else
        {
            actualNormalBodyMassDiffernce.setText(String.valueOf((Math.round((measuredBodyMass - normalBodyMass) * 100.00) ) / 100.00));
        }
        }
    private void showWather(){
        watherTextField.setText(String.valueOf(wather));
    }

    void showResultArea(){
        resultsAreaTextArea.setText(sb.toString());
    }
    public void setPhisicalActivity() {
        //Ustawiamy aktywnosc fizyczna uzytkownika na podstawie dostepnych dla niego radiobuttonow
        if (noActivity.isSelected()) { //Uzytkownik okresla to radiobutonem ktory zawiera tekst jednak odpowiednikami
            // tych tekstow sa konkretne wartosci liczbowe
            this.phisicalActivity = 1.2;
        } else if (lowActivity.isSelected()) {
            this.phisicalActivity = 1.4;
        } else if (mediumActivity.isSelected()) {
            this.phisicalActivity = 1.6;
        } else if (highActivity.isSelected()) {
            this.phisicalActivity = 1.8;
        } else if (veryHighActivity.isSelected()) {
            this.phisicalActivity = 2.2;
        }
    }
}
