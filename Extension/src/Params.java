
public class Params {
	
    // the percent of white daisies in the initial setup
	public final static int percent_white = 25;
	
    // the percent of black daisies in the initial setup
	public final static int percent_black = 25;

    // the albedo of the white daisies
	public final static double albedo_white = 0.75;
	
    // the albedo of the black daisies
	public final static double albedo_black = 0.75;
	
    // the solar luminiosity
	public final static double solar_luminosity = 0.800;
	
    // the albedo of the surface
	public final static double albedo_surface = 0.40;
	
    // the number of surface tiles for the x-axis
	public final static int surface_x = 29;
	

    // the number of surface tiles for the y-axis
	public final static int surface_y = 29;
	
	// the max age for all organisms
	public final static int max_age = 25;

	public final static String CURRENT = "maintain current luminosity";

	public final static String RAMP = "ramp-up-ramp-down";

	public final static String LOW = "low solar luminosity";

	public final static String OUR = "our solar luminosity";

	public final static String HIGH = "high solar luminosity";

	public final static double HERBIVORE_REPRODUCTION_RATE = 0.05;

	public final static int HERBIVORE_STARVE_TIME = 50;

}
