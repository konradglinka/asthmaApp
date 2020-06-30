package BMI.CalculateMethods;

import Repositories.UserSettingsRepository;
public class PpmCalculate { //Klasa zajmuje sie wyliczaniem wartosci BMR na podstawie danych podanych przez uzytkownika

    //Obliczanie podstawowego przemiany materii kalorycznego (BMR) metodą Mifflin-St Jeor
    public double calculatePpm(double bodyMass, UserSettingsRepository userSettingsRepository) { //Metoda wylicza PPM
        if (userSettingsRepository.getUserSettings().getPpmMassFormule().equals("M")) {
            if (userSettingsRepository.getUserSettings().isWhomen() == false) {
                return Math.round(((9.99 * bodyMass) + (6.25 * (userSettingsRepository.getUserSettings().getGrowth() * 100.00)) - (4.92 * userSettingsRepository.getUserSettings().getAge()) + 5.0) * 100.0) / 100.0;
            } else {
                return Math.round(((9.99 * bodyMass) + (6.25 * (userSettingsRepository.getUserSettings().getGrowth() * 100.00)) - (4.92 * userSettingsRepository.getUserSettings().getAge()) - 161.0) * 100.0) / 100.0;
            }
        } else { //Metoda Harisa Bendeicta
            if (userSettingsRepository.getUserSettings().isWhomen() == false) {
                return Math.round(66.47 + (13.47 * bodyMass) + (5 * userSettingsRepository.getUserSettings().getGrowth() * 100) - (6.76 * userSettingsRepository.getUserSettings().getAge()));
            }
            else{
                    return Math.round(655.1+(9.567*bodyMass)+(1.85*userSettingsRepository.getUserSettings().getGrowth()*100)-(4.68*userSettingsRepository.getUserSettings().getAge()));
                }
        }
    }
}
