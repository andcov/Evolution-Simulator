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


	MutableInt level;
    Person[][] mapPerson;
    int[][] mapWealth;
    NaturalDisaster eq;
    WorldGraphics frame;
    ArrayList<Colony> colonies;
    
    private int[] col = new int[4];
    
    public static void main(String[] args) throws InterruptedException{
    	EvolutionSimulator evsim = new EvolutionSimulator();	
        evsim.placeColonies();
        TimeUnit.MILLISECONDS.sleep(3000);
        
    }  

    public EvolutionSimulator(){
    		level = new MutableInt(0);
        mapPerson = new Person[WIDTH][HEIGHT];
        eq = new NaturalDisaster();
        colonies = Configuration.initColonies();
        mapWealth = Configuration.initWealth();
        frame = new WorldGraphics(this);
    }
    
    public void run() {
    	//evolution life cycle
        for(int i = 0; i < 10000; i++){
        		level.setValue(i);
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
            TimeUnit.MILLISECONDS.sleep(7);
        }
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
                en = random.nextInt((int)col.getEnergy())+15;
                mapPerson[x][y] = new Person(0, random.nextInt((int)col.getStrength())+5, i < col.getFemalePopulation(), col, 
                		0, diseaseRandom(), en, en, -1, -col.getReproductionCycle(), mapWealth[x][y]);
            }
        }
        frame.repaint();
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
}
