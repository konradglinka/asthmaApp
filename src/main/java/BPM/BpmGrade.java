package BPM;

import Repositories.BpmMeasuresRepository;
import Repositories.UserSettingsRepository;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class BpmGrade {
   private StringBuilder sb=new StringBuilder();
   private BpmMeasuresRepository bpmMeasuresRepository;
    private UserSettingsRepository userSettingsRepository;
   private TextField actualMeasuredBpmTextField;
   private TextField normalBpmTextField;
   private TextField actualNormalBpmDiffernce;
   private TextField lastActualBpmDifferenceTextField;
   private TextField lastBpmTextField;
private TextArea resultsBpmTextArea;
   double bpm;
   double lastbpm;
   double normalbpm;
    public BpmGrade(BpmMeasuresRepository bpmMeasuresRepository, UserSettingsRepository userSettingsRepository, TextField actualMeasuredBpmTextField, TextField normalBpmTextField, TextField actualNormalBpmDiffernce, TextField lastActualBpmDifferenceTextField, TextField lastBpmTextField,TextArea resultsBpmTextArea) {
        this.bpmMeasuresRepository = bpmMeasuresRepository;
        this.userSettingsRepository = userSettingsRepository;
        this.actualMeasuredBpmTextField = actualMeasuredBpmTextField;
        this.normalBpmTextField = normalBpmTextField;
        this.actualNormalBpmDiffernce = actualNormalBpmDiffernce;
        this.lastActualBpmDifferenceTextField = lastActualBpmDifferenceTextField;
        this.lastBpmTextField = lastBpmTextField;
        this.resultsBpmTextArea=resultsBpmTextArea;
        bpm=bpmMeasuresRepository.getMeasures().get(bpmMeasuresRepository.getMeasures().size() - 1).getValue();
        try {
            this.lastbpm = bpmMeasuresRepository.getMeasures().get(bpmMeasuresRepository.getMeasures().size() - 2).getValue();
        }
        catch (Exception e){
            this.lastbpm =-1.0;
        }
        NormalBpm normalBpm=new NormalBpm();
        normalbpm=normalBpm.calculateNormalBpm(userSettingsRepository);
        showResultsAboutBpm();
        showActualBpm();
        showNormalBpm();
        showLastBpm();
        showDiffernceLastActualBpm();
        showDiffernceNormalActualBpm();

    }
    private void showActualBpm(){
        actualMeasuredBpmTextField.setText(String.valueOf(bpm));
    }

    private void showNormalBpm(){
        normalBpmTextField.setText(String.valueOf(normalbpm));
    }

    private void showLastBpm(){
            lastBpmTextField.setText(String.valueOf(lastbpm));
    }
    private void showDiffernceLastActualBpm(){
        if(bpm-lastbpm>0) {
            sb.append("Wartość twojego pulsu jest wyższa niż ostatnio \n");
            lastActualBpmDifferenceTextField.setText("+" + String.valueOf(Math.round(bpm - lastbpm)));
        }
        else{
            sb.append("Wartość twojego pulsu jest niższa niż ostatnio \n");
            lastActualBpmDifferenceTextField.setText(String.valueOf(Math.round(bpm-lastbpm)));
        }
    }
    private void showDiffernceNormalActualBpm(){
       if (bpm-normalbpm>0){

            actualNormalBpmDiffernce.setText("+"+String.valueOf(Math.round(bpm - normalbpm)));
        }
       else
       {

           actualNormalBpmDiffernce.setText("+"+String.valueOf(Math.round(bpm - normalbpm)));
       }

    }
    private void showResultsAboutBpm(){
    if(bpm>100){
        sb.append("Twój puls przekracza 100 uderzeń na sekunde i jest zbyt wysoki. Taki stan to tachykardia. \n");
    }
    if(bpm<60){
        sb.append("Twój puls jest niższy niż 60 uderzeń na sekunde i jest zbyt niski. Taki stan to bradykardia. \n");
    }
    else{
        sb.append("Podana przez Ciebie wartość pulsu jest prawidłowa \n");
    }
    resultsBpmTextArea.setText(sb.toString());
    }
}
