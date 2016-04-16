package model;

public class WallCollision implements Collision {
	public static final String HORIZONTAL = "horizontal";
	public static final String VERTICAL = "vertical";
	
	private Particle particle;
	private double collisionTime;
	private String wall;
	
	public WallCollision(double collisionTime, Particle particle, String wall) {
		this.collisionTime = collisionTime;
		this.particle = particle;
	}
	
	public Particle getParticle() {
		return particle;
	}
	
	public double getCollisionTime() {
		return collisionTime;
	}
	
	@Override
	public void collide() {
		switch (wall) {
		case HORIZONTAL:
			particle.setAngle(2 * Math.PI - particle.getAngle());
			break;
		case VERTICAL:
			particle.setAngle(Math.PI - particle.getAngle());
			break;
		}
	}
}
