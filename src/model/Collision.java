package model;

public interface Collision {
	/**
	 * Changes the involved particles velocity
	 */
	public void collide();
	
	/**
	 * Retrieves the collision time
	 */
	public double getCollisionTime();
}
