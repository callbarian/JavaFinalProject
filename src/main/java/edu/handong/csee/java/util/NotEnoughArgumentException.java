package edu.handong.csee.java.util;



public class NotEnoughArgumentException extends Exception{

	public NotEnoughArgumentException() {
		super("No CLI argument Exception! Please put a file path.");
		// TODO Auto-generated constructor stub
	}

	public NotEnoughArgumentException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	
}
