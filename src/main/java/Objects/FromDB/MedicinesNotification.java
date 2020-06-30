package Objects.FromDB;

public final class MedicinesNotification {
    private final int iD;
    private final int idUser;
    private final String medicinesName;
    private final double medicinesDose;
    private final String notificationTime;

    public MedicinesNotification(int iD, int idUser, String medicinesName, double medicinesDose, String notificationTime) {
        this.iD = iD;
        this.idUser = idUser;
        this.medicinesName = medicinesName;
        this.medicinesDose = medicinesDose;
        this.notificationTime = notificationTime;
    }

    public int getiD() {
        return iD;
    }

    public int getIdUser() {
        return idUser;
    }

    public String getMedicinesName() {
        return medicinesName;
    }

    public double getMedicinesDose() {
        return medicinesDose;
    }

    public String getNotificationTime() {
        return notificationTime;
    }
}
