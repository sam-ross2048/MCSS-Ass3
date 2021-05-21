
public class Patch {

	public Organism organism;
	public double temperature;
	public double albedo;
	
	public Patch() {
		this.albedo = Params.albedo_surface;
		this.organism = null;
	}

}
