/*Andrew Li, Ava Pun, Jasmine Zhu
 January 24th, 2017
 ICS 4U1
 Boids*/
package boids;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.event.*; //Needed for ActionListener
import java.io.*;
import javax.swing.border.*;
import javax.swing.event.*; //Needed for ActionListener
import javax.swing.Timer;

class GUI extends JFrame implements ActionListener, ChangeListener, MouseListener {
    static JSlider speedSlider = new JSlider(1, 10, 7), avoidRSlider = new JSlider(0, 20, 8),
            coheseRSlider = new JSlider(0, 20, 10), avoidSSlider = new JSlider(0, 4, 1), 
            coheseSSlider = new JSlider(1, 6, 5), randomSSlider = new JSlider(0, 10, 5),
            obstacleSSlider = new JSlider(0, 20, 8), chaseSSlider = new JSlider(5, 15, 10),
            chaseRSlider = new JSlider(15, 35, 25), predatorSSlider = new JSlider(3, 7, 5),
            boidSSlider = new JSlider(1, 5, 3);
    public static int AREA_HEIGHT = 800, AREA_WIDTH = 1000;
    public static boolean start = false, trail = false;
    public static Color bgColor = Color.black, textColor = UIManager.getColor("Panel.background");
    public static JButton walls, theme, trails;
    private Font textFont;
    private static Timer t;
    private Flock flock = new Flock();

    public GUI(String name) {
        super(name);
        init_font();
        Movement moveFlock = new Movement(flock); // Updates the flock
        t = new Timer(50 - 5 * speedSlider.getValue(), moveFlock); // Set up timer

        JPanel content = new JPanel();      //content areas
        content.setLayout(new BorderLayout());
        JPanel north = new JPanel();
        north.setLayout(new FlowLayout());
        JPanel south = new JPanel();
        DrawArea board = new DrawArea(AREA_WIDTH, AREA_HEIGHT); // The area where the boids are

        JTextArea speed = new JTextArea("Speed:");      //all the buttons
        JButton simulate = new JButton("Simulate");
        JButton settings = new JButton("Settings");
        JButton info = new JButton("Info");
        walls = new JButton("Walls: Off");
        walls.setActionCommand("Walls");
        JTextArea clear = new JTextArea("Clear:");
        JButton clrBoids = new JButton("Boids");
        JButton clrPreds = new JButton("Predators");
        JButton clrObs = new JButton("Obstacles");
        theme = new JButton("Dark");
        theme.setActionCommand("Theme");
        trails = new JButton("Trails: Off");
        trails.setActionCommand("Trail");
        
        textFormat(speed, clear);
        buttonFormat(simulate, info, settings, walls, clrBoids, clrPreds, clrObs, theme, trails);

        speedSlider.setName("Speed");
        speedSlider.setPreferredSize(new Dimension(120, 20));
        speedSlider.addChangeListener(this);

        north.add(simulate);        //add all components to their appropriate spots
        north.add(speed);
        north.add(speedSlider);
        north.add(settings);
        north.add(walls);
        north.add(clear);
        north.add(clrBoids);
        north.add(clrPreds);
        north.add(clrObs);
        south.add(info);
        south.add(trails);
        south.add(theme);
        content.add(north, "North"); // Input area
        content.add(board, "Center"); // Output area
        content.add(south, "South");
        content.addMouseListener(this);

        setContentPane(content);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        switch (source.getName()) {
            case "Speed":
                if (t != null) {
                    t.setDelay(50 - 5 * speedSlider.getValue());
                }
                break;
            case "AvoidR":
                flock.setAvoidRadius(avoidRSlider.getValue());
                break;
            case "CoheseR":
                flock.setCoheseRadius(coheseRSlider.getValue());
                break;
            case "AvoidS":
                flock.setAvoidStrength(avoidSSlider.getValue());
                break;
            case "CoheseS":
                flock.setCoheseStrength(coheseSSlider.getValue());
                break;
            case "ObstacleS":
                flock.setObstacleStrength(obstacleSSlider.getValue());
                break;
            case "ChaseS":
                flock.setChaseStrength(chaseSSlider.getValue() / 100.0);
                break;  
            case "ChaseR":
                flock.setChaseRadius(chaseRSlider.getValue());
                break; 
            case "PredatorS":
                flock.setPredatorSpeed(predatorSSlider.getValue());
                break;   
            case "BoidS":
                flock.setBoidSpeed(boidSSlider.getValue());
                break;     
            case "Random":
                flock.setRandomStrength(randomSSlider.getValue() / 10.0);
                break;
               
        }
    }

    public void mouseClicked(MouseEvent e) {
        int x = e.getX(), y = e.getY();  
        y -= 38;        //coordinates are subtracted to take into account the buttons on top
        if (e.isShiftDown()) {
            flock.addPredator(new Predator(x, y));
        } else if ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) { // if right-clicked, add obstacle
            flock.addObstacle(new Obstacle(x, y));
        } else if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) { // if left-clicked, add boid
            flock.addBoid(new Boid(x, y));
        }
        repaint();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Simulate":
                if (!start) {
                    t.start();
                    start = true;
                } else {
                    t.stop();
                    start = false;
                }
                break;
            case "Settings":
                JTextArea avoidR = new JTextArea("Avoidance Radius:");
                JTextArea coheseR = new JTextArea("Cohesion Radius:");
                JTextArea avoidS = new JTextArea("Avoidance Strength:");
                JTextArea coheseS = new JTextArea("Cohesion Strength:");
                JTextArea obstacleS = new JTextArea("Obstacle Strength:");
                JTextArea chaseS = new JTextArea("Chase Strength:");
                JTextArea chaseR = new JTextArea("Chase Radius:");
                JTextArea predatorS = new JTextArea("Predator Speed:");
                JTextArea boidS = new JTextArea("Boid Speed:");
                JTextArea randomS = new JTextArea("Randomness:");
                
                avoidRSlider.setName("AvoidR");
                coheseRSlider.setName("CoheseR");
                avoidSSlider.setName("AvoidS");
                coheseSSlider.setName("CoheseS");
                obstacleSSlider.setName("ObstacleS");
                chaseSSlider.setName("ChaseS");
                chaseRSlider.setName("ChaseR");
                predatorSSlider.setName("PredatorS");
                boidSSlider.setName("BoidS");
                randomSSlider.setName("Random");
                
                textFormat(avoidR, coheseR, avoidS, coheseS, randomS, obstacleS, chaseS, chaseR, predatorS, boidS);
                addCListeners(avoidRSlider, avoidSSlider, coheseRSlider, coheseSSlider, obstacleSSlider, chaseSSlider, chaseRSlider, predatorSSlider, boidSSlider, randomSSlider);

                JFrame settings = new JFrame(); // creating the Settings Panel and adding components
                settings.setLayout(new FlowLayout());
                settings.add(avoidR);
                settings.add(avoidRSlider);
                settings.add(coheseR);
                settings.add(coheseRSlider);
                settings.add(avoidS);
                settings.add(avoidSSlider);
                settings.add(coheseS);
                settings.add(coheseSSlider);
                settings.add(obstacleS);
                settings.add(obstacleSSlider);
                settings.add(chaseS);
                settings.add(chaseSSlider);
                settings.add(chaseR);
                settings.add(chaseRSlider);
                settings.add(predatorS);
                settings.add(predatorSSlider);
                settings.add(boidS);
                settings.add(boidSSlider);
                settings.add(randomS);
                settings.add(randomSSlider);

                settings.setSize(270, 600);
                settings.setResizable(false);
                settings.setTitle("Settings");
                settings.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                settings.setLocationRelativeTo(null);
                settings.setVisible(true);
                break;
            case "Info":
                JFrame infobox = new JFrame();
                infobox.setSize(660, 530);
                JPanel area = new JPanel();
                area.setLayout(new BorderLayout());
                area.setBackground(Color.LIGHT_GRAY);
                JTextArea text = new JTextArea("Welcome to the world of Boids!\n\n"
                        + "Boids is a program that simulates the movement of flocking animals such as birds and fish.\n\n"
                        + "Controls:\n"
                        + "------------\n"
                        + "Left-click to create a boid. Boids will flock together and attempt to avoid obstacles and predators.\n"
                        + "Right-click to create an obstacle.\n"
                        + "Shift-click to create a predator. Predators will pursue boids and avoid obstacles.\n"
                        + "\n"
                        + "Notes:\n"
                        + "------------\n"
                        + "Boids will teleport to the other side of the screen after travelling off the other side. Turning on walls will make entities avoid the edges.\n\n"
                        + "Certain variables can be adjusted in the Settings Panel, which will affect interactions.\n\n"
                        + "Trails can be turned on, which show the path of each entity. WARNING: Turning this on with many entites on screen will lag the simulation.");
                text.setMargin(new Insets(25, 25, 25, 25));
                textFormat(text);
                text.setLineWrap(true);
                text.setWrapStyleWord(true);
                //text.setFont(textFont.deriveFont(Font.PLAIN, 16));
                area.add(text);
                
                infobox.add(area);
                infobox.setResizable(false);
                infobox.setTitle("Info");
                infobox.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                infobox.setLocationRelativeTo(null);
                infobox.setVisible(true);
                break;
            case "Walls":
                if (flock.toggleWalls()) { // toggles walls, will return "true" if walls are now ON and "false" if walls are now OFF
                    walls.setText("Walls: On");
                } else {
                    walls.setText("Walls: Off");
                }
                repaint();
                break;
            case "Boids":
                flock.clrBoids();
                break;
            case "Predators":
                flock.clrPreds();
                break;
            case "Obstacles":
                flock.clrObs();
                break;
            case "Theme":
                if(bgColor.equals(Color.black)) {
                    bgColor = UIManager.getColor("Panel.background");
                    theme.setText("Light");
                    flock.changeColours();
                }
                else if(bgColor.equals(UIManager.getColor("Panel.background"))) {
                    bgColor = Color.black;
                    theme.setText("Dark");
                    flock.changeColours();
                }
                repaint();
                break;
            case "Trail":
                if(trail) {
                    trail = false;
                    trails.setText("Trails: Off");
                }
                else {
                    trail = true;
                    trails.setText("Trails: On");
                }
                break;
        }
    }
    
    private void textFormat(JTextArea... textAreas) {
        for(JTextArea j: textAreas) {
            j.setEditable(false);
            j.setBackground(textColor);
            j.setFont(textFont);
        }
    }
    
    private void buttonFormat(JButton... buttons) {
        for(JButton j: buttons) {
            j.addActionListener(this);
            j.setFocusPainted(false);
            j.setFont(textFont);
        }
    }
    
    private void addCListeners(JSlider... sliders) {
        for(JSlider s: sliders) {
            s.addChangeListener(this);
        }
    }
    
    public static void setDimensions(int width, int height) {
        AREA_HEIGHT = height;
        AREA_WIDTH = width;
    }

    private void init_font() {
        try {
            File file = new File("HigashiOme-Gothic.ttf");      //load font from file
            textFont = Font.createFont(Font.TRUETYPE_FONT, file);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(textFont);
            textFont = textFont.deriveFont(Font.PLAIN, 14);     //change font size
        }
        catch (Exception e) {
            System.out.println("Font \"HigashiOme-Gothic.ttf\" not found.");
        }
    }


    class Movement implements ActionListener {
        private Flock flock;
        
        public Movement(Flock f) {
            flock = f;
        }
        
        public void actionPerformed(ActionEvent e) {
            flock.updateAllPos();
            repaint();
        }
    }

    class DrawArea extends JPanel {
        public DrawArea(int width, int height) {
            this.setPreferredSize(new Dimension(width, height));    //size
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(createImage(), 0, 0, this);
        }
        
        private BufferedImage createImage() {
            BufferedImage bufferedImage = new BufferedImage(AREA_WIDTH, AREA_HEIGHT, BufferedImage.TYPE_INT_RGB);
            flock.show(bufferedImage.getGraphics());
            return bufferedImage;
        }
    }
}

