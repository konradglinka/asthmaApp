package AnotherClasses;

import Repositories.FromDB.AppSettingsRepository;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class AddUserMeasureHelper {

    private StringBuilder stringBuilderWithAlertToLabel = new StringBuilder(); //Zawiera komunikat o nieprawidłowym pomiarze
    private AppSettingsRepository appSettingsRepository; //Przechowuje aktualne ustawienia wartości granicznych
    private Alert alert = new Alert(Alert.AlertType.WARNING); //Alert pokazujacy komunikat o nieprawidłowym pomiarze

    public AddUserMeasureHelper(AppSettingsRepository appSettingsRepository) {
        alert.setTitle("Nie można dodać nowego pomiaru");
        alert.setHeaderText("");
        this.appSettingsRepository = appSettingsRepository;
    }

    public boolean isNewMeasureOk(TextField pressureTextField, TextField temperatureTextField, TextField windTextField,
                                  TextField humidityTextField, ComboBox<String> claudinessFromIserComboBox) {
        boolean temperature = isTemperatureValueCorrect(temperatureTextField);
        boolean windSpeed = isWindSpeedValueCorrect(windTextField);
        boolean pressure = isPressureValueCorrect(pressureTextField);
        boolean humidity = isHumidityValueCorrect(humidityTextField);
        if (isAllValuesEmpty(pressureTextField, temperatureTextField, windTextField, humidityTextField, claudinessFromIserComboBox) == true) { //Nie można dodać pustego pomiaru
            stringBuilderWithAlertToLabel.append("Nie można dodać pomiaru bez danych");
            alert.setContentText(stringBuilderWithAlertToLabel.toString());
            alert.showAndWait();
            stringBuilderWithAlertToLabel.delete(0, stringBuilderWithAlertToLabel.length());
            return false;
        }
        if (temperature == true && windSpeed == true && pressure == true && humidity == true) //Można dodać pomiar tylko gdy wszystkie wartości są prawidłowe
        {
            stringBuilderWithAlertToLabel.delete(0, stringBuilderWithAlertToLabel.length());
            return true; //Jeśli  przeszło weryfikacje  dodajemy pomiar
        } else {
            alert.setContentText(stringBuilderWithAlertToLabel.toString());
            alert.showAndWait();
            stringBuilderWithAlertToLabel.delete(0, stringBuilderWithAlertToLabel.length());
            return false; //Jeśli nie przeszło weryfikacji nie dodajemy pomiaru i wyświetlamy alert
        }
    }

    private boolean isAllValuesEmpty(TextField pressureTextField, TextField temperatureTextField, TextField windTextField,
                                     TextField humidityTextField, ComboBox<String> claudinessFromUserComboBox) {
        if (temperatureTextField.getText().length() == 0 && windTextField.getText().length() == 0 && pressureTextField.getText().length() == 0 && humidityTextField.getText().length() == 0 && claudinessFromUserComboBox.getSelectionModel().getSelectedItem().equals("")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isTemperatureValueCorrect(TextField temperatureTextField) {
        if (temperatureTextField.getText().equals("")) {
            temperatureTextField.setStyle("");
            return true;
        }
        Double temperature = Double.parseDouble(temperatureTextField.getText());
        if (temperature >= appSettingsRepository.getAppSettings().getMinTemperature() && temperature <= appSettingsRepository.getAppSettings().getMaxTemperature()) {
            temperatureTextField.setStyle("");
            return true;
        } else {
            temperatureTextField.setStyle("-fx-background-color:red;");
            stringBuilderWithAlertToLabel.append("Podana temperatura nie mieści się w wartościach granicznych (" + appSettingsRepository.getAppSettings().getMinTemperature() + " - " + appSettingsRepository.getAppSettings().getMaxTemperature() + ")\n");
            return false;
        }
    }

    private boolean isWindSpeedValueCorrect(TextField windSpeedTextField) {
        if (windSpeedTextField.getText().equals("")) {
            windSpeedTextField.setStyle("");
            return true;
        }
        Double windSpeed = Double.parseDouble(windSpeedTextField.getText());
        if (windSpeed >= appSettingsRepository.getAppSettings().getMinWindSpeed() && windSpeed <= appSettingsRepository.getAppSettings().getMaxWindSpeed()) {
            windSpeedTextField.setStyle("");
            return true;
        } else {
            windSpeedTextField.setStyle("-fx-background-color:red;");
            stringBuilderWithAlertToLabel.append("Podana prędkość wiatru nie mieści się w wartościach granicznych (" + appSettingsRepository.getAppSettings().getMinWindSpeed() + " - " + appSettingsRepository.getAppSettings().getMaxWindSpeed() + ")\n");
            return false;
        }
    }

    private boolean isPressureValueCorrect(TextField pressureTextField) {
        if (pressureTextField.getText().equals("")) {
            pressureTextField.setStyle("");
            return true;
        }
        Double pressure = Double.parseDouble(pressureTextField.getText());
        if (pressure >= appSettingsRepository.getAppSettings().getMinPressure() && pressure <= appSettingsRepository.getAppSettings().getMaxPressure()) {
            pressureTextField.setStyle("");
            return true;
        } else {
            pressureTextField.setStyle("-fx-background-color:red;");
            stringBuilderWithAlertToLabel.append("Podane ciśnienie powietrza nie mieści się w wartościach granicznych (" + appSettingsRepository.getAppSettings().getMinPressure() + " - " + appSettingsRepository.getAppSettings().getMaxPressure() + ")\n");
            return false;
        }
    }

    private boolean isHumidityValueCorrect(TextField humidityTextField) {
        if (humidityTextField.getText().equals("")) {
            humidityTextField.setStyle("");
            return true;
        }
        Double humidity = Double.parseDouble(humidityTextField.getText());
        if (humidity >= appSettingsRepository.getAppSettings().getMinHumidity() && humidity <= appSettingsRepository.getAppSettings().getMaxHumidity()) {
            humidityTextField.setStyle("");
            return true;
        } else {
            humidityTextField.setStyle("-fx-background-color:red;");
            stringBuilderWithAlertToLabel.append("Podana wilgotność musi mieścić się w przedziale procentowym od 0 do 100\n");
            return false;
        }
    }
}
