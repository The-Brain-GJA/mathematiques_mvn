package outils;

import java.util.Locale;

public class PairDouble {
	private double x;
	private double y;
	
	public static PairDouble of(double x, double y) {
		return new PairDouble(x, y);
	}
	
	private PairDouble(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	
	@Override
	public String toString() {
		return String.format(Locale.ROOT, "(%.3f,%.5f)", x, y);
	}
	
}
