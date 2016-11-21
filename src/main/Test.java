package main;

import core.Calculation;

public class Test {
	public static void main (String[] args){
		Calculation c = new Calculation();
		long t1 = System.currentTimeMillis();
		c.calcAll();
		long t2 = System.currentTimeMillis() - t1;
		System.out.println(t2);
	}
}
