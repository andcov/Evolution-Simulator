/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolution.simulator;

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
    public int energy;
    public int maxEnergy;
    public boolean sex;
    public Colony colony;
    public double disease;
    public int swimAngle;
    public int reproductionCycle;
    
    public Person(int age, double strength, boolean sex, Colony colony, int experience, double disease, int energy, 
    		int maxEnergy, int swimAngle, int reproductionCycle){
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
    }
    
    public void die(int xLoc, int yLoc, Person[][] map, int level){
	    if(age >= strength || energy == 0){
    			map[xLoc][yLoc] = null;
    	    		colony.setPopulation(colony.getPopulation() - 1);
    	    		return;
	    }
    		if(disease == 1){
	        strength--;
	        colony.setStrength(colony.getStrength() - 1);
	    }
		if(disease == 2){
	        energy--;
	        colony.setEnergy(colony.getEnergy() - 1);
	    }
		if(swimAngle != -1){
	        energy--;
	        colony.setEnergy(colony.getEnergy() - 1);
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
		if(disease == 3){
	        strength-=20;
	        for(int x = -3; x < 4; x++) {
		        	if(xLoc + x >= 1357 || xLoc + x <= 0) {
	    				break;
	    			}
	        		for(int y = -3; y < 4; y++) {
	        			if(yLoc + y >= 628 || yLoc + y <= 0) {
	        				break;
	        			}
	        			if(map[xLoc + x][yLoc + y] != null && map[xLoc + x][yLoc + y].colony == colony) {
	        				map[xLoc + x][yLoc + y].disease = 3;
	        			}
		        }
	        }
	    }
	}
    
    public void move(int xLoc, int yLoc, Person[][] map, WorldGraphics frame){
	    	if(swimAngle == -1) {
	        Random random = new Random(); 
	        int dir;
	        int x = 0, y = 0, i = 0, angle = 0;
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
	            Colony col = null;
	            if(map[xLoc + x][yLoc + y] != null) {col =  map[xLoc + x][yLoc + y].colony;}
	            
	            if (frame.getColor(xLoc + x, yLoc + y) == 2 
	                    && (map[xLoc + x][yLoc + y] == null)){
	            		break;
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
	        		Colony col = null;
	            if(map[xLoc + x][yLoc + y] != null) {col =  map[xLoc + x][yLoc + y].colony;}
		        if(i<8){
		            if(map[xLoc + x][yLoc + y] == null){
		                map[xLoc + x][yLoc + y] = map[xLoc][yLoc];
		                map[xLoc][yLoc] = null;
		            }else if(colony != map[xLoc + x][yLoc + y].colony){
		            		fight(xLoc, yLoc, xLoc + x, yLoc + y, map);
		            }
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
        str1 = (experience * 2 + strength * 3 + age * 2 + energy * 3)/10;
        str2 = (map[locX2][locY2].experience * 2 + map[locX2][locY2].strength * 4 
                + map[locX2][locY2].age * 2 + map[locX1][locY1].energy * 2)/10;
        if(str1 > str2){
            map[locX1][locY1].experience++;
            map[locX2][locY2] = map[locX1][locY1];
            map[locX1][locY1] = null;
    	    		colony.setPopulation(colony.getPopulation() - 1);
            return 1;
        }else if(str1 < str2){
            map[locX2][locY2].experience++;
            map[locX1][locY1] = map[locX2][locY2];
            map[locX2][locY2] = null;
    	    		colony.setPopulation(colony.getPopulation() - 1);
            return 2;
        }
        map[locX2][locY2] = null;
        map[locX1][locY1] = null;
	    colony.setPopulation(colony.getPopulation() - 2);
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
		}else if(colony != map[x][y].colony){
            fight(xLoc, yLoc, x, y, map);
        }
    }
    
    public void reproduction(int xLoc, int yLoc, int generation, Person[][] map, WorldGraphics frame){
        if(map[xLoc][yLoc] != null){
            if(isReproduction()){
                Random random = new Random(); 
                int dir;
                int x = 0, y = 0, j = 0;
                dir = random.nextInt(9-1)+1;
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
                int maxE = 0;
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
                            maxD = map[xLoc + xa][yLoc + ya].disease;
                        }
                    }
                }
                if(maxS != 0 && j<8){
                	    double str = (strength + maxS)/2 + mutation(generation);
                	    int en = (maxEnergy + maxE)/2 + (int) mutation(generation);
                	    //reproductionCycle = 0;
                	    colony.setPopulation(colony.getPopulation() + 1);
                	    reproductionCycle = colony.getReproductionCycle();
                	    colony.setStrength(colony.getStrength() + (int)Math.round(str));
                	    colony.setEnergy(colony.getEnergy() + en);
                    Person newBorn = new Person(0, str, random.nextBoolean(), 
                    				colony, 0, diseaseChild(disease, 
                            		maxD), en, en, -1, -1);
                    if(random.nextInt(400000) == 0){
                    		newBorn.disease = 3;
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
    
    public double diseaseChild(double feDisease, double maDisease){
	    		if(feDisease != 3 && maDisease != 3) {
			Random random = new Random();
			if(feDisease == 2) {feDisease = 0;}
			if(maDisease == 2) {maDisease = 0;}
			if(feDisease == 0 && maDisease == 0) {
				return 0;
			}else if(feDisease == 1 && maDisease == 1) {
				return 1;
			}else if((feDisease == 1 && maDisease == 0.5) || (maDisease == 1 && feDisease == 0.5)) {
				return 0.5;
			}else if((feDisease == 0.5 && maDisease == 0) || (maDisease == 0.5 && feDisease == 0)) {
				if(random.nextBoolean()) {
					return 0;
				}else {
					return 0.5;
				}
			}else if(feDisease == 0.5 && maDisease == 0.5) {
				if(random.nextBoolean()) {
					return 0.5;
				}else {
					if(random.nextBoolean()) {
						return 0;
					}else {
						return 1;
					}
				}
			}
			return 1;
    		}else {
    			return 3;
    		}
    }
}
