package com.techelevator.parksystem.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Park {
	private int parkId;
	private String name;
	private String location;
	private LocalDate establishDate;
	private int area;
	private int visitors;
	private String description;

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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public LocalDate getEstablishDate() {
		return establishDate;
	}

	public void setEstablishDate(LocalDate establishDate) {
		this.establishDate = establishDate;
	}

	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
	}

	public int getVisitors() {
		return visitors;
	}

	public void setVisitors(int visitors) {
		this.visitors = visitors;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return all park info from customer selected park name(String),
	 *         location(String), date established(Date), area-sq.km.(decimal),
	 *         annual visitor count(decimal), park description(String)
	 */
	public String getAllInfo() {
		String str = name + " National Park\n";
		str += String.format("%-20s%s\n", "Location:", location);
		str += String.format("%-20s%s\n", "Established:",
				establishDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
		str += String.format("%-20s%,d sq km\n", "Area:", area);
		str += String.format("%-20s%,d\n", "Annual Visitors:", visitors);
		str += String.format("\n%s", description);

		return str;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
