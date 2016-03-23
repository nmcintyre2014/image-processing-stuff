package org.ndm;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestOrthorectifier {

	@Test
	public void test(){
		
		Orthorectifier orthorectifier = new Orthorectifier();
		Boolean thrown = Boolean.FALSE;
		try{
			orthorectifier.go();
		} catch (Exception e){
			thrown = Boolean.TRUE;
		}
		assertTrue(thrown);
		
	}
}
