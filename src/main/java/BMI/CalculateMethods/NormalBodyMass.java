package BMI.CalculateMethods;

import Repositories.UserSettingsRepository;

public class NormalBodyMass {
    public double calculateNBM(UserSettingsRepository userSettingsRepository){
        if(userSettingsRepository.getUserSettings().getBodyMassFormule().equals("B")){ //Wzor Broca
            return userSettingsRepository.getUserSettings().getGrowth()*100.00-100.00;
        }
        else if(userSettingsRepository.getUserSettings().getBodyMassFormule().equals("L")){ //Wzor Lorenza
            if(userSettingsRepository.getUserSettings().isWhomen()==true){
                return Math.round(userSettingsRepository.getUserSettings().getGrowth()*100.00-100.00 -((userSettingsRepository.getUserSettings().getGrowth()*100.00-150.00))/2.00);
            }
            else{
                return Math.round(userSettingsRepository.getUserSettings().getGrowth()*100.00-100.00 -((userSettingsRepository.getUserSettings().getGrowth()*100.00-150.00))/4.00);
            }
        }
        else { //Wzor Pottona
            if(userSettingsRepository.getUserSettings().isWhomen()==true){
                return Math.round(userSettingsRepository.getUserSettings().getGrowth()*100.00-100.00 -((userSettingsRepository.getUserSettings().getGrowth()*100.00-100.00))/10.00);
            }
            else {
                return Math.round(userSettingsRepository.getUserSettings().getGrowth()*100.00-100.00 -((userSettingsRepository.getUserSettings().getGrowth()*100.00-100.00))/20.00);
            }
        }
    }
}
