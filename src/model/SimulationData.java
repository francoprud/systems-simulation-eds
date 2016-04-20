package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimulationData {
	private static final double BOLTZMANN = 1.38064852e-23;

	private Double interactionRadius;
	private double spaceDimension; // All particle's coordinates are contained
									// inside spaceDimension x spaceDimension
	private Integer particlesAmount;
	private Double noiceAmplitude;
	private List<Particle> particles;
	private HashMap<Integer, Particle> particlesMap;

	private SimulationData() {
	}

	public void clearMarks() {
		for (Particle particle : particles) {
			particle.clearMark();
		}
	}

	public Double getInteractionRadius() {
		return interactionRadius;
	}

	public double getSpaceDimension() {
		return spaceDimension;
	}

	public Integer getParticlesAmount() {
		return particlesAmount;
	}

	public List<Particle> getParticles() {
		return particles;
	}

	public Particle getParticleById(int id) {
		return particlesMap.get(id);
	}

	public Double getNoiceAmplitude() {
		return noiceAmplitude;
	}

	public void setNoiceAmplitude(double noiceAmplitude) {
		this.noiceAmplitude = noiceAmplitude;
	}

	public void setParticles(List<Particle> particles) {
		this.particles = particles;
	}

	public double calculateTemperature() {
		double acum = 0;
		for (Particle particle : getParticles()) {
			acum += (particle.getMass() * particle.getVelocity() * particle.getVelocity()) / (2 * BOLTZMANN);
		}
		return acum / getParticlesAmount();
	}

	public static class Builder {
		private SimulationData cellIndexObject;

		private Builder() {
			cellIndexObject = new SimulationData();
			cellIndexObject.particles = new ArrayList<>();
			cellIndexObject.particlesMap = new HashMap<>();
		}

		public static Builder create() {
			return new Builder();
		}

		public Builder withInteractionRadius(double interactionRadius) {
			cellIndexObject.interactionRadius = interactionRadius;
			return this;
		}

		public Builder withParticlesAmount(int particlesAmount) {
			this.cellIndexObject.particlesAmount = particlesAmount;
			return this;
		}

		public Builder withSpaceDimension(double spaceDimension) {
			this.cellIndexObject.spaceDimension = spaceDimension;
			return this;
		}

		public Builder withParticle(Particle particle) {
			cellIndexObject.particles.add(particle);
			cellIndexObject.particlesMap.put(particle.getId(), particle);
			return this;
		}

		public Builder withNoiceAmplitude(double noiceAmplitude) {
			cellIndexObject.noiceAmplitude = noiceAmplitude;
			return this;
		}

		public SimulationData build() {
			return cellIndexObject;
		}
	}
}
