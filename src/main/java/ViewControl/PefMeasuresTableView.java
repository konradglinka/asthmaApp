package ViewControl;

import Objects.Measure;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class PefMeasuresTableView {
    public PefMeasuresTableView(TableView<Measure> PEFResultsTableView, TableColumn<Measure,Integer> pefIdColumn, TableColumn<Measure,Integer> pefIdUserColumn, TableColumn<Measure,Double> pefValueColumn, TableColumn<Measure,String> pefDateColumn, ArrayList<Measure> measures) {
        PEFResultsTableView.getItems().clear();
        pefIdColumn.setCellValueFactory(new PropertyValueFactory<>("iD"));
        pefIdUserColumn.setCellValueFactory(new PropertyValueFactory<>("iDUser"));
        pefValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        pefDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        PEFResultsTableView.getItems().addAll(measures);
    }
}
