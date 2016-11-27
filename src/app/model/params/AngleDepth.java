package app.model.params;

import java.util.ArrayList;

public class AngleDepth {
	private double angle;
	private double depth;
	
	public AngleDepth(double angle, double depth) {
		this.angle = angle;
		this.depth = depth;
	}
	public double getAngle() {
		return angle;
	}
	public void setAngle(double angle) {
		this.angle = angle;
	}
	public double getDepth() {
		return depth;
	}
	public void setDepth(double depth) {
		this.depth = depth;
	}
	
	public static ArrayList<AngleDepth> makeAngleDepth(ArrayList<Double> angle, ArrayList<Double> depth, int size){
		ArrayList<AngleDepth> res =  new ArrayList<>();
		for(int i = 0; i < size; i++) {
			res.add(new AngleDepth(angle.get(i), depth.get(i)));
		}		
		return res;
	}
}
