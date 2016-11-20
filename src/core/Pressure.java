package core;

public class Pressure {
	private double baraval;
	private final double BARA2PSIA = 14.5037743897283;
	private final double BARA2KPA = 100;
	public Pressure() {
		baraval = 0;
	}
	
	public Pressure(double val, String unit) {
		setValue(val, unit);
	}
	
	public void setValue(double val, String unit) {
		String unitp = unit.toLowerCase();
		if(unitp.equals("kpa")) {
			baraval = val / BARA2KPA;
		} else if (unitp.equals("psia")) {
			baraval = val / BARA2PSIA;
		} else {
			baraval = val;
		}
	}
	
	public double getBara() {
		return baraval;
	}
	
	public double getKpa() {
		return baraval * BARA2KPA;
	}
	
	public double getPsia() {
		return baraval * BARA2PSIA;
	}
}
