/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolution.simulator;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.mutable.MutableInt;

/**
 *
 * @author AndreiCo
 */

// it contains the logic of the application
public class EvolutionSimulator
{
	// map dimensions
	public final static int WIDTH = 1357;
	public final static int HEIGHT = 628;
	
	// graph info
    public int[][] populationGraph;

    // enable/disable external factors
	public boolean isEpidemics;
	public boolean isEarthquake;
	
	// current iteration in evolution
	MutableInt level;
	private int localLevel = 0;
	
	// array of individuals
    Person[][] mapPerson;
    
    // array of zones of wealth
    int[][] mapWealth;
    
    // earthquake object
    NaturalDisaster earthquake;
    
    // graphical representation
    WorldGraphics frame;
    
    // the list of all colonies
    ArrayList<Colony> colonies;
    
    public static void main(String[] args) throws InterruptedException{
    		EvolutionSimulator evsim = new EvolutionSimulator();	
        evsim.placeColonies();
        
        TimeUnit.MILLISECONDS.sleep(3000);
        
        while(true) {
        		evsim.run();
        		TimeUnit.MILLISECONDS.sleep(50);
        }   
    }  

    // constructor - initialize all of the world's characteristics
    public EvolutionSimulator(){
    		level = new MutableInt(0);
        mapPerson = new Person[WIDTH][HEIGHT];
        isEpidemics = Configuration.isEpidemics();
        isEarthquake = Configuration.isEarthquake();
        earthquake = new NaturalDisaster();
        colonies = Configuration.initColonies();
        mapWealth = Configuration.initWealth();
        populationGraph = new int[WorldGraphics.GRAPH_SIZE][colonies.size() + 1];
	    	for(int i = 0; i < WorldGraphics.GRAPH_SIZE; i++) {
	    		for(int j = 0; j < colonies.size(); j++) {
	    			populationGraph[i][j] = 0;
	    		}
	    		populationGraph[i][colonies.size()] = -1;
	    }
	    	frame = new WorldGraphics(this);
    }
    
    //runs the simulation
    public void run() {
    		//evolution life cycle
        while(!WorldGraphics.isPaused){
        		populationGraph();
        		// evolving and dying
            for(int x = 0; x<WIDTH; x++){
                for(int y = 0; y<HEIGHT; y++){
                    if(mapPerson[x][y] != null){
                    		// randomly get energy disease
	                		Random random = new Random();
	                		if(random.nextInt(100)+1 <= 2){
	                			mapPerson[x][y].disease[3] = true;
	                		} 
	                		
	                		// die because of earthquakes
                        if(isEarthquake) {
	                        if(earthquake.eq == true && (x > earthquake.x  && x< earthquake.x + earthquake.size) && 
	                        			(y > earthquake.y  && y < earthquake.y + earthquake.size)) {
	                        		mapPerson[x][y] = null;
	                        }
                        }
                        
	                		if(mapPerson[x][y] != null) {
	                			mapPerson[x][y].evolve(x, y, mapPerson, level.toInteger(), mapWealth[x][y]); // NullPointer error, probably because of earthquakes
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
            				mapPerson[x][y].reproduction(x, y, level.toInteger(), mapPerson, frame, mapWealth[x][y], this);    
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
            
            // iterate
            localLevel++;
            level.setValue(localLevel);
        }
    }
    
    // restart the simulation
    public void restart() {
    		WorldGraphics.isPaused = true;
        try {
			TimeUnit.MILLISECONDS.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        // delete previous individuals
	    	for(int x = 0; x < WIDTH; x++) {
	    		for(int y = 0; y < HEIGHT; y++) {
	        		mapPerson[x][y] = null;
	        	}
	    	}
	    	
	    	// reset iteration
	    	level.setValue(0);
	    	localLevel = 0;
	    	
	    	// read configuration file again
	    	colonies = Configuration.initColonies();
	    	mapWealth = Configuration.initWealth();
	    	
	    	//reset graph
	    	for(int i = 0; i < WorldGraphics.GRAPH_SIZE; i++) {
	    		for(int j = 0; j < colonies.size(); j++) {
	    			populationGraph[i][j] = 0;
	    		}
	    		populationGraph[i][colonies.size()] = -1;
	    }
	    	
	    	//replace individuals
	    	placeColonies();
	    	
        WorldGraphics.isPaused = false;
    }
    
    // place initial individuals
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
                		0, diseaseRandom(), en, en, -1, col.getReproductionCycle(), mapWealth[x][y]);
            }
        }
    }
    
    // randomly infect with disease
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

    // choose person location near a point
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
    
    // calculate graphic statistics
    private void populationGraph() {
        for(int i = 1; i < WorldGraphics.GRAPH_SIZE; i++) {
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
            populationGraph[WorldGraphics.GRAPH_SIZE-1][i] = colony.getPopulation() * 100 / totalPopulation;	
            sum100 += populationGraph[WorldGraphics.GRAPH_SIZE-1][i];
            if (max < populationGraph[WorldGraphics.GRAPH_SIZE-1][i]) {
            		max = populationGraph[WorldGraphics.GRAPH_SIZE-1][i];
            		iMaxPos = i;
            }
            
		}
		populationGraph[WorldGraphics.GRAPH_SIZE-1][iMaxPos] += - sum100 + 100;
		if(level.toInteger() % 10 == 0) {
			populationGraph[WorldGraphics.GRAPH_SIZE-1][colonies.size()] = level.toInteger();
		}else {
			populationGraph[WorldGraphics.GRAPH_SIZE-1][colonies.size()] = -1;
		}
		 
    }
}
