package Calcul;

import java.math.*;

public class Process {
	
	private double amount;
	private double point;
	private double score;

	public Process () {}
	
	public double getScore(String[] grade, int[] num) {		
		
		for(int i=0; i<10; i++) {
			if(grade[i] == null)
				break;
			if(grade[i] == "A+") {
				amount += 4.5 * num[i];
				point += num[i];
				continue;
			}
			if(grade[i] == "A") {
				amount += 4 * num[i];
				point += num[i];
				continue;
			}
			if(grade[i] == "B+") {
				amount += 3.5 * num[i];
				point += num[i];
				continue;
			}			
			if(grade[i] == "B") {
				amount += 3 * num[i];
				point += num[i];
				continue;
			}
			if(grade[i] == "C+") {
				amount += 2.5 * num[i];
				point += num[i];
				continue;
			}
			if(grade[i] == "C") {
				amount += 2 * num[i];
				point += num[i];
				continue;
			}
			if(grade[i] == "D+") {
				amount += 1.5 * num[i];
				point += num[i];
				continue;				
			}
			if(grade[i] == "D") {
				amount += 1 * num[i];
				point += num[i];
				continue;
			}
			if(grade[i] == "F") {
				amount += 0 * num[i];
				point += num[i];
				continue;
			}
		}		
		score = amount / point;
		amount = 0;
		point = 0;
		score = decimalScale(Double.toString(score), 2, 2);
		return score;
	}
	
	public double getTarget(double total, double grade, double hope) {
		
		double target = 0;
		
		target = ( ((140.0 * hope) - (total * grade)) / (140 - total) );
		
		target = decimalScale(Double.toString(target), 2, 2);
		
		return target;
	}
	
	public double getFailTarget(double total, double grade, double hope) {
		
		double target = 0;
		
		target = ( ((total * grade) + ((140 - total) * 4.5)) / 140 );
		
		target = decimalScale(Double.toString(target), 2, 2);
		
		return target;
	}
	

	
	public double decimalScale(String decimal , int loc , int mode) {
		
		BigDecimal bd = new BigDecimal(decimal);
		BigDecimal result = null;
		
		if(mode == 1) {
			result = bd.setScale(loc, BigDecimal.ROUND_DOWN);
		}
		else if(mode == 2) {
			result = bd.setScale(loc, BigDecimal.ROUND_HALF_UP);
		}
		else if(mode == 3) {
			result = bd.setScale(loc, BigDecimal.ROUND_UP);
		}
		
		return result.doubleValue();
	}	
}
