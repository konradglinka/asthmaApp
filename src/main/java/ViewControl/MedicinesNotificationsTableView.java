package ViewControl;

import Objects.FromDB.MedicinesNotification;
import Objects.Measure;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class MedicinesNotificationsTableView {
    public MedicinesNotificationsTableView(TableView<MedicinesNotification> medicinesNotificationTableView, TableColumn<MedicinesNotification,String> medicinesNameColumn, TableColumn<MedicinesNotification,Double> medicinesDoseColumn, TableColumn<MedicinesNotification,String> notificationTimeColumn, ArrayList<MedicinesNotification> medicinesNotifications) {
        medicinesNotificationTableView.getItems().clear();
        medicinesNameColumn.setCellValueFactory(new PropertyValueFactory<>("medicinesName"));
        medicinesDoseColumn.setCellValueFactory(new PropertyValueFactory<>("medicinesDose"));
        notificationTimeColumn.setCellValueFactory(new PropertyValueFactory<>("notificationTime"));
        medicinesNotificationTableView.getItems().addAll(medicinesNotifications);
    }
}
