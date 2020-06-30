package ViewControl;

import Objects.Measure;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class BmiMeasuresTableView {
    public BmiMeasuresTableView(TableView<Measure> bmiResultsTableView, TableColumn<Measure,Integer> bmiIdColumn, TableColumn<Measure,Integer> bmiIdUserColumn, TableColumn<Measure,Double> bmiValueColumn, TableColumn<Measure,String> bmiDateColumn, ArrayList<Measure> measures) {
        bmiResultsTableView.getItems().clear();
        bmiIdColumn.setCellValueFactory(new PropertyValueFactory<>("iD"));
        bmiIdUserColumn.setCellValueFactory(new PropertyValueFactory<>("iDUser"));
        bmiValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        bmiDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        bmiResultsTableView.getItems().addAll(measures);
    }
}
