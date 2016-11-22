package app.view;

import java.util.ArrayList;

import app.model.core.*;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class ChartViewController {
	@FXML
	private LineChart<Number, Number> lineChart;
	@FXML
	private NumberAxis xAxis;
	@FXML
	private NumberAxis yAxis;
	
	public void initialize() {
		lineChart.setTitle("Chart");
		lineChart.setAnimated(false);
		lineChart.setCreateSymbols(false);
	}
	
	public void drawChart(Calculation calc) {
		ArrayList<Double> tubingl = calc.getTubingl();
		ArrayList<Double> ph = calc.getpHactual();
		ArrayList<Temperature> temp = calc.getTemperature();
		 XYChart.Series<Number, Number> series = new XYChart.Series<>();
		 series.setName("My portfolio");
        // Create a XYChart.Data
        for (int i = 0; i < temp.size(); i++) {
        	System.out.println(ph.get(i));
            series.getData().add(new XYChart.Data<>(tubingl.get(i).doubleValue(), ph.get(i)));
        }
        lineChart.getData().add(series);
	}
	
	
	
}
