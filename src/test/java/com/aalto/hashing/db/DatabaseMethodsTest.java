package com.aalto.hashing.db;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DatabaseMethodsTest {

	private DatabaseMethods database;
	
	@Before
	public void setUp() throws Exception {
		database = new DatabaseMethods("7servers.db");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testQueryData() {
//		for (int i = 0; i < 1000; i ++) {
//			
//		}
		fail("Not yet implemented");
	}

	@Test
	public void testSearchData() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadAllData() {
		fail("Not yet implemented");
	}

}
