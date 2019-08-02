package com.techelevator.parksystem.model;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.time.LocalDate;

public class Campground {
	
	public static final String DISPLAY_FORMAT = "%-30s%-13s%-13s%-10s";
	public static final String DISPLAY_HEADER = String.format("%-5s" + DISPLAY_FORMAT, "", "Name", "Open", "Close", "Daily Fee");
	
	private int campgroundId;
	private int parkId;
	private String name;
	private String openFromMM;
	private String openToMM;
	private BigDecimal dailyFee;

	public int getCampgroundId() {
		return campgroundId;
	}

	public void setCampgroundId(int campgroundId) {
		this.campgroundId = campgroundId;
	}

	public int getParkId() {
		return parkId;
	}

	public void setParkId(int parkId) {
		this.parkId = parkId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOpenFromMM() {
		return openFromMM;
	}

	public void setOpenFromMM(String openFromMM) {
		this.openFromMM = openFromMM;
	}

	public String getOpenToMM() {
		return openToMM;
	}

	public void setOpenToMM(String openToMM) {
		this.openToMM = openToMM;
	}

	public BigDecimal getDailyFee() {
		return dailyFee;
	}

	public void setDailyFee(BigDecimal dailyFee) {
		this.dailyFee = dailyFee.setScale(2);
	}

	/**
	 * @param date takes dates from customer entry (reservation To and From) pulls
	 *             the month value from the date
	 * @return isOpen = true if the park is open during that month
	 */
	public boolean isOpenOnDate(LocalDate date) {
		boolean isOpen = false;
		int fromMM = Integer.parseInt(openFromMM);
		int toMM = Integer.parseInt(openToMM);

		int month = date.getMonthValue();

		if (month <= toMM && month >= fromMM) {
			isOpen = true;
		}
		return isOpen;
	}
	
	/*
	 * Used to make converting the month number to a name more easy for this and 
	 * other classes
	 */
	public static String monthNumToMonthName(int monthNum) {
		String monthName = new DateFormatSymbols().getMonths()[monthNum - 1];

		return monthName.substring(0, 1).toUpperCase() + monthName.substring(1).toLowerCase();
	}
	
	@Override
	public String toString() {
		String startMonth = monthNumToMonthName(Integer.parseInt(openFromMM));
		String endMonth = monthNumToMonthName(Integer.parseInt(openToMM));
		
		return String.format("%-30s%-13s%-13s%-10s", name, startMonth, endMonth, "$" + dailyFee);
	}
}
