package boids;

import java.awt.Color;
import java.util.*;

public abstract class Entity {
    protected Vector2D pos, move;
    protected Flock flock;
    protected Color color;
    protected Queue<Vector2D> trail;
    protected LinkedList<Boid> nearby;
    
    public Entity(double x, double y) {
        pos = new Vector2D(x, y);
        move = new Vector2D(0, 0);
        trail = new LinkedList<>();
        nearby = new LinkedList<>();
    }
    
    public double getX() {
        return pos.getX();
    }

    public double getY() {
        return pos.getY();
    }

    public Color getColor() {
        return color;
    }

    public Vector2D getPos() {
        return pos;
    }
    
    public void setFlock(Flock f) {
        this.flock = f;
    }
    
    public abstract void getNearby();
    
    public Vector2D getObstacleAvoid() {
        Vector2D dir = new Vector2D(0, 0);
        for (Obstacle o : flock.getObstacles()) {
            double dist = pos.dist(o.getPos());
            if (dist <= flock.getObstacleRadius()) {
                Vector2D copy = pos.copy();
                copy.sub(o.getPos());
                copy.normalize();
                copy.div(dist);
                dir.add(copy);
            }
        }
        return dir;
    }
    
    public Vector2D getWallAvoid() {
        Vector2D dir = new Vector2D(0, 0);
        for (Obstacle o : flock.getWalls()) {
            double dist = pos.dist(o.getPos());
            if (dist <= flock.getWallRadius()) {    //the radius for the walls are the same as that of the user-inputted obstacles
                Vector2D copy = pos.copy();
                copy.sub(o.getPos());
                copy.normalize();
                copy.div(dist);
                dir.add(copy);
            }
        }
        return dir;
    }
    
    public void addVectors(Vector2D... vectors) {
        for(Vector2D v: vectors) {
            move.add(v);
        }
    }
    public abstract Vector2D getAvoid();
    public abstract void updatePos();
    
    public void generateTrail() {
        trail.add(new Vector2D(pos.getX(), pos.getY()));
        if(trail.size() > 50) {
            trail.remove();
        }
    }
    
    public void changeColour() {
        color = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
    }
    
    public Queue getTrail() {
        return trail;
    }
    
    public void adjust() {
        if (pos.getX() > GUI.AREA_WIDTH + 10) {
            pos.setX(pos.getX() - GUI.AREA_WIDTH - 10);
        } else if (pos.getX() < -10) {
            pos.setX(pos.getX() + GUI.AREA_WIDTH + 10);
        }
        if (pos.getY() > GUI.AREA_HEIGHT + 10) {
            pos.setY(pos.getY() - GUI.AREA_HEIGHT - 10);
        } else if (pos.getY() < -10) {
            pos.setY(pos.getY() + GUI.AREA_HEIGHT + 10);
        }
    }
}