package app.view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;

import javax.imageio.ImageIO;

import com.jfoenix.controls.JFXSpinner;

import app.model.core.Calculation;
import app.model.core.MinMax;
import app.model.info.DrawOpt;
import app.model.info.Vars;
import app.model.params.Additional;
import app.model.params.AngleDepth;
import app.model.params.ProjectData;
import app.model.params.StellInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.util.converter.NumberStringConverter;
import utils.Conf;
import utils.IOFile;

public class MainFieldController implements Initializable {

	
	//ELEMENTS OF CONDITIONS TAB//
	@FXML
	private TextField tfTitle;
	@FXML
	private TextArea tfDesc;
	@FXML
	private TextField tempWhC;
	@FXML
	private TextField tempWhF;
	@FXML
	private TextField tempBhC;
	@FXML
	private TextField tempBhF;
	@FXML
	private TextField pressWhBara;
	@FXML
	private TextField pressWhPsia;
	@FXML
	private TextField pressBhBara;
	@FXML
	private TextField pressBhPsia;
	@FXML
	private TextField gasCO2;
	@FXML
	private TextField gasH2Smol;
	@FXML
	private TextField gasH2Sppm;
	@FXML
	private TextField NaClgl;
	@FXML
	private TextField NaClppm;
	@FXML
	private TextField N2;
	@FXML
	private TextField bicarbon;
	@FXML
	private TextField stC;
	@FXML
	private TextField stF;
	@FXML
	private TextField spBara;
	@FXML
	private TextField spPsia;
	//END OF ELEMENTS OF CONDITIONS TAB//
	
	//PRODUCTION DATA//
	@FXML
	private TextField oilFr;
	@FXML
	private TextField oilApi;
	@FXML
	private TextField gasFr;
	@FXML
	private TextField waterFr;
	@FXML
	private TextField liqHold;
	@FXML
	private TextField watercut;
	//END OF PRODUCTION DATA//
	
	//STELL INFO//
	@FXML
	private TextField depth;
	@FXML
	private TextField outd;
	@FXML
	private TextField thickness;
	@FXML
	private TextField weight;
	@FXML
	private TextField stellCr;
	@FXML
	private TextField stellC;
	@FXML
	private RadioButton rbNorm;
	@FXML
	private RadioButton rbQT;
	//END OF STELL INFO//
	
	//ADDITIONAL DATA//
	@FXML
	private TextField inAvail;
	@FXML
	private TextField inEff;
	@FXML
	private TextField erVel;
	@FXML
	private RadioButton inNone;
	@FXML
	private RadioButton inContinu;
	//END OF ADDITIONAL DATA//
	
	//ADVANCED//
	@FXML
	private TextField segment;
	@FXML
	private TextField exporsure;
	@FXML
	private TextField gravity;
	//END OF ADVANCED//
	
	//Table//
	@FXML
    private TableView<AngleDepth> tableView;
    @SuppressWarnings("rawtypes")
	@FXML
    private TableColumn angleCol;
    @SuppressWarnings("rawtypes")
	@FXML
    private TableColumn depthCol;
    private final ObservableList<AngleDepth> dataAngle = FXCollections.observableArrayList();
	//end table
	//CHARTS//
	@FXML
	private LineChart<Number, Number> corrosionChart;
	@FXML
	private NumberAxis xAxisCor;
	@FXML
	private NumberAxis yAxisCor;
	@FXML
	private Label minCor;
	@FXML
	private Label maxCor;
	@FXML
	private LineChart<Number, Number> riskChart;
	@FXML
	private NumberAxis xAxisRisk;
	@FXML
	private NumberAxis yAxisRisk;
	@FXML
	private Label minRisk;
	@FXML
	private Label maxRisk;
	@FXML
	private LineChart<Number, Number> otherChart;
	@FXML
	private NumberAxis xAxisOth;
	@FXML
	private NumberAxis yAxisOth;
	@FXML
	private Label minOth;
	@FXML
	private Label maxOth;
	@FXML
	private ChoiceBox<String> chartOption;

	@FXML
	private JFXSpinner spinner;
	//END OF CHARTS
	
	//Detail Output //
	@FXML
	private TextField gorwh;
	@FXML
	private TextField wcwh;
	@FXML
	private TextField flowwh;
	@FXML
	private TextField co2wh;
	@FXML
	private TextField wrbh;
	@FXML
	private TextField vgas;
	@FXML
	private TextField vliq;
	@FXML
	private TextField vero;
	@FXML
	private TextField denslb;
	@FXML
	private TextField densg;
	//end detail output
	
	private HashMap<String, Integer> chartOpt;
	private Calculation calc;
	private String filePath = "";

	public MainFieldController() {
		calc = new Calculation();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		initAll();
        calc.calcAll();
        fillTable();
        drawCorrosion();
		drawRisk();
		initOtherChart();
		drawOtherChart();
		fillDetail();
		spinner.setVisible(false);
	}
	
	private void initOtherChart() {
		chartOpt = new HashMap<>();
		chartOpt.put("Uncorrected Corrosion Rate - Normal", DrawOpt.ucr_norm);
		chartOpt.put("Uncorrected Corrosion Rate - Quenched and Tempered", DrawOpt.ucr_qt);
		chartOpt.put("Temperature", DrawOpt.temperature);
		chartOpt.put("Pressure", DrawOpt.pressure);
		chartOpt.put("Temperature", DrawOpt.temperature);
		chartOpt.put("PH", DrawOpt.ph);
		chartOpt.put("Watercut", DrawOpt.watercut);
		chartOpt.put("Water Rate", DrawOpt.waterrate);
		chartOpt.put("Liquid Holdup Change", DrawOpt.liq_holdup);
		chartOpt.put("Liquid Velocity", DrawOpt.liq_vel);
		chartOpt.put("Gas Velocity", DrawOpt.gas_vel);
		
		Set<String> keys = chartOpt.keySet();
		ObservableList<String> elem = FXCollections.observableArrayList();
		elem.addAll(keys);
		chartOption.setItems(elem);
		chartOption.setValue(elem.get(0));
	}
	
	@SuppressWarnings("unchecked")
	private void fillTable() {
		
        tableView.getColumns().clear();
        dataAngle.clear();
        ArrayList<AngleDepth> tuppleArr = AngleDepth.makeAngleDepth(calc.getAngle().getAngles(), calc.getDepth(), calc.getSize());
        dataAngle.addAll(tuppleArr);
        tableView.setItems(dataAngle);
        tableView.getColumns().addAll(depthCol, angleCol);
    }
	
	private void drawCorrosion() {
		if(calc.getStell().getStellStats() == StellInfo.NORMALIZED) {
			drawChart(calc, DrawOpt.ccr_norm, corrosionChart, "Corrosion Rate Curve", xAxisCor, yAxisCor, minCor, maxCor);
		} else {
			drawChart(calc, DrawOpt.ccr_qt, corrosionChart, "Corrosion Rate Curve", xAxisCor, yAxisCor, minCor, maxCor);
		}
		
	}
	
	private void drawRisk() {
		drawChart(calc, DrawOpt.mass_loss, riskChart, "Risk Curve", xAxisRisk, yAxisRisk, minRisk, maxRisk);
	}
	
	@FXML
	private void reDreawOther(ActionEvent event) throws Exception  {
		drawOtherChart();
	}
	
	private void drawOtherChart() {
		String item = chartOption.getValue();
		drawChart(calc, chartOpt.get(item) ,otherChart, "", xAxisOth, yAxisOth, minOth, maxOth);
	}
	
	
	@FXML
	private void run(ActionEvent event) throws Exception  {
		spinner.setVisible(true);
		updateAll();
		drawCorrosion();
		drawRisk();
		drawOtherChart();
		fillTable();
		fillDetail();
		spinner.setVisible(false);
	}
	
	@FXML
	private void open(ActionEvent event) {
		Node node = (Node) event.getSource();
		FileChooser fileChooser = new FileChooser();
		Conf.configureSelectFile(fileChooser);
		fileChooser.setTitle("Open Resource File");
		File file = fileChooser.showOpenDialog(node.getScene().getWindow());
		if(file != null) {
			filePath = file.getAbsolutePath();
			calc.getProjectData().setProject(filePath);
			initAll();
		}
	}
	
	
	@FXML
	private void save(ActionEvent event) throws Exception  {
		if(filePath.equals("")) {
			saveAs(event);
		} else {
			IOFile io = new IOFile();
			io.writeText(filePath, calc.getProjectData().toString());
		}
	}
	
	@FXML
	private void saveAs(ActionEvent event) throws Exception  {
		Node node = (Node) event.getSource();
		FileChooser fileChooser = new FileChooser();
		Conf.configureSelectFile(fileChooser);
		fileChooser.setTitle("Save File");
		File file = fileChooser.showSaveDialog(node.getScene().getWindow());
		if(file != null) {
			filePath = file.getAbsolutePath();
			updateAll();
			IOFile io = new IOFile();
			io.writeText(filePath, calc.getProjectData().toString());
		}
	}
	
	private void saveAsPNG(LineChart<Number,Number> chart, Node node) {	
	    WritableImage image = chart.snapshot(new SnapshotParameters(), null);
	    // TODO: probably use a file chooser here
	    
	    FileChooser fileChooser = new FileChooser();
		Conf.configureSaveImage(fileChooser);
		fileChooser.setTitle("Save File");
		File file = fileChooser.showSaveDialog(node.getScene().getWindow());
		if(file != null) {
		    try {
		        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		    } catch (IOException e) {
		        // TODO: handle exception here
		    }
		}
	}
	@FXML
	private void saveRiskChart(ActionEvent event) {
		Node node = (Node) event.getSource();
		saveAsPNG(riskChart, node);
	}
	@FXML
	private void saveCorrosionChart(ActionEvent event) {
		Node node = (Node) event.getSource();
		saveAsPNG(corrosionChart, node);
	}
	@FXML
	private void saveOtherChart(ActionEvent event) {
		Node node = (Node) event.getSource();
		saveAsPNG(otherChart, node);
	}
	@FXML
	private void newProject(ActionEvent event) {
		calc.getProjectData().setProject(new ProjectData());
		initAll();
	}
	
	
	public void drawChart(Calculation calc, int op, LineChart<Number, Number> lineChart, String name, NumberAxis x, NumberAxis y, Label min, Label max) {
		lineChart.getData().clear();
		MinMax m = new MinMax(0,0);
		XYChart.Series<Number, Number> series = new XYChart.Series<>();
        // Create a XYChart.Data
		
		if(op == DrawOpt.mass_loss) {
			for (int i = 0; i <= Vars.EXPORSURE_TIME; i+=6) {
				series.getData().add(new XYChart.Data<>(i/6, calc.getMassLoss().get(i/6)));
	        }
			 m = MinMax.getMinMax(calc.getMassLoss());
			 min.setText(Double.toString(m.getMin()));
			 max.setText(Double.toString(m.getMax()));
			x.setLabel("Time (hours)");
			y.setLabel("Risk (%)");
		} else if (op == DrawOpt.temperature) {
			for (int i = 0; i < calc.getSize(); i++) {
				series.getData().add(new XYChart.Data<>(calc.getTubingl().get(i), calc.getTemperature().get(i).getC()));
	        }
			m = MinMax.getMinMaxTemp(calc.getTemperature());
			x.setLabel("Tubing Length (ft)");
			y.setLabel("Temperature (degree C)");
			name = "Temperature Curve";
			lineChart.setTitle("Temperature vs Tubing Length");
		} else if (op == DrawOpt.pressure) {
			for (int i = 0; i < calc.getSize(); i++) {
				series.getData().add(new XYChart.Data<>(calc.getTubingl().get(i), calc.getPressure().get(i).getBara()));
	        }
			m = MinMax.getMinMaxPress(calc.getPressure());
			x.setLabel("Tubing Length (ft)");
			y.setLabel("Pressure (bara)");
			lineChart.setTitle("Pressure vs Tubing Length");
			name = "Pressure Curve";
		} else if (op == DrawOpt.ph) {
			for (int i = 0; i < calc.getSize(); i++) {
				series.getData().add(new XYChart.Data<>(calc.getTubingl().get(i), calc.getpHactual().get(i)));
	        }
			m = MinMax.getMinMax(calc.getpHactual());
			x.setLabel("Tubing Length (ft)");
			y.setLabel("pH");
			name = "pH Curve";
			lineChart.setTitle("pH vs Tubing Length");
		} else if(op == DrawOpt.ucr_norm) {
			for (int i = 0; i < calc.getSize(); i++) {
				series.getData().add(new XYChart.Data<>(calc.getTubingl().get(i), calc.getVcorNorm().get(i)));
	        }
			m = MinMax.getMinMax(calc.getVcorNorm());
			x.setLabel("Tubing Length (ft)");
			y.setLabel("Corrosion Rate - Normal");
			lineChart.setTitle("Corrosion Rate vs Tubing Length");
			name = "Corrosion Curve";
		} else if (op == DrawOpt.ucr_qt) {
			for (int i = 0; i < calc.getSize(); i++) {
				series.getData().add(new XYChart.Data<>(calc.getTubingl().get(i), calc.getVcorQT().get(i)));
	        }
			m = MinMax.getMinMax(calc.getVcorQT());
			x.setLabel("Tubing Length (ft)");
			y.setLabel("Corrosion Rate - Quenched and Tempered");
			lineChart.setTitle("Corrosion Rate vs Tubing Length");
			name = "Corrosion Curve";
		} else if (op == DrawOpt.ccr_norm) {
			for (int i = 0; i < calc.getSize(); i++) {
				series.getData().add(new XYChart.Data<>(calc.getTubingl().get(i), calc.getVcorCorrectedNorm().get(i)));
	        }
			m = MinMax.getMinMax(calc.getVcorCorrectedNorm());
			x.setLabel("Tubing Length (ft)");
			y.setLabel("Corrosion Rate");
		} else if (op == DrawOpt.ccr_qt) {
			for (int i = 0; i < calc.getSize(); i++) {
				series.getData().add(new XYChart.Data<>(calc.getTubingl().get(i), calc.getVcorCorrectedQT().get(i)));
	        }
			m = MinMax.getMinMax(calc.getVcorCorrectedQT());
			x.setLabel("Tubing Length (ft)");
			y.setLabel("Corrosion Rate");
		} else  if (op == DrawOpt.waterrate) {
			for (int i = 0; i < calc.getSize(); i++) {
				series.getData().add(new XYChart.Data<>(calc.getTubingl().get(i), calc.getWaterRate().get(i)));
	        }
			m = MinMax.getMinMax(calc.getWaterRate());
			x.setLabel("Tubing Length (ft)");
			y.setLabel("Water Rate");
			lineChart.setTitle("Water Rate vs Tubing Length");
			name = "Waterrate Curve";
		} else if (op == DrawOpt.watercut) {
			for (int i = 0; i < calc.getSize(); i++) {
				series.getData().add(new XYChart.Data<>(calc.getTubingl().get(i), calc.getWaterCut().get(i)));
	        }
			m = MinMax.getMinMax(calc.getWaterCut());
			x.setLabel("Tubing Length (ft)");
			y.setLabel("Watercut");
			name = "Watercut Curve";
			lineChart.setTitle("Watercut vs Tubing Length");
		} else if (op == DrawOpt.liq_holdup) {
			for (int i = 0; i < calc.getSize(); i++) {
				series.getData().add(new XYChart.Data<>(calc.getTubingl().get(i), calc.getLiqHold().get(i)));
	        }
			m = MinMax.getMinMax(calc.getLiqHold());
			x.setLabel("Tubing Length (ft)");
			y.setLabel("Liquid Holdup + Change");
			lineChart.setTitle("Liquid Holdup vs Tubing Length");
			name = "Liquid Holdup Curve";
		} else if (op == DrawOpt.liq_vel) {
			for (int i = 0; i < calc.getSize(); i++) {
				series.getData().add(new XYChart.Data<>(calc.getTubingl().get(i), calc.getLiqVel().get(i)));
	        }
			m = MinMax.getMinMax(calc.getLiqVel());
			x.setLabel("Tubing Length (ft)");
			y.setLabel("Liquid Velocity");
			lineChart.setTitle("Liquid Velocity vs Tubing Length");
			name = "Liquid Velocity Curve";
		} else if (op == DrawOpt.gas_vel) {
			for (int i = 0; i < calc.getSize(); i++) {
				series.getData().add(new XYChart.Data<>(calc.getTubingl().get(i), calc.getGasVel().get(i)));
	        }
			m = MinMax.getMinMax(calc.getGasVel());
			x.setLabel("Tubing Length (ft)");
			y.setLabel("Gas Velocity");
			lineChart.setTitle("Gas Velocity vs Tubing Length");
			name = "Gas Velocity Curve";
		} else {
			System.out.println("Pilihan tidak tersedia");
		}
		NumberFormat df = new DecimalFormat("#0.00"); 
		min.setText(df.format(m.getMin()));
		max.setText(df.format(m.getMax()));
		series.setName(name);
        lineChart.getData().add(series);
	}
	
	
	@SuppressWarnings("unchecked")
	public void initAll() {
		initCondition();
		initProduction();
		initStell();
		initAdditional();
		initAdv();
		
		corrosionChart.setAnimated(false);
		corrosionChart.setCreateSymbols(false);
		corrosionChart.setTitle("Corrosion Rate");
		otherChart.setAnimated(false);
		otherChart.setCreateSymbols(false);
		riskChart.setAnimated(false);
		riskChart.setCreateSymbols(false);
		riskChart.setTitle("Risk of Failure");
		tableView.setEditable(true);
		
		angleCol.setCellValueFactory(new PropertyValueFactory<>("angle"));
		angleCol.setCellFactory(TextFieldTableCell.<AngleDepth, Number>forTableColumn(new NumberStringConverter()));
		angleCol.setOnEditCommit(new EventHandler<CellEditEvent<AngleDepth, Double>>() {

			@Override
			public void handle(CellEditEvent<AngleDepth, Double> arg0) {
				// TODO Auto-generated method stub
				int pos = arg0.getTablePosition().getRow();
				Number val = arg0.getNewValue();
				Double d = val.doubleValue();
				System.out.println(d);
				((AngleDepth) arg0.getTableView().getItems().get(pos)).setAngle(d);
				calc.getAngle().setAngle(pos, d);
			}
			
		});
        depthCol.setCellValueFactory(new PropertyValueFactory<>("depth"));
        
	}
	
	public void updateAll() {
		updateCondition();
		updateProduction();
		updateStell();
		updateAdditional();
		updateAdv();
		calc.calcAll();
	}
	
	private void fillDetail() {
		NumberFormat df = new DecimalFormat("#0.00"); 
		gorwh.setText(df.format(calc.getDetailOut().getGOR()));
		wcwh.setText(df.format(calc.getDetailOut().getWatercut()));
		int flow = calc.getDetailOut().getFlowPattern();
		String text  = "";
		if(flow == Vars.ANNULAR_MIST_FLOW) {
			text = "Annular-mist flow";
		}else if(flow == Vars.BUBBLE_SLUG_FLOW) {
			text = "Bubble/slug flow";
		}
		flowwh.setText(text);
		co2wh.setText(df.format(calc.getDetailOut().getCO2Press()));
		wrbh.setText(df.format(calc.getDetailOut().getWaterRate()));
		vgas.setText(df.format(calc.getDetailOut().getGasVelocity()));
		vliq.setText(df.format(calc.getDetailOut().getLiqVelocity()));
		vero.setText(df.format(calc.getDetailOut().getErosionalVel()));
		denslb.setText(df.format(calc.getDetailOut().getDensStell()*62.428));
		densg.setText(df.format(calc.getDetailOut().getDensStell()));
	}
	
	public void initCondition() {
		tfTitle.setText(calc.getProjectData().getName());
		tfDesc.setText(calc.getProjectData().getDescriptions());
		tempWhC.setText(Double.toString(calc.getConditions().getTempWellhead()));
		tempBhC.setText(Double.toString(calc.getConditions().getTempBottomhole()));
		
		pressWhBara.setText(Double.toString(calc.getConditions().getPressWellhead()));
		pressBhBara.setText(Double.toString(calc.getConditions().getPressBottomhole()));
		
		gasCO2.setText(Double.toString(calc.getConditions().getGasCO2()));
		gasH2Smol.setText(Double.toString(calc.getConditions().getGasH2S()));
		NaClgl.setText(Double.toString(calc.getConditions().getEqNaCl()));
		N2.setText(Double.toString(calc.getConditions().getN2()));
		bicarbon.setText(Double.toString(calc.getConditions().getDisolveBicarbon()));
		stC.setText(Double.toString(calc.getConditions().getStandardTemp()));
		spBara.setText(Double.toString(calc.getConditions().getStandardPress()));
	}
	
	public void updateCondition() {
		
		calc.getProjectData().setName(tfTitle.getText());
		calc.getProjectData().setDescriptions(tfDesc.getText());
		double tempwh = Double.parseDouble(tempWhC.getText());
		double tempbh = Double.parseDouble(tempBhC.getText());
		calc.getConditions().setTempWellhead(tempwh);
		calc.getConditions().setTempBottomhole(tempbh);
		double press = Double.parseDouble(pressWhBara.getText());
		double pressb = Double.parseDouble(pressBhBara.getText());
		calc.getConditions().setPressWellhead(press);
		calc.getConditions().setPressBottomhole(pressb);
		
		calc.getConditions().setGasCO2(Double.parseDouble(gasCO2.getText()));
		calc.getConditions().setGasH2S(Double.parseDouble(gasH2Smol.getText()));
		calc.getConditions().setEqNaCl(Double.parseDouble(NaClgl.getText()));
		calc.getConditions().setN2(Double.parseDouble(N2.getText()));
		calc.getConditions().setDisolveBicarbon(Double.parseDouble(bicarbon.getText()));
		calc.getConditions().setStandardTemp(Double.parseDouble(stC.getText()));
		calc.getConditions().setStandardPress(Double.parseDouble(spBara.getText()));
	}
	
	
	public void initProduction() {
		oilFr.setText(Double.toString(calc.getProduction().getOilFlowrate()));
		oilApi.setText(Double.toString(calc.getProduction().getAPIGravity()));
		gasFr.setText(Double.toString(calc.getProduction().getGasFlowrate()));
		waterFr.setText(Double.toString(calc.getProduction().getWaterFlowrate()));
		liqHold.setText(Double.toString(calc.getProduction().getHoldupLiquid()));
		watercut.setText(Double.toString(calc.getProduction().getWatercut()));
	}
	
	public void updateProduction() {
		calc.getProduction().setOilFlowrate(Double.parseDouble(oilFr.getText()));
		calc.getProduction().setAPIGravity(Double.parseDouble(oilApi.getText()));
		calc.getProduction().setWaterFlowrate(Double.parseDouble(waterFr.getText()));
		calc.getProduction().setHoldupLiquid(Double.parseDouble(liqHold.getText()));
		calc.getProduction().setWatercut(Double.parseDouble(watercut.getText()));
	}
	
	public void initStell() {
		depth.setText(Double.toString(calc.getStell().getTubingDepth()));
		outd.setText(Double.toString(calc.getStell().getTubingOutDiameter()));
		thickness.setText(Double.toString(calc.getStell().getTubingThick()));
		weight.setText(Double.toString(calc.getStell().getWeight()));
		stellCr.setText(Double.toString(calc.getStell().getChromium()));
		stellC.setText(Double.toString(calc.getStell().getCarbon()));
		int stat = calc.getStell().getStellStats();
		if(stat == StellInfo.NORMALIZED) {
			rbNorm.setSelected(true);
		} else {
			rbQT.setSelected(true);
		}
	}
	
	public void updateStell() {
		calc.getStell().setTubingDepth(Double.parseDouble(depth.getText()));
		calc.getStell().setTubingOutDiameter(Double.parseDouble(outd.getText()));
		calc.getStell().setTubingThick(Double.parseDouble(thickness.getText()));
		calc.getStell().setWeight(Double.parseDouble(weight.getText()));
		calc.getStell().setChromium(Double.parseDouble(stellCr.getText()));
		calc.getStell().setCarbon(Double.parseDouble(stellC.getText()));
		
		if(rbNorm.isSelected()) {
			calc.getStell().setStellStats(StellInfo.NORMALIZED);
		} else {
			calc.getStell().setStellStats(StellInfo.QUNCHED_AND_TEMP);
		}
	}
	
	public void initAdditional() {
		inAvail.setText(Double.toString(calc.getAddInfo().getInhibitorAvail()));
		inEff.setText(Double.toString(calc.getAddInfo().getInhibitorEff()));
		erVel.setText(Double.toString(calc.getAddInfo().getErosionalVel()));
		if(calc.getAddInfo().getInhibitionStat() == Additional.FE_NONE) {
			inNone.setSelected(true);
		} else if (calc.getAddInfo().getInhibitionStat() == Additional.INHIB_CONT) {
			inContinu.setSelected(true);
		}
	}
	
	public void updateAdditional() {
		calc.getAddInfo().setInhibitorAvail(Double.parseDouble(inAvail.getText()));
		calc.getAddInfo().setInhibitorEff(Double.parseDouble(inEff.getText()));
		calc.getAddInfo().setErosionalVel(Double.parseDouble(erVel.getText()));
		
		if(inNone.isSelected()) {
			calc.getAddInfo().setInhibitionStat(Additional.INHIB_NONE);
		} else if(inContinu.isSelected()) {
			calc.getAddInfo().setInhibitionStat(Additional.INHIB_CONT);
		}
	}
	
	public void initAdv() {
		segment.setText(Integer.toString(calc.getProjectData().getTotalSegement()));
		exporsure.setText(Integer.toString(calc.getProjectData().getExporsureTime()));
		gravity.setText(Double.toString(calc.getProjectData().getGasGravity()));
		
	}
	
	public void updateAdv() {
		int before = calc.getAngle().getSize();
		int current = Integer.parseInt(segment.getText());
		calc.setSize(current);
		calc.getProjectData().setTotalSegement(current);
		calc.getProjectData().setExporsureTime(Integer.parseInt(exporsure.getText()));
		calc.getProjectData().setGasGravity(Double.parseDouble(gravity.getText()));
		int overhead = current - before;
		if(overhead > 0) {
			for(int i = 0 ; i < overhead; i++) {
				calc.getAngle().getAngles().add(0.d);
			}
		}
	}
	
}
