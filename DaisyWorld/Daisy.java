
public abstract class Daisy extends Organism{

	private double albedo;
	public final int MAX_AGE = Params.max_age;
	
	public Daisy(double albedo, int age) {
		super(age);
		this.albedo = albedo;
		//this.color = color;
	}
	
	@Override
	public void reproduce() {
		
	}

	@Override
	public boolean die() {
		return false;
	}
	
	//Outputs the first letter of the color of the Daisy
	//public String toString() {
		//return this.color.toString().substring(0,1);
	//}

	public double getAlbedo() {
		return this.albedo;
	}


}
