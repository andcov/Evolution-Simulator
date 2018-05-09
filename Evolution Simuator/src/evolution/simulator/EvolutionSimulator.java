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
        //evolution life cycle
        for (int xLoc = 0; xLoc<1357; xLoc++) {
            for (int yLoc = 0; yLoc<628; yLoc++) {
                //System.out.println(evsim.mapWealth[xLoc][yLoc]);
            }
        }
        for(int i = 0; i < 10000; i++){
        		evsim.level.setValue(i);
        		
        		//aging and dying
            for(int x = 0; x<WIDTH; x++){
                for(int y = 0; y<HEIGHT; y++){
                    if(evsim.mapPerson[x][y] != null){           		
                    		Random random = new Random();
                    		if(random.nextInt(100)+1 == 1 || random.nextInt(100)+1 == 2){
                    			evsim.mapPerson[x][y].disease = 2;
                    		} 
                        if(evsim.eq.eq == true && (x > evsim.eq.x  && x< evsim.eq.x + evsim.eq.size) && 
                        			(y > evsim.eq.y  && y < evsim.eq.y + evsim.eq.size)) {
                        		evsim.mapPerson[x][y] = null;
                        }
                    		if(evsim.mapPerson[x][y] != null) {
                    			evsim.mapPerson[x][y].die(x, y, evsim.mapPerson, evsim.level.toInteger()); // NullPointer error, probably because of earthquakes
                    		}
                    }
                }
            }
            //moving
            for(int x = 0; x<WIDTH; x++){
                for(int y = 0; y<HEIGHT; y++){
                    if(evsim.mapPerson[x][y] != null){
                        evsim.mapPerson[x][y].move(x, y, evsim.mapPerson, evsim.frame);
                    }
                    
                }
            }
            //reproduction 
            for(int x = 0; x<WIDTH; x++){
                for(int y = 0; y<HEIGHT; y++){
                		if(evsim.mapPerson[x][y] != null){
                			if(evsim.mapPerson[x][y].swimAngle == -1) {
                				evsim.mapPerson[x][y].reproduction(x, y, evsim.level.toInteger(), evsim.mapPerson, evsim.frame);    
                			}
                		}
                }
            }
            //draw
            evsim.frame.repaint();
            TimeUnit.MILLISECONDS.sleep(7);
        }
    }  

    public EvolutionSimulator(){
    		level = new MutableInt(0);
        mapPerson = new Person[WIDTH][HEIGHT];
        eq = new NaturalDisaster();
        colonies = Configuration.initColonies();
        mapWealth = Configuration.initWealth();
        frame = new WorldGraphics(this);
    }
    
    //build colonies randomly in Africa
    /*public void placeColony(int X1, int Y1, int X2, int Y2, int X3, int Y3, int X4, int Y4){
        Random random = new Random();
        int x, y;
        int en;
        //red colony
        for(int i=0; i<70; i++){
            do{
                x = exponential(-50, 50, 4) + X1;
                y = exponential(-50, 50, 4) + Y1;
            }while(frame.getColor(x, y) == 1 && mapPerson[x][y] == null);
            en = random.nextInt(151)+150;
            mapPerson[x][y] = new Person(0, random.nextInt(6)+5, i%2==0, 1, 0, diseaseRandom(), en, en, -1, -1);
        }
        //pink colony
        for(int i=0; i<70; i++){
            do{
	            	x = exponential(-50, 50, 4) + X2;
	            	y = exponential(-50, 50, 4) + Y2;
            }while(frame.getColor(x, y) == 1 && mapPerson[x][y] == null);
            en = random.nextInt(151)+150;
            mapPerson[x][y] = new Person(0, random.nextInt(6)+5, i%2==0, 2, 0, diseaseRandom(), en, en, -1, -1);
        }
        //black colony
        for(int i=0; i<70; i++){
            do{
	            	x = exponential(-50, 50, 4) + X3;
	            	y = exponential(-50, 50, 4) + Y3;
            }while(frame.getColor(x, y) == 1 && mapPerson[x][y] == null);
            en = random.nextInt(151)+150;
            mapPerson[x][y] = new Person(0, random.nextInt(6)+5, i%2==0, 3, 0, diseaseRandom(), en, en, -1, -1);
        }
        //yellow colony
        for(int i=0; i<70; i++){
            do{
	            	x = exponential(-50, 50, 4) + X4;
	            	y = exponential(-50, 50, 4) + Y4;
            }while(frame.getColor(x, y) == 1 && mapPerson[x][y] == null);
            en = random.nextInt(151)+150;
            mapPerson[x][y] = new Person(0, random.nextInt(6)+5, i%2==0, 4, 0, diseaseRandom(), en, en, -1, -1);
        }
        frame.repaint();
    }*/
    
    public void placeColonies(){
        Random random = new Random();
        int x, y;
        int en;
        for(Colony col : colonies) {
        		for(int i=0; i<col.getPopulation(); i++){
                do{
                    x = exponential(-50, 50, 4) + col.getxPosition();
                    y = exponential(-50, 50, 4) + col.getyPosition();
                }while(frame.getColor(x, y) == 1 && mapPerson[x][y] == null);
                en = random.nextInt(col.getEnergy())+15;
                mapPerson[x][y] = new Person(0, random.nextInt(col.getStrength())+5, i < col.getFemalePopulation(), col, 
                		0, diseaseRandom(), en, en, -1, -col.getReproductionCycle());
            }
        }
        frame.repaint();
    }

    /*public void colonyPopulations() {
    	int type = 0;
	    	for(int x = 0; x < WIDTH; x++) {
	    		for(int y = 0; y < HEIGHT; y++) {
	    			if(mapPerson[x][y] != null) {
		    			type = mapPerson[x][y].colony;
		        		switch(type){
			    	        case 1:
			    	            col[0]++;
			    	            break;
			    	        case 2:
			    	        		col[1]++;
			    	            break;
			    	        case 3:
			    	        		col[2]++;
			    	            break;
			    	        case 4:
			    	        		col[3]++;
			    	            break;
		        		}
	    			}
	        	}
	    	}
    }*/
    
    public double diseaseRandom(){

    		Random random = new Random();
    		int disease = random.nextInt(10)+1;
    		switch(disease) {
    			case 1:
    				return 0.5;
    			case 2:
    				return 1;
    		}
    		return 0;
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
