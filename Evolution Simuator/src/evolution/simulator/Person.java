/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolution.simulator;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author AndreiCo
 */
public class Person {
	public final static int WIDTH = 1357;
	public final static int HEIGHT = 628;
	private static final int STEPS = 15;
    private static final int YEAR_TIME = 12;

	
	//WorldGraphics frame = new WorldGraphics(map);
	
    public int age;
    public int experience;
    public double strength;
    public double energy;
    public double maxEnergy;
    public boolean sex;
    public Colony colony;
    public boolean[] disease;
    public int swimAngle;
    public int reproductionCycle;
    public double adaptability;
    
    public Person(int age, double strength, boolean sex, Colony colony, int experience, boolean[] disease, double energy, 
    		double maxEnergy, int swimAngle, int reproductionCycle, int adaptability){
        this.age = age;
        this.strength = strength;
        this.sex = sex;
        this.colony = colony;
        this.experience = experience;
        this.disease = disease;
        this.energy = energy;
        this.maxEnergy = maxEnergy;
        this.swimAngle = swimAngle;
        this.reproductionCycle = reproductionCycle;
        this.adaptability = adaptability;
    }
    
    public void die(int xLoc, int yLoc, Person[][] map, int level, int wealth){
	    if(age >= strength || energy == 0){
    			map[xLoc][yLoc] = null;
    	    		colony.setPopulation(colony.getPopulation() - 1);
    	    		return;
	    }
	    boolean isEnergyModified = false;
	    if(swimAngle != -1){
			colony.setEnergy(colony.getEnergy() - energy*0.04);
			energy = energy*0.96;
			isEnergyModified = true;
	    }else if(disease[0]) {
	    		colony.setEnergy(colony.getEnergy() - energy*(0.1 - ((this.adaptability - wealth) * 0.0125)));
			energy = energy*((this.adaptability - wealth) * 0.0125);
	    		isEnergyModified = true;
	    }
    		if(disease[1]){
    			colony.setStrength(colony.getStrength() - strength*0.04);
	        strength = strength*0.96;
	    }
    		if(disease[3]){
    			colony.setEnergy(colony.getEnergy() - energy*0.04);
    			energy = energy*0.96;
    			isEnergyModified = true;
    		}
    		if(disease[4]){
			colony.setStrength(colony.getStrength() - strength*0.42);
	        strength = strength*0.58;
	        for(int x = -3; x < 4; x++) {
		        	if(xLoc + x >= 1357 || xLoc + x <= 0) {
	    				break;
	    			}
	        		for(int y = -3; y < 4; y++) {
	        			if(yLoc + y >= 628 || yLoc + y <= 0) {
	        				break;
	        			}
	        			if(map[xLoc + x][yLoc + y] != null && map[xLoc + x][yLoc + y].colony == colony) {
	        				map[xLoc + x][yLoc + y].disease[4] = true;
	        			}
		        }
	        }
	    }
    		if(isEnergyModified == false && Math.round(energy) <= Math.round(maxEnergy)) {
    			colony.setEnergy(colony.getEnergy() + energy*0.05);
    			energy = energy*1.05;
    		}
    		
		if(level % YEAR_TIME == 0) {
			age++;
			colony.setAge(colony.getAge() + 1);
		}
		if(level % colony.getReproductionCycle() == 0) {
			if(reproductionCycle > -1) {
				reproductionCycle--;
			}
		}
	}
    
    public void move(int xLoc, int yLoc, Person[][] map, WorldGraphics frame, int wealth, ArrayList<Colony> colonies){
	    	if(swimAngle == -1) {
	        Random random = new Random(); 
	        int dir;
	        int x = 0, y = 0, i = 0, angle = 0, xFight = 0, yFight = 0;
	        dir = random.nextInt(8)+1;
	        for(i =0; i < 8; i++){
	            switch(dir){
	                case 1:
	                    y++;
	                    angle = random.nextInt(46) + 45;
	                    break;
	                case 2:
	                    y--;
	                    angle = random.nextInt(46) + 225;
	                    break;
	                case 3:
	                    x++;
	                    angle = random.nextInt(46) + 315;
	                    break;
	                case 4:
	                    x--;
	                    angle = random.nextInt(46) + 135;
	                    break;
	                case 5:
	                    x++;
	                    y++;
	                    angle = random.nextInt(46) + 0;
	                    break;
	                case 6:
	                    x--;
	                    y++;
	                    angle = random.nextInt(46) + 90;
	                    break;
	                case 7:
	                    x++;
	                    y--;
	                    angle = random.nextInt(46) + 270;
	                    break;
	                case 8:
	                    x--;
	                    y--;
	                    angle = random.nextInt(46) + 180;
	                    break;
	            }
	            
	            if((xLoc + x >= WIDTH || yLoc + y >= HEIGHT) || (xLoc + x <= 0 || yLoc + y <= 0)) {
	                return;
	            }
	            //Colony col = null;
	            //if(map[xLoc + x][yLoc + y] != null) {col =  map[xLoc + x][yLoc + y].colony;}
	            
	            if (frame.getColor(xLoc + x, yLoc + y) == 2) {
	                if (map[xLoc + x][yLoc + y] == null){
	            			break;
	                } else if (!colony.equals(map[xLoc + x][yLoc + y].colony)) {
	                	xFight = x;
	                	yFight = y;
	                }
	            }else if(frame.getColor(xLoc + x, yLoc + y) == 1 && random.nextInt(500) + 1 == 1 
	            			&& map[xLoc + x][yLoc + y] == null) {
	            		swimAngle = angle;
	            		break;
	            }
	            if(dir==8){
	                dir = 1;
	            }else{
	                dir = (dir+1)%9;
	            }
	        }
	        if(swimAngle == -1) {
	        	
	        		//if(map[xLoc + x][yLoc + y] != null) {	System.out.println(colonies.indexOf(colony) +" "+ colonies.indexOf(map[xLoc + x][yLoc + y].colony));}
		        if(map[xLoc + x][yLoc + y] == null){
	                map[xLoc + x][yLoc + y] = map[xLoc][yLoc];
	                map[xLoc][yLoc] = null;
		        }//else if(colonies.indexOf(colony) != colonies.indexOf(map[xLoc + x][yLoc + y].colony)){
		        else if(xFight != 0 || yFight != 0){
            			fight(xLoc, yLoc, xLoc + xFight, yLoc + yFight, map);
            			System.out.println("fight");
	            }
		        
		        if((int)this.adaptability == wealth) {
		        		adaptability = wealth;
		        }else if((int)adaptability != wealth) {
		        		adapt(wealth);
		        }else {
		        		this.disease[0] = false;
		        }
	        }else {
	        		swim(xLoc, yLoc, map, frame);
	        }
	    }else {
	    		swim(xLoc, yLoc, map, frame);
	    }
	}
    
    public int fight(int locX1, int locY1, int locX2, int locY2, Person[][] map){
    		double str1, str2;
        str1 = (experience * 2.0 + strength * 3.0 + (strength - age) * 2.0+ energy * 3.0)/10.0;
        str2 = (map[locX2][locY2].experience * 2.0 + map[locX2][locY2].strength * 3.0 
                + (map[locX2][locY2].strength - map[locX2][locY2].age) * 2.0 + map[locX1][locY1].energy * 3.0)/10.0;
        if(str1 > str2){
            map[locX1][locY1].experience++;
            map[locX2][locY2] = map[locX1][locY1];
            map[locX1][locY1] = null;
    	    		colony.setPopulation(colony.getPopulation() - 1);
    	    		//System.out.println("fight");
            return 1;
        }else if(str1 < str2){
            map[locX2][locY2].experience++;
            map[locX1][locY1] = map[locX2][locY2];
            map[locX2][locY2] = null;
    	    		colony.setPopulation(colony.getPopulation() - 1);
    	    		//System.out.println("fight");
            return 2;
        }
        map[locX2][locY2] = null;
        map[locX1][locY1] = null;
	    colony.setPopulation(colony.getPopulation() - 2);
	    //System.out.println("fight");
        return 0;
    }
    
    public void swim(int xLoc, int yLoc, Person[][] map, WorldGraphics frame){
		double radian = swimAngle;
		double sin = Math.sin( Math.toRadians(radian));
		double cos = Math.cos( Math.toRadians(radian));
		int x = (int) (Math.round(cos * STEPS) + xLoc);
		int y = (int) (Math.round(sin * STEPS) + yLoc);
		if(x >= 1356) {
			x = 0;
		}else if(x <= 1) {
			x = 1356;
		}
		if(y >= 627) {
			y=0;
		}else if(y <= 1) {
			y = 627;
		}
		if(map[x][y] == null) {
			map[x][y] = map[xLoc][yLoc];
			map[xLoc][yLoc] =null;
			if(frame.getColor(x, y) == 2) {
				swimAngle = -1;
			}
		}else{
            swimAngle = swimAngle + 180;
            if(swimAngle < 360) {
            		swimAngle = swimAngle - 360;
            }
        }
    }
    
    public void adapt(int wealth) {
    		if(wealth < this.adaptability) {
    			Random random = new Random();
        		if((int)(adaptability - wealth) == random.nextInt(7)) {
        			this.disease[0] = true;
        		}
    			this.adaptability -= 0.5;
    		}else {
    			this.adaptability += 1;
    		}
    }
    
    public void reproduction(int xLoc, int yLoc, int generation, Person[][] map, WorldGraphics frame, int wealth){
        if(map[xLoc][yLoc] != null){
            if(isReproduction()){
                Random random = new Random(); 
                int dir;
                int x = 0, y = 0, j = 0;
                dir = random.nextInt(8)+1;
                for(j =0; j < 8; j++){
                    switch(dir){
                        case 1:
                            y++;
                            break;
                        case 2:
                            y--;
                            break;
                        case 3:
                            x++;
                            break;
                        case 4:
                            x--;
                            break;
                        case 5:
                            x++;
                            y++;
                            break;
                        case 6:
                            x--;
                            y++;
                            break;
                        case 7:
                            x++;
                            y--;
                            break;
                        case 8:
                            x--;
                            y--;
                            break;
                    }
                    
                    if((xLoc + x >= WIDTH || yLoc + y >= HEIGHT) || (xLoc + x <= 0 || yLoc + y <= 0)) {
                        return;
                    }

                    if (frame.getColor(xLoc + x, yLoc + y) == 2 
                            && map[xLoc + x][yLoc + y] == null){
                        break;
                    }
                    if(dir==8){
                        dir = 1;
                    }else{
                        dir = (dir+1)%9;
                    }
                }
                double maxS = 0;
                double maxE = 0;
                double maxD = 0;
                for(int xa = -10; xa < 11; xa++){
                    for(int ya = -10; ya < 11; ya++){
                        if((xLoc + xa >= WIDTH || yLoc + ya >= HEIGHT) || (xLoc + xa <= 0 || yLoc + ya <= 0)) {
                        		return;
                        }
                        if (map[xLoc + xa][yLoc + ya] != null && 
		                        map[xLoc + xa][yLoc + ya].colony == colony &&
                                map[xLoc + xa][yLoc + ya].sex == false && 
                                map[xLoc + xa][yLoc + ya].strength>=maxS &&
                                map[xLoc + xa][yLoc + ya].maxEnergy>=maxE
                                ){
                            maxS = map[xLoc + xa][yLoc + ya].strength;
                            maxE = map[xLoc + xa][yLoc + ya].maxEnergy;
	                    	   if(map[xLoc + xa][yLoc + ya].disease[1]) {
	                                maxD = 1;
                            }else if(map[xLoc + xa][yLoc + ya].disease[2]) {
                                maxD = 0.5;
                            }else if(map[xLoc + xa][yLoc + ya].disease[4]) {
                            	   maxD = 3;
	                        }else {
	                        	   maxD = 0;
	                        }
                        }
                    }
                }
                if(maxS != 0 && j<8){
                	    double str = (strength + maxS)/2 + mutation(generation);
                	    double en = (maxEnergy + maxE)/2 + mutation(generation);
                	    //reproductionCycle = 0;
                	    colony.setPopulation(colony.getPopulation() + 1);
                	    reproductionCycle = colony.getReproductionCycle();
                	    colony.setStrength(colony.getStrength() + Math.round(str));
                	    colony.setEnergy(colony.getEnergy() + en);
                    Person newBorn = new Person(0, str, random.nextBoolean(), 
                    				colony, 0, diseaseChild(maxD), en, en, -1, -1, wealth);
                    if(random.nextInt(400000) == 0){
                    		newBorn.disease[4] = true;
                    }
                    map[xLoc + x][yLoc +y] = newBorn;
                }
            }
        }
    }
    
    public boolean isReproduction() {
    		if(sex == true && age > 1 && age < 25 && reproductionCycle == -1) {
    			return true;
    		}else {
    			return false;
    		}
    		
    }
    
    public double mutation(int generation){
        Random random = new Random();
        double number = 0;
        for(int i=1; i<=100; i++){
            if(random.nextBoolean() || i == 100){
                number = i;
                break;
            }
        }
        if(generation != 0){
            if(random.nextBoolean()){
                return number/(generation/5);
            }else {
                return (0-number)/(generation/5);
            }
        }else{
            if(random.nextBoolean()){
                return number;
            }else{
                return 0-number;
            }
        }
    }
    
    public boolean[] diseaseChild(double maDisease){
    		double feDisease; 
    		if(disease[1]) {
    			feDisease = 1;
	    }else if(disease[2]) {
	    		feDisease = 0.5;
	    }else if(disease[4]) {
	    		feDisease = 3;
	    }else {
	    		feDisease = 0;
	    }
    		boolean[] Disease = new boolean[5];
    		for(int i = 0; i < 5; i++) {
    			Disease[i] = false;
    		}
	    	if(feDisease == 3 || maDisease == 3) {
	    		Disease[4] = true;
	    	}
	    	
		Random random = new Random();
		if(feDisease == 0 && maDisease == 0) {
			return Disease;
		}else if(feDisease == 1 && maDisease == 1) {
			Disease[1] = true;
			return Disease;
		}else if((feDisease == 1 && maDisease == 0.5) || (maDisease == 1 && feDisease == 0.5)) {
			if(random.nextBoolean()) {
				Disease[	1] = true;
				return Disease;
			}else {
				Disease[2] = true;
				return Disease;
			}
		}else if((feDisease == 0.5 && maDisease == 0) || (maDisease == 0.5 && feDisease == 0)) {
			if(random.nextBoolean()) {
				return Disease;
			}else {
				Disease[2] = true;
				return Disease;
			}
		}else if(feDisease == 0.5 && maDisease == 0.5) {
			if(random.nextBoolean()) {
				Disease[2] = true;
				return Disease;
			}else {
				if(random.nextBoolean()) {
					return Disease;
				}else {
					Disease[1] = true;
					return Disease;
				}
			}
		}
		return Disease;
    }
}
