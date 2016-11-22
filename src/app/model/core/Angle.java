package app.model.core;

import java.util.ArrayList;

public class Angle {
	private ArrayList<Double> angles;
	
	public Angle(int size) {
		angles = new ArrayList<Double>();
		for(int i = 0; i < size; i++) {
			angles.add((new Double(0)));
		}
	}
	
	public void setAngle(int idx, double val) {
		angles.set(idx, val);
	}
	
	public void setAngleAll(double val) {
		for(int i = 0; i < angles.size(); i++) {
			angles.add((new Double(0)));
		}
	}
	
	public double getAngleAt(int idx) {
		return angles.get(idx);
	}
	
	public ArrayList<Double> getAngles() {
		return angles;
	}
	
	public int getSize() {
		return angles.size();
	}
	
	public void setSize(int size) {
		for(int i = angles.size()-1; i >= size; i--) {
			angles.remove(i);
		}
	}
}
