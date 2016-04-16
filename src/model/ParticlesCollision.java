package model;

public class ParticlesCollision implements Collision {
	private Particle firstParticle;
	private Particle secondParticle;
	private double collisionTime;

	public ParticlesCollision(double collisionTime, Particle firstParticle,
			Particle secondParticle) {
		this.collisionTime = collisionTime;
		this.firstParticle = firstParticle;
		this.secondParticle = secondParticle;
	}

	public Particle getFirstParticle() {
		return firstParticle;
	}

	public Particle getSecondParticle() {
		return secondParticle;
	}

	public double getCollisionTime() {
		return collisionTime;
	}

	@Override
	public void collide() {
		double dx = firstParticle.getX() - secondParticle.getX();
		double dy = firstParticle.getY() - secondParticle.getY();
		double dvx = firstParticle.getXVelocity()
				- secondParticle.getXVelocity();
		double dvy = firstParticle.getYVelocity()
				- secondParticle.getYVelocity();
		double dvxdr = (dvx * dx) + (dvy * dy);
		double sigma = firstParticle.getRadius() + secondParticle.getRadius();
		double j = (2 * firstParticle.getMass() * secondParticle.getMass() * dvxdr)
				/ (sigma * (firstParticle.getMass() + secondParticle.getMass()));
		double jx = (j * dx) / sigma;
		double jy = (j * dy) / sigma;
		
		double firstParticleXVelocity = firstParticle.getXVelocity() + (jx /firstParticle.getMass());
		double firstParticleYVelocity = firstParticle.getYVelocity() + (jy /firstParticle.getMass());
		
		double secondParticleXVelocity = secondParticle.getXVelocity() + (jx /secondParticle.getMass());
		double secondParticleYVelocity = secondParticle.getYVelocity() + (jy /secondParticle.getMass());
		
		firstParticle.setAngleAndCalculateVelocity(firstParticleXVelocity, firstParticleYVelocity);
		secondParticle.setAngleAndCalculateVelocity(secondParticleXVelocity, secondParticleYVelocity);
	}
}