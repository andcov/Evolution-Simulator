/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolution.simulator;

import java.awt.Color;

/**
 *
 * @author AndreiCo
 */
public class Colony {
	
	
	private Color color;
	private int population;
	private int xPosition;
	private int yPosition;	
	private int femalePopulation;
	private int reproductionCycle;
	private double strength;
	private double energy;
	private int age;
	
	
    public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Color getColor() {
		return color;
	}
    
	public void setColor(int red, int green, int blue) {
		this.color = new Color(red, green, blue);
	}


    public int getPopulation() {
		return population;
	}
	public void setPopulation(int population) {
		this.population = population;
	}
	
	public int getxPosition() {
		return xPosition;
	}
	public void setxPosition(int xPosition) {
		this.xPosition = xPosition;
	}
	
	
	public int getyPosition() {
		return yPosition;
	}
	public void setyPosition(int yPosition) {
		this.yPosition = yPosition;
	}


	public int getFemalePopulation() {
		return femalePopulation;
	}
	public void setFemalePopulation(int femalePopulation) {
		this.femalePopulation = femalePopulation;
	}
	
	
	public int getReproductionCycle() {
		return reproductionCycle;
	}
	public void setReproductionCycle(int reproductionCycle) {
		this.reproductionCycle = reproductionCycle;
	}
	
	
	public double getStrength() {
		return strength;
	}
	public void setStrength(double strength) {
		this.strength = strength;
	}
	
	
	public double getEnergy() {
		return energy;
	}
	public void setEnergy(double energy) {
		this.energy = energy;
	}
}
