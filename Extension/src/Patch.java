import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

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

	public void updateHerbivore(Patch[] neighbours) {
		if (this.herbivore == null) {
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

	// Variable moving symbolises whether the herbivore is moving or not (i.e. reproducing).
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
				seedingPlace.setDaisy(null);
			}
			this.herbivore = null;
		}
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
