package evolution.simulator;

import java.awt.Color;
import java.util.Random;

// natural disasters ( only earthquakes at the moment )
public class NaturalDisaster {
	public  int size;
	public  int x;
	public  int y;
	public  int ux;
	public  int uy;
	public  boolean eq = false;
	private  int times = 3;
	public  Color color = new Color(139,69,19);
	
	// create and move earthquakes
	public void earthquake() {
		Random ran = new Random();
		int chance = ran.nextInt(1000);
		if(eq == false) {
			eq = true;
			times = 3;
			if(chance <= 16) {
				size = 128;
			}else if(chance <= 32) {
				size = 64;
			}else if(chance <= 64) {
				size = 32;
			}else if(chance <= 128) {
				size = 16;
			}else {
				size = 0;
				eq = false;
				return;
			}
			
			x = ran.nextInt(1357);
			y = ran.nextInt(628);
			if(x + size >= 1357) {
				x = x - size;
			}
			if(y + size >= 628) {
				y = y - size;
			}
			ux = x;
			uy = y;
		}else if(eq == true){
			int deviationX = ran.nextInt(5) + 1;
			int deviationY = ran.nextInt(5) + 1;
			boolean isMoveX = ran.nextBoolean();
			boolean isMoveY = ran.nextBoolean();
			
			if(isMoveX && x + deviationX < 1357) {
				ux = x + deviationX;
			}else {
				ux = x - deviationX;
			}
			
			if(isMoveY && x + deviationY < 1357) {
				uy = y + deviationY;
			}else {
				uy = y - deviationY;
			}
			times--;
			if(times == 1) {
				eq = false;
			}
		}
	}
}