package boids;

import java.awt.Color;
import java.util.LinkedList;

public class Predator extends Entity {

    public Predator(double x, double y) {
        super(x, y);
        color = new Color(255 - (int)(Math.random() * 150), 255 - (int)(Math.random() * 150), 255 - (int)(Math.random() * 150));
    }

    public void updateMove() {
        Vector2D avoid = getAvoid();
        Vector2D obstacleAvoid = getObstacleAvoid();
        Vector2D wallAvoid = getWallAvoid();
        Vector2D rand = new Vector2D(Math.random() * 2 - 1, Math.random() * 2 - 1);
        avoid.mult(flock.getAvoidStrength());
        obstacleAvoid.mult(flock.getObstacleStrength());
        wallAvoid.mult(flock.getWallStrength());
        rand.mult(flock.getRandomStrength());
        addVectors(avoid, obstacleAvoid, wallAvoid, rand);
        Vector2D closest = pos.copy();
        if (!nearby.isEmpty()) {
            closest = nearby.get(0).getPos().copy();
            for (Boid b : nearby) {
                if (pos.dist(b.getPos()) < pos.dist(closest)) {
                    closest = b.getPos().copy();
                }
            }
        }
        closest.sub(pos);
        closest.setMag(flock.getChaseStrength());
        move.add(closest);
        move.limit(flock.getPredatorSpeed());
        generateTrail();
    }
    
    public void getNearby() {
        LinkedList<Boid> tmp = new LinkedList<>();
        for (Boid b : flock.getBoids()) {
            if (pos.dist(b.pos) <= flock.getChaseRadius()) {
                tmp.add(b);
            }
        }
        nearby = tmp;
    }

    public Vector2D getAvoid() {
        Vector2D dir = new Vector2D(0, 0);
        for (Predator p : flock.getPredators()) {
            double dist = pos.dist(p.pos);
            if (p != this && dist <= flock.getAvoidRadius()) {
                Vector2D copy = pos.copy();
                copy.sub(p.pos);
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
        for (int i = 0; i < flock.getBoids().size(); i++) {     //boid eating
            double dist = pos.dist(flock.getBoids().get(i).getPos());
            if (dist <= flock.getEatRadius()) {
                flock.removeBoid(i);
            }
        }
    }
}