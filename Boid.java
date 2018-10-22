package boids;

import java.awt.Color;
import java.util.*;

public class Boid extends Entity {

    public Boid(double x, double y) {
        super(x, y);
        color = new Color((int)(Math.random() * 256), (int)(Math.random() * 256), (int)(Math.random() * 256));
    }

    public void updateMove() {
        Vector2D align = getAlign();
        Vector2D cohese = getCohesion();
        Vector2D avoid = getAvoid();
        Vector2D predatorAvoid = getPredatorAvoid();
        Vector2D obstacleAvoid = getObstacleAvoid();
        Vector2D wallAvoid = getWallAvoid();
        Vector2D rand = new Vector2D(Math.random() * 2 - 1, Math.random() * 2 - 1);
        avoid.mult(flock.getAvoidStrength());
        cohese.mult(flock.getCoheseStrength());
        predatorAvoid.mult(10);
        obstacleAvoid.mult(flock.getObstacleStrength());
        wallAvoid.mult(flock.getWallStrength());
        rand.mult(flock.getRandomStrength());
        addVectors(align, cohese, avoid, predatorAvoid, obstacleAvoid, wallAvoid, rand);
        move.limit(flock.getBoidSpeed());
        generateTrail();
    }
    
    public void getNearby() {
        LinkedList<Boid> tmp = new LinkedList<>();
        for (Boid b : flock.getBoids()) {
            if (b != this && pos.dist(b.pos) <= flock.getNearbyRadius()) {
                tmp.add(b);
            }
        }
        nearby = tmp;
    }

    public Vector2D getAlign() {
        Vector2D dir = new Vector2D(0, 0);
        for (Boid b : nearby) {
            double dist = pos.dist(b.pos);
            if (b != this && dist <= flock.getNearbyRadius()) {
                Vector2D copy = b.move.copy();
                copy.normalize();
                copy.div(dist);
                dir.add(copy);
            }
        }
        return dir;
    }

    public Vector2D getCohesion() {
        Vector2D dir = new Vector2D(0, 0);
        int count = 0;
        for (Boid b : nearby) {
            double dist = pos.dist(b.pos);
            if (b != this && dist <= flock.getCoheseRadius()) {
                dir.add(b.pos);
                count++;
            }
        }
        if (count > 0) {
            dir.div(count);
            dir.sub(pos);
            dir.setMag(0.05);
            return dir;
        } else {
            return new Vector2D(0, 0);
        }
    }

    public Vector2D getAvoid() {
        Vector2D dir = new Vector2D(0, 0);
        for (Boid b : nearby) {
            double dist = pos.dist(b.pos);
            if (b != this && dist <= flock.getAvoidRadius()) {
                Vector2D copy = pos.copy();
                copy.sub(b.pos);
                copy.normalize();
                copy.div(dist);
                dir.add(copy);
            }
        }
        return dir;
    }

    public Vector2D getPredatorAvoid() {
        Vector2D dir = new Vector2D(0, 0);
        for (Predator p : flock.getPredators()) {
            double dist = pos.dist(p.getPos());
            if (dist <= flock.getObstacleRadius()) {
                Vector2D copy = pos.copy();
                copy.sub(p.getPos());
                copy.normalize();
                copy.div(dist);
                dir.add(copy);
            }
        }
        return dir;
    }

    public void updatePos() {
        getNearby();
        updateMove();
        pos.add(move);
        adjust();
    }
}