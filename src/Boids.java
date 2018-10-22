package boids;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import boids.Boids.FrameListener;

public class Boids {
    static GUI window;
    
    public static void main(String[] args) {
        window = new GUI("Boids Simulation");
        window.addComponentListener(new FrameListener());
        window.setVisible(true);
    }
    
    static class FrameListener extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            GUI.setDimensions(window.getWidth() - 18, window.getHeight() - 119);
            Flock.defineWalls();
            window.repaint();
        }
    }
}