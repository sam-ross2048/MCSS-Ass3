

public class Patch {

	public Organism organism;
	public double temperature;
	public double albedo;
	
	public Patch() {
		this.albedo = Params.albedo_surface;
		this.organism = null;
	}
	
	
	/*
	 * Identical to calc-temperature method in NetLogo.
	 * Currently assumes that daisies are the only organisms
	 * other than the surface with albedoes.
	 */
	
	public void calc_temperature(double solar_luminosity){
		double absorbed_luminosity = 0;
		double local_heating = 0;
		if (!(this.organism instanceof Daisy)) {
			absorbed_luminosity = 
					((1 - this.albedo) * solar_luminosity);
		}
		else {
			Daisy daisy = (Daisy) this.organism;
			absorbed_luminosity = 
					((1 - daisy.color.albedo()) * solar_luminosity);
		}
		if (absorbed_luminosity > 0) {
			local_heating = 
					(72 * Math.log(absorbed_luminosity) + 80);
		}
		else {
			local_heating = 80;
		}
		this.temperature = ((this.temperature + local_heating) / 2);
	}

}
