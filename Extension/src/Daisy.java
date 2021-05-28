
public abstract class Daisy extends Organism{

	private final double albedo;
	public final int MAX_AGE = Params.max_age;
	
	public Daisy(double albedo, int age) {
		super(age);
		this.albedo = albedo;
	}

	public double getAlbedo() {
		return this.albedo;
	}


}
