package app.view;

import java.net.URL;
import java.util.ResourceBundle;

import app.model.core.*;
import app.model.info.DrawOpt;
import app.model.info.Vars;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class ChartViewController implements Initializable {
	@FXML
	private LineChart<Number, Number> lineChart;
	@FXML
	private NumberAxis xAxis;
	@FXML
	private NumberAxis yAxis;
	
	
	public void initialize() {
		lineChart.setAnimated(false);
		lineChart.setCreateSymbols(false);
	}
	
	public void drawChart(Calculation calc, int op, LineChart<Number, Number> lineChart, String name) {
		lineChart.getData().clear();
		XYChart.Series<Number, Number> series = new XYChart.Series<>();
        // Create a XYChart.Data
		if(op == DrawOpt.mass_loss) {
			for (int i = 0; i <= Vars.EXPORSURE_TIME; i++) {
				series.getData().add(new XYChart.Data<>(i, calc.getMassLoss().get(i)));
	        }
		} else if (op == DrawOpt.temperature) {
			for (int i = 0; i < calc.getSize(); i++) {
				series.getData().add(new XYChart.Data<>(calc.getTubingl().get(i), calc.getTemperature().get(i).getC()));
	        }
		} else if (op == DrawOpt.pressure) {
			for (int i = 0; i < calc.getSize(); i++) {
				series.getData().add(new XYChart.Data<>(calc.getTubingl().get(i), calc.getPressure().get(i).getBara()));
	        }
		} else if (op == DrawOpt.ph) {
			for (int i = 0; i < calc.getSize(); i++) {
				series.getData().add(new XYChart.Data<>(calc.getTubingl().get(i), calc.getpHactual().get(i)));
	        }
		} else if(op == DrawOpt.ucr_norm) {
			for (int i = 0; i < calc.getSize(); i++) {
				series.getData().add(new XYChart.Data<>(calc.getTubingl().get(i), calc.getVcorNorm().get(i)));
	        }
		} else if (op == DrawOpt.ucr_qt) {
			for (int i = 0; i < calc.getSize(); i++) {
				series.getData().add(new XYChart.Data<>(calc.getTubingl().get(i), calc.getVcorQT().get(i)));
	        }
		} else if (op == DrawOpt.ccr_norm) {
			for (int i = 0; i < calc.getSize(); i++) {
				series.getData().add(new XYChart.Data<>(calc.getTubingl().get(i), calc.getVcorCorrectedNorm().get(i)));
	        }
		} else if (op == DrawOpt.ccr_qt) {
			for (int i = 0; i < calc.getSize(); i++) {
				series.getData().add(new XYChart.Data<>(calc.getTubingl().get(i), calc.getVcorCorrectedQT().get(i)));
	        }
		} else  if (op == DrawOpt.waterrate) {
			for (int i = 0; i < calc.getSize(); i++) {
				series.getData().add(new XYChart.Data<>(calc.getTubingl().get(i), calc.getWaterRate().get(i)));
	        }
		} else if (op == DrawOpt.watercut) {
			for (int i = 0; i < calc.getSize(); i++) {
				series.getData().add(new XYChart.Data<>(calc.getTubingl().get(i), calc.getWaterCut().get(i)));
	        }
		} else if (op == DrawOpt.liq_holdup) {
			for (int i = 0; i < calc.getSize(); i++) {
				series.getData().add(new XYChart.Data<>(calc.getTubingl().get(i), calc.getLiqHold().get(i)));
	        }
		} else if (op == DrawOpt.liq_vel) {
			for (int i = 0; i < calc.getSize(); i++) {
				series.getData().add(new XYChart.Data<>(calc.getTubingl().get(i), calc.getLiqVel().get(i)));
	        }
		} else if (op == DrawOpt.gas_vel) {
			for (int i = 0; i < calc.getSize(); i++) {
				series.getData().add(new XYChart.Data<>(calc.getTubingl().get(i), calc.getGasVel().get(i)));
	        }
		} else {
			System.out.println("Pilihan tidak tersedia");
		}
		series.setName(name);
        lineChart.getData().add(series);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
}
