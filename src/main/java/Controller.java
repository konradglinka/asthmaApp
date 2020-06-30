import AnotherClasses.*;
import BMI.BmiGrade;
import BPM.BpmGrade;
import EmailActions.EmailToAlarm;
import EmailActions.EmailToRegister;
import EmailActions.EmailToResetPassword;
import OWM.WeatherMeasuresFactory;
import Objects.FromDB.*;
import Objects.Measure;
import Objects.SensorData;
import Objects.WeatherMeasureOWM;
import ObjectsForMapper.GIOSAirIndex.AirIndexLevel;
import PEF.PefGrade;
import Presenters.DataFromGiosSensorPresenter;
import Presenters.DataFromGiosStationPresenter;
import Presenters.DataFromOwmPresenter;
import Repositories.*;
import Repositories.FromDB.*;
import ViewControl.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;


import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import javafx.application.Platform;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.json.JSONException;


import static java.util.Calendar.getInstance;
public class Controller {
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    Thread timerThread;
    DrawChartsHelper drawChartsHelper;
    PickflowmeterWearRepository pickflowmeterWearRepository;
    AppSettingsRepository appSettingsRepository;
    DustyPlantsRepository dustyPlantsRepository;
    OWMStationsRepository owmStationsRepository;
    UserStationsRepository usersStationsRepository;
    GIOSStationsRepository giosStationsRepository;
    GIOSSensorsRepository giosSensorsRepository;
    OWMClaudinesTranslatorRepository owmClaudinesTranslatorRepository;
    UserMeasuresPLNamesRepository userMeasuresPLNamesRepository;
    MedicinesNotificationsRepository medicinesNotificationsRepository;
    IDStationFinder idStationFinder=new IDStationFinder();
    BmiMeasuresRepository bmiMeasuresRepository;
    BpmMeasuresRepository bpmMeasuresRepository;
    PefMeasuresRepository pefMeasuresRepository;
    UserRepository userRepository;
    UserSettingsRepository userSettingsRepository;
    AlertEmailsRepository alertEmailsRepository;
    String resetPasswordCode;
    String registerCode;
    RegisterHelper registerHelper = new RegisterHelper();
    TextFieldRestrict textFieldRestrict = new TextFieldRestrict();
    Jdbc jdbc = new Jdbc(); //Do połączenia z baza
    JdbcQuery jdbcQuery; //Do wykonywania zapytań do bazy
    UserMeasuresRepository userMeasuresRepository = new UserMeasuresRepository(); //Do tworzenia tabel z pomiarami dla wybranych miast
    StationBrowser stationBrowser=new StationBrowser();
    WeatherMeasuresFactory weatherMeasuresFactory;
    //BPM
    @FXML
    TextField bpmTextField;
    @FXML
    TextField actualMeasuredBpmTextField;
    @FXML
    TextField normalBpmTextField;
    @FXML
    TextField actualNormalBpmDiffernce;
    @FXML
    TextField lastActualBpmDifferenceTextField;
    @FXML
    TextField lastBpmTextField;
    @FXML
    TextArea resultsBpmTextArea;
    @FXML
    private TextField howManyBpmMeasuresTextField;
    @FXML
    private TableView<Measure> bpmMeasureTableView;
    @FXML
    private TableColumn<Measure, Integer> bpmMeasureIdColumn;
    @FXML
    private TableColumn<Measure, Integer> bpmMeasureIdUserColumn;
    @FXML
    private TableColumn<Measure, Double> bpmMeasureValueColumn;
    @FXML
    private TableColumn<Measure, String> bpmMeasureDateColumn;
    @FXML
    LineChart lineChartOfBmi;
    @FXML
    BarChart barChartOfBmi;

    //
    //ELEMENTY OKNA Z ROŚLINAMI PYLĄCYMI
    @FXML
    ListView actualDustyPlantsListView;
    //ELEMENTY DODANIA POMIARU PRZEZ UŻYTKOWNIKA
    @FXML
    ListView<String> usersStationsToAddMeasureListView;
    @FXML
    TextField addTemperatureFromUserTextField;
    @FXML
    TextField addHumidityFromUserTextField;
    @FXML
    TextField addWindSpeedFromUserTextField;
    @FXML
    TextField addPressureFromUserTextField;
    //ELEMENTY POBRANIA POMIARU PRZEZ UŻYTKOWNIKA
    @FXML
    ListView<String> usersStationsToTakeMaeasureListView;
    @FXML
    ComboBox<String> takeMeasuresFromUserComboBox;  //wybór rodzaju pomiaru
    //TEMPERATURA POWIETRZA
    @FXML
    TableView<TemperatureFromUser> temperatureTableView;
    @FXML
    TableColumn<TemperatureFromUser, String> dateTempUser;
    @FXML
    TableColumn<TemperatureFromUser, String> userNameTemp;
    @FXML
    TableColumn<TemperatureFromUser, Double> temperature;
    //PREDKOŚĆ WIATRU
    @FXML
    TableView<WindSpeedFromUser> windSpeedTableView;
    @FXML
    TableColumn<WindSpeedFromUser, String> dateWindSpeedUser;
    @FXML
    TableColumn<WindSpeedFromUser, String> userNameWindSpeed;
    @FXML
    TableColumn<WindSpeedFromUser, Double> windSpeed;
    //WILGOTNOŚĆ POWIETRZA
    @FXML
    TableView<HumidityFromUser> humidityTableView;
    @FXML
    TableColumn<HumidityFromUser, String> dateHumidityUser;
    @FXML
    TableColumn<HumidityFromUser, String> userNameHumidity;
    @FXML
    TableColumn<HumidityFromUser, Double> humidity;
    //CIŚNIENIE POWIETRZA
    @FXML
    TableView<PressureFromUser> pressureTableView;
    @FXML
    TableColumn<PressureFromUser, String> datePressureUser;
    @FXML
    TableColumn<PressureFromUser, String> userNamePressure;
    @FXML
    TableColumn<PressureFromUser, Double> pressure;
    //ZACHMURZENIE
    @FXML
    TableView<ClaudinessFromUser> claudinessTableView;
    @FXML
    TableColumn<ClaudinessFromUser, String> dateClaudinessUser;
    @FXML
    TableColumn<ClaudinessFromUser, String> userNameClaudiness;
    @FXML
    TableColumn<ClaudinessFromUser, String> claudiness;
    //OWM
    @FXML
    ListView<String> OWMStationsListView;
    @FXML
    TextField OWMStationsBrowserTextField;
    @FXML
    TableView<WeatherMeasureOWM> measuresFromOWMTableView;
    @FXML
    TableColumn<WeatherMeasureOWM, Double> tempColumn;
    @FXML
    TableColumn<WeatherMeasureOWM, Double> pressureColumn;
    @FXML
    TableColumn<WeatherMeasureOWM, Double> windColumn;
    @FXML
    TableColumn<WeatherMeasureOWM, Double> humidityColumn;
    @FXML
    TableColumn<WeatherMeasureOWM, String> claudinessColumn;
    @FXML
    TableColumn<WeatherMeasureOWM, String> dateOWMColumn;



    @FXML
    TextField takeMeasureFromUserBrowserTextField;
    @FXML
    TextField addMesureFromUserBrowserTextField;
    @FXML
    ComboBox<String> claudinessFromIserComboBox;


    //gios
    @FXML
    ListView<String> sensorsListView;
    @FXML
    TableView<SensorData> GIOSTableView;
    @FXML
    TableColumn<SensorData, String> GIOSValueColumn;
    @FXML
    TableColumn<SensorData, String> GIOSDateColumn;

    @FXML
    TableColumn<AirIndexLevel,String>GIOSAirIndexValueColumn;
    @FXML
    TableColumn<AirIndexLevel,String>GIOSAirIndexNameColumn;
    @FXML
    TableView<AirIndexLevel>GIOSAirIndexTableView;
    @FXML
    TextField GIOSStationsBrowserTextField;
    @FXML
    ListView<String>GIOSStationsListView;






    //
    @FXML
    StackPane registerAndLoginStackPane;
    //LOGOWANIE
    @FXML
    VBox loginVBox;
    @FXML
    TextField loginEmailTextField;
    @FXML
    PasswordField passwordPasswordField;
    //REJESTRACJA
    @FXML
    VBox registerVBox;
    @FXML
    TextField registrationEmailTextField;
    @FXML
    PasswordField registrationPasswordPasswordField;
    @FXML
    PasswordField registrationConfirmedPasswordPasswordField;
    @FXML
    DatePicker dateOfBirthDatePicker;
    @FXML
    TextField growthTextField;
    @FXML
    RadioButton menRadioButton;
    @FXML
    Label badEmailOrPasswordLabel;
    @FXML
    Label registrationAlertLabel;
    //ELEMENTY GŁÓWNEGO OKNA APLIKACJI
    @FXML
    HBox mainViewHBox;

    //RESETOWANIE HASŁA
    @FXML
    VBox changePassword1VBox;
    @FXML
    VBox changePassword2VBox;
    @FXML
    VBox changePassword3VBox;
    @FXML
    TextField emailToResetPasswordTextField;
    @FXML
    TextField resetCodeTextField;
    @FXML
    PasswordField resetPasswordPasswordField;
    @FXML
    PasswordField conifrmedResetPasswordPasswordField;
    @FXML
    Label badPasswordLabel;
    @FXML
    VBox register2VBox;
    @FXML
    TextField registrationCodeTextField;
    @FXML
    Label badEmailResetPasswordLabel;
    @FXML
    Label badCodeResetPasswordLabel;
    @FXML
    Label registrationCodeAlertLabel;
    //RESET CODE
    @FXML
    Button sendResetPasswordCodeButton;
    @FXML
    Button sendRegistrationCodeButton;
    @FXML
    private TabPane TabPanePEF;
    @FXML
    private TabPane TabPaneBMI;
    @FXML
    private TabPane TabPaneBPM;
    @FXML
    private TabPane TabPaneAir;
    @FXML
    private TabPane TabPaneSettings;
    @FXML
    private TabPane TabPaneAlarm;
    @FXML
    Button TabPanePEFButton;
    @FXML
    Button TabPaneBMIButton;
    @FXML
    Button TabPaneBPMButton;
    @FXML
    Button TabPaneAirButton;
    @FXML
    Button TabPaneSettingsButton;
    @FXML
    Button TabPaneAlarmButton;

    @FXML
    private Label TimeLabel;
    @FXML
    private Label DateLabel;
    //ALERTEMAILS

    @FXML
    TextField addNewAlertEmailTextField;
    @FXML
    ListView<String> alertEmailsListView;
    @FXML
    TextArea alertValueTextArea;
    @FXML
    Button sendAlertEmailsButton;
    @FXML
    TextField alertTitleTextField;
    @FXML
    ListView<String> alertEmailLookListView;

    //SETTINGS
    @FXML
    PasswordField confirmedNewPasswordPasswordField;
    @FXML
    PasswordField newPasswordPasswordField;
    @FXML
    PasswordField oldPasswordPasswordField;
    @FXML
    Label passwordAlertLabel;
    //PEF
    @FXML
    private TextField measuredPefTextField;
    @FXML
    private TextField actualMeasuredPefTextField;
    @FXML
    private TextField normalPefTextField;
    @FXML
    private TextField measuredNormalPefDifferenceTextField;
    @FXML
    private TextField measuredNormalPefPercentTextField;
    @FXML
    private TextField lastPefTextField;
    @FXML
    private TextField measuredPefLastPefDiffernceTextField;
    @FXML
    private TextField dailyPefDifferncePercent;
    @FXML
    private TextField sfereColorTextField;
    @FXML
    private TextArea resultsAreaTextArea;
    @FXML
    private TableView<Measure> pefMeasureTableView;
    @FXML
    private TableColumn<Measure, Integer> pefMeasuresIdColumn;
    @FXML
    private TableColumn<Measure, Integer> pefMeasuresIdUserColumn;
    @FXML
    private TableColumn<Measure, Double> pefMeasuresValueColumn;
    @FXML
    private TableColumn<Measure, String> pefMeasuresDateColumn;
    @FXML
    private TextField howManyPefMeasuresTextField;
    @FXML
    LineChart lineChartOfPEF;
    @FXML
    BarChart barChartOfPEF;
    //PEF WEAR
    @FXML
    private DatePicker pickflowmeterPurdhaseDateDatePicker;
    @FXML
    private TextField newPickflowmeterDays;
    //BMI
    @FXML
    private TextField howManyBmiMeasuresTextField;

    @FXML
    private TableView<Measure> bmiMeasureTableView;
    @FXML
    private TableColumn<Measure, Integer> bmiMeasureIdColumn;
    @FXML
    private TableColumn<Measure, Integer> bmiMeasureIdUserColumn;
    @FXML
    private TableColumn<Measure, Double> bmiMeasureValueColumn;
    @FXML
    private TableColumn<Measure, String> bmiMeasureDateColumn;
    @FXML
    LineChart lineChartOfBpm;
    @FXML
    BarChart barChartOfBpm;
    @FXML
    RadioButton noActivity;
    @FXML
    RadioButton mediumActivity;
    @FXML
    RadioButton lowActivity;
    @FXML
    RadioButton highActivity;
    @FXML
    RadioButton veryHighActivity;
    @FXML
    private TextField actualMeasuredBodyMassTextField;
    @FXML
    private TextField normalBodyMassTextField;
    @FXML
    private TextField actualNormalBodyMassDiffernce;
    @FXML
    private TextField bmiTextField;
    @FXML
    private TextField ppmTextField;
    @FXML
    private TextField cpmTextField;
    @FXML
    private TextField watherTextField;
    @FXML
    private TextField lastBodyMassTextField;
    @FXML
    private TextField bodyMassDiffernce;
    @FXML
    private TextArea resultsBmiTextArea;
    @FXML
    TextField bodymassTextField;

    //SETTINGS
    @FXML
    DatePicker settingsBirthDatePicker;
    @FXML
    TextField settingsGrowthTextField;
    @FXML
    RadioButton settingsWhomenButton;
    @FXML
    RadioButton settingsMenButton;
    @FXML
    ComboBox<String>ppmMethodsComboBox;
    @FXML
    ComboBox<String>normalBodyMassMethodsComboBox;
    //MEDICINES
    @FXML
    TabPane medicinesTabPane;
    @FXML
    TextField noificationTimeTextField;
    @FXML
    Button  medicinesTabPaneButton;
    @FXML
    TextField medicinesDoseTextField;
    @FXML
    TextField medicinesNameTextField;
    @FXML
    TableView<MedicinesNotification>notificationsTableView;
    @FXML
    TableColumn<MedicinesNotification,String> medicineNameColumn;
    @FXML
    TableColumn<MedicinesNotification,Double> medicineDoseColumn;
    @FXML
    TableColumn<MedicinesNotification,String> notificationTimeColumn;
    @FXML
    void initialize() throws SQLException, IOException, InterruptedException {
        showDateTime();
        try {
            DatePickerHelper datePickerHelper=new DatePickerHelper();
            datePickerHelper.disableFutureDatesInDatePicker(pickflowmeterPurdhaseDateDatePicker);
            startConectionWithDataBase();
            activateRestrictionsOnTextFields();
            normalBodyMassMethodsComboBox.getItems().add("wzór Broca");
            normalBodyMassMethodsComboBox.getItems().add("wzór Lorenza");
            normalBodyMassMethodsComboBox.getItems().add("wzór Pottona");
            ppmMethodsComboBox.getItems().add("wzór Mifflina");
            ppmMethodsComboBox.getItems().add("wzór Harisa-Benedicta");
            takeDataFromDBToRepositories();
            addDataFromRepositioriesToView();
            activeStationsBrowsers();
            deletePlaceholders();
        }
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nie udało się połączyć z serverem");
            alert.setHeaderText("Nie udało się uzyskać połączenia z serverem");
            alert.setContentText("Sprawdż stan łącza internetowego lub skontaktuj się z administratorem systemu");
            ButtonType buttonTypeOne = new ButtonType("Wyjdz z systemu");
            ButtonType buttonTypeTwo = new ButtonType("Sprobuj ponownie");
            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOne){
                onClickLogoutOrExit();
            }
            else if(result.get()==buttonTypeTwo){
                    initialize();
            }

        }

    }
    private void activateRestrictionsOnTextFields(){
        textFieldRestrict.onlyTimeInTextField(noificationTimeTextField);
        textFieldRestrict.limitCharsForTextField(howManyPefMeasuresTextField,3);
        textFieldRestrict.onlyIntegerInTextField(howManyPefMeasuresTextField);
        textFieldRestrict.limitCharsForTextField(howManyBpmMeasuresTextField,3);
        textFieldRestrict.onlyIntegerInTextField(howManyBpmMeasuresTextField);
        textFieldRestrict.limitCharsForTextField(measuredPefTextField,7);
        textFieldRestrict.onlyPlusDigitsInTextField(measuredPefTextField);
        textFieldRestrict.limitCharsForTextField(bodymassTextField,7);
        textFieldRestrict.onlyPlusDigitsInTextField(bodymassTextField);
        textFieldRestrict.onlyIntegerInTextField(bmiTextField);
        textFieldRestrict.limitCharsForTextField(bmiTextField,4);
        textFieldRestrict.limitCharsForTextField(addMesureFromUserBrowserTextField,40);
        textFieldRestrict.limitCharsForTextField(OWMStationsBrowserTextField,40);
        textFieldRestrict.limitCharsForTextField(takeMeasureFromUserBrowserTextField,40);
        textFieldRestrict.limitCharsForTextField(GIOSStationsBrowserTextField,40);
        textFieldRestrict.onlyTextInTextField(addMesureFromUserBrowserTextField);
        textFieldRestrict.onlyTextInTextField(takeMeasureFromUserBrowserTextField);
        textFieldRestrict.onlyTextInTextField(OWMStationsBrowserTextField);
        textFieldRestrict.onlyTextInTextField(GIOSStationsBrowserTextField);
        textFieldRestrict.onlyDigitsInTextField(addTemperatureFromUserTextField);
        textFieldRestrict.onlyPlusDigitsInTextField(addHumidityFromUserTextField);
        textFieldRestrict.onlyPlusDigitsInTextField(addWindSpeedFromUserTextField);
        textFieldRestrict.onlyPlusDigitsInTextField(addPressureFromUserTextField);
        textFieldRestrict.limitCharsForTextField(addPressureFromUserTextField,8);
        textFieldRestrict.limitCharsForTextField(addWindSpeedFromUserTextField,8);
        textFieldRestrict.limitCharsForTextField(addHumidityFromUserTextField,8);
        textFieldRestrict.limitCharsForTextField(addTemperatureFromUserTextField,8);
    }
    private void activeStationsBrowsers(){
        stationBrowser.searchByNameOnWriteInTextField(OWMStationsBrowserTextField, owmStationsRepository.getStationNames(),OWMStationsListView);
        stationBrowser.searchByNameOnWriteInTextField(addMesureFromUserBrowserTextField, usersStationsRepository.getStationNames(), usersStationsToAddMeasureListView);
        stationBrowser.searchByNameOnWriteInTextField(takeMeasureFromUserBrowserTextField, usersStationsRepository.getStationNames(), usersStationsToTakeMaeasureListView);
        stationBrowser.searchByNameOnWriteInTextField(GIOSStationsBrowserTextField,giosStationsRepository.getStationNames(),GIOSStationsListView);
    }
    private void deletePlaceholders(){
        Label emptyLabel=new Label("");
        claudinessTableView.setPlaceholder(emptyLabel);
        temperatureTableView.setPlaceholder(emptyLabel);
        pressureTableView.setPlaceholder(emptyLabel);
        windSpeedTableView.setPlaceholder(emptyLabel);
        humidityTableView.setPlaceholder(emptyLabel);
        GIOSAirIndexTableView.setPlaceholder(emptyLabel);
        GIOSTableView.setPlaceholder(emptyLabel);
        measuresFromOWMTableView.setPlaceholder(emptyLabel);
        bmiMeasureTableView.setPlaceholder(emptyLabel);
        pefMeasureTableView.setPlaceholder(emptyLabel);
        bpmMeasureTableView.setPlaceholder(emptyLabel);

    }
    private void startConectionWithDataBase() throws SQLException { //Połączenie aplikacji z bazą danych
        jdbc.getDbConnection();
        jdbcQuery = new JdbcQuery(jdbc);
    }
    private void takeDataFromDBToRepositories() throws SQLException {
        appSettingsRepository =new AppSettingsRepository(jdbcQuery.getAppSettings());
        dustyPlantsRepository=new DustyPlantsRepository( jdbcQuery.getdustyplantsTable());
        owmStationsRepository = new OWMStationsRepository(jdbcQuery.getstationsTable("owmstations"));
        usersStationsRepository =new UserStationsRepository(jdbcQuery.getstationsTable("usersstations"));
        giosStationsRepository=new GIOSStationsRepository(jdbcQuery.getstationsTable("giosstations"));
        giosSensorsRepository=new GIOSSensorsRepository(jdbcQuery.getGiosSensorsFromDataBase());
        owmClaudinesTranslatorRepository=new OWMClaudinesTranslatorRepository(jdbcQuery.getCloudinessTranslatorTableENGNames(), jdbcQuery.getCloudinessTranslatorTablePLNames());
        userMeasuresPLNamesRepository=new UserMeasuresPLNamesRepository();
    }
    private void addDataFromRepositioriesToView(){
        ActualDustyPlantsView actualDustyPlantsView = new ActualDustyPlantsView(actualDustyPlantsListView,dustyPlantsRepository);
        OWMStationsListView.getItems().addAll(owmStationsRepository.getStationNames());
        usersStationsToAddMeasureListView.getItems().addAll(usersStationsRepository.getStationNames());
        usersStationsToTakeMaeasureListView.getItems().addAll(usersStationsRepository.getStationNames());
        GIOSStationsListView.getItems().addAll(giosStationsRepository.getStationNames());
        takeMeasuresFromUserComboBox.getItems().addAll(userMeasuresPLNamesRepository.getNamesOfMeasuresArraylist());
        claudinessFromIserComboBox.getItems().add(""); //Mozliwe dodanie pomiaru bez danych o zachmurzeniu
        claudinessFromIserComboBox.getSelectionModel().select(0); //Pomiar bez danych, a pusty ComboBox to
        // co innego wiec domyślnie wybrany jest bez danych
        claudinessFromIserComboBox.getItems().addAll(owmClaudinesTranslatorRepository.getPolishStringArraylist());
        takeMeasuresFromUserComboBox.getSelectionModel().select(0); //Wartość domyślna do pobrania z danych od
        // userów by combobox nie był pusty

    }
    private void showDateTime() {
        Date date = getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat.format(date);
        DateLabel.setText(strDate);
        timerThread = new Thread(() -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            while (true) {
                try {
                    Thread.sleep(1000); //1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final String time = simpleDateFormat.format(new Date());
                Platform.runLater(() -> {
                    TimeLabel.setText(time);
                });
            }
        });
        timerThread.start();
    }

    public void invisibleAllTabPanesInStackPane() {
        TabPanePEF.setVisible(false);
        TabPanePEFButton.setStyle(" -fx-background-color: linear-gradient(#61a2ff, #2A5058);");
        TabPaneBMI.setVisible(false);
        TabPaneBMIButton.setStyle(" -fx-background-color: linear-gradient(#61a2ff, #2A5058);");
        TabPaneBPM.setVisible(false);
        TabPaneBPMButton.setStyle(" -fx-background-color: linear-gradient(#61a2ff, #2A5058);");
        TabPaneAir.setVisible(false);
        TabPaneAirButton.setStyle(" -fx-background-color: linear-gradient(#61a2ff, #2A5058);");
        TabPaneAlarm.setVisible(false);
        TabPaneAlarmButton.setStyle(" -fx-background-color: linear-gradient(#61a2ff, #2A5058);");
        TabPaneSettings.setVisible(false);
        TabPaneSettingsButton.setStyle(" -fx-background-color: linear-gradient(#61a2ff, #2A5058);");
        medicinesTabPane.setVisible(false);
        medicinesTabPaneButton.setStyle(" -fx-background-color: linear-gradient(#61a2ff, #2A5058);");
    }

    @FXML
    public void onClickShowPEFTabPane() {
        invisibleAllTabPanesInStackPane();
        TabPanePEF.setVisible(true);
        TabPanePEFButton.setStyle(" -fx-background-color: linear-gradient(#aa98a9, #2a3439);");
    }

    @FXML
    public void onClickShowBMITabPane() {
        invisibleAllTabPanesInStackPane();
        TabPaneBMI.setVisible(true);
        TabPaneBMIButton.setStyle(" -fx-background-color: linear-gradient(#aa98a9, #2a3439);");
    }

    @FXML
    public void onClickShowBPMTabPane() {
        invisibleAllTabPanesInStackPane();
        TabPaneBPM.setVisible(true);
        TabPaneBPMButton.setStyle(" -fx-background-color: linear-gradient(#aa98a9, #2a3439);");

    }

    @FXML
    public void onClickShowAirTabPane() {
        invisibleAllTabPanesInStackPane();
        TabPaneAir.setVisible(true);
        TabPaneAirButton.setStyle(" -fx-background-color: linear-gradient(#aa98a9, #2a3439);");

    }

    @FXML
    public void onClickShowAlarmTabPane() {
        invisibleAllTabPanesInStackPane();
        TabPaneAlarm.setVisible(true);
        TabPaneAlarmButton.setStyle(" -fx-background-color: linear-gradient(#aa98a9, #2a3439);");

    }

    @FXML
    public void onClickShowSettingsTabPane() {
        invisibleAllTabPanesInStackPane();
        TabPaneSettings.setVisible(true);
        TabPaneSettings.setStyle(" -fx-background-color: linear-gradient(#aa98a9, #2a3439);");

    }
    @FXML
    public void onClickShowMedicinesTabPane(){
        invisibleAllTabPanesInStackPane();
        medicinesTabPane.setVisible(true);
        medicinesTabPaneButton.setStyle(" -fx-background-color: linear-gradient(#aa98a9, #2a3439);");
    }



    @FXML
    void onClickSendAlertEmailsButton() {
        EmailToAlarm emailToAlarm = new EmailToAlarm(userRepository, userSettingsRepository, sendAlertEmailsButton, alertEmailsRepository);
        Thread emailAlertsThread = new Thread(emailToAlarm);
        emailAlertsThread.start();
    }

    //FUNKCJE DOTYCZĄCE LOGOWANIA I REJESTRACJI
    @FXML
    void loginButton() throws SQLException { //Funkcja zajmuje się uwierzytelnianiem i przełącza na główny ekran aplikacji
        if (jdbcQuery.loginUser(loginEmailTextField, passwordPasswordField) == true) {
            registerAndLoginStackPane.setVisible(false);
            mainViewHBox.setVisible(true);
            userRepository = new UserRepository(jdbcQuery.getActualUser(loginEmailTextField));
            userSettingsRepository = new UserSettingsRepository(jdbcQuery.getusersettingsTable(userRepository));
            pickflowmeterWearRepository=new PickflowmeterWearRepository(jdbcQuery.getpickflowmeterwearTable(userRepository));
            if(pickflowmeterWearRepository.getPickflowmeterWear().getPurchaseDate()!=null);
            {
                pickflowmeterPurdhaseDateDatePicker.setValue(LocalDate.parse(pickflowmeterWearRepository.getPickflowmeterWear().getPurchaseDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            }

            newPickflowmeterDays.setText(String.valueOf((int) ChronoUnit.DAYS.between(LocalDate.now(),LocalDate.parse(pickflowmeterWearRepository.getPickflowmeterWear().getPurchaseDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")).plusDays(1095))));
            showAlertEmails();
            alertValueTextArea.setText(userSettingsRepository.getUserSettings().getAlertValue());
            alertTitleTextField.setText(userSettingsRepository.getUserSettings().getAlertEmailTitle());
            showPefMeasures();
            showBmiMeasures();
            showUserSettings();
            showBpmMeasures();
            showMedicinesNotifications();
        } else if (jdbcQuery.loginUser(loginEmailTextField, passwordPasswordField) == false) {
            badEmailOrPasswordLabel.setVisible(true);
        }
    }

    @FXML
    void switchOnRegisterButton() { //Funkcja przełącza na okienko do rejestracji
        loginVBox.setVisible(false);
        registerVBox.setVisible(true);
        registrationAlertLabel.setVisible(false);
    }

    @FXML
    void sendResetPasswordCode() {

        EmailToResetPassword emailToResetPassword = new EmailToResetPassword(emailToResetPasswordTextField, sendResetPasswordCodeButton);
        if (registerHelper.isEmail(emailToResetPasswordTextField) == true) {
            Thread threadResetPasswordEmail = new Thread(emailToResetPassword);
            threadResetPasswordEmail.start();
            changePassword1VBox.setVisible(false);
            changePassword2VBox.setVisible(true);
            resetPasswordCode = emailToResetPassword.getResetPasswordCode();
            badCodeResetPasswordLabel.setVisible(false);
        } else {
            badEmailResetPasswordLabel.setVisible(true);
        }
    }

    @FXML
    void sendResetPasswordCodeAgain() {
        EmailToResetPassword emailToResetPassword = new EmailToResetPassword(emailToResetPasswordTextField, sendResetPasswordCodeButton);
        Thread threadResetPasswordEmail = new Thread(emailToResetPassword);
        threadResetPasswordEmail.start();
        changePassword1VBox.setVisible(false);
        changePassword2VBox.setVisible(true);
        resetPasswordCode = emailToResetPassword.getResetPasswordCode();
        badCodeResetPasswordLabel.setVisible(false);
    }

    @FXML
    void goToChangePasswordAfterEnterCodeButton() {
        if (resetCodeTextField.getText().equals(resetPasswordCode)) {
            changePassword2VBox.setVisible(false);
            changePassword3VBox.setVisible(true);
        } else {
            badCodeResetPasswordLabel.setVisible(true);
        }
    }

    @FXML
    void switchOnForgotPasswordButton() {
        loginVBox.setVisible(false);
        changePassword1VBox.setVisible(true);
        badEmailResetPasswordLabel.setVisible(false);
    }

    @FXML
    void changePasswordButton() throws SQLException {
        if (registerHelper.isPasswordStrength(resetPasswordPasswordField, badPasswordLabel) == true) {
            if (resetPasswordPasswordField.getText().equals(conifrmedResetPasswordPasswordField.getText())) {
                if (jdbcQuery.changeUserPassword(emailToResetPasswordTextField, resetPasswordPasswordField) == true) {
                    changePassword3VBox.setVisible(false);
                    loginVBox.setVisible(true);
                }
            } else
                badPasswordLabel.setVisible(true);
            badPasswordLabel.setText("Hasła nie są jednakowe");
        }
    }

    @FXML
    void registerButton() throws SQLException { //Funkcja dodaje nowego użytkownika do bazy i przechodzi do logowania
        if (registerCode.equals(registrationCodeTextField.getText())) {
            if (jdbcQuery.addNewUser(registrationEmailTextField, registrationPasswordPasswordField) == true) {
                jdbcQuery.addNewUserSettings(registrationEmailTextField, growthTextField, menRadioButton, dateOfBirthDatePicker);
                loginVBox.setVisible(true);
                register2VBox.setVisible(false);
            }
        } else {
            registrationCodeAlertLabel.setVisible(true);
            registrationCodeAlertLabel.setText("Nie prawidłowy kod");
        }
    }

    @FXML
    void sendRegistrationCode() throws SQLException {
        if (registerHelper.isEmail(registrationEmailTextField) == true
                && registerHelper.isPasswordStrength(registrationPasswordPasswordField, registrationAlertLabel) == true
                && jdbcQuery.isAccountAlreadyEmailInDataBase(registrationEmailTextField.getText()) == false
                && registrationPasswordPasswordField.getText().equals(registrationConfirmedPasswordPasswordField.getText()) == true) {
            registerVBox.setVisible(false);
            register2VBox.setVisible(true);
            EmailToRegister emailToRegister = new EmailToRegister(registrationEmailTextField, sendRegistrationCodeButton);
            Thread threadRegisterEmail = new Thread(emailToRegister);
            threadRegisterEmail.start();
            registerCode = emailToRegister.getRegistrationCode();
            registrationCodeAlertLabel.setVisible(false);
        } else if (registerHelper.isEmail(registrationEmailTextField) == false) {
            registrationAlertLabel.setText("Nie prawidłowy adres E-mail");
            registrationAlertLabel.setVisible(true);
        } else if (jdbcQuery.isAccountAlreadyEmailInDataBase(registrationEmailTextField.getText()) == true) {
            registrationAlertLabel.setText("Konto jest już zarejstrowane");
            registrationAlertLabel.setVisible(true);
        } else if (registerHelper.isPasswordStrength(registrationPasswordPasswordField, registrationAlertLabel) == false) {
        } else if (!registrationPasswordPasswordField.equals(registrationConfirmedPasswordPasswordField)) {
            registrationAlertLabel.setText("Hasła nie są jednakowe");
            registrationAlertLabel.setVisible(true);
        }

    }

    @FXML
    void sendResetRegistrationCodeAgain() {
        registerVBox.setVisible(false);
        register2VBox.setVisible(true);
        EmailToRegister emailToRegister = new EmailToRegister(registrationEmailTextField, sendRegistrationCodeButton);
        Thread threadRegisterEmail = new Thread(emailToRegister);
        threadRegisterEmail.start();
        registerCode = emailToRegister.getRegistrationCode();
        registrationCodeAlertLabel.setVisible(false);
    }

    @FXML
    void switchOnLoginButton() {
        changePassword1VBox.setVisible(false);
        changePassword2VBox.setVisible(false);
        changePassword3VBox.setVisible(false);
        registerVBox.setVisible(false);
        register2VBox.setVisible(false);
        badEmailOrPasswordLabel.setVisible(false);
        loginVBox.setVisible(true);
    }

    private void showAlertEmails() throws SQLException {
        alertEmailsListView.getItems().clear();
        alertEmailLookListView.getItems().clear();
        alertEmailsRepository = new AlertEmailsRepository(jdbcQuery.getalertemailsTable(userRepository));
        alertEmailsListView.getItems().addAll(alertEmailsRepository.getEmailList());
        alertEmailLookListView.getItems().addAll(alertEmailsRepository.getEmailList());
    }

    //Emailalerts
    @FXML
    void onClickAddNewAlertEmail() throws SQLException {
        jdbcQuery.addNewAlertEmail(userRepository, alertEmailsRepository, addNewAlertEmailTextField);
        showAlertEmails();
    }

    @FXML
    void onClickRemoveSelectedAlertEmail() throws SQLException {
        jdbcQuery.removeSelectedAlertEmail(alertEmailsListView, userRepository);
        showAlertEmails();
    }

    @FXML
    void onClickSaveAlertEmailValue() throws SQLException {
        jdbcQuery.updateEmailAlertValue(alertValueTextArea);
        userSettingsRepository = new UserSettingsRepository(jdbcQuery.getusersettingsTable(userRepository));
        alertValueTextArea.setText(userSettingsRepository.getUserSettings().getAlertValue());
    }


    //settings
    @FXML
    void onClickChangeAlreadyLogedUserPassword() throws SQLException {
        jdbcQuery.changeLogedUserPassword(userRepository, oldPasswordPasswordField, newPasswordPasswordField, confirmedNewPasswordPasswordField, passwordAlertLabel);

    }

    //PEF
    @FXML
    void onClickAddNewPEFMeasure() throws SQLException {
        if(!measuredPefTextField.getText().equals("")) {
            jdbcQuery.addNewPEFMeasure(userRepository, measuredPefTextField);
            showPefMeasures();
            new PefGrade(pefMeasuresRepository, userSettingsRepository, actualMeasuredPefTextField, normalPefTextField, measuredNormalPefDifferenceTextField, measuredNormalPefPercentTextField, lastPefTextField, measuredPefLastPefDiffernceTextField, dailyPefDifferncePercent, sfereColorTextField, resultsAreaTextArea);
            measuredPefTextField.setText("");
        }
        else{
                alert.setTitle("Nie można dodać pomiaru");
                alert.setHeaderText("");
                alert.setContentText("Wartość maksymalnego przepływu wydechowego nie może być pusta");
                alert.showAndWait();
        }
    }
    @FXML
    void onClickRemoveSelectedPefMeasure() throws SQLException {
        jdbcQuery.removeSelectedPEFMeasure(pefMeasureTableView);
        showPefMeasures();
    }
    @FXML
    void onClickRemoveAllPefMeasures() throws SQLException {
    jdbcQuery.removeAllPEFMeasures(userRepository);
    showPefMeasures();
    }
    private void showPefMeasures() throws SQLException {
        pefMeasuresRepository = new PefMeasuresRepository(jdbcQuery.getpefmeasuresTable(userRepository));
        new PefMeasuresTableView(pefMeasureTableView, pefMeasuresIdColumn, pefMeasuresIdUserColumn, pefMeasuresValueColumn, pefMeasuresDateColumn, pefMeasuresRepository.getMeasures());
    }
    @FXML
    void onClickDrawPEFLineChart() {
        drawChartsHelper=new DrawChartsHelper(howManyPefMeasuresTextField,barChartOfPEF,lineChartOfPEF);
        barChartOfPEF.setVisible(false);
        lineChartOfPEF.setVisible(true);
        drawChartsHelper.drawLineChart(pefMeasuresRepository.getMeasures());
    }
    @FXML
    void onClickDrawPEFBarChart() {
        drawChartsHelper=new DrawChartsHelper(howManyPefMeasuresTextField,barChartOfPEF,lineChartOfPEF);
        barChartOfPEF.setVisible(true);
        lineChartOfPEF.setVisible(false);
        drawChartsHelper.drawBarChart(pefMeasuresRepository.getMeasures());
    }
    //NOTIFICATIONS
    private  void showMedicinesNotifications() throws SQLException {
        medicinesNotificationsRepository=new MedicinesNotificationsRepository(jdbcQuery.getmedicinesnotificationsTable(userRepository));
        new MedicinesNotificationsTableView(notificationsTableView,medicineNameColumn,medicineDoseColumn,notificationTimeColumn,medicinesNotificationsRepository.getMedicinesNotificationArrayList());
    }

    //BMI
    private void showBmiMeasures() throws SQLException {
        bmiMeasuresRepository = new BmiMeasuresRepository(jdbcQuery.getbmimeasuresTable(userRepository));
        new BmiMeasuresTableView(bmiMeasureTableView, bmiMeasureIdColumn, bmiMeasureIdUserColumn, bmiMeasureValueColumn, bmiMeasureDateColumn, bmiMeasuresRepository.getMeasures());
    }

    @FXML
    void onClickAddNewBmiMeasure() throws SQLException {
        if(!bodymassTextField.getText().equals("")){
        jdbcQuery.addNewBmiMeasure(userRepository,userSettingsRepository, bodymassTextField);
        showBmiMeasures();
            new BmiGrade(bmiMeasuresRepository, userSettingsRepository, noActivity, mediumActivity, lowActivity, highActivity, veryHighActivity, actualMeasuredBodyMassTextField, normalBodyMassTextField, actualNormalBodyMassDiffernce, bmiTextField, ppmTextField, cpmTextField, watherTextField, lastBodyMassTextField, bodyMassDiffernce, bodymassTextField, resultsBmiTextArea);
            bodymassTextField.setText("");
        }
        else{
                alert.setTitle("Nie można dodać pomiaru");
                alert.setHeaderText("");
                alert.setContentText("Wartość masy ciała nie może być pusta");
                alert.showAndWait();
        }

    }
    @FXML
    void onClickRemoveSelectedBmiMeasure() throws SQLException {
        jdbcQuery.removeSelectedBmiMeasure(bmiMeasureTableView);
        showBmiMeasures();
    }
    @FXML
    void onClickRemoveAllBmiMeasures() throws SQLException {
        jdbcQuery.removeAllBmiMeasures(userRepository);
        showBmiMeasures();
    }

    @FXML
    void onClickDrawBmiLineChart() {
        drawChartsHelper=new DrawChartsHelper(howManyBmiMeasuresTextField,barChartOfBmi,lineChartOfBmi);
        barChartOfBmi.setVisible(false);
        lineChartOfBmi.setVisible(true);
        drawChartsHelper.drawLineChart(bmiMeasuresRepository.getMeasures());
    }
    @FXML
    void onClickDrawBmiBarChart() {
        drawChartsHelper=new DrawChartsHelper(howManyBmiMeasuresTextField,barChartOfBmi,lineChartOfBmi);
        barChartOfBmi.setVisible(true);
        lineChartOfBmi.setVisible(false);
        drawChartsHelper.drawBarChart(bmiMeasuresRepository.getMeasures());
    }

    //BPM

    private void showBpmMeasures() throws SQLException {
        bpmMeasuresRepository = new BpmMeasuresRepository(jdbcQuery.getbpmmeasuresTable(userRepository));
        new BpmMeasuresTableView(bpmMeasureTableView, bpmMeasureIdColumn, bpmMeasureIdUserColumn, bpmMeasureValueColumn, bpmMeasureDateColumn, bpmMeasuresRepository.getMeasures());
    }
    @FXML
    void onClickAddNewBpmMeasure() throws SQLException {
        if(!bpmTextField.getText().equals("")) {
        jdbcQuery.addNewBpmMeasure(userRepository,userSettingsRepository, bpmTextField);
        showBpmMeasures();

            new BpmGrade(bpmMeasuresRepository, userSettingsRepository, actualMeasuredBpmTextField, normalBpmTextField, actualNormalBpmDiffernce, lastActualBpmDifferenceTextField, lastBpmTextField, resultsBpmTextArea);
            bpmTextField.setText("");
        }
        else {
            alert.setTitle("Nie można dodać pomiaru");
            alert.setHeaderText("");
            alert.setContentText("Wartość pulsu nie może być pusta");
            alert.showAndWait();
        }

    }
    @FXML
    void onClickRemoveSelectedBpmMeasure() throws SQLException {
        jdbcQuery.removeSelectedBpmMeasure(bpmMeasureTableView);
        showBpmMeasures();
    }
    @FXML
    void onClickRemoveAllBpmMeasures() throws SQLException {
        jdbcQuery.removeAllBpmMeasures(userRepository);
        showBpmMeasures();
    }
    @FXML
    void onClickDrawBpmLineChart() {
        drawChartsHelper=new DrawChartsHelper(howManyBpmMeasuresTextField,barChartOfBpm,lineChartOfBpm);
        barChartOfBpm.setVisible(false);
        lineChartOfBpm.setVisible(true);
        drawChartsHelper.drawLineChart(bpmMeasuresRepository.getMeasures());
    }
    @FXML
    void onClickDrawBpmBarChart() {
        drawChartsHelper=new DrawChartsHelper(howManyBpmMeasuresTextField,barChartOfBpm,lineChartOfBpm);
        barChartOfBpm.setVisible(true);
        lineChartOfBpm.setVisible(false);
        drawChartsHelper.drawBarChart(bpmMeasuresRepository.getMeasures());
    }

    //settings

    private void showUserSettings(){
        if(userSettingsRepository.getUserSettings().isWhomen()){
            settingsWhomenButton.setSelected(true);
        }
        else{
            settingsMenButton.setSelected(true);
        }
        settingsBirthDatePicker.setValue(LocalDate.parse(userSettingsRepository.getUserSettings().getDateOfBirth(), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        settingsGrowthTextField.setText(String.valueOf(userSettingsRepository.getUserSettings().getGrowth()*100));
        if(userSettingsRepository.getUserSettings().getPpmMassFormule().equals("H")){
            ppmMethodsComboBox.getSelectionModel().select("wzór Harisa-Benedicta");
        }
        else{
            ppmMethodsComboBox.getSelectionModel().select("wzór Mifflina");
        }
        if(userSettingsRepository.getUserSettings().getBodyMassFormule().equals("B")){
        normalBodyMassMethodsComboBox.getSelectionModel().select("wzór Broca");
        }
        else if(userSettingsRepository.getUserSettings().getBodyMassFormule().equals("L")){
            normalBodyMassMethodsComboBox.getSelectionModel().select("wzór Lorenza");
        }
        else{
            normalBodyMassMethodsComboBox.getSelectionModel().select("wzór Pottona");
        }}


    //FUNKCJE DOTYCZĄCE DODANIA POMIARU PRZEZ UŻYTKOWNIKA
    @FXML
    void addMeasureFromUserButton() throws SQLException {
        try {
            appSettingsRepository = new AppSettingsRepository(jdbcQuery.getAppSettings());
            AddUserMeasureHelper addUserMeasureHelper = new AddUserMeasureHelper(appSettingsRepository);
            jdbcQuery.addMeasuresFromUserToDataBase(addUserMeasureHelper, loginEmailTextField, addPressureFromUserTextField,
                    addTemperatureFromUserTextField, addWindSpeedFromUserTextField, addHumidityFromUserTextField, claudinessFromIserComboBox,
                    idStationFinder.getIDSelectedStation(usersStationsToAddMeasureListView, usersStationsRepository.getStations())    );
        }
        catch (NullPointerException e) //jeżeli użytkownik nie zaznaczył stacji
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nie wybrano stacji pomiarowej");
            alert.setHeaderText("");
            alert.setContentText("Wybierz stacje pomiarową z menu po lewej stronie");
            alert.showAndWait();
        }

    }
    //FUNKCJE DOTYCZĄCE POBRANIA POMIARU PRZEZ UŻYTKOWNIKA
    private void turnOffAllMeasuresFromUser() { //Funkcja pomocnicza wyłącza wszystkie widoczne tabele z pomiarami
        claudinessTableView.setVisible(false);
        temperatureTableView.setVisible(false);
        pressureTableView.setVisible(false);
        windSpeedTableView.setVisible(false);
        humidityTableView.setVisible(false);
    }

    private void clearAllTablesWithMeasuresFromUser() { //Funkcja pomocnicza czyści wszystkie tabele z pomiarami
        temperatureTableView.getItems().removeAll();
        pressureTableView.getItems().removeAll();
        windSpeedTableView.getItems().removeAll();
        humidityTableView.getItems().removeAll();
        claudinessTableView.getItems().removeAll();

    }

    @FXML
    void showMeasuresFromUserButton() throws SQLException { //Funkcja odpowiadająca za wypełnienie tabeli pomiarami po kliknieciu przycisku
        try {
            turnOffAllMeasuresFromUser(); //Wyłączamy poprzedni widok
            clearAllTablesWithMeasuresFromUser(); //Czyścimy poprzednie tabele

            if (takeMeasuresFromUserComboBox.getSelectionModel().getSelectedItem().equals("Temperatura powietrza")) {
                temperatureTableView.setVisible(true);
                ObservableList<TemperatureFromUser> listOfTemperatureResults = FXCollections.observableArrayList(userMeasuresRepository.showTemperaturesFromUsersInCity(idStationFinder.getIDSelectedStation(usersStationsToTakeMaeasureListView, usersStationsRepository.getStations()), jdbcQuery.getTemperaturesFromUserList()));
                dateTempUser.setCellValueFactory(new PropertyValueFactory<>("date"));
                userNameTemp.setCellValueFactory(new PropertyValueFactory<>("userName"));
                temperature.setCellValueFactory(new PropertyValueFactory<>("temperature"));
                temperatureTableView.setItems(listOfTemperatureResults);
            } else if (takeMeasuresFromUserComboBox.getSelectionModel().getSelectedItem().equals("Wilgotność powietrza")) {
                humidityTableView.setVisible(true);
                ObservableList<HumidityFromUser> listOfHumidityResults = FXCollections.observableArrayList(userMeasuresRepository.showHumidityFromUsersInCity(idStationFinder.getIDSelectedStation(usersStationsToTakeMaeasureListView, usersStationsRepository.getStations()), jdbcQuery.getHumidityFromUserList()));
                dateHumidityUser.setCellValueFactory(new PropertyValueFactory<>("date"));
                userNameHumidity.setCellValueFactory(new PropertyValueFactory<>("userName"));
                humidity.setCellValueFactory(new PropertyValueFactory<>("humidity"));
                humidityTableView.setItems(listOfHumidityResults);
            } else if (takeMeasuresFromUserComboBox.getSelectionModel().getSelectedItem().equals("Prędkość wiatru")) {
                windSpeedTableView.setVisible(true);
                ObservableList<WindSpeedFromUser> listOfHumidityResults = FXCollections.observableArrayList(userMeasuresRepository.showWindSpeedFromUsersInCity(idStationFinder.getIDSelectedStation(usersStationsToTakeMaeasureListView, usersStationsRepository.getStations()), jdbcQuery.getWindSpeedFromUserList()));
                dateWindSpeedUser.setCellValueFactory(new PropertyValueFactory<>("date"));
                userNameWindSpeed.setCellValueFactory(new PropertyValueFactory<>("userName"));
                windSpeed.setCellValueFactory(new PropertyValueFactory<>("windSpeed"));
                windSpeedTableView.setItems(listOfHumidityResults);
            } else if (takeMeasuresFromUserComboBox.getSelectionModel().getSelectedItem().equals("Ciśnienie")) {
                pressureTableView.setVisible(true);
                ObservableList<PressureFromUser> listOfPressureResults = FXCollections.observableArrayList(userMeasuresRepository.showPressureFromUsersInCity(idStationFinder.getIDSelectedStation(usersStationsToTakeMaeasureListView, usersStationsRepository.getStations()), jdbcQuery.getPressureFromUserList()));
                datePressureUser.setCellValueFactory(new PropertyValueFactory<>("date"));
                userNamePressure.setCellValueFactory(new PropertyValueFactory<>("userName"));
                pressure.setCellValueFactory(new PropertyValueFactory<>("pressure"));
                pressureTableView.setItems(listOfPressureResults);
            } else if (takeMeasuresFromUserComboBox.getSelectionModel().getSelectedItem().equals("Zachmurzenie")) {
                claudinessTableView.setVisible(true);
                ObservableList<ClaudinessFromUser> listOfClaudinessResults = FXCollections.observableArrayList(userMeasuresRepository.showClaudinessFromUsersInCity(idStationFinder.getIDSelectedStation(usersStationsToTakeMaeasureListView, usersStationsRepository.getStations()), jdbcQuery.getCloudinessFromUserList()));
                dateClaudinessUser.setCellValueFactory(new PropertyValueFactory<>("date"));
                userNameClaudiness.setCellValueFactory(new PropertyValueFactory<>("userName"));
                claudiness.setCellValueFactory(new PropertyValueFactory<>("claudiness"));
                claudinessTableView.setItems(listOfClaudinessResults);
            }
        }
        catch (NullPointerException e) //jeżeli użytkownik nie zaznaczył stacji
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nie wybrano stacji pomiarowej");
            alert.setHeaderText("");
            alert.setContentText("Wybierz stacje pomiarową z menu po lewej stronie");
            alert.showAndWait();
        }
    }
    //DO POBRANIA POMIAROW Z OWM
    @FXML
    void onClickOwmStationsListView()  {
        DataFromOwmPresenter dataFromOwmPresenter=new DataFromOwmPresenter(measuresFromOWMTableView,tempColumn,pressureColumn,
                windColumn,humidityColumn,claudinessColumn,dateOWMColumn,weatherMeasuresFactory,idStationFinder,OWMStationsListView,
                owmStationsRepository,owmClaudinesTranslatorRepository);
        Thread showOwmDataThread =new Thread(dataFromOwmPresenter);
        showOwmDataThread.start();

    }

    @FXML
    void onClickGIOSStation() throws IOException, JSONException, InterruptedException {
        sensorsListView.getItems().clear();
        sensorsListView.getItems().addAll(giosSensorsRepository.getSensorsForSelectedStation(idStationFinder.getIDSelectedStation(GIOSStationsListView,giosStationsRepository.getStations())));
        DataFromGiosStationPresenter dataFromGiosStationPresenter=new DataFromGiosStationPresenter(GIOSAirIndexValueColumn,GIOSAirIndexNameColumn,GIOSAirIndexTableView,GIOSStationsListView,GIOSTableView,sensorsListView,giosSensorsRepository,giosStationsRepository,idStationFinder);
        Thread dataFromGiosStationThread=new Thread(dataFromGiosStationPresenter);
        dataFromGiosStationThread.start();
    }
    @FXML
    void onClickGIOSSensor() throws IOException, JSONException, ParseException {
        DataFromGiosSensorPresenter dataFromGiosSensorPresenter =new DataFromGiosSensorPresenter(giosSensorsRepository,GIOSTableView,GIOSValueColumn,GIOSDateColumn,sensorsListView);
        Thread dataFromGiosSensorThread=new Thread(dataFromGiosSensorPresenter);
        dataFromGiosSensorThread.start();
    }
    @FXML
    void pressEnterToTypePassword(ActionEvent ae) {
        passwordPasswordField.requestFocus();
    }

    @FXML
    void pressEnterToLogin(ActionEvent ae) throws SQLException {
        loginButton();
    }
    @FXML
    void pressEnterToSendPasswordCode(ActionEvent ae) {
        sendResetPasswordCode();
    }

    @FXML
    void pressEnterToSendRegistrationCode(ActionEvent ae) throws SQLException {
        sendRegistrationCode();
    }

    @FXML
    void pressEnterToTypeRegistrationPassword(ActionEvent ae) {
        registrationPasswordPasswordField.requestFocus();
    }

    @FXML
    void pressEnterToTypeConfirmedRegistrationPassword(ActionEvent ae) {
        registrationConfirmedPasswordPasswordField.requestFocus();
    }

    @FXML
    void pressEnterToRegister(ActionEvent ae) throws SQLException {
        registerButton();
    }

    @FXML
    void pressEnterToResetPassword(ActionEvent ae) {
        goToChangePasswordAfterEnterCodeButton();
    }

    @FXML
    void pressEnterToTypeResetConfirmedPassword(ActionEvent ae) {
        conifrmedResetPasswordPasswordField.requestFocus();
    }

    @FXML
    void pressEnterToChangePassword(ActionEvent ae) throws SQLException {
        changePasswordButton();
    }
    @FXML
    void onClickRefreshGiosStationsAndSensorsButton() throws SQLException {
        GIOSStationsListView.getItems().clear();
        giosStationsRepository=new GIOSStationsRepository(jdbcQuery.getstationsTable("giosstations"));
        GIOSStationsListView.getItems().addAll(giosStationsRepository.getStationNames());
        stationBrowser.searchByNameOnWriteInTextField(GIOSStationsBrowserTextField,giosStationsRepository.getStationNames(),GIOSStationsListView);
        GIOSStationsBrowserTextField.setText("");

    }
    @FXML
    void onClickRefreshOwmStationsButton() throws SQLException {
        OWMStationsListView.getItems().clear();
        owmStationsRepository = new OWMStationsRepository(jdbcQuery.getstationsTable("owmstations"));
        OWMStationsListView.getItems().addAll(owmStationsRepository.getStationNames());
        stationBrowser.searchByNameOnWriteInTextField(OWMStationsBrowserTextField, owmStationsRepository.getStationNames(),OWMStationsListView);
        OWMStationsBrowserTextField.setText("");
    }
    @FXML
    void onClickRefreshUsersStations() throws SQLException {
        usersStationsToAddMeasureListView.getItems().clear();
        usersStationsToTakeMaeasureListView.getItems().clear();
        usersStationsRepository = new UserStationsRepository(jdbcQuery.getstationsTable("usersstations"));
        usersStationsToTakeMaeasureListView.getItems().addAll(usersStationsRepository.getStationNames());
        usersStationsToAddMeasureListView.getItems().addAll(usersStationsRepository.getStationNames());
        stationBrowser.searchByNameOnWriteInTextField(addMesureFromUserBrowserTextField, usersStationsRepository.getStationNames(), usersStationsToAddMeasureListView);
        stationBrowser.searchByNameOnWriteInTextField(takeMeasureFromUserBrowserTextField, usersStationsRepository.getStationNames(), usersStationsToTakeMaeasureListView);
        addHumidityFromUserTextField.setText("");
        takeMeasureFromUserBrowserTextField.setText("");
    }
    @FXML
    void onClickOpenDocumentationButton() throws IOException {
        OpenFileHelper openFileHelper =new OpenFileHelper();
        openFileHelper.openPdfFile(getClass().getResource("Dokumentacja_uzytkownika.pdf").getPath());
    }
    @FXML
    void onClickOpenDustyPlantsCalendarButton() throws IOException {
        OpenFileHelper openFileHelper =new OpenFileHelper();
        openFileHelper.openPdfFile(getClass().getResource("Calendar.PNG").getPath());
    }
    @FXML
    void onClickLogoutOrExit() throws IOException, InterruptedException {
    Platform.exit();
    System.exit(0);
    }

    @FXML
    void onClickSavePickflometerPurchaseDate() throws SQLException {
        jdbcQuery.addNewPickflowmeterWear(userRepository,pickflowmeterPurdhaseDateDatePicker,pickflowmeterWearRepository);
        pickflowmeterWearRepository=new PickflowmeterWearRepository(jdbcQuery.getpickflowmeterwearTable(userRepository));
        newPickflowmeterDays.setText(String.valueOf((int) ChronoUnit.DAYS.between(LocalDate.now(),LocalDate.parse(pickflowmeterWearRepository.getPickflowmeterWear().getPurchaseDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")).plusDays(1095))));

    }
    @FXML
    void onCLickAddNewNotificationButton() throws SQLException {
        jdbcQuery.addNewNotification(medicinesNameTextField, medicinesDoseTextField,noificationTimeTextField,userRepository);
        showMedicinesNotifications();
    }
    @FXML
    void onClickRemoveSelectedNotification() throws SQLException {
        jdbcQuery.removeSelectedNotification(notificationsTableView);
        showMedicinesNotifications();
    }
}
