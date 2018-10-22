package boids;

public class Obstacle {
    private Vector2D pos;

    public Obstacle(double x, double y) {
        pos = new Vector2D(x, y);
    }

    public Vector2D getPos() {
        return pos;
    }
    
    public double getX() {
        return pos.getX();
    }

    public double getY() {
        return pos.getY();
    }
}