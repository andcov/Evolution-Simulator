package evolution.simulator;

import java.awt.Color;
import java.util.Random;

public class NaturalDisaster {
	public  int size;
	public  int x;
	public  int y;
	public  int ux;
	public  int uy;
	public  boolean eq = false;
	private  int times = 3;
	public  Color color = new Color(139,69,19);
	
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
			int deX = ran.nextInt(5) + 1;
			int deY = ran.nextInt(5) + 1;
			boolean diX = ran.nextBoolean();
			boolean diY = ran.nextBoolean();
			
			if(diX && x + deX < 1357) {
				ux = x + deX;
			}else {
				ux = x - deX;
			}
			
			if(diY && x + deY < 1357) {
				uy = y + deY;
			}else {
				uy = y - deY;
			}
			times--;
			if(times == 1) {
				eq = false;
			}
		}
	}
}