package simulation;

import java.io.FileNotFoundException;

import model.Particle;
import model.SimulationData;
import parser.InformationParser;

public class Main {
	private static final String DYNAMIC_FILE_PATH = "doc/examples/Dynamic300.txt";
	private static final String STATIC_FILE_PATH = "doc/examples/Static300.txt";
	private static final int M = 1;
	private static final int T = 3000;
	private static final int animationTime = 1;
	
	public static void main(String[] args) {
		SimulationData simulationData = parseSimulationData();
		if (simulationData == null || !isAValidMValue(simulationData)) {
			return;
		}
		EventDrivenSimulation simulation = new EventDrivenSimulation();
		
		for (int i = 0; i < T; i++) {
			simulation.simulate(simulationData);
			if (animationTime < simulation.getSimulationCurrentTime()) {
//				print
//				reset counter of simulation
			}
		}
	}

	private static SimulationData parseSimulationData() {
		try {
			return InformationParser.generateCellIndexObject(DYNAMIC_FILE_PATH, STATIC_FILE_PATH).build();
		} catch (FileNotFoundException e) {
			System.err.println("Can not generate cell index object. Error: " + e.getMessage());
			return null;
		}
	}
	
	private static boolean isAValidMValue(SimulationData simulationData) {
		double spaceDimension = simulationData.getSpaceDimension();
		double r = simulationData.getInteractionRadius();
		if ( spaceDimension / M > r + 2 * getMaximumRadius(simulationData)) {
			return true;
		} else {
			System.err.println("Not a valid M value.");
			return false;
		}
	}

	private static Double getMaximumRadius(SimulationData simulationData) {
		Double max = 0.0;
		for (Particle particle : simulationData.getParticles()) {
			if (max < particle.getRadius()) {
				max = particle.getRadius();
			}
		}
		return max;
	}
}
