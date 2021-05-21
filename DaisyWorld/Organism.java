
public abstract class Organism {
	
	public int age;
	
	public Organism() {
		this.age = 0;
	}

	public void grow() {
		this.age += 1;
	}
	
	
	public abstract void reproduce();
	public abstract boolean die();
	
	public String toString() {
		return "O";
	}
}
	
