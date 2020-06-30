package EmailActions;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;


public class EmailToResetPassword implements Runnable {
    private String resetPasswordCode ="";
    private String resetPasswordEmail;
    private Button sendResetPasswordCodeButton;
    @Override
    public void run() {
            sendResetPasswordEmail();}





    public EmailToResetPassword(TextField resetPasswordEmailTextField, Button sendResetPasswordCodeButton) {
        this.sendResetPasswordCodeButton=sendResetPasswordCodeButton;
        this.resetPasswordEmail= resetPasswordEmailTextField.getText();
        setResetPasswordCode();
        this.resetPasswordCode=getResetPasswordCode();
    }



    // Adres email aplikacji która wysyła maila
    private static final String FROM = Secret.getEmail();
    // Hasło do konta aplikacji która wysyła maila
    private static final String PASSWORD = Secret.getPassword();




    public void sendResetPasswordEmail()  {

sendResetPasswordCodeButton.setDisable(true);
        // Temat wiadomości
       String SubjectOfEmail = "Reset hasła";
        // Treść wiadomości
        String CONTENT = "Aby zresetować hasło wpisz kod: "+ getResetPasswordCode();

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
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(resetPasswordEmail)
            );
            message.setSubject(SubjectOfEmail);
            message.setText(CONTENT);
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        sendResetPasswordCodeButton.setDisable(false);
    }



    public void setResetPasswordCode(){
        this.resetPasswordCode ="";
        Random random = new Random();
        while (resetPasswordCode.length()<8) {
            this.resetPasswordCode += random.nextInt(10);
        }

    }
public String getResetPasswordCode(){
        return resetPasswordCode;
}
}



