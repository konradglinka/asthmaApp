package EmailActions;

import Repositories.AlertEmailsRepository;
import Repositories.UserRepository;
import Repositories.UserSettingsRepository;
import javafx.scene.control.Button;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.Properties;


public class EmailToAlarm implements Runnable {
    @Override
    public void run() {
        try {
            sendAlertEmail();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    UserRepository userRepository;
    UserSettingsRepository userSettingsRepository;
    Button sendAlertEmailValueButton;
    AlertEmailsRepository alertEmailsRepository;


    public EmailToAlarm(UserRepository userRepository,UserSettingsRepository userSettingsRepository, Button sendAlertEmailValueButton, AlertEmailsRepository alertEmailsRepository) {
       this.userRepository=userRepository;
        this.userSettingsRepository = userSettingsRepository;
        this.sendAlertEmailValueButton = sendAlertEmailValueButton;
        this.alertEmailsRepository = alertEmailsRepository;
    }

    // Adres email aplikacji która wysyła maila
    private static final String FROM = "weatherappkma@gmail.com";
    // Hasło do konta aplikacji która wysyła maila
    private static final String PASSWORD = "c7kj92rmz";




    public void sendAlertEmail() throws MessagingException {

        sendAlertEmailValueButton.setDisable(true);
        // Temat wiadomości
       String SubjectOfEmail = userSettingsRepository.getUserSettings().getAlertEmailTitle();
        // Treść wiadomości
        String CONTENT = userSettingsRepository.getUserSettings().getAlertValue()+"\nNadawca: "+userRepository.getUser().getEmail()+"\nData wysłania: "+ LocalDate.now();
              /*alertValueDefault.append(registrationEmail.getText());
        alertValueDefault.append("\n Data wysłania:");
        alertValueDefault.append(LocalDate.now());*/
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(FROM, PASSWORD);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM));

            message.setSubject(SubjectOfEmail);
            message.setText(CONTENT);
            for(int i=0; i<alertEmailsRepository.getEmailList().size();i++){
                message.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(alertEmailsRepository.getEmailList().get(i)));
                 try {
                     Transport.send(message);
                 }
                 catch (Exception e){
                     Transport.send(message);
                 }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        sendAlertEmailValueButton.setDisable(false);
    }





}



