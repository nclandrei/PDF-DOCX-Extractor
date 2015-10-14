package uk.ac.glasgow.senotes.ant.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.glasgow.senotes.ant.HelloWorldProject;

public class HelloWorldTest {

	private HelloWorldProject project;
	
	@Before
	public void setUp() throws Exception {
		project = new HelloWorldProject();
	}

	@After
	public void tearDown() throws Exception {
		project = null;
	}

	@Test
	public void testDummyMethod() {
		assertTrue(project.dummyMethod());
	}

}
