package edu.handong.csee.java.util;

public class FileNotExcelFormatException extends Exception{

	public FileNotExcelFormatException() {
		super("the file extension is does not match excel format");
		// TODO Auto-generated constructor stub
	}
	
	public FileNotExcelFormatException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
