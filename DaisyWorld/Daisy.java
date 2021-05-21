
public class Daisy extends Organism{

	public double albedo;
	public Color color;
	
//	public Daisy(double albedo, Color color) {
//		super();
//		this.albedo = albedo;
//		this.color = color;
//	}
	
	public Daisy(Color color) {
		super();
		this.color = color;
	}
	
	@Override
	public void reproduce() {
		
	}

	@Override
	public boolean die() {
		return false;
	}
	
}
