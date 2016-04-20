package simulation;

import com.sun.istack.internal.NotNull;

import model.Collision;
import model.Particle;
import model.ParticlesCollision;
import model.SimulationData;
import model.WallCollision;

public class EventDrivenSimulation implements Simulation {
	private static final double EPSILON = 1e-8;
	
	private SimulationData simulationData;
	private long T;
	private double framesEachTimeUnits;
	private Listener listener;

	private double simulationCurrentTime;
	private Collision nextCollision;

	public interface Listener {
		public void onFrameAvailable(SimulationData frame);
	}

	public EventDrivenSimulation(long T, double framesEachTimeUnits, @NotNull Listener listener) {
		this.T = T;
		this.framesEachTimeUnits = framesEachTimeUnits;
		this.listener = listener;
	}

	@Override
	public void simulate(SimulationData simulationData) {
		this.simulationData = simulationData;
		this.simulationCurrentTime = 0;

		listener.onFrameAvailable(simulationData);
		
		for (int t = 0; t < T - 1; t++) {
			System.out.println("instant t = " + t);
			boolean frameReached = false;
			while (!frameReached) {
				nextCollision = calculateNextCollision();
				if (timeUntilNextFrame() < nextCollision.getCollisionTime()) {
					moveSystemForward(timeUntilNextFrame());
					simulationCurrentTime += timeUntilNextFrame();
					listener.onFrameAvailable(simulationData);
					frameReached = true;
				} else {
					moveSystemForward(nextCollision.getCollisionTime());
					nextCollision.collide();
					simulationCurrentTime += nextCollision.getCollisionTime();
				}
			}
		}
	}

	public double timeUntilNextFrame() {
		double time = framesEachTimeUnits * (Math.floor(simulationCurrentTime / framesEachTimeUnits) + 1)
				- simulationCurrentTime;
		if (time < EPSILON) return framesEachTimeUnits;
		return time;
	}

	public double getSimulationCurrentTime() {
		return simulationCurrentTime;
	}

	private Collision calculateNextCollision() {
		Collision closestCollision = null;

		for (Particle particle : simulationData.getParticles()) {
			closestCollision = minimumCollision(closestCollision,
					new WallCollision(getHorizontalWallCollisionTime(particle), particle, WallCollision.HORIZONTAL));
			closestCollision = minimumCollision(closestCollision,
					new WallCollision(getVerticalWallCollisionTime(particle), particle, WallCollision.VERTICAL));
			for (Particle p : simulationData.getParticles()) {
				if (particle.getId() == p.getId())
					continue;
				closestCollision = minimumCollision(closestCollision,
						new ParticlesCollision(getParticlesCollisionTime(particle, p), particle, p));
			}
		}
		return closestCollision;
	}

	private Collision minimumCollision(Collision collision1, Collision collision2) {
		if (collision1 == null)
			return collision2;
		if (collision2 == null)
			return collision1;
		return collision2.getCollisionTime() < collision1.getCollisionTime() ? collision2 : collision1;
	}

	private double getHorizontalWallCollisionTime(Particle particle) {
		if (particle.getXVelocity() > 0) {
			return (simulationData.getSpaceDimension() - particle.getRadius() - particle.getX())
					/ particle.getXVelocity();
		} else if (particle.getXVelocity() < 0) {
			return (particle.getRadius() - particle.getX()) / particle.getXVelocity();
		} else {
			return Double.MAX_VALUE;
		}
	}

	private double getVerticalWallCollisionTime(Particle particle) {
		if (particle.getYVelocity() > 0) {
			return (simulationData.getSpaceDimension() - particle.getRadius() - particle.getY())
					/ particle.getYVelocity();
		} else if (particle.getYVelocity() < 0) {
			return (particle.getRadius() - particle.getY()) / particle.getYVelocity();
		} else {
			return Double.MAX_VALUE;
		}
	}

	private double getParticlesCollisionTime(Particle firstParticle, Particle secondParticle) {
		double dx = firstParticle.getX() - secondParticle.getX();
		double dy = firstParticle.getY() - secondParticle.getY();
		double dvx = firstParticle.getXVelocity() - secondParticle.getXVelocity();
		double dvy = firstParticle.getYVelocity() - secondParticle.getYVelocity();
		double dvxdr = (dvx * dx) + (dvy * dy);
		double dvxdv = Math.pow(dvx, 2) + Math.pow(dvy, 2);
		double drxdr = Math.pow(dx, 2) + Math.pow(dy, 2);
		double sigma = firstParticle.getRadius() + secondParticle.getRadius();
		double d = Math.pow(dvxdr, 2) - dvxdv * (drxdr - Math.pow(sigma, 2));
		if (dvxdr < 0 && d >= 0) {
			return -(dvxdr + Math.sqrt(d)) / dvxdv;
		} else {
			return Double.MAX_VALUE;
		}
	}

	private void moveSystemForward(double time) {
		for (Particle particle : simulationData.getParticles()) {
			double newXposition = particle.getX() + particle.getXVelocity() * time;
			double newYPosition = particle.getY() + particle.getYVelocity() * time;
			particle.setX(newXposition);
			particle.setY(newYPosition);
		}
	}
}
