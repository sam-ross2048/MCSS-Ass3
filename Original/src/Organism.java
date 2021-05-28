/**
 * The Organism class is an abstract class that represents
 * a living entity.
 */
public abstract class Organism {
	
	public int age;
	
	public Organism(int age) {
		this.age = age;
	}

	// Increases the age of the organism by 1
	public void grow() {
		this.age += 1;
	}
	
	public String toString() {
		return "O";
	}

	public int getAge() {
		return age;
	}
}
	
