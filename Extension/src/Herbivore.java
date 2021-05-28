/**
 * This class represents herbivores.
 * Each herbivore has a hunger attribute.
 * If the herbivore eats a daisy then the hunger
 * is decreased.
 * However, if the herbivore is not eating, then
 * the hunger attribute will increase.
 * If the hunger reaches the HERBIVORE_STARVE_TIME,
 * the herbivore will die.
 */
public class Herbivore extends Organism {

    private int hunger;

    public Herbivore(int age, int hunger) {
        super(age);
        this.hunger = hunger;
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

    public String toString() {
        return "H";
    }
}
