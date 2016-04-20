package simulation;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import model.Particle;
import model.SimulationData;
import parser.InformationParser;
import parser.OvitoFileInputGenerator;

public class Main {
	private static final String DYNAMIC_FILE_PATH = "doc/examples/Dynamic440.txt";
	private static final String STATIC_FILE_PATH = "doc/examples/Static440.txt";
	private static final long T = 100;
	private static final double FPU = 25;

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		SimulationData simulationData = parseSimulationData();
		if (simulationData == null) {
			return;
		}

		final OvitoFileInputGenerator ovito = new OvitoFileInputGenerator("doc/examples/result.txt");
		ovito.generateFile();
		EventDrivenSimulation simulation = new EventDrivenSimulation(T, 1.0/FPU, new EventDrivenSimulation.Listener() {

			@Override
			public void onFrameAvailable(SimulationData frame) {
				ovito.printSimulationInstance(frame);
			}
		});

		simulation.simulate(simulationData);
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
