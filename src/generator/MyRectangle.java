package generator;

public class MyRectangle {
	private MyPoint p1;
	private MyPoint p2;

	public MyRectangle(MyPoint p1, MyPoint p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	public MyPoint getP1() {
		return p1;
	}

	public MyPoint getP2() {
		return p2;
	}
	

	public boolean containsPointBordersInclusive(MyPoint p) {
		return p1.x <= p.x && p.x <= p2.x && p1.y <= p.y && p.y <= p2.y;
	}
	
	public String toString() {
		return p1.toString() + ", " + p2.toString();
	}
}
