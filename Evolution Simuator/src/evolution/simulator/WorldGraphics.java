/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolution.simulator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

// it takes care of the application's graphics 
public class WorldGraphics extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final int PERSON_SIZE = 1;
    public static final int GRAPH_SIZE = 150;
    
    public static boolean isPaused = false;
    
    public Map map;
    private EvolutionSimulator evSimulator;

    //initialize graphical variables
    public WorldGraphics(EvolutionSimulator evSimulator) {
        super("Evolution Simultor");
        this.evSimulator = evSimulator;
        map = new Map();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().add(map);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        JButton pauseButton = new JButton("Pause");
        JButton restartButton = new JButton("Restart");
        pauseButton.setVisible(true);
        restartButton.setVisible(true);
        pauseButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		if(!isPaused) {
        			isPaused = true;
        			pauseButton.setText("Resume");
        		}else {
        			isPaused = false;
        			pauseButton.setText("Pause");
        		}
        	}
        });
        restartButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		evSimulator.restart();
        		isPaused = false;
        		pauseButton.setText("Pause");
        	}
        });
        pauseButton.setBounds(0, 0, 0, 0);
        restartButton.setBounds(0, 0, 0, 0);
        map.add(restartButton);
        map.add(pauseButton);        
    }
    
    // gets the color of a certain pixel
    public int getColor(int x, int y) {
    		return map.pixelColor(x, y);
    }
    
    // the painting class
    public class Map extends JPanel {
		private static final long serialVersionUID = 1L;
		private BufferedImage img; 
        
		// reads map image
        public Map() {
            try {
                img = ImageIO.read(new File("/Users/test/Documents/GitHub/Evolution-Simulator/Evolution Simuator/additional/map.png"));        
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        // sends the color of a pixel
        public int pixelColor(int xPos, int yPos){
            int packedInt = img.getRGB(xPos, yPos);
            Color color = new Color(packedInt, true);
            int r, g, b;
            r = Integer.parseInt(Integer.toString(color.getRed()));
            g = Integer.parseInt(Integer.toString(color.getGreen()));
            b = Integer.parseInt(Integer.toString(color.getBlue()));
            if(r == 0 && g == 8 && b == 255){
                return 1;
            }else if(r == 15 && g == 226 && b == 0){
                return 2;
            }
            return 0;
        }
        
        
        @Override
        // painting function
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            
            // draw map image
            if (img != null) {
                g2d.drawImage(img, 0, 0, this);
            }
            
            // draw individuals
            for (int xLoc = 0; xLoc<1357; xLoc++) {
                for (int yLoc = 0; yLoc<628; yLoc++) {
                    if(evSimulator.mapPerson[xLoc][yLoc] != null){ 
                    		Colony colony = evSimulator.mapPerson[xLoc][yLoc].colony;
                    		g2d.setColor(colony.getColor());
                    		g2d.fillRect(xLoc, yLoc, PERSON_SIZE, PERSON_SIZE);
                    }
                }
            }
            
            // draw earthquake
            if(evSimulator.isEarthquake) {
	            evSimulator.earthquake.earthquake();
	            if(evSimulator.earthquake.eq == true) {
	            		g2d.setColor(evSimulator.earthquake.color);
	            		g2d.fillRect(evSimulator.earthquake.ux, evSimulator.earthquake.uy, evSimulator.earthquake.size, evSimulator.earthquake.size);
	            }
            }
            
            // draw graphic
			g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 528, GRAPH_SIZE, 100);
            for(int i=0; i<GRAPH_SIZE; i++) {
            		int previousPopulationSum = 0;
	            	for(int j = 0; j < evSimulator.colonies.size(); j++) {
	            		previousPopulationSum += evSimulator.populationGraph[i][j];
	            		g2d.setColor(evSimulator.colonies.get(j).getColor());
	            		g2d.fillRect(i, 628 - previousPopulationSum, 1, evSimulator.populationGraph[i][j]);
	        		}
        			if(evSimulator.populationGraph[i][evSimulator.colonies.size()] != -1) {
	        			Color color = new Color(128, 0, 128);
	            		if(evSimulator.populationGraph[i][evSimulator.colonies.size()] % 20 == 0) {
		        			g2d.setColor(color);
		        			g2d.fillRect(i, 628-100, 1, 100);
	            		}
	            		if(evSimulator.populationGraph[i][evSimulator.colonies.size()] % 100 == 0) {
	            			g2d.setColor(Color.WHITE);
	            			g2d.drawString(Integer.toString(evSimulator.populationGraph[i][evSimulator.colonies.size()]), i, 628-100-2);
	            		}
	        		}
	        }
            
            g2d.dispose();
        }
        
        @Override
        public Dimension getPreferredSize() {
            return img == null ? new Dimension(200, 200) : new Dimension(img.getWidth(), img.getHeight());
        }
        
    }

}
