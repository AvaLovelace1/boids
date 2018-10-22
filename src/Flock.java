package boids;

import java.awt.*;
import java.util.*;

public class Flock {
    private LinkedList<Boid> boids;
    private LinkedList<Predator> predators;
    private LinkedList<Obstacle> obstacles;
    private static LinkedList<Obstacle> walls;
    private double nearbyRadius = 10, avoidRadius = 8, coheseRadius = 10, obstacleRadius = 10, wallRadius = 8.5;
    private double boidSpeed = 3, predatorSpeed = 5, eatRadius = 3, chaseStr = 0.1, chaseRadius = 25;
    private double avoidStr = 1, coheseStr = 5, obstacleStr = 8, randomStr = 0.5, wallStr = 0;

    public Flock() {
        this.boids = new LinkedList<>();
        this.predators = new LinkedList<>();
        this.obstacles = new LinkedList<>();
        this.walls = new LinkedList<>();
        defineWalls();
    }
    
    public static void defineWalls() {
        walls.clear();
        for (int y = 0; y <= GUI.AREA_HEIGHT - 10; y += GUI.AREA_HEIGHT - 10) {
            for (int x = 0; x <= GUI.AREA_WIDTH - 10; x += 30) {
                walls.add(new Obstacle(x, y));
            }
        }
        for (int x = 0; x <= GUI.AREA_WIDTH - 10; x += GUI.AREA_WIDTH - 10) {
            for (int y = 10; y <= GUI.AREA_HEIGHT - 20; y += 30) {
                walls.add(new Obstacle(x, y));
            }
        }
        walls.add(new Obstacle(25, 25)); // these are obstacles placed in the corner to discourage squishing
        walls.add(new Obstacle(GUI.AREA_WIDTH - 25, 25));
        walls.add(new Obstacle(25, GUI.AREA_HEIGHT - 25));
        walls.add(new Obstacle(GUI.AREA_WIDTH - 25, GUI.AREA_HEIGHT - 25));
    }

    public LinkedList<Boid> getBoids() {    // setters and getters
        return boids;
    }

    public LinkedList<Predator> getPredators() {
        return predators;
    }

    public LinkedList<Obstacle> getObstacles() {
        return obstacles;
    }

    public LinkedList<Obstacle> getWalls() {
        return walls;
    }

    public double getNearbyRadius() {
        return nearbyRadius;
    }

    public double getAvoidRadius() {
        return avoidRadius;
    }

    public double getCoheseRadius() {
        return coheseRadius;
    }

    public double getObstacleRadius() {
        return obstacleRadius;
    }

    public double getWallRadius() {
        return wallRadius;
    }

    public double getBoidSpeed() {
        return boidSpeed;
    }
    
    public double getPredatorSpeed() {
        return predatorSpeed;
    }

    public double getCoheseStrength() {
        return coheseStr;
    }

    public double getAvoidStrength() {
        return avoidStr;
    }

    public double getObstacleStrength() {
        return obstacleStr;
    }

    public double getWallStrength() {
        return wallStr;
    }

    public double getRandomStrength() {
        return randomStr;
    }
    
    public double getEatRadius() {
        return eatRadius;
    }
    
    public double getChaseStrength() {
        return chaseStr;
    }
    
    public double getChaseRadius() {
        return chaseRadius;
    }

    public void setAvoidRadius(double ar) {
        avoidRadius = ar;
    }

    public void setCoheseRadius(double cr) {
        coheseRadius = cr;
    }

    public void setAvoidStrength(double cs) {
        avoidStr = cs;
    }

    public void setCoheseStrength(double cs) {
        coheseStr = cs;
    }

    public void setObstacleStrength(double os) {
        obstacleStr = os;
    }

    public void setRandomStrength(double rs) {
        randomStr = rs;
    }
    
    public void setChaseStrength(double cs){
        chaseStr = cs;
    }
    
    public void setChaseRadius(double cr){
        chaseRadius = cr;
    }
    
    public void setPredatorSpeed(double ps){
        predatorSpeed = ps;
    }
    
    public void setBoidSpeed(double bs){
        boidSpeed = bs;
    }

    public void addBoid(Boid b) {
        b.setFlock(this);
        boids.add(b);
    }

    public void removeBoid(int pos) {
        boids.remove(pos);
    }

    public void addPredator(Predator p) {
        p.setFlock(this);
        predators.add(p);
    }

    public void addObstacle(Obstacle o) {
        obstacles.add(o);
    }

    public void updateAllPos() {
        for (Boid b : boids) {
            b.updatePos();
        }
        for (Predator p : predators) {
            p.updatePos();
        }
    }

    public void clrBoids() {
        boids.clear();
    }
    
    public void clrPreds() {
        predators.clear();
    }

    public void clrObs() {
        obstacles.clear();
    }

    public boolean toggleWalls() {
        if (wallStr == 0) {
            wallStr = 10;
            return true;
        } else {
            wallStr = 0;
            return false;
        }
    }
    
    public void changeColours() {
        for(Predator p: predators) {
            p.changeColour();
        }
        for(Boid b: boids) {
            b.changeColour();
        }
    }

    public void show(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g.setColor(GUI.bgColor);
        g.fillRect(0, 1, GUI.AREA_WIDTH, GUI.AREA_HEIGHT - 2);      //specially drawn to keep the dividers between buttons and boids
        for (Boid b : boids) {
            g.setColor(b.getColor());
            g.fillOval((int) Math.round(b.getX()) - 5, (int) Math.round(b.getY()) - 5, 10, 10); // draw boid
            if(GUI.trail) {
                Object[] trail = b.getTrail().toArray();
                Vector2D[] vs = new Vector2D[trail.length];
                g2.setStroke(new BasicStroke(2));
                for(int i = 0; i < trail.length; i++) {
                    vs[i] = (Vector2D)trail[i];
                }    
                for(int i = 0; i < trail.length - 1; i++) {
                    g2.setColor(new Color(b.getColor().getRed(), b.getColor().getGreen(), b.getColor().getBlue(), (int)(5 * i)));
                    g2.drawLine((int) Math.round(vs[i].getX()), (int) Math.round(vs[i].getY()), (int) Math.round(vs[i + 1].getX()), (int) Math.round(vs[i + 1].getY())); // draw trail
                }
            }
        }
        
        for (Predator p : predators) {
            g.setColor(p.getColor());
            g.fillOval((int) Math.round(p.getX()) - 10, (int) Math.round(p.getY()) - 10, 20, 20); // draw predator
            if(GUI.trail) {
                Object[] trail = p.getTrail().toArray();
                Vector2D[] vs = new Vector2D[trail.length];
                g2.setStroke(new BasicStroke(4));
                for(int i = 0; i < trail.length; i++) {
                    vs[i] = (Vector2D)trail[i];
                }    
                for(int i = 0; i < trail.length - 1; i++) {
                    g2.setColor(new Color(p.getColor().getRed(), p.getColor().getGreen(), p.getColor().getBlue(), (int)(5 * i)));
                    g2.drawLine((int) Math.round(vs[i].getX()), (int) Math.round(vs[i].getY()), (int) Math.round(vs[i + 1].getX()), (int) Math.round(vs[i + 1].getY())); // draw trail
                }
            }
        }
        for (Obstacle o : obstacles) {
            g.setColor(GUI.theme.getText().equals("Light") ? Color.black : Color.white);
            g.fillRect((int) Math.round(o.getX()) - 5, (int) Math.round(o.getY()) - 5, 10, 10); // draw obstacle
        }
//        for (Obstacle o: walls) {   //debugging purposes only
//            g.setColor(new Color(255, 255, 255));
//            g.fillRect((int) Math.round(o.getX()), (int) Math.round(o.getY()), 10, 10);
//        }
    }
}