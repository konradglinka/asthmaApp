package ViewControl;

import Objects.Measure;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class BpmMeasuresTableView {
    public BpmMeasuresTableView(TableView<Measure> bpmResultsTableView, TableColumn<Measure,Integer> bpmIdColumn, TableColumn<Measure,Integer> bpmIdUserColumn, TableColumn<Measure,Double> bpmValueColumn, TableColumn<Measure,String> bpmDateColumn, ArrayList<Measure> measures) {
        bpmResultsTableView.getItems().clear();
        bpmIdColumn.setCellValueFactory(new PropertyValueFactory<>("iD"));
        bpmIdUserColumn.setCellValueFactory(new PropertyValueFactory<>("iDUser"));
        bpmValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        bpmDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        bpmResultsTableView.getItems().addAll(measures);
    }
}
