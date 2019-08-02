package com.techelevator.parksystem.model;

import java.util.List;

public interface CampgroundDAO {
	public List<Campground> getAllCampgrounds();

	public Campground getCampgroundById(int id);

	public List<Campground> getCampgroundsForParkId(int parkId);

}
