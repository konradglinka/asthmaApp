package BPM;

import Repositories.UserSettingsRepository;

public class NormalBpm {
    public double calculateNormalBpm(UserSettingsRepository userSettingsRepository){
        if(userSettingsRepository.getUserSettings().getAge()<=1.0) {
            return 130.0;
        }
        else if (userSettingsRepository.getUserSettings().getAge()>=1.0 && userSettingsRepository.getUserSettings().getAge()<11.0){
            return 100.0;
        }
        else if (userSettingsRepository.getUserSettings().getAge()>=11.0 && userSettingsRepository.getUserSettings().getAge()<21.0){
            return 85.0;
        }
        else if (userSettingsRepository.getUserSettings().getAge()>=21.0 && userSettingsRepository.getUserSettings().getAge()<60.0) {
            return 70.0;
        }
        else {
            return 60.0;
        }
    }
}
