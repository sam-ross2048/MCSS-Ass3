import java.util.ArrayList;
import java.util.Random;

/**
 * The Patch class represents a patch on earth.
 * This patch can be empty, or it can have either
 * a black or white daisy on it, or it can have a herbivore.
 * Each patch has a temperature and an albedo.
 * The temporary temparture is used when diffusing
 * heat among patches.
 */
public class Patch {

	private Herbivore herbivore;
	private double temperature;
	private double temporaryTemp;
	private final double albedo;

	private Daisy daisy = null;

	public Patch(double albedo) {
		this.albedo = albedo;
		this.herbivore = null;
	}
	
	
	/*
	 * Identical to calc-temperature method in NetLogo.
	 * Currently assumes that daisies are the only organisms
	 * other than the surface with albedoes.
	 */
	public void calc_temperature(double solar_luminosity) {
		double absorbed_luminosity;
		double local_heating;

		if (this.daisy == null) {
			absorbed_luminosity = ((1 - this.albedo) * solar_luminosity);
		} else {
			absorbed_luminosity = ((1 - this.daisy.getAlbedo()) * solar_luminosity);
		}

		if (absorbed_luminosity > 0) {
			local_heating = 72 * Math.log(absorbed_luminosity) + 80;
		} else {
			local_heating = 80;
		}

		this.temperature = (this.temperature + local_heating) / 2;
	}

	/**
	 * This method increases the daisys' age by 1.
	 * If a daisy reaches its maximum age, then the daisy dies.
	 * Moreover, every daisy has a chance of producing a seed (another daisy)
	 * in one of its empty neighbouring patches.
	 * @param neighbours list of neighbours to patch
	 */
	public void checkSurvivability(Patch[] neighbours) {
		if (this.daisy == null) {
			return;
		}
		double seedThreshold;
		Random r = new Random();
		this.daisy.grow();

		if (this.daisy.getAge() >= this.daisy.MAX_AGE) {
			this.daisy = null;
			return;
		}

		seedThreshold = ((0.1457 * temperature) - (0.0032 * (temperature*temperature)) - 0.6443);

		if (r.nextDouble() < seedThreshold) {
			findDaisylessNeighbour(neighbours);
		}

	}

	/**
	 * This method first gets a list of all empty neighbouring patches.
	 * It then chooses a random patch of these empty patches to plant the seed.
	 * @param neighbours list of neighbours to patch
	 */
	private void findDaisylessNeighbour(Patch[] neighbours) {
		ArrayList<Patch> daisylessNeighbours = new ArrayList<>();

		for (Patch neighbour : neighbours) {
			if(neighbour.getDaisy() == null && neighbour.getHerbivore() == null) {
				daisylessNeighbours.add(neighbour);
			}
		}
		if (daisylessNeighbours.size() == 0) {
			return;
		}

		Random r = new Random();
		int chosenIndex = r.nextInt(daisylessNeighbours.size());
		Patch seedingPlace = daisylessNeighbours.get(chosenIndex);
		if (this.daisy instanceof WhiteDaisy) {
			seedingPlace.setDaisy(new WhiteDaisy(this.daisy.getAlbedo(), 0));
		} else {
			seedingPlace.setDaisy(new BlackDaisy(this.daisy.getAlbedo(), 0));
		}
	}

	/**
	 * This method increases the herbivores' age and hunger by 1.
	 * If a herbivore reaches its maximum age or maximum hunger,
	 * then the herbivore dies.
	 * Moreover, every herbivore has a chance of reproducing.
	 * The herbivore moves to a neighbouring patch.
	 * @param neighbours list of neighbours to patch
	 */
	public void updateHerbivore(Patch[] neighbours) {
		if (this.herbivore == null || this.herbivore.getMoved()) {
			return;
		}

		this.herbivore.grow();
		this.herbivore.incrementHunger();

		if (this.herbivore.getAge() >= Params.max_age || this.herbivore.getHunger() >= Params.HERBIVORE_STARVE_TIME) {
			this.herbivore = null;
			return;
		}
		// Possibly reproduces first
		findHerbivorelessNeighbours(neighbours, false);
		// then moves to the next herbivore-less patch
		findHerbivorelessNeighbours(neighbours, true);
	}

	/**
	 * Variable moving symbolises whether the herbivore is moving or not (i.e. reproducing).
	 * @param neighbours list of neighbours to patch
	 * @param moving whether the herbivore has already moved this tick or not
	 */
	private void findHerbivorelessNeighbours(Patch[] neighbours, boolean moving) {
		ArrayList<Patch> herbivorelessNeighbours = new ArrayList<>();

		for (Patch neighbour : neighbours) {
			if(neighbour.getHerbivore() == null) {
				herbivorelessNeighbours.add(neighbour);
			}
		}
		if (herbivorelessNeighbours.size() == 0) {
			return;
		}
		Random r = new Random();
		int chosenIndex = r.nextInt(herbivorelessNeighbours.size());
		Patch seedingPlace = herbivorelessNeighbours.get(chosenIndex);

		if (!moving) {
			double reproduceValue = r.nextDouble();
			if(reproduceValue <= Params.HERBIVORE_REPRODUCTION_RATE) {
				// Seed new herbivore. Share hunger between parent and offspring (i.e. multiply hunger by 2)
				seedingPlace.setHerbivore(new Herbivore(0, (this.herbivore.getHunger()*2)));
				this.herbivore.setHunger(this.herbivore.getHunger()*2);
				if (seedingPlace.getDaisy() != null) {
					seedingPlace.getHerbivore().eatDaisy();
				}
				seedingPlace.setDaisy(null);
			}
		} else {
			seedingPlace.setHerbivore(new Herbivore(this.herbivore.getAge(), this.herbivore.getHunger()));
			if (seedingPlace.getDaisy() != null) {
				seedingPlace.getHerbivore().eatDaisy();
				seedingPlace.getHerbivore().setMoved(true);
				seedingPlace.setDaisy(null);
			}
			this.herbivore = null;
		}
	}

	public void resetMoved() {
		if (this.herbivore == null ) {
			return;
		}
		this.herbivore.setMoved(false);
	}


	public Daisy getDaisy() {
		return this.daisy;
	}

	public void setDaisy(Daisy daisy) {
		if (daisy == null) {
			this.daisy = null;
			return;
		}
		this.daisy = daisy;
	}

	public double getTemperature() {
		return this.temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public double getTemporaryTemp() {
		return temporaryTemp;
	}

	public void setTemporaryTemp(double temporaryTemp) {
		this.temporaryTemp = temporaryTemp;
	}

	public Herbivore getHerbivore() {
		return this.herbivore;
	}

	public void setHerbivore(Herbivore herbivore) {
		this.herbivore = herbivore;
	}
}
