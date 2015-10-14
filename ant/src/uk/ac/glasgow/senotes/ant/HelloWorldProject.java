package uk.ac.glasgow.senotes.ant;

public class HelloWorldProject {
		
	/**
	 * A method that always returns true to demonstrate invocation of unit tests via ant.
	 * @return true.
	 */
	public Boolean dummyMethod(){
		return true;
	}
	
	public static void main (String[] args){
		System.out.println("if it says [java] to the left, I was invoked from ant.");
	}
}
