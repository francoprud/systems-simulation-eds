package simulation;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import model.SimulationData;
import parser.InformationParser;
import parser.OvitoFileInputGenerator;

public class Main {
	private static final String DYNAMIC_FILE_PATH = "doc/examples/Dynamic100.txt";
	private static final String STATIC_FILE_PATH = "doc/examples/Static100.txt";
	private static final int T = 2000;
	private static final double animationTime = 0.1;
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		SimulationData simulationData = parseSimulationData();
		if (simulationData == null) {
			return;
		}
		EventDrivenSimulation simulation = new EventDrivenSimulation();
		OvitoFileInputGenerator ovito = new OvitoFileInputGenerator("doc/examples/result.txt");
		ovito.generateFile();
		ovito.printSimulationInstance(simulationData);
		
		for (int i = 0; i < T; i++) {
			simulation.simulate(simulationData);
			if (animationTime < simulation.getSimulationCurrentTime()) {
				ovito.printSimulationInstance(simulationData);
				// TODO: Check this operation
				simulation.setSimulationCurrentTime(simulation.getSimulationCurrentTime() - animationTime);
			}
		}
		ovito.endSimulation();
	}

	private static SimulationData parseSimulationData() {
		try {
			return InformationParser.generateCellIndexObject(DYNAMIC_FILE_PATH, STATIC_FILE_PATH).build();
		} catch (FileNotFoundException e) {
			System.err.println("Can not generate cell index object. Error: " + e.getMessage());
			return null;
		}
	}
}
