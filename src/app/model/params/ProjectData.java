package app.model.params;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import app.model.core.Angle;
import app.model.info.Vars;
import utils.IOFile;

public class ProjectData {
	private String name;
	private String descriptions;
	private Conditions condition;
	private ProductionData production;
	private StellInfo stell;
	private Angle angle;
	private Additional additional;
	private int totalSegement;
	private int exporsureTime;
	private double gasGravity;

	public ProjectData() {
		name = "";
		descriptions = "";
		condition = new Conditions();
		production = new ProductionData();
		stell =  new StellInfo();
		angle = new Angle(Vars.TotalSegment);
		additional = new Additional();
		totalSegement  = Vars.TotalSegment;
		exporsureTime = Vars.EXPORSURE_TIME;
		gasGravity = Vars.GasGravity;
	}
	
	public ProjectData(String name, String desc, Conditions cond, ProductionData production, StellInfo stell,
			Angle angle, Additional additional, int segment, int expTime, double gasGrav) {
		this.name = name;
		this.descriptions = desc;
		this.condition = cond;
		this.production = production;
		this.stell =  stell;
		this.angle = angle;
		this.additional = additional;
		this.totalSegement  = segment;
		this.exporsureTime = expTime;
		this.gasGravity = gasGrav;

	}
	
	public ProjectData(ProjectData other) {
		setProject(other);
	}
	
	public ProjectData(String path) {
		setProject(path);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	public Angle getAngle() {
		return angle;
	}

	public void setAngle(Angle angle) {
		this.angle = angle;
	}

	public Conditions getCondition() {
		return condition;
	}

	public void setCondition(Conditions condition) {
		this.condition = condition;
	}

	public ProductionData getProduction() {
		return production;
	}

	public void setProduction(ProductionData production) {
		this.production = production;
	}

	public StellInfo getStell() {
		return stell;
	}

	public void setStell(StellInfo stell) {
		this.stell = stell;
	}

	public int getTotalSegement() {
		return totalSegement;
	}

	public void setTotalSegement(int totalSegement) {
		this.totalSegement = totalSegement;
		Vars.TotalSegment = totalSegement;
	}

	public int getExporsureTime() {
		return exporsureTime;
	}

	public void setExporsureTime(int exporsureTime) {
		this.exporsureTime = exporsureTime;
		Vars.EXPORSURE_TIME = exporsureTime;
	}

	public double getGasGravity() {
		return gasGravity;
	}

	public void setGasGravity(double gasGravity) {
		this.gasGravity = gasGravity;
		Vars.GasGravity = gasGravity;
	}

	public Additional getAdditional() {
		return additional;
	}

	public void setAdditional(Additional additional) {
		this.additional = additional;
	}
	
	public String toString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(this);
        return json;
	}
	
	public void setProject(String path) {
		Gson gson = new Gson();
        try (Reader reader = new FileReader(path)) {
			// Convert JSON to Java Object
            ProjectData other = gson.fromJson(reader, ProjectData.class);
            setProject(other);
        } catch (IOException e) {
            e.printStackTrace();
        }

	}
	
	public void save(String fileName, String path) {
		save(path+"/"+fileName);
	}
	
	public void save(String absPath) {
		IOFile io = new IOFile();
		io.writeText(absPath, this.toString());
	}
	
	public void setProject(ProjectData other) {
		this.name = other.name;
		this.descriptions = other.descriptions;
		this.condition = other.condition;
		this.production = other.production;
		this.stell =  other.stell;
		this.angle = other.angle;
		this.additional = other.additional;
		this.totalSegement  = other.totalSegement;
		this.exporsureTime = other.exporsureTime;
		this.gasGravity = other.gasGravity;
	}
}