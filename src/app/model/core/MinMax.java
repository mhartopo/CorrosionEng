package app.model.core;

import java.util.ArrayList;

public class MinMax {
	private double min;
	private double max;
	
	public MinMax(double min, double max) {
		this.min = min;
		this.max = max;
	}
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}
	
	public static MinMax getMinMax(ArrayList<Double> arr) {
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		
		for(Double d : arr) {
			if(d < min) {
				min = d;
			} else if(d > max) {
				max = d;
			}
		}
		return new MinMax(min, max);
	}
	
	public static MinMax getMinMaxTemp(ArrayList<Temperature> arr) {
		ArrayList<Double> temp = new ArrayList<>();
		for(Temperature t : arr) {
			temp.add(t.getC());
		}
		return getMinMax(temp);
	}
	
	public static MinMax getMinMaxPress(ArrayList<Pressure> arr) {
		ArrayList<Double> temp = new ArrayList<>();
		for(Pressure p : arr) {
			temp.add(p.getBara());
		}
		return getMinMax(temp);
	}
}
