package com.techelevator.parksystem.model;

import java.time.LocalDate;
import java.util.List;

public interface SiteDAO {
	public List<Site> getAllSites();

	public Site getSiteById(int id);

	public List<Site> getSitesForCampgroundId(int campgroundId);

	/**
	 * @param parkId   --selected by customer
	 * @param dateFrom --customer chosen arrival date
	 * @param dateTo   --customer chosen departure date
	 * @return an array of available sites by reservation date at selected
	 *         campground
	 */
	public List<Site> getAvailableSitesForCampgroundId(int campgroundId, LocalDate arrivalDate, LocalDate departureDate);

}
