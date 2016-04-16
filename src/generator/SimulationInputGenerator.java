package generator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import structure.RandomPopper;

public class SimulationInputGenerator {
	private static final double SPACE_DIMENSION = 0.5;
	private static final double SMALL_RADIUS = 0.005;
	private static final double BIG_RADIUS = 0.05;
	private static final double SMALL_MASS = 1;
	private static final double BIG_MASS = 100;

	public static void generateRandomInput(int N, MyPoint xVelocityInterval, MyPoint yVelocityInterval)
			throws FileNotFoundException, UnsupportedEncodingException {

		if (N >= 2400)
			throw new IllegalArgumentException("no more than 2400 particles");
		PrintWriter dynamicWriter = new PrintWriter("doc/examples/Dynamic" + N + ".txt", "UTF-8");
		PrintWriter staticWriter = new PrintWriter("doc/examples/Static" + N + ".txt", "UTF-8");
		//
		// staticWriter.println(N);
		// staticWriter.println(SPACE_DIMENSION);

		// la particula grande va a ocupar 10x10 celdas peque;as. la explicacion
		// es la siguiente:
		// 0,5 es el espacio. 0,005 es el radio peque;o. cada celda va a ser de
		// 2*0,005 = 0,01
		// 0,5/0,01 = 50 celdas. Cuantas de estas celdas de 0,01 hacen falta
		// para encerrar a la particula grande?
		// la particula grande es de 0,05 de radio. 2*0,05 = 0,1. 0,1/0,01 = 10
		// celdas de lado.
		// la particula grande va a ocupar 10x10 celdas peque;as
		double bigParticleCells = 10;

		// para asegurarnos que la particula grande va a entrar en el espacio
		// hay que restringir si x, y
		// la grilla es de 50x50, y las posiciones en esta grilla las estamos
		// tomando como posiciones enteras
		// que al ser multiplicadas por SMALL_RADIUS nos da la coordenada en el
		// espacio.
		// si la particula grande la spawneamos a partir de una celda xy hacia
		// arriba y a la derecha entonces
		// estas posiciones xy tienen que ser menor o igual a 45.
		double bigParticleXIndex = PointGenerator.randomBetween(0, (int) (50 - bigParticleCells));
		double bigParticleYIndex = PointGenerator.randomBetween(0, (int) (50 - bigParticleCells));

		System.out.println("big x: " + bigParticleXIndex);
		System.out.println("big y: " + bigParticleYIndex);

		printParticleLine(staticWriter, dynamicWriter,
				new MyPoint(bigParticleXIndex * (2 * SMALL_RADIUS), bigParticleYIndex * (2 * SMALL_RADIUS)),
				new MyPoint(0, 0), new MyPoint(0, 0), BIG_RADIUS, BIG_MASS);

		MyRectangle bigParticleRect = new MyRectangle(new MyPoint(bigParticleXIndex, bigParticleYIndex),
				new MyPoint(bigParticleXIndex + bigParticleCells - 1, bigParticleYIndex + bigParticleCells - 1));

		System.out.println(bigParticleRect);

		// despues del for en esta lista quedan todos los points en donde es
		// valido
		// posicionar una particula teniendo la garantia de que no va a pisar a
		// la grande ni a otra chica
		List<MyPoint> validPoints = new ArrayList<MyPoint>();

		for (double x = 0; x * (2 * SMALL_RADIUS) < SPACE_DIMENSION; x++) {
			for (double y = 0; y * (2 * SMALL_RADIUS) < SPACE_DIMENSION; y++) {
				MyPoint p = new MyPoint(x, y);
				if (!bigParticleRect.containsPointBordersInclusive(p)) {
					p.x = p.x * (2 * SMALL_RADIUS) + SMALL_RADIUS;
					p.y = p.y * (2 * SMALL_RADIUS) + SMALL_RADIUS;
					validPoints.add(p);
				}
			}
		}

		RandomPopper<MyPoint> pointRandomPopper = new RandomPopper<>(validPoints);
		System.out.println("valid points size: " + validPoints.size());

		for (int i = 0; i < N; i++) {
			MyPoint p = pointRandomPopper.randomPop();
			printParticleLine(staticWriter, dynamicWriter, p, xVelocityInterval, yVelocityInterval, SMALL_RADIUS,
					SMALL_MASS);
		}
		// for (MyPoint p: validPoints) {
		// System.out.println(p);
		// }

		staticWriter.close();
		dynamicWriter.close();
	}

	private static void printParticleLine(PrintWriter staticWriter, PrintWriter dynamicWriter, MyPoint position,
			MyPoint xVelocityInterval, MyPoint yVelocityInterval, double radius, double mass) {
		double xVelocity = PointGenerator.randomBetween(xVelocityInterval.x, xVelocityInterval.y);
		double yVelocity = PointGenerator.randomBetween(yVelocityInterval.x, yVelocityInterval.y);
		double velocity = Math.sqrt(xVelocity * xVelocity + yVelocity * yVelocity);
		double angle = Math.atan2(yVelocity, xVelocity);

		StaticFileEntry staticEntry = new StaticFileEntry(radius, mass);
		DynamicFileEntry dynamicEntry = new DynamicFileEntry(position.x, position.y, angle, velocity);

		dynamicWriter.println(dynamicEntry);
		staticWriter.println(staticEntry);
	}

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		generateRandomInput(100, new MyPoint(-0.1, 0.1), new MyPoint(-0.1, 0.1));
	}

	// public static void generateInput(int N, double spaceDimensions, MyPoint
	// xVelocityInterval,
	// MyPoint yVelocityInterval) throws FileNotFoundException,
	// UnsupportedEncodingException {
	//
	// PrintWriter dynamicWriter = new PrintWriter("doc/examples/Dynamic" + N +
	// ".txt", "UTF-8");
	// PrintWriter staticWriter = new PrintWriter("doc/examples/Static" + N +
	// ".txt", "UTF-8");
	//
	// staticWriter.println(N);
	// staticWriter.println(spaceDimensions);
	// staticWriter.println(noiceAmplitude);
	// staticWriter.println(interactionRadius);
	// staticWriter.println(particleVelocity);
	//
	// for (int i = 0; i < N; i++) {
	// staticWriter.println(DEFAULT_PARTICLE_RADIO);
	// dynamicWriter.println(PointGenerator.randomPointBetween(0,
	// spaceDimensions));
	// }
	//
	// staticWriter.close();
	// dynamicWriter.close();
	// }
}
