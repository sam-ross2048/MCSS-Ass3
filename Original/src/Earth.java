import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.Buffer;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Earth {

	private final Patch[][] earth = new Patch[Params.surface_x][Params.surface_y];
	private final Random random = new Random();
	private int numWhites;
	private int numBlacks;
	private int ticks;
	private static final int MAX_AGE = Params.max_age;
	private double globalTemp = 0;
	private final double albedoWhite;
	private final double albedoBlack;
	private double solarLuminosity;
	private final int maxTicks;
	private final String scenario;
	private File outputCSV;
	private BufferedWriter outputBW;
	


	public Earth(double startPercentWhite, double startPercentBlack, double albedoWhite, double albedoBlack,
				 double solarLuminosity, double albedoSurface, int maxTicks, String scenario) {
		this.numWhites = (int) Math.floor(Params.surface_x * Params.surface_y * startPercentWhite);
		this.numBlacks = (int) Math.floor(Params.surface_x * Params.surface_y * startPercentBlack);
		this.albedoWhite = albedoWhite;
		this.albedoBlack = albedoBlack;
		this.solarLuminosity = solarLuminosity;
		this.maxTicks = maxTicks;
		this.scenario = scenario;

		this.init(albedoSurface);
	}

	public void init(double albedoSurface) {

		for(int i=0; i < Params.surface_x; i++) {
			for (int j = 0; j < Params.surface_y; j++) {
				earth[i][j] = new Patch(albedoSurface);
			}
		}
		seed_randomly(numWhites, numBlacks);

		switch (scenario) {
			case Params.RAMP: solarLuminosity = 0.8;
			case Params.LOW: solarLuminosity = 0.6;
			case Params.OUR: solarLuminosity = 1.0;
			case Params.HIGH: solarLuminosity = 1.4;
			default: {
			}
		}

		updatePatchTemp();
		calcGlobalTemp();
		setupCSV();
	}


	public void run() {
		while(ticks < maxTicks) {
			countDaisies();
			updatePatchTemp();
			diffuse();
			checkSurvivability();
			calcGlobalTemp();
			ticks++;

			if (scenario.equals(Params.RAMP)) {
				if (ticks > 200 && ticks <= 400) {
					solarLuminosity = new BigDecimal(solarLuminosity + 0.005).setScale(4, RoundingMode.HALF_UP).doubleValue() ;
				} else if (ticks > 600 && ticks <= 850) {
					solarLuminosity = new BigDecimal(solarLuminosity - 0.0025).setScale(4, RoundingMode.HALF_UP).doubleValue();
				}
			}
			writeCSV();
		}
	}

	private void checkSurvivability() {
		for (int i=0; i < Params.surface_x; i++) {
			for (int j = 0; j < Params.surface_y; j++) {
				earth[i][j].checkSurvivability(findNeighbours(i,j));
			}
		}
	}

	private void diffuse() {

		for (int i=0; i < Params.surface_x; i++) {
			for (int j = 0; j < Params.surface_y; j++) {
				earth[i][j].setTemporaryTemp(0);
			}
		}

		for (int i=0; i < Params.surface_x; i++) {
			for (int j=0; j < Params.surface_y; j++) {
				Patch[] neighbours = findNeighbours(i, j);
				double DIFFUSION_RATE = 0.5;
				double diffusionTemp = earth[i][j].getTemperature() * DIFFUSION_RATE;
				earth[i][j].setTemperature(earth[i][j].getTemperature() - diffusionTemp);

				for (Patch neighbour : neighbours) {
					neighbour.setTemporaryTemp(neighbour.getTemporaryTemp() + diffusionTemp*0.125);
				}
			}
		}

		for (int i=0; i < Params.surface_x; i++) {
			for (int j = 0; j < Params.surface_y; j++) {
				earth[i][j].setTemperature(earth[i][j].getTemperature() + earth[i][j].getTemporaryTemp());
			}
		}
	}

	private Patch[] findNeighbours(int x, int y) {
		Patch[] neighbours = new Patch [8];

		int leftX = (x - 1 + Params.surface_x) % Params.surface_x;
		int rightX = (x + 1) % Params.surface_x;
		int aboveY = (y - 1 + Params.surface_y) % Params.surface_y;
		int belowY = (y + 1) % Params.surface_y;

		neighbours[0] = earth[leftX][y];
		neighbours[1] = earth[leftX][aboveY];
		neighbours[2] = earth[leftX][belowY];
		neighbours[3] = earth[x][aboveY];
		neighbours[4] = earth[x][belowY];
		neighbours[5] = earth[rightX][aboveY];
		neighbours[6] = earth[rightX][y];
		neighbours[7] = earth[rightX][belowY];

		return neighbours;
	}

	/*
	 * This will add Black and White daisies to the Earth until greater than the percent
	 * specified in the Params file. This currently will not work if the number of total
	 * patches is odd and each is allocated at 50%. Right now this should only be run
	 * when the Earth is originally empty.
	*/
	private void seed_randomly(int numWhites, int numBlacks) {
		int x,y;
		for (int i=0; i < numWhites; i++) {
			x = random.nextInt(Params.surface_x);
			y = random.nextInt(Params.surface_y);
			if (earth[x][y].getDaisy() == null) {
				placeWhite(x, y);
			} else {
				i--;
			}
		}
		for (int i=0; i < numBlacks; i++) {
			x = random.nextInt(Params.surface_x);
			y = random.nextInt(Params.surface_y);
			if (earth[x][y].getDaisy() == null) {
				placeBlack(x, y);
			} else {
				i--;
			}
		}
	}

	private void placeWhite(int x, int y) {
		WhiteDaisy daisy = new WhiteDaisy(albedoWhite, random.nextInt(MAX_AGE));
		earth[x][y].setDaisy(daisy);
	}

	private void placeBlack(int x, int y) {
		BlackDaisy daisy = new BlackDaisy(albedoBlack, random.nextInt(MAX_AGE));
		earth[x][y].setDaisy(daisy);
	}

	private void updatePatchTemp() {
		for(int i=0;i < Params.surface_x; i++) {
			for(int j=0; j < Params.surface_y; j++) {
				earth[i][j].calc_temperature(solarLuminosity);
			}
		}
	}

	private void calcGlobalTemp() {
		double total = 0;
		for(int i=0;i < Params.surface_x; i++) {
			for (int j = 0; j < Params.surface_y; j++) {
				total += earth[i][j].getTemperature();
			}
		}
		this.globalTemp = total / (Params.surface_x * Params.surface_y);
	}

	private void countDaisies() {
		int white = 0, black = 0;

		for(int i=0;i < Params.surface_x; i++) {
			for (int j=0;j < Params.surface_y; j++) {
				if (earth[i][j].getDaisy() != null) {
					if(earth[i][j].getDaisy() instanceof WhiteDaisy) {
						white++;
					} else if (earth[i][j].getDaisy() instanceof BlackDaisy){
						black++;
					}
				}
			}
		}
		this.numWhites = white;
		this.numBlacks = black;
	}

	private void setupCSV() {
		outputCSV = new File("output.csv");

		try {
			outputBW = new BufferedWriter(new FileWriter(outputCSV, false));

			outputBW.write("ticks,numWhites,numBlacks,globalTemp,solarLuminosity");
			outputBW.newLine();
			outputBW.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeCSV() {
		try {
			outputBW = new BufferedWriter(new FileWriter(outputCSV, true));

			outputBW.write(ticks+","+numWhites+","+numBlacks+","+globalTemp+","+solarLuminosity);
			outputBW.newLine();
			outputBW.close();
		} catch (IOException e) {
			e.printStackTrace();
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
				if (this.earth[i][j].getDaisy() == null) {
					res.append(".");
				}
				else {
					res.append(this.earth[i][j].getDaisy().toString());
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
}
