/**
 * The Organism class is an abstract class that represents
 * a living entity.
 */
public abstract class Organism {
	
	public int age;
	
	public Organism(int age) {
		this.age = age;
	}

	public void grow() {
		this.age += 1;
	}
	
	public String toString() {
		return "O";
	}

	public int getAge() {
		return age;
	}

	public void incrementAge() {
		age += 1;
	}
}
	
