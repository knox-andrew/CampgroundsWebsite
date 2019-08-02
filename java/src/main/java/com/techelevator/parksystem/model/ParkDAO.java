package com.techelevator.parksystem.model;

import java.util.List;

public interface ParkDAO {
	public List<Park> getAllParks();

	public Park getParkById(int id);

	public Park getParkByName(String name);
}
