package calc;

public class ErBar {
	private double TC;
	private double PC;
	
	public ErBar(double tc, double pc) {
		setTC(tc);
		setPC(pc);
	}

	public double getTC() {
		return TC;
	}

	public void setTC(double tC) {
		TC = tC;
	}

	public double getPC() {
		return PC;
	}

	public void setPC(double pC) {
		PC = pC;
	}
}
