/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolution.simulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.apache.commons.lang.mutable.MutableInt;

public class WorldGraphics extends JFrame {
	//static Graph graph;
    private static final int PERSON_SIZE = 1;
    private static final int SQUARE_SIZE = 10;
    public static final int GRAPH_SIZE = 150;
    private static final int YEAR_TIME = 10;
    public static boolean isPaused = false;
    
    public Map map;
    private EvolutionSimulator evSimulator;
    //Person[][] mapPerson;
    //NaturalDisaster eq;
    //public MutableInt level;
    
    private double[] col1 = new double[3];
    private double[] col2 = new double[3];
    private double[] col3 = new double[3];
    private double[] col4 = new double[3];
    //[0] population
    //[1] strength
    //[2] age
    
    //[0] col1 population %
    //[1] col2 population %
    //[2] col3 population %
    //[3] col4 population %
    //public WorldGraphics(Person[][] mapPerson, MutableInt level) {
    public WorldGraphics(EvolutionSimulator evSimulator) {
        super("Evolution Simultor");
        this.evSimulator = evSimulator;
        map = new Map();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setLayout(new BorderLayout());
        setLayout(new FlowLayout());
        getContentPane().add(map);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        JButton pauseButton = new JButton("Pause");
        JButton restartButton = new JButton("Restart");
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
    
    /*private void ppGraph() {
        for(int i = 1; i < GRAPH_SIZE; i++) {
	    		for(int j = 0; j < this.evSimulator.colonies.size()+1; j++) {
	    			map.populationGraph[i-1][j] = map.populationGraph[i][j];
	    		}
        }
	    int totalPopulation = 0;
	    for(Colony colony : this.evSimulator.colonies) {
	    		totalPopulation += colony.getPopulation();
	    }
	    double max = 0;
	    int iMaxPos = -1;
	    double sum100 = 0;
		for(int i = 0; i < this.evSimulator.colonies.size(); i++) {
			Colony colony = this.evSimulator.colonies.get(i);
            map.populationGraph[GRAPH_SIZE-1][i] = colony.getPopulation() * 100 / totalPopulation;	
            sum100 += map.populationGraph[GRAPH_SIZE-1][i];
            if (max < map.populationGraph[GRAPH_SIZE-1][i]) {
            		max = map.populationGraph[GRAPH_SIZE-1][i];
            		iMaxPos = i;
            }
            
		}
		map.populationGraph[GRAPH_SIZE-1][iMaxPos] += - sum100 + 100;
		if(evSimulator.level.toInteger() % YEAR_TIME == 0) {
			map.populationGraph[GRAPH_SIZE-1][this.evSimulator.colonies.size()] = evSimulator.level.toInteger() / YEAR_TIME;
			//System.out.println(map.populationGraph[GRAPH_SIZE-1][this.evSimulator.colonies.size()]);
		}else {
			map.populationGraph[GRAPH_SIZE-1][this.evSimulator.colonies.size()] = -1;
		}
		 
    }*/
    
    public int getColor(int x, int y) {
    		return map.pixelColor(x, y);
    }
    
    public class Map extends JPanel {
        private BufferedImage img; 
        
        public Map() {
            try {
                img = ImageIO.read(new File("C:\\Users\\Andrei\\git\\Evolution-Simulator\\Evolution Simuator\\additional\\map.png"));        
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
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
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            
            int xPos;
            int yPos;
            if (img != null) {
                g2d.drawImage(img, 0, 0, this);
            }
            /*for(int i=0; i<3; i++){
                col1[i] = 0;
                col2[i] = 0;
                col3[i] = 0;
                col4[i] = 0;
            }*/
            for (int xLoc = 0; xLoc<1357; xLoc++) {
                for (int yLoc = 0; yLoc<628; yLoc++) {
                    if(evSimulator.mapPerson[xLoc][yLoc] != null){ 
                    		Colony colony = evSimulator.mapPerson[xLoc][yLoc].colony;
                    		//addPerson(xLoc, yLoc);
                    		
                    		//g2d.setColor(evSimulator.colonies.get(colony).getColor());
                    		g2d.setColor(colony.getColor());
                    		g2d.fillRect(xLoc, yLoc, PERSON_SIZE, PERSON_SIZE);
                    }
                }
            }
            /*for(int posCol = 0; posCol < evSimulator.colonies.size(); posCol++) {
        	    		Colony col = evSimulator.colonies.get(posCol);
            }*/
            /*col1[0] = evSimulator.colonies.get(0).getPopulation();
            col1[1] = evSimulator.colonies.get(0).getEnergy();
            col1[2] = evSimulator.colonies.get(0).getStrength();
            
            col2[0] = evSimulator.colonies.get(0).getPopulation();
            col2[1] = evSimulator.colonies.get(0).getEnergy();
            col2[2] = evSimulator.colonies.get(0).getStrength();
            
            col3[0] = evSimulator.colonies.get(0).getPopulation();
            col3[1] = evSimulator.colonies.get(0).getEnergy();
            col3[2] = evSimulator.colonies.get(0).getStrength();
            
            col4[0] = evSimulator.colonies.get(0).getPopulation();
            col4[1] = evSimulator.colonies.get(0).getEnergy();
            col4[2] = evSimulator.colonies.get(0).getStrength();*/
            //col1[0] = evSimulator.colonies.get(0).getPopulation();
            //col2[0] = evSimulator.colonies.get(1).getPopulation();
            //col3[0] = evSimulator.colonies.get(2).getPopulation();
            //col4[0] = evSimulator.colonies.get(3).getPopulation();
            
            
			int strM = 0, ageM = 0, p = 0;
			if( MouseInfo.getPointerInfo() != null){
				xPos = (int) MouseInfo.getPointerInfo().getLocation().getX();
				yPos = (int) MouseInfo.getPointerInfo().getLocation().getY()-83;
				for(int i = xPos - (SQUARE_SIZE/2); i < xPos + (SQUARE_SIZE/2); i++) {
					for(int j = yPos - (SQUARE_SIZE/2); j < yPos + (SQUARE_SIZE/2); j++) {
						if((i >= 0 && i < 1357) && (j >= 0 && j < 628)) {
							if(evSimulator.mapPerson[i][j] != null) {
								p++;
								strM += evSimulator.mapPerson[i][j].strength;
								ageM += evSimulator.mapPerson[i][j].age;
							}
						}
					}
				}
				
				g2d.setColor(Color.WHITE);
				g2d.fillRect(xPos-(SQUARE_SIZE/2), yPos-(SQUARE_SIZE/2), SQUARE_SIZE, SQUARE_SIZE);
            }
			
			g2d.setColor(Color.WHITE);
			g2d.fillRect(1220, 568, 60, 60);
			
			g2d.setColor(Color.BLACK);
			g2d.drawString("P:" + Integer.toString(p), 1220, 628-(60/3*2) - 5);
			if(p>0) {
				g2d.drawString("S:" + Integer.toString(strM/p), 1220, 628-(60/3) -5 );
				g2d.drawString("A:" + Integer.toString(ageM/p), 1220, 628 -5 );
			}else {
				g2d.drawString("S:none", 1220, 628-(60/3) -5 );
				g2d.drawString("A:none", 1220, 628 -5 );
			}
            
            if(evSimulator.isEarthquake) {
	            evSimulator.eq.earthquake();
	            if(evSimulator.eq.eq == true) {
	            		g2d.setColor(evSimulator.eq.color);
	            		g2d.fillRect(evSimulator.eq.ux, evSimulator.eq.uy, evSimulator.eq.size, evSimulator.eq.size);
	            }
            }
			
			//ppGraph((int) (col4[0] + col3[0] + col2[0] + col1[0]), (int) col1[0], (int) col2[0], 
			//		(int) col3[0], (int) col4[0]);
			///*
            
            //ppGraph();
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
	            		if(evSimulator.populationGraph[i][evSimulator.colonies.size()] % 2 == 0) {
		        			g2d.setColor(color);
		        			g2d.fillRect(i, 628-100, 1, 100);
	            		}
	            		if(evSimulator.populationGraph[i][evSimulator.colonies.size()] % 10 == 0) {
	            			g2d.setColor(Color.WHITE);
	            			g2d.drawString(Integer.toString(evSimulator.populationGraph[i][4]), i, 628-100-2);
	            		}
	        		}
	        }
	        //*/
			/*
            if(col4[0] != 0){
                g2d.setColor(getColor(4));
                g2d.drawString("Colony 4:" + Integer.toString((int)col4[0]) + " " + 
                        Double.toString(col4[1]/col4[0]) + " " + 
                        Integer.toString((int)col4[2]/(int)col4[0]), 10, 600);
            }
            if(col3[0] != 0){
                g2d.setColor(getColor(3));
                g2d.drawString("Colony 3:" + Integer.toString((int)col3[0]) + " " + 
                		    Double.toString(col3[1]/col3[0]) + " " + 
                		    Integer.toString((int)col3[2]/(int)col3[0]), 10, 570);
            }
            if(col2[0] != 0){
                g2d.setColor(getColor(2));
                g2d.drawString("Colony 2:" + Integer.toString((int)col2[0]) + " " + 
                			Double.toString(col2[1]/col2[0]) + " " + 
                			Integer.toString((int)col2[2]/(int)col2[0]), 10, 540);
            }
            if(col1[0] != 0){
                g2d.setColor(getColor(1));
                g2d.drawString("Colony 1:" + Integer.toString((int)col1[0]) + " " + 
                        Double.toString(col1[1]/col1[0]) + " " + 
                        Integer.toString((int)col1[2]/(int)col1[0]), 10, 510);
            }
            */
            
            g2d.dispose();
        }
        
        @Override
        public Dimension getPreferredSize() {
            return img == null ? new Dimension(200, 200) : new Dimension(img.getWidth(), img.getHeight());
        }
        
        /*private void addPerson(int x, int y){
        		if(evSimulator.mapPerson[x][y] != null) {
        			int colony = evSimulator.mapPerson[x][y].colony;
	        		switch(colony){
	                case 1:
	                    col1[0]++;
	                    //col1[1] += mapPerson[x][y].strength;
	                    col1[1] += evSimulator.mapPerson[x][y].energy;
	                    col1[2] += evSimulator.mapPerson[x][y].age;
	                    break;
	                case 2:
	                    col2[0]++;
	                    //col2[1] += mapPerson[x][y].strength;
	                    col2[1] += evSimulator.mapPerson[x][y].energy;
	                    col2[2] += evSimulator.mapPerson[x][y].age;
	                    break;
	                case 3:
	                    col3[0]++;
	                    //col3[1] += mapPerson[x][y].strength;
	                    col3[1] += evSimulator.mapPerson[x][y].energy;
	                    col3[2] += evSimulator.mapPerson[x][y].age;
	                    break;
	                case 4:
	                    col4[0]++;
	                    //col4[1] += mapPerson[x][y].strength;
	                    col4[1] += evSimulator.mapPerson[x][y].energy;
	                    col4[2] += evSimulator.mapPerson[x][y].age;
	                    break;
	            	}
        		}
        }*/
        

        
/*        private void ppGraph(int pp, int pp1, int pp2, int pp3, int pp4){ //pp = population; ppx = colx's population
            if(pp > 0) {
	        	    double p1, p2, p3, p4; //px = colx's population percent
	            p1 = 100. * pp1 / pp;
	            p2 = 100. * pp2 / pp;
	            p3 = 100. * pp3 / pp;
	            p4 = 100. * pp4 / pp;
	            p1 = (int) Math.round(p1);
	            p2 = (int) Math.round(p2);
	            p3 = (int) Math.round(p3);
	            p4 = (int) Math.round(p4);
	            while(p1 + p2 + p3 + p4 > 100) {
	            		for(int i = 0; i < array.length(); i++) {}
	            }
	            while(p1 + p2 + p3 + p4 < 100) {
		        		if(p1 <= p2) {
		        			if(p1 <= p3) {
		        				if(p1 <= p4) {
		        					p1++;
		        				}else {
		        					p4++;
		        				}
		        			}else if(p3 <= p4) {
		        				p3++;
		        			}else{
		        				p4++;
		        			}
		        		}else {
		        			if(p2 <= p3) {
		        				if(p2 <= p4) {
		        					p2++;
		        				}else {
		        					p4++;
		        				}
		        			}else if(p3 <= p4) {
		        				p3++;
		        			}else{
		        				p4++;
		        			}
		        		}
		        }
	            for(int i = 1; i < GRAPH_SIZE; i++) {
	            		for(int j = 0; j < 5; j++) {
	            			populationGraph[i-1][j] = populationGraph[i][j];
	            		}
	            }
	            populationGraph[GRAPH_SIZE-1][0] = p1;
	            populationGraph[GRAPH_SIZE-1][1] = p2;
	            populationGraph[GRAPH_SIZE-1][2] = p3;
	            populationGraph[GRAPH_SIZE-1][3] = p4;
	            if(evSimulator.level.toInteger() % YEAR_TIME == 0) {
	    				populationGraph[GRAPH_SIZE-1][4] = evSimulator.level.toInteger() / YEAR_TIME;
	    			}else {
	    				populationGraph[GRAPH_SIZE-1][4] = -1;
	    			}
	        }
        }*/
    }

}
