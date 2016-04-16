package model;

import java.util.ArrayList;
import java.util.List;

public class Particle {
	private int id;
	private double x;
	private double y;
	private double radius;
	private double velocity;
	private double angle;
	private double mass;
	private List<Particle> neighbors;
	private boolean mark;

	public Particle(int id, double x, double y, double radius, double velocity,
			double angle, double mass) {
		if (radius < 0 || id <= 0)
			throw new IllegalArgumentException("particle wrong");
		this.id = id;
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.neighbors = new ArrayList<Particle>();
		this.velocity = velocity;
		this.angle = angle;
		this.mass = mass;
	}

	public boolean isMarked() {
		return mark;
	}

	public void clearMark() {
		this.mark = false;
	}

	public void mark() {
		this.mark = true;
	}

	public int getId() {
		return id;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getRadius() {
		return radius;
	}
	
	public double getMass()	{
		return mass;
	}

	public double getVelocity() {
		return velocity;
	}

	public double getAngle() {
		return angle;
	}

	public double getXVelocity() {
		return Math.cos(angle) * velocity;
	}

	public double getYVelocity() {
		return Math.sin(angle) * velocity;
	}

	public List<Particle> getNeighbors() {
		return neighbors;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}
	
	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}
	
	public void setAngleAndCalculateVelocity(double vx, double vy) {
		this.velocity = Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
		this.angle = Math.atan2(vy, vx);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Particle other = (Particle) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return new StringBuilder().append("(" + x + ", " + y + ")").toString();
	}
}
