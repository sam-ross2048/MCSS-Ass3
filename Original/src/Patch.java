/**
 * The Patch class represents a patch on earth.
 * This patch can be empty, or it can have either
 * a black or white daisy on it.
 * Each patch has a temperature and an albedo.
 * The temporary temparture is used when diffusing
 * heat among patches.
 */
import java.util.ArrayList;
import java.util.Random;

public class Patch {

	private double temperature;
	private double temporaryTemp;
	private final double albedo;

	private Daisy daisy = null;

	public Patch(double albedo) {
		this.albedo = albedo;
	}
	
	/**
	 * Identical to calc-temperature method in NetLogo.
	 * Currently assumes that daisies are the only organisms
	 * other than the surface with albedoes.
	 * @param solar_luminosity
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
	 * @param neighbours
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
	 * @param neighbours
	 */
	private void findDaisylessNeighbour(Patch[] neighbours) {
		ArrayList<Patch> daisylessNeighbours = new ArrayList<>();

		for (Patch neighbour : neighbours) {
			if(neighbour.getDaisy() == null) {
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

	public Daisy getDaisy() {
		return this.daisy;
	}

	public void setDaisy(Daisy daisy) {
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
}
