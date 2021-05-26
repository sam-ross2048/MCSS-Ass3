
public abstract class Organism {
	
	public int age;
	
	public Organism(int age) {
		this.age = age;
	}

	public void grow() {
		this.age += 1;
	}
	
	
	public abstract void reproduce();
	public abstract boolean die();
	
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
	
