/**
 * This is the main class where the simulation occurs.
 * To run the program, run: javac Sim.java
 * Then run: java Sim with the appropriate command line arguments.
 * Example:  
 * java Sim 0.25 0.25 0.75 0.25 0.8 0.4 100 our
 */
public class Sim {

	public static void main(String [] args) {
		double startPercentWhite = Double.parseDouble(args[0]);
		double startPercentBlack = Double.parseDouble(args[1]);
		double albedoWhite = Double.parseDouble(args[2]);
		double albedoBlack = Double.parseDouble(args[3]);
		double solarLuminosity = Double.parseDouble(args[4]);
		double albedoSurface = Double.parseDouble(args[5]);
		int maxTicks = Integer.parseInt(args[6]);

		String scenario;

		switch (args[7]) {
			case "ramp": scenario = Params.RAMP;
			break;
			case "low": scenario = Params.LOW;
			break;
			case "our": scenario = Params.OUR;
			break;
			case "high": scenario = Params.HIGH;
			break;
			default: scenario = Params.CURRENT;
		}

		if (startPercentWhite + startPercentBlack > 1) {
			System.out.println("Invalid Input. Please ensure that the daisy percentages do not exceed 100%");
			return;
		}

		Earth earth = new Earth(startPercentWhite, startPercentBlack, albedoWhite, albedoBlack, solarLuminosity, albedoSurface, maxTicks,
				scenario);

		earth.run();
		
		System.out.println("Final State:");
		System.out.println("-----------------------");
		System.out.println(earth);

	}
}
