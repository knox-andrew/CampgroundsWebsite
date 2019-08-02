package com.techelevator.parksystem.exception;


/*
 * A simple class for cathcing bad date ranges, this includes
 * a start date that is after the end date and a start date
 * that is before the current date
 */
public class InvalidDateRangeException extends Exception {
	
	public static final String ARRIVAL_DATE_IS_PASSED_ERROR_MESSAGE = "Cannot enter an arrival date that is already passed";
	public static final String DEPARTURE_IS_BEFORE_ARRIVAL_ERROR_MESSAGE = "Departure date cannot be before arrival date";
	
	public InvalidDateRangeException () {
		super();
	}
	
	public InvalidDateRangeException(String s) {
		super(s);
	}
}
