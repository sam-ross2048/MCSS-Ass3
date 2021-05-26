import java.util.ArrayList;
import java.util.Random;

public class Patch {

	private Organism organism;
	private double temperature;
	private double temporaryTemp;
	private double albedo;

	private Daisy daisy = null;

	public Patch() {
		this.albedo = Params.albedo_surface;
		this.organism = null;
	}
	
	
	/*
	 * Identical to calc-temperature method in NetLogo.
	 * Currently assumes that daisies are the only organisms
	 * other than the surface with albedoes.
	 */
	
	public void calc_temperature(double solar_luminosity) {
		double absorbed_luminosity = 0;
		double local_heating = 0;

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
		double seedThreshold = 0;
		Random r = new Random();
		this.daisy.incrementAge();

		if (this.daisy.getAge() >= daisy.MAX_AGE) {
			this.daisy = null;
			return;
		}

		seedThreshold = ((0.1457 * temperature) - (0.0032 * (temperature*temperature)) - 0.6443);

		if (r.nextDouble() < seedThreshold) {
			findDaisylessNeighbour(neighbours);
		}

	}

	private void findDaisylessNeighbour(Patch[] neighbours) {
		ArrayList<Patch> daisylessNeighbours = new ArrayList<Patch>();

		for (Patch neighbour : neighbours) {
			if(neighbour.getDaisy() != null) {
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
