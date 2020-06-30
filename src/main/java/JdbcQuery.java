
import AnotherClasses.AddUserMeasureHelper;
import AnotherClasses.AgeFromStringDateHelper;
import AnotherClasses.MD5;
import AnotherClasses.RegisterHelper;
import BMI.CalculateMethods.BMICalculate;
import Objects.*;
import Objects.FromDB.*;
import Repositories.AlertEmailsRepository;
import Repositories.FromDB.PickflowmeterWearRepository;
import Repositories.UserRepository;
import Repositories.UserSettingsRepository;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import static java.util.Calendar.getInstance;


public class JdbcQuery {
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    private static Connection connection; //Polaczenie z baza danych
    public JdbcQuery(Jdbc jdbc) throws SQLException { //Laczymy sie z baza
        connection = jdbc.getConnection();
        // getMeasureFromUserListFromDataBase();
    }
    //Funkcja odpowaida za logowanie
    public boolean loginUser(TextField emailTextField, PasswordField passwordFromUser) throws SQLException {
        String email=emailTextField.getText();
        String password = MD5.getMD5Password(passwordFromUser.getText());
        String loginQuerySQL = "SELECT EMAIL,PASSWORD from users " +
                "WHERE EMAIL = '" + email + "' AND PASSWORD ='" + password + "'";
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(loginQuerySQL);
        if (resultSet.next()) {
            return true;
        }
        return false;
    }
    public User getActualUser(TextField emailTextField) throws SQLException {
        String email=emailTextField.getText();
        String getUserQuerySQL = "SELECT IDUSER,EMAIL,PASSWORD from users "+"WHERE EMAIL ='" + email + "'";
        Statement stmt = connection.createStatement();
        ResultSet resultSet =stmt.executeQuery(getUserQuerySQL);
        resultSet.next();
        return new User(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3));
    }
    public UserSettings getusersettingsTable(UserRepository userRepository) throws SQLException {
        AgeFromStringDateHelper ageFromStringDateHelper =new AgeFromStringDateHelper();
        String getUserSettingsQuerySQL = "SELECT * FROM usersettings "+"WHERE IDUSER = "+userRepository.getUser().getID();
        Statement stmt = connection.createStatement();
        ResultSet resultSet =stmt.executeQuery(getUserSettingsQuerySQL);
        resultSet.next();
        return new UserSettings(resultSet.getDouble(3),resultSet.getBoolean(4), ageFromStringDateHelper.getAgeFromStringDate(resultSet.getString(5)),resultSet.getString(6),resultSet.getBoolean(7),resultSet.getString(8),resultSet.getString(9),resultSet.getString(10),resultSet.getString(5));
    }
    //REJESTRACJA
    public boolean addNewUser(TextField emailTextField, PasswordField passwordFromUser) throws SQLException {
        String email = emailTextField.getText();
        String password = MD5.getMD5Password(passwordFromUser.getText());
        String addUserQuerySQL ="INSERT INTO users (EMAIL,PASSWORD) VALUES ('" + email+ "', '" + password + "')";
        Statement stmt =connection.createStatement();
        stmt.executeUpdate(addUserQuerySQL);
        return true;
    }
    public void addNewUserSettings(TextField registrationEmail ,TextField growthTextField, RadioButton menRadioButton,DatePicker birthDatePicker) throws SQLException {
        String alertValueDefault="Uwaga! \nNastąpił niespodziewany silny atak astmy, prosze o szybką pomoc.";
        String alertEmaillTitleDefault="Nastąpił atak astmy!";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate=birthDatePicker.getValue();
        String date=localDate.format(formatter);
        Double growth = (Double.parseDouble(growthTextField.getText()))/100;
        int isWhomen;
        if(menRadioButton.isSelected()){
            isWhomen =0;}
        else
        {
            isWhomen=1;
        }
        String addNewUserSettingsQuerySQL="INSERT INTO usersettings (IDUSER,GROWTH,ISWHOMEN,DATEOFBIRTH,ALERTVALUE,ISALERTWARNING,ALERTEMAILTITLE,BODYMASSFORMULE,PPMFORMULE) VALUES ("+getUserID(registrationEmail)+","+growth+","+isWhomen+",'"+date+"','"+alertValueDefault+"',1, '"+alertEmaillTitleDefault+"','L','M')";
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(addNewUserSettingsQuerySQL);
    }
    public void updateUserSettings(UserRepository userRepository,TextField growthTextField, RadioButton menRadioButton,DatePicker birthDatePicker,ComboBox<String>ppmMethod,ComboBox<String>normalBodyMassMethod){
        Double growth = (Double.parseDouble(growthTextField.getText()))/100;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate=birthDatePicker.getValue();
        int isWhomen;
        if(menRadioButton.isSelected()){
            isWhomen =0;}
        else
        {
            isWhomen=1;
        }
        String bodymass;
        String ppm;
        String date=localDate.format(formatter);
        if(normalBodyMassMethod.getSelectionModel().getSelectedItem().equals("wzór Broca")){
            bodymass="B";
        }
        else if (normalBodyMassMethod.getSelectionModel().getSelectedItem().equals("wzór Lorenza")){
            bodymass="L";
        }
        else {
            bodymass="P";
        }
        if(ppmMethod.getSelectionModel().getSelectedItem().equals("wzór Miffina"))
        {
            ppm="M";
        }
        else
        {
            ppm="H";
        }

        String changeUserPasswordQuerySQL = "UPDATE usersettings SET GROWTH="+growth+",DATEOFBIRTH="+date+",ISWHOMEN="+isWhomen+",BODYMASSFORMULE='"+bodymass+"',PPMFORMULE='"+ppm+"' WHERE IDUSEER="+userRepository.getUser().getID();
    }
    private int getUserID(TextField email) throws SQLException {
        String getUserIDQuerySQL="SELECT IDUSER FROM users WHERE EMAIL='"+email.getText()+"'";
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(getUserIDQuerySQL);
        resultSet.next();
        return resultSet.getInt(1);
    }
    //FUNKCJA SPRAWDZA CZY PODANY PRZY REJESTRACJI ADRES NIE DUBLUJE SIĘ
    public boolean isAccountAlreadyEmailInDataBase(String email) throws SQLException {
        String findEmailInDataBase = "SELECT EMAIL from users WHERE EMAIL = '" + email + "'";
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(findEmailInDataBase);
        if (resultSet.next()) {
            return true;
        }
        return false;
    }
    //ZMIANA HASLA UŻYTKOWNIKA
    public boolean changeUserPassword(TextField emailToPasswordChange,PasswordField passwordFieldToChange) throws SQLException {
        String password= MD5.getMD5Password(passwordFieldToChange.getText());
        String email=emailToPasswordChange.getText();
        String changeUserPasswordQuerySQL = "UPDATE users SET `PASSWORD`='" + password + "' WHERE email='" + email + "'";
        Statement stmt= connection.createStatement();
        stmt.executeUpdate(changeUserPasswordQuerySQL);
        return true;
    }
    public ArrayList<AlertEmail> getalertemailsTable(UserRepository userRepository) throws SQLException {
        ArrayList<AlertEmail> alertEmailsList=new ArrayList<>();
        String getAlertEmailsListQuerySQL="SELECT * FROM alertemails WHERE IDUSER="+userRepository.getUser().getID();
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(getAlertEmailsListQuerySQL);
        while(resultSet.next())
        {
            alertEmailsList.add(new AlertEmail(resultSet.getInt(1),resultSet.getString(3)));
        }
        return alertEmailsList;
    }
    public void addNewAlertEmail(UserRepository userRepository, AlertEmailsRepository alertEmailsRepository, TextField addEmailAlertTextField) throws SQLException {
            RegisterHelper registerHelper = new RegisterHelper();
            if (registerHelper.isEmail(addEmailAlertTextField) == true) {
                if(isAlertEmailAlreadyInDataBase(alertEmailsRepository,addEmailAlertTextField)==false) {
                    String addNewEmail = "INSERT INTO alertemails (IDUSER,EMAIL) VALUES (" + userRepository.getUser().getID() + ",'" + addEmailAlertTextField.getText() + "')";
                    Statement stmt = connection.createStatement();
                    stmt.executeUpdate(addNewEmail);
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Nie można dodać nowego adresu E-mail");
                    alert.setHeaderText("");
                    alert.setContentText("Taki adres jest już na liście");
                    alert.showAndWait();
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Nie można dodać nowego adresu E-mail");
                alert.setHeaderText("");
                alert.setContentText("Nie prawidłowy adres E-mail");
                alert.showAndWait();
            }
    }
    private boolean isAlertEmailAlreadyInDataBase(AlertEmailsRepository alertEmailsRepository,TextField addEmailAlertTextField){
        try {
            for (int i = 0; i < alertEmailsRepository.getEmailList().size(); i++) {
                if (addEmailAlertTextField.getText().equals(alertEmailsRepository.getEmailList().get(i))) {
                    return true;
                }
            }
        }
        catch (NullPointerException e){

        }
        return false;
    }
    public void removeSelectedAlertEmail(ListView<String>listOfEmails, UserRepository userRepository) throws SQLException {
        try {
            String removeSelectedAlertEmailQuerySql = "DELETE FROM alertemails WHERE IDUSER=" + userRepository.getUser().getID() + " AND EMAIL='" + listOfEmails.getSelectionModel().getSelectedItem() + "'";
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(removeSelectedAlertEmailQuerySql);
        }
        catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Nie można usunać adresu E-mail");
            alert.setHeaderText("");
            alert.setContentText("Nie zaznaczono adresu E-mail");
            alert.showAndWait();
        }
    }
    public void updateEmailAlertValue(TextArea alertValue) throws SQLException {

        if(!alertValue.getText().equals("")) {
            String updateEmailAlertValueQuerySQL = "UPDATE usersettings SET ALERTVALUE='" + alertValue.getText() + "'";
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(updateEmailAlertValueQuerySQL);
        }
    }
    public void changeLogedUserPassword(UserRepository userRepository,PasswordField oldPasswordField,PasswordField passwordFieldToChange,PasswordField passwordFieldConfirmedToChange,Label alertLabel) throws SQLException {
        RegisterHelper registerHelper=new RegisterHelper();
        if(registerHelper.isPasswordStrength(passwordFieldToChange,alertLabel)&&MD5.getMD5Password(oldPasswordField.getText()).equals(userRepository.getUser().getPassword())) {
            if (passwordFieldToChange.getText().equals(passwordFieldConfirmedToChange.getText())) {
                String password = MD5.getMD5Password(passwordFieldToChange.getText());
                String changeUserPasswordQuerySQL = "UPDATE users SET `PASSWORD`='" + password + "' WHERE email='" + userRepository.getUser().getEmail() + "'";
                Statement stmt = connection.createStatement();
                stmt.executeUpdate(changeUserPasswordQuerySQL);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Hasło zostało zmienione");
                alert.setHeaderText("");
                alert.setContentText("Hasło zmienione pomyślnie");
                alert.showAndWait();
                passwordFieldToChange.setText("");
                passwordFieldConfirmedToChange.setText("");
                oldPasswordField.setText("");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Nie można zmienić hasła");
                alert.setHeaderText("");
                alert.setContentText("Nowe hasła nie zgadzają się");
                alert.showAndWait();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Nie można zmienić hasła");
            alert.setHeaderText("");
            alert.setContentText("Wpisane stare hasło jest niepoprawne");
            alert.showAndWait();
        }
    }
    public void addNewPEFMeasure(UserRepository userRepository,TextField PEFValueFromUserTextField) throws SQLException {
        {
                String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm"));
                Double PEFValue = Double.parseDouble(PEFValueFromUserTextField.getText());
                String addPEFMeasureToDB = "INSERT INTO pefmeasures (IDUSER,PEFVALUE,DATE) VALUES(" + userRepository.getUser().getID() + "," + PEFValue + ",'" + date + "')";
                Statement stmt = connection.createStatement();
                stmt.executeUpdate(addPEFMeasureToDB);
        }
    }
    public ArrayList<Measure> getpefmeasuresTable(UserRepository userRepository) throws SQLException {
        ArrayList<Measure> MeasureMeasuresList =new ArrayList();
        String loadPEFMeasuresQuerySQL="SELECT * FROM pefmeasures where IDUSER="+userRepository.getUser().getID();
        Statement stmt = connection.createStatement();
        ResultSet resultSet =stmt.executeQuery(loadPEFMeasuresQuerySQL);
        while (resultSet.next()) {
            MeasureMeasuresList.add(new Measure(resultSet.getInt(1),resultSet.getInt(2),resultSet.getDouble(3),resultSet.getString(4)));
        }
        return MeasureMeasuresList;
    }
    public void removeAllPEFMeasures(UserRepository userRepository) throws SQLException {
        String deleteAllPEFMeasuresQuerySQL="DELETE FROM pefmeasures where IDUSER="+userRepository.getUser().getID();
        Statement stmt =connection.createStatement();
        stmt.executeUpdate(deleteAllPEFMeasuresQuerySQL);
    }
    public void removeSelectedPEFMeasure(TableView<Measure>PEFMeasurestableView) throws SQLException {
        String deleteAllPEFMeasuresQuerySQL="DELETE FROM pefmeasures WHERE ID="+PEFMeasurestableView.getSelectionModel().getSelectedItem().getId();
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(deleteAllPEFMeasuresQuerySQL);
    }

    //BMI

    public void addNewBmiMeasure(UserRepository userRepository, UserSettingsRepository userSettingsRepository, TextField BmiValueFromUserTextField) throws SQLException {
        {

                BMICalculate bmiCalculate = new BMICalculate();
                String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm"));
                Double BmiValue = bmiCalculate.calculateBMI(userSettingsRepository, Double.parseDouble(BmiValueFromUserTextField.getText()));
                String addPEFMeasureToDB = "INSERT INTO bmimeasures (IDUSER,BMIVALUE,DATE) VALUES(" + userRepository.getUser().getID() + "," + BmiValue + ",'" + date + "')";
                Statement stmt = connection.createStatement();
                stmt.executeUpdate(addPEFMeasureToDB);



        }
    }
    public ArrayList<Measure> getbmimeasuresTable(UserRepository userRepository) throws SQLException {
        ArrayList<Measure> MeasureMeasuresList =new ArrayList();
        String loadPEFMeasuresQuerySQL="SELECT * FROM bmimeasures where IDUSER="+userRepository.getUser().getID();
        Statement stmt = connection.createStatement();
        ResultSet resultSet =stmt.executeQuery(loadPEFMeasuresQuerySQL);
        while (resultSet.next()) {
            MeasureMeasuresList.add(new Measure(resultSet.getInt(1),resultSet.getInt(2),resultSet.getDouble(3),resultSet.getString(4)));
        }
        return MeasureMeasuresList;
    }
    public void removeAllBmiMeasures(UserRepository userRepository) throws SQLException {
        String deleteAllPEFMeasuresQuerySQL="DELETE FROM bmimeasures where IDUSER="+userRepository.getUser().getID();
        Statement stmt =connection.createStatement();
        stmt.executeUpdate(deleteAllPEFMeasuresQuerySQL);
    }
    public void removeSelectedBmiMeasure(TableView<Measure>BmiMeasurestableView) throws SQLException {
        String deleteAllPEFMeasuresQuerySQL="DELETE FROM bmimeasures WHERE ID="+BmiMeasurestableView.getSelectionModel().getSelectedItem().getId();
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(deleteAllPEFMeasuresQuerySQL);
    }

    //BPM
    public void addNewBpmMeasure(UserRepository userRepository, UserSettingsRepository userSettingsRepository, TextField BpmValueFromUserTextField) throws SQLException {
        {
              String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm"));
              String addPEFMeasureToDB = "INSERT INTO bpmmeasures (IDUSER,BMIVALUE,DATE) VALUES(" + userRepository.getUser().getID() + "," + BpmValueFromUserTextField.getText() + ",'" + date + "')";
              Statement stmt = connection.createStatement();
              stmt.executeUpdate(addPEFMeasureToDB);
        }
    }
    public ArrayList<Measure> getbpmmeasuresTable(UserRepository userRepository) throws SQLException {
        ArrayList<Measure> MeasureMeasuresList =new ArrayList();
        String loadPEFMeasuresQuerySQL="SELECT * FROM bpmmeasures where IDUSER="+userRepository.getUser().getID();
        Statement stmt = connection.createStatement();
        ResultSet resultSet =stmt.executeQuery(loadPEFMeasuresQuerySQL);
        while (resultSet.next()) {
            MeasureMeasuresList.add(new Measure(resultSet.getInt(1),resultSet.getInt(2),resultSet.getDouble(3),resultSet.getString(4)));
        }
        return MeasureMeasuresList;
    }
    public void removeAllBpmMeasures(UserRepository userRepository) throws SQLException {
        String deleteAllPEFMeasuresQuerySQL="DELETE FROM bpmmeasures where IDUSER="+userRepository.getUser().getID();
        Statement stmt =connection.createStatement();
        stmt.executeUpdate(deleteAllPEFMeasuresQuerySQL);
    }
    public void removeSelectedBpmMeasure(TableView<Measure>BpmMeasurestableView) throws SQLException {
        String deleteAllPEFMeasuresQuerySQL="DELETE FROM bpmmeasures WHERE ID="+BpmMeasurestableView.getSelectionModel().getSelectedItem().getId();
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(deleteAllPEFMeasuresQuerySQL);
    }
    //POBIERAMY DANE O ROSLINACH
    public ArrayList<DustyPlant> getdustyplantsTable() throws SQLException {
        ArrayList<DustyPlant>dustyPlants=new ArrayList<>();
        String getdustyplantsTableQuerySQL="SELECT * FROM dustyplants";
        Statement stmt =connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(getdustyplantsTableQuerySQL);
        while (resultSet.next()) {
            dustyPlants.add(new DustyPlant(resultSet.getString(2),resultSet.getInt(3),resultSet.getInt(4),resultSet.getInt(5),resultSet.getInt(6)));
        }
        return dustyPlants;
    }
    //POBIERAMY DANE O STACJACH OWM/USEROW/ObjectsForMapper
    public ArrayList<Station> getstationsTable(String nameOfTable) throws SQLException {
        //nameOfTable zawiera nazwe tabeli z jakiej chcemy pobrac stacje
        //userstations - stacje do pomiarow od użytkowników
        //giosstations - stacje ObjectsForMapper
        //owmstations -stacje OWM
        ArrayList<Station> stations =new ArrayList<>();
        String loadSettingsAboutActualUser="SELECT * FROM "+nameOfTable;
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(loadSettingsAboutActualUser);
        while (resultSet.next()) {
            stations.add(new Station(resultSet.getInt(1),resultSet.getString(2),resultSet.getDouble(3),resultSet.getDouble(4)));
        }
        return stations;
    }
    //POBIERAMY DANE O SENSORACH ObjectsForMapper
    public ArrayList<GIOSSensor> getGiosSensorsFromDataBase() throws SQLException {
        ArrayList<GIOSSensor>giosSensors=new ArrayList<>();
        String loadSettingsAboutActualUser="SELECT * FROM giossensors";
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(loadSettingsAboutActualUser);
        while (resultSet.next()) {
            giosSensors.add(new GIOSSensor(resultSet.getInt(2),resultSet.getInt(1),resultSet.getString(3),resultSet.getString(4)));
        }
        return giosSensors;
    }
    //POBIERAMY DANE TŁUMACZA ZACHMURZENIA
    //PL
    public ArrayList<String> getCloudinessTranslatorTablePLNames() throws SQLException {
        ArrayList<String>PLNames=new ArrayList<>();
        String loadSettingsAboutActualUser="SELECT PL FROM cloudinesstranslator";
        Statement stmt = connection.createStatement();
        ResultSet resultSet =  resultSet = stmt.executeQuery(loadSettingsAboutActualUser);
        while (resultSet.next()) {
            PLNames.add(resultSet.getString(1));
        }
        return PLNames;
    }
    //ENG
    public ArrayList<String> getCloudinessTranslatorTableENGNames() throws SQLException {
        ArrayList<String>ENGNames=new ArrayList<>();
        String loadSettingsAboutActualUser="SELECT ENG FROM cloudinesstranslator";
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(loadSettingsAboutActualUser);
        while (resultSet.next()) {
            ENGNames.add(resultSet.getString(1));
        }
        return ENGNames;
    }
    //DODANIE POMIARU PRZEZ UŻYTKOWNIKA
    public void addMeasuresFromUserToDataBase(AddUserMeasureHelper addUserMeasureHelper, TextField loginEmail, TextField pressureTextField, TextField temperatureTextField, TextField windTextField,
                                              TextField humidityTextField, ComboBox<String> cloudinessFromUserComboBox, int IDUserStation) throws SQLException {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date actualDate = getInstance().getTime();
        String date = dateFormat.format(actualDate);
        String temperature = temperatureTextField.getText();
        if(temperature.length()==0){
            temperature="NULL";
        }
        String windSpeed = windTextField.getText();
        if(windSpeed.length()==0){
            windSpeed="NULL";
        }
        String humidity = humidityTextField.getText();
        if(humidity.length()==0){
            humidity="NULL";
        }
        String claudiness = cloudinessFromUserComboBox.getSelectionModel().getSelectedItem();

        String pressure = pressureTextField.getText();
        if(pressure.length()==0){
            pressure="NULL";
        }
        int ID = IDUserStation;
        String addMeasureFromUserQuerySQL =
                "INSERT INTO measuresfromusers (DATE,IDUSER,TEMP,WINDSPEED,HUMIDITY,CLOUDINESS,PRESSURE,IDUSERSTATION) VALUES " +
                        "('" + date + "', '" + getUserID(loginEmail) + "', " + temperature + ", " + windSpeed + ", " + humidity +
                        ", '" + claudiness + "', " + pressure + ", " + ID + ")";
        if(claudiness.length()==0) {
             addMeasureFromUserQuerySQL =
                    "INSERT INTO measuresfromusers (DATE,IDUSER,TEMP,WINDSPEED,HUMIDITY,CLOUDINESS,PRESSURE,IDUSERSTATION) VALUES " +
                            "('" + date + "', '" + getUserID(loginEmail) + "', " + temperature + ", " + windSpeed + ", " + humidity +
                            ", NULL, " + pressure + ", " + ID + ")";
        }
        Statement stmt = connection.createStatement();
        if(addUserMeasureHelper.isNewMeasureOk(pressureTextField,temperatureTextField,windTextField,
                humidityTextField,cloudinessFromUserComboBox)==true)  {
            stmt.executeUpdate(addMeasureFromUserQuerySQL);
        }
    }
    //POBIERA DANE O TEMPERATURZE Z BAZY
    public ArrayList<TemperatureFromUser> getTemperaturesFromUserList() throws SQLException {
        ArrayList<TemperatureFromUser> temperaturesFromUserArrayList = new ArrayList();
        String takeTemperatureMeasuresQuerySQL = "SELECT * FROM measuresfromusers WHERE TEMP IS NOT NULL";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(takeTemperatureMeasuresQuerySQL);
        while (rs.next()){
            temperaturesFromUserArrayList.add(new TemperatureFromUser(rs.getString(4),
                    getEmailUser(rs.getInt(2)),
                    rs.getDouble(5),
                    rs.getInt(3)));
        }
        return temperaturesFromUserArrayList;
    }
    private String getEmailUser(int ID) throws SQLException {
        String takeTemperatureMeasuresQuerySQL = "SELECT EMAIL FROM users WHERE IDUSER="+ID;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(takeTemperatureMeasuresQuerySQL);
        rs.next();
        return rs.getString(1);
    }
    //POBIERA DANE O PREKOSCI WIATRU Z BAZY
    public ArrayList<WindSpeedFromUser> getWindSpeedFromUserList() throws SQLException {
        ArrayList<WindSpeedFromUser> windSpeedFromUserArrayList = new ArrayList();
        String takeWindSpeedMeasuresQuerySQL = "SELECT * FROM measuresfromusers WHERE WINDSPEED IS NOT NULL";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(takeWindSpeedMeasuresQuerySQL);
        while (rs.next()){
            windSpeedFromUserArrayList.add(new WindSpeedFromUser(rs.getString(4),
                    getEmailUser(rs.getInt(2)),
                    rs.getDouble(6),
                    rs.getInt(3)));
        }
        return windSpeedFromUserArrayList;
    }
    //POBIERA DANE O WILGOTNOSCI Z BAZY
    public ArrayList<HumidityFromUser> getHumidityFromUserList() throws SQLException {
        ArrayList<HumidityFromUser> humidityFromUserArrayList = new ArrayList();
        String takeHumidityMeasuresQuerySQL = "SELECT * FROM measuresfromusers WHERE HUMIDITY IS NOT NULL";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(takeHumidityMeasuresQuerySQL);
        while (rs.next()){
            humidityFromUserArrayList.add(new HumidityFromUser(rs.getString(4),
                    getEmailUser(rs.getInt(2)),
                    rs.getDouble(7),
                    rs.getInt(3)));
        }
        return humidityFromUserArrayList;
    }
    //POBIERA DANE O CISNIENIU Z BAZY
    public ArrayList<PressureFromUser> getPressureFromUserList() throws SQLException {
        ArrayList<PressureFromUser> pressureFromUserArrayList = new ArrayList();
        String takePressureMeasuresQuerySQL = "SELECT * FROM measuresfromusers WHERE PRESSURE IS NOT NULL";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(takePressureMeasuresQuerySQL);
        while (rs.next()){
            pressureFromUserArrayList.add(new PressureFromUser(rs.getString(4),
                    getEmailUser(rs.getInt(2)),
                    rs.getDouble(9),
                    rs.getInt(3)));
        }
        return pressureFromUserArrayList;
    }
    //POBIERA DANE O ZACHMURZENIU Z BAZY
    public ArrayList<ClaudinessFromUser> getCloudinessFromUserList() throws SQLException {
        ArrayList<ClaudinessFromUser> claudinessFromUserArrayList = new ArrayList();
        String takeClaudinessMeasuresQuerySQL = "SELECT * FROM measuresfromusers WHERE CLOUDINESS IS NOT NULL";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(takeClaudinessMeasuresQuerySQL);
        while (rs.next()){
            claudinessFromUserArrayList.add(new ClaudinessFromUser(rs.getString(4),
                    getEmailUser(rs.getInt(2)),
                    rs.getString(8),
                    rs.getInt(3)));
        }
        return claudinessFromUserArrayList;
    }
    public AppSettings getAppSettings() throws SQLException {
        String loadSettingsAboutActualUser="SELECT * FROM appsettings";
        Statement stmt =connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(loadSettingsAboutActualUser);
        resultSet.next();
        AppSettings appSettings=new AppSettings(resultSet.getDouble(3),resultSet.getDouble(2),resultSet.getDouble(5),resultSet.getDouble(4),resultSet.getDouble(6),resultSet.getDouble(7));
        return appSettings;
    }

    //PEF WEAR
    public PickflowmeterWear getpickflowmeterwearTable(UserRepository userRepository) throws SQLException {
        String loadSettingsAboutActualUser="SELECT * FROM pickflowmeterwear where IDUSER="+userRepository.getUser().getID();
        Statement stmt =connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(loadSettingsAboutActualUser);
        resultSet.next();
        try {

            return new PickflowmeterWear(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3));
        }
        catch (SQLException e){
            return null;
        }
    }
    public void addNewPickflowmeterWear(UserRepository userRepository, DatePicker purchaseDateDatePicker, PickflowmeterWearRepository pickflowmeterWearRepository) throws SQLException {
  if(pickflowmeterWearRepository.getPickflowmeterWear().getPurchaseDate()==null) {
      try {
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
          LocalDate localDate = purchaseDateDatePicker.getValue();
          String date = localDate.format(formatter);
          String addUserQuerySQL = "INSERT INTO pickflowmeterwear (IDUSER,DATEOFPURCHASE) VALUES (" + userRepository.getUser().getID() + ", '" + date + "')";
          Statement stmt = connection.createStatement();
          stmt.executeUpdate(addUserQuerySQL);
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setTitle("Sukces");
          alert.setContentText("Zapisano date zakupu urządzenia");
          alert.showAndWait();
      } catch (NullPointerException e) {
          alert.setTitle("Nie udało się zapisać daty");
          alert.setHeaderText("");
          alert.setContentText("Data zakupu urządzenia nie może być pusta");
          alert.showAndWait();
      }
  }
  else {
      try {
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
          LocalDate localDate = purchaseDateDatePicker.getValue();
          String date = localDate.format(formatter);
          String addUserQuerySQL = "UPDATE pickflowmeterwear SET DATEOFPURCHASE='" + date + "' WHERE IDUSER="+userRepository.getUser().getID();
          Statement stmt = connection.createStatement();
          stmt.executeUpdate(addUserQuerySQL);
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setTitle("Sukces");
          alert.setContentText("Zapisano date zakupu urządzenia");
          alert.showAndWait();
      } catch (NullPointerException e) {
          alert.setTitle("Nie udało się zapisać daty");
          alert.setHeaderText("");
          alert.setContentText("Data zakupu urządzenia nie może być pusta");
          alert.showAndWait();
      }



  }
    }

    public void addNewNotification(TextField medicineName, TextField medicineDose, TextField notificationTime, UserRepository userRepository) throws SQLException {
        String addUserQuerySQL = "INSERT INTO medicinesnotifications (IDUSER, MEDICNESNAME, MEDICINESDOSE, NOTIFICATIONTIME ) VALUES (" + userRepository.getUser().getID() + ", '" + medicineName.getText()+ "', "+medicineDose.getText()+ ", '"+ notificationTime.getText()+"')";
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(addUserQuerySQL);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sukces");
        alert.setContentText("Dodano nowe powiadomeinie o przyjęciu leków");
        alert.showAndWait();
    }
    public ArrayList<MedicinesNotification> getmedicinesnotificationsTable(UserRepository userRepository) throws SQLException {
        ArrayList<MedicinesNotification>medicinesNotificationArrayList=new ArrayList<>();
        String takeWindSpeedMeasuresQuerySQL = "SELECT * FROM medicinesnotifications  WHERE IDUSER="+userRepository.getUser().getID();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(takeWindSpeedMeasuresQuerySQL);
        while (rs.next()){
            medicinesNotificationArrayList.add(new MedicinesNotification(rs.getInt(1),rs.getInt(2),rs.getNString(3),rs.getDouble(4),rs.getString(5)));
        }
        return medicinesNotificationArrayList;
    }
    public void removeSelectedNotification(TableView<MedicinesNotification>medicinesNotificationTableView) throws SQLException {
      try {
          String removeSelectedAlertEmailQuerySql = "DELETE FROM medicinesnotifications WHERE IDNOTIFICATION=" + medicinesNotificationTableView.getSelectionModel().getSelectedItem().getiD();
          Statement stmt = connection.createStatement();
          stmt.executeUpdate(removeSelectedAlertEmailQuerySql);
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setTitle("Sukces");
          alert.setHeaderText("");
          alert.setContentText("Powiadomienie zostało usunięte");
          alert.showAndWait();
      }
      catch (NullPointerException e){
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Nie można usunąc powiadmienia");
          alert.setHeaderText("");
          alert.setContentText("Musisz zaznaczyć jedno z powiadomień");
          alert.showAndWait();
      }
    }

}
