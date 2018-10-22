package boids;

public class Vector2D {

	private double x, y;

	public Vector2D() {
		this.x = 0;
		this.y = 0;
	}

	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector2D(Vector2D v) {
		this.x = v.x;
		this.y = v.y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void add(Vector2D v) {
		x += v.x;
		y += v.y;
	}

	public void sub(Vector2D v) {
		x -= v.x;
		y -= v.y;
	}

	public void mult(double n) {
		x *= n;
		y *= n;
	}

	public void div(double n) {
		if (n != 0) {
			x /= n;
			y /= n;
		}
	}

	public double dist(Vector2D v) {
		return Math.sqrt(Math.abs(v.x - x) + Math.abs(v.y - y));
	}

	public double magnitude() {
		return Math.sqrt(x * x + y * y);
	}

	public void setMag(double n) {
		if (n == 0) {
			x = 0;
			y = 0;
		} else {
			double m = magnitude() / n;
			if (m != 0) {
				div(m);
			}
		}
	}

	public void limit(double n) {
		setMag(Math.min(magnitude(), n));
	}

	public void normalize() {
		setMag(1);
	}

	public Vector2D copy() {
		return new Vector2D(x, y);
	}

}
