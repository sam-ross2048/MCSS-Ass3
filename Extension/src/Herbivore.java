public class Herbivore extends Organism {

    private int hunger;
    private boolean moved;


    public Herbivore(int age, int hunger) {
        super(age);
        this.hunger = hunger;
        this.moved = false;
    }

    public int getHunger() {
        return this.hunger;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public void incrementHunger() {
        this.hunger += 1;
    }

    public void eatDaisy() {
        this.hunger -= 25;
    }

    public boolean getMoved() {
        return this.moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public String toString() {
        return "H";
    }
}
