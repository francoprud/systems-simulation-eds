package generator;

import java.util.Locale;

public class MyPoint {
	public double x;
	public double y;
	
	public MyPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public String toString() {
		return String.format(Locale.US, "(%1.7e, %1.7e)", x, y);
	}
}
