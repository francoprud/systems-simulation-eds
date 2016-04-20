package statistics;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import model.Particle;
import model.SimulationData;

public class StatisticsGenerator {
	private long collisions;
	private long framesReached;
	private double FPU;
	private List<Double> collisionTimes;
	private List<Double> velocities;

	public StatisticsGenerator(double FPU) {
		collisions = 0;
		framesReached = 0;
		this.FPU = FPU;
		collisionTimes = new LinkedList<Double>();
		velocities = new LinkedList<Double>();
	}

	public void onCollision(double time) {
		collisions++;
		collisionTimes.add(time);
	}

	public void onFrameReached() {
		framesReached++;
	}

	public double getCollisionsAverage() {
		if (framesReached <= 0)
			throw new IllegalStateException("can't calculate average");
		return ((double) collisions) / (framesReached/FPU);
	}
	
	public void saveVelocities(SimulationData simulationData) {
		for (Particle particle: simulationData.getParticles()) {
			velocities.add(particle.getVelocity());
		}
	}
	
	public void generateCollisionsDistribution() throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("doc/examples/collisionDistribution" + ".txt", "UTF-8");
		for (Double collisionTime: collisionTimes) {
			writer.print(collisionTime + " ");
		}
		writer.close();
	}
	
	public void generateVelocitiesDistribution() throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("doc/examples/velocitiesDistribution" + ".txt", "UTF-8");
		for (Double velocity: velocities) {
			writer.print(velocity + " ");
		}
		writer.close();
	}
}
