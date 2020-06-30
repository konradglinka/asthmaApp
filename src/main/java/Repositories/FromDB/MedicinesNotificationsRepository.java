package Repositories.FromDB;

import Objects.FromDB.MedicinesNotification;

import java.util.ArrayList;

public final class MedicinesNotificationsRepository {
    private final ArrayList<MedicinesNotification>medicinesNotificationArrayList;
    public MedicinesNotificationsRepository(ArrayList<MedicinesNotification> medicinesNotificationArrayList) {
        this.medicinesNotificationArrayList = medicinesNotificationArrayList;
    }



    public ArrayList<MedicinesNotification> getMedicinesNotificationArrayList() {
        return medicinesNotificationArrayList;
    }
}
