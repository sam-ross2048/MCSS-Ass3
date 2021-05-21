
public class Daisy extends Organism{

	public Color color;
	
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
	
	//Outputs the first letter of the color of the Daisy
	public String toString() {
		return this.color.toString().substring(0,1);
	}
	
}
