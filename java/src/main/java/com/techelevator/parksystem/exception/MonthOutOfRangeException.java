package com.techelevator.parksystem.exception;

/**
 * A simple class for catching bad month inputs
 */
public class MonthOutOfRangeException extends Exception {
	public MonthOutOfRangeException() {
		super();
	}
	
	public MonthOutOfRangeException(String s) {
		super(s);
	}
}
