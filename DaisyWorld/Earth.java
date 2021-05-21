import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Earth {

	public Patch[][] earth;
	public Random random;
	public static int num_whites;
	public static int num_blacks;
	
	public Earth(int seed) {
		this.earth = new Patch[Params.surface_x][Params.surface_y];
		this.random = new Random(seed);
		Earth.num_whites = 0;
		Earth.num_blacks = 0;
	}
	
	public Earth() {
		this.earth = new Patch[Params.surface_x][Params.surface_y];
		int i = 0;
		int j = 0;
		while (i < Params.surface_x) {
			while (j < Params.surface_y) {
				this.earth[i][j] = new Patch();
				j++;
			}
			i++;
			j=0;
		}
		this.random = new Random();
		Earth.num_whites = 0;
		Earth.num_blacks = 0;
	}
	
	/*
	 * This will add Black and White daisies to the Earth until greater than the percent
	 * specified in the Params file. This currently will not work if the number of total
	 * patches is odd and each is allocated at 50%. Right now this should only be run
	 * when the Earth is originally empty.
	*/
	public void seed_randomly() {
		int number_of_patches = Params.surface_x * Params.surface_y;
		List<Integer> range = IntStream.rangeClosed(0, number_of_patches-1)
			    .boxed().collect(Collectors.toList()); //Generate a 1D matrix of all patches
		while ((( (double) Earth.num_whites / (double) number_of_patches)*100) 
				< Params.percent_white) { //While the number of whites on Earth is less than the initial percent
			int rand_int = random.nextInt(range.size()); //Choose a random value
			int rand_patch = range.get(rand_int); //Get the patch that corresponds with that value
			range.remove(rand_int); //Remove it so we cannot select it again
			int x_pos = rand_patch / Params.surface_x; //X Position of Daisy
			int y_pos = rand_patch % Params.surface_y; //Y Position of Daisy
			this.earth[x_pos][y_pos].organism = new Daisy(Color.WHITE); //Add a White daisy
			Earth.num_whites++;
		}
		while ((((double) Earth.num_blacks/ (double) number_of_patches)*100) < Params.percent_black) {
			int rand_int = random.nextInt(range.size()); //Repeat the same steps for black flowers
			int rand_patch = range.get(rand_int);
			range.remove(rand_int);
			int x_pos = rand_patch / Params.surface_x;
			int y_pos = rand_patch % Params.surface_y;
			this.earth[x_pos][y_pos].organism = new Daisy(Color.BLACK);
			Earth.num_blacks++;
		}
	}
	
	public String toString() {
		StringBuilder res = new StringBuilder();
		String newline = System.getProperty("line.separator");
		int i = 0;
		int j = 0;
		while (i < Params.surface_x) {
			while (j < Params.surface_y) {
				res.append("[");
				if (this.earth[i][j].organism == null) {
					res.append(".");
				}
				else {
					res.append(this.earth[i][j].organism.toString());
				}
				res.append("]");
				j++;
			}
			res.append(newline);
			i++;
			j=0;
		}
		return res.toString();
	}
	
	public static void main(String[] args) {
		Earth earth = new Earth();
		earth.seed_randomly();
		System.out.println(earth.toString());
	}
}
