package AnotherClasses;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;

import java.time.LocalDate;

public class DatePickerHelper {
    Callback<DatePicker, DateCell> callB = new Callback<DatePicker, DateCell>() {
        @Override
        public DateCell call(final DatePicker param) {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                    LocalDate today = LocalDate.now();
                    setDisable(empty || item.compareTo(today) > 0);
                }

            };
        }

    };
    public void disableFutureDatesInDatePicker(DatePicker datePicker){
        datePicker.setDayCellFactory(callB);
    }

}
