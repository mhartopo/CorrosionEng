package app.model.params;

public class Additional {
	
	public static final int INHIB_NONE = 0;
	public static final int INHIB_CONT = 1;
	public static final int INHIB_SEQ = 2;
	public static final int FE_NONE = 0;
	public static final int FE_SUPERSATURATED = 1;
	
	private int inhibitionStat;
	private double inhibitorAvail;
	private int inhibitionPeriod;
	private double inhibitorEff;
	private int FeStatus;
	private double aceticAcid;
	private double erosionalVel;
	
	public Additional() {
		setInhibitionStat(0);
		setInhibitorAvail(0);
		setInhibitionPeriod(0);
		setInhibitorEff(0);
		setFeStatus(0);
		setAceticAcid(0);
		setErosionalVel(122);
	}

	public int getInhibitionStat() {
		return inhibitionStat;
	}

	public void setInhibitionStat(int inhibitionStat) {
		this.inhibitionStat = inhibitionStat;
	}

	public double getInhibitorAvail() {
		return inhibitorAvail;
	}

	public void setInhibitorAvail(double inhibitorAvail) {
		this.inhibitorAvail = inhibitorAvail;
	}

	public int getInhibitionPeriod() {
		return inhibitionPeriod;
	}

	public void setInhibitionPeriod(int inhibitionPeriod) {
		this.inhibitionPeriod = inhibitionPeriod;
	}

	public int getFeStatus() {
		return FeStatus;
	}

	public void setFeStatus(int feStatus) {
		FeStatus = feStatus;
	}

	public double getAceticAcid() {
		return aceticAcid;
	}

	public void setAceticAcid(double aceticAcid) {
		this.aceticAcid = aceticAcid;
	}

	public double getErosionalVel() {
		return erosionalVel;
	}

	public void setErosionalVel(double erosionalVel) {
		this.erosionalVel = erosionalVel;
	}

	public double getInhibitorEff() {
		return inhibitorEff;
	}

	public void setInhibitorEff(double inhibitorEff) {
		this.inhibitorEff = inhibitorEff;
	}
}
