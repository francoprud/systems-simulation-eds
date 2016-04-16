package parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Scanner;

import model.Particle;
import model.SimulationData;

public class InformationParser {
	public static SimulationData.Builder generateCellIndexObject(String dynamicFilePath, String staticFilePath)
			throws FileNotFoundException {
		SimulationData.Builder builder = SimulationData.Builder.create();

		InputStream dynamicIS = new FileInputStream(dynamicFilePath);
		Scanner dynamicScanner = new Scanner(dynamicIS);
		dynamicScanner.useLocale(Locale.US);

		InputStream staticIS = new FileInputStream(staticFilePath);
		Scanner staticScanner = new Scanner(staticIS);
		staticScanner.useLocale(Locale.US);

		int particlesAmount = staticScanner.nextInt();
		double spaceDimension = staticScanner.nextDouble();
		builder = builder.withParticlesAmount(particlesAmount)
						 .withSpaceDimension(spaceDimension);

		for (int i = 1; i <= particlesAmount; i++) {
			double radius = staticScanner.nextDouble();
			double mass = staticScanner.nextDouble();
			double x = dynamicScanner.nextDouble();
			double y = dynamicScanner.nextDouble();
			double angle = dynamicScanner.nextDouble();
			double velocity = dynamicScanner.nextDouble();
			Particle particle = new Particle(i, x, y, radius, velocity, angle, mass);
			builder = builder.withParticle(particle);
		}
		
		dynamicScanner.close();
		staticScanner.close();
		
		return builder;
	}
}