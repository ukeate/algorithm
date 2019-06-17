package test;

import java.util.Random;

import org.junit.Test;

public class TestHere {
	
	@Test
	public void testHere(){
		Random ra = new Random();
		for(int i = 0; i < 100; i++){
			System.out.println(ra.nextInt(20));
		}
	}
	
}
