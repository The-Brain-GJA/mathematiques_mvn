package outils;

import java.util.Locale;

public class TripleDouble {
	private double x;
	private double y;
	private double z;
	
	public static TripleDouble of(double x, double y, double z) {
		return new TripleDouble(x, y, z);
	}
	
	private TripleDouble(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getZ() {
		return z;
	}

	@Override
	public String toString() {
		return String.format(Locale.ROOT, "(%.3f,%.5f,%.5f)", x, y, z);
	}
	
}
