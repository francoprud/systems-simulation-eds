package model;

public interface Collision {
	/**
	 * Changes the involved particles velocity
	 */
	public void collide();
	
	/**
	 * Retrieves the collision offset time, meaning the actual time of the
	 * collision is system current time + collistion offset time.
	 */
	public double getCollisionTime();
}
