package generator;

import java.util.Locale;

public class DynamicFileEntry {
	double x;
	double y;
	double angle;
	double velocity;
	public DynamicFileEntry(double x, double y, double angle, double velocity) {
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.velocity = velocity;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getAngle() {
		return angle;
	}
	public double getVelocity() {
		return velocity;
	}
	
	public String toString() {
		return String.format(Locale.US, "%1.4e %1.4e %1.4e %1.4e", x, y, angle, velocity);
	}
}
