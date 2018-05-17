/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolution.simulator;

import java.awt.MouseInfo;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.mutable.MutableInt;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author AndreiCo
 */

public class EvolutionSimulator
{
	public final static int WIDTH = 1357;
	public final static int HEIGHT = 628;
    private static final int YEAR_TIME = 12;
    public int[][] populationGraph;


	MutableInt level;
    Person[][] mapPerson;
    int[][] mapWealth;
    NaturalDisaster eq;
    WorldGraphics frame;
    ArrayList<Colony> colonies;
    
    private int[] col = new int[4];
    private int localLevel = 0;
    
    public static void main(String[] args) throws InterruptedException{
    	EvolutionSimulator evsim = new EvolutionSimulator();	
        evsim.placeColonies();
        TimeUnit.MILLISECONDS.sleep(3000);
        while(true) {
        	evsim.run();
        	TimeUnit.MILLISECONDS.sleep(50);
        }   
    }  

    public EvolutionSimulator(){
    	level = new MutableInt(0);
        mapPerson = new Person[WIDTH][HEIGHT];
        eq = new NaturalDisaster();
        colonies = Configuration.initColonies();
        mapWealth = Configuration.initWealth();
        frame = new WorldGraphics(this);
        populationGraph = new int[frame.GRAPH_SIZE][colonies.size() + 1];
    	for(int i = 0; i < frame.GRAPH_SIZE; i++) {
    		for(int j = 0; j < colonies.size(); j++) {
    			populationGraph[i][j] = 0;
    		}
    		populationGraph[i][colonies.size()] = -1;
	    }
    }
    
    public void run() {
    	int i = 0;
    	i++;
    	//evolution life cycle
        while(!frame.isPaused){
        	ppGraph();
        	
        	//aging and dying
            for(int x = 0; x<WIDTH; x++){
                for(int y = 0; y<HEIGHT; y++){
                    if(mapPerson[x][y] != null){           		
                    		Random random = new Random();
                    		if(random.nextInt(100)+1 == 1 || random.nextInt(100)+1 == 2){
                    			mapPerson[x][y].disease[3] = true;
                    		} 
                        if(eq.eq == true && (x > eq.x  && x< eq.x + eq.size) && 
                        			(y > eq.y  && y < eq.y + eq.size)) {
                        		mapPerson[x][y] = null;
                        }
                    		if(mapPerson[x][y] != null) {
                    			mapPerson[x][y].die(x, y, mapPerson, level.toInteger(), mapWealth[x][y]); // NullPointer error, probably because of earthquakes
                    		}
                    }
                }
            }
            //moving
            for(int x = 0; x<WIDTH; x++){
                for(int y = 0; y<HEIGHT; y++){
                    if(mapPerson[x][y] != null){
                       mapPerson[x][y].move(x, y, mapPerson, frame, mapWealth[x][y], colonies);
                    }
                    
                }
            }
            //reproduction 
            for(int x = 0; x<WIDTH; x++){
                for(int y = 0; y<HEIGHT; y++){
            		if(mapPerson[x][y] != null){
            			if(mapPerson[x][y].swimAngle == -1) {
            				mapPerson[x][y].reproduction(x, y, level.toInteger(), mapPerson, frame, mapWealth[x][y]);    
            			}
            		}
                }
            }
            //draw
            frame.repaint();
            
            
            
            try {
				TimeUnit.MILLISECONDS.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            
            localLevel++;
            level.setValue(localLevel);
        }
    }
    
    public void restart() {
    	for(int x = 0; x < this.WIDTH; x++) {
    		for(int y = 0; y < this.HEIGHT; y++) {
        		mapPerson[x][y] = null;
        	}
    	}
    	level.setValue(0);
    	localLevel = 0;
    	colonies = Configuration.initColonies();
    	for(int i = 0; i < frame.GRAPH_SIZE; i++) {
    		for(int j = 0; j < colonies.size(); j++) {
    			populationGraph[i][j] = 0;
    		}
    		populationGraph[i][colonies.size()] = -1;
	    }
    	placeColonies();
    }
    
    public void placeColonies(){
        Random random = new Random();
        int x, y;
        int en;
        for(Colony col : colonies) {
        		for(int i=0; i<col.getPopulation(); i++){
                do{
                    x = exponential(-50, 50, 2) + col.getxPosition();
                    y = exponential(-50, 50, 2) + col.getyPosition();
                }while(frame.getColor(x, y) == 1 && mapPerson[x][y] == null);
                en = random.nextInt(Math.abs((int)col.getEnergy()))+15;
                mapPerson[x][y] = new Person(0, random.nextInt(Math.abs((int)col.getStrength()))+5, i < col.getFemalePopulation(), col, 
                		0, diseaseRandom(), en, en, -1, -col.getReproductionCycle(), mapWealth[x][y]);
            }
        }
        //frame.repaint();
    }
    
    public boolean[] diseaseRandom() {
    		Random random = new Random();
    		boolean[] Disease = new boolean[5];
    		for(int i = 0; i < 5; i++) {
    			Disease[i] = false;
    		}
    		int disease = random.nextInt(20)+1;
    		switch(disease) {
    			case 1:
    				Disease[2] = true;
    			case 2:
    				Disease[1] = true;
    		}
    		return Disease;
    }

    public int exponential(int min, int max, int limit){
        Random random = new Random();
        double distance = 0;
        if(random.nextBoolean() || min >= 0) {
	        for(int i=0; i<=max; i++){
	            if(random.nextInt(limit) == 0 || i == max){
	                distance = i;
	                break;
	            }
	        }
        }else {
        		for(int i=0; i>=min; i--){
	            if(random.nextInt(limit) == 0 || i == min){
	                distance = i;
	                break;
	            }
	        }
        }
        return (int)distance;
    }
    
    public void earthquake() {
    		int magnitude  = exponential(0, 8, 2);
    		int x, y;
    		switch(magnitude) {
    			case 4:
    				x = 1;
    				y = 1;
    				break;
    			case 5:
    				x = 3;
    				y = 3;
    				break;
    			case 6:
    				x = 10;
    				y = 10;
    				break;
    			case 7:
    				x = 30;
    				y = 30;
    				break;
    			case 8:
    				x = 50;
    				y = 200;
    				break;
    		}
    		
    }
    
    private void ppGraph() {
        for(int i = 1; i < frame.GRAPH_SIZE; i++) {
	    		for(int j = 0; j < colonies.size()+1; j++) {
	    			populationGraph[i-1][j] = populationGraph[i][j];
	    		}
        }
	    int totalPopulation = 0;
	    for(Colony colony : colonies) {
	    		totalPopulation += colony.getPopulation();
	    }
	    double max = 0;
	    int iMaxPos = -1;
	    double sum100 = 0;
		for(int i = 0; i < colonies.size(); i++) {
			Colony colony = colonies.get(i);
            populationGraph[frame.GRAPH_SIZE-1][i] = colony.getPopulation() * 100 / totalPopulation;	
            sum100 += populationGraph[frame.GRAPH_SIZE-1][i];
            if (max < populationGraph[frame.GRAPH_SIZE-1][i]) {
            		max = populationGraph[frame.GRAPH_SIZE-1][i];
            		iMaxPos = i;
            }
            
		}
		populationGraph[frame.GRAPH_SIZE-1][iMaxPos] += - sum100 + 100;
		if(level.toInteger() % YEAR_TIME == 0) {
			populationGraph[frame.GRAPH_SIZE-1][colonies.size()] = level.toInteger() / YEAR_TIME;
			//System.out.println(map.populationGraph[GRAPH_SIZE-1][this.evSimulator.colonies.size()]);
		}else {
			populationGraph[frame.GRAPH_SIZE-1][colonies.size()] = -1;
		}
		 
    }
}
