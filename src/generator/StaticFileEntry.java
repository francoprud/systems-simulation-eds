package generator;

import java.util.Locale;

public class StaticFileEntry {
	double radius;
	double mass;
	public StaticFileEntry(double radius, double mass) {
		this.radius = radius;
		this.mass = mass;
	}
	
	public String toString() {
		return String.format(Locale.US, "%1.4e %1.4e", radius, mass);
	}
}
