
public class Herbivore extends Organism{
	
	public double hunger;
	
	public Herbivore(double hunger) {
		super();
		this.hunger = hunger;
	}
	
	@Override
	public void reproduce() {
		
	}

	@Override
	public boolean die() {
		return false;
	}

}
