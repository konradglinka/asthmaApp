package Repositories;

import Objects.AlertEmail;

import java.util.ArrayList;

public final class AlertEmailsRepository {
    private final ArrayList<AlertEmail>alertEmailsList;
    private final ArrayList<String>emailList=new ArrayList<>();
    public AlertEmailsRepository(ArrayList<AlertEmail> alertEmailsList) {
        this.alertEmailsList = alertEmailsList;
        for(int i=0; i<alertEmailsList.size();i++){
            emailList.add(alertEmailsList.get(i).getEmail());
        }
    }

    public ArrayList<AlertEmail> getAlertEmailsList() {
        return alertEmailsList;
    }
    public ArrayList<String> getEmailList() {
        return emailList;
    }

}
