package com.techelevator.parksystem.model.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.parksystem.model.Site;
import com.techelevator.parksystem.model.SiteDAO;

public class JDBCSiteDAO implements SiteDAO {
	private JdbcTemplate jdbcTemplate;

	public JDBCSiteDAO(DataSource source) {
		this.jdbcTemplate = new JdbcTemplate(source);
	}

	/*
	 * Gets 'results' from SQL query to find all sites from database created out of 
	 * campground.sql file. adds site objects as 's' to "site" array using 
	 * mapRowToPark method to set all characteristics
	 */
	@Override
	public List<Site> getAllSites() {
		List<Site> sites = new ArrayList<>();

		String sql = "SELECT site_id, campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities "
				+ "FROM site";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

		while (results.next()) {
			sites.add(mapRowToSite(results));
		}
		// 

		return sites;
	}

	@Override
	public Site getSiteById(int id) {
		Site site = null;

		String sql = "SELECT site_id, campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities "
				+ "FROM site " + "WHERE site_id = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);

		if (results.next()) {
			site = mapRowToSite(results);
		}
		// Gets 'results' from SQL query to find a site by the siteId from database
		// created out of campground.sql file.
		// adds possible one line from Sql query to a 'site' object using mapRowToPark
		// method to set all characteristics

		return site;
	}

	@Override
	public List<Site> getSitesForCampgroundId(int campgroundId) {
		List<Site> sites = new ArrayList<>();

		String sql = "SELECT site_id, campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities "
				+ "FROM site " + "WHERE campground_id = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, campgroundId);

		while (results.next()) {
			sites.add(mapRowToSite(results));
		}
		// Gets 'results' from SQL query to find sites by campgroundId from database
		// created out of campground.sql file.
		// adds site objects as 's' to "site" array using mapRowToPark method to set all
		// characteristics

		return sites;
	}

	@Override
	public List<Site> getAvailableSitesForCampgroundId(int campgroundId, LocalDate arrivalDate, LocalDate departureDate) {
		List<Site> availableSites = new ArrayList<>();
		String sql = "SELECT site_id, campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities "
				+ "FROM site " + "WHERE site.campground_id = ? AND site_id NOT IN (" + "SELECT site.site_id FROM site "
				+ "JOIN reservation on reservation.site_id = site.site_id "
				+ "WHERE(NOT(reservation.to_date < ? OR reservation.from_date > ?)))" + "ORDER BY site_id LIMIT 5";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, campgroundId, arrivalDate, departureDate);
		while (results.next()) {
			availableSites.add(mapRowToSite(results));
		}
		// Gets all available campgrounds from a Sql query that checks requested
		// reservation against existing reservations in the database
		// adds site objects as 's' to "site" array using mapRowToPark method to set all
		// characteristics

		return availableSites;
	}

	private Site mapRowToSite(SqlRowSet row) { // method to add characteristics to an object for given row of data
		Site s = new Site();
		s.setSiteId(row.getInt("site_id")); // sets the siteId
		s.setCampgroundId(row.getInt("campground_id")); // sets the campgroundId that the site is in
		s.setSiteNumber(row.getInt("site_number")); // sets the site number. this is the unique identifier across all
													// sites in all parks
		s.setMaxOccupancy(row.getInt("max_occupancy")); // sets max occupancy of the site
		s.setAccessible(row.getBoolean("accessible")); // sets the boolean whether the site is handicap accessible
		s.setMaxRvLength(row.getInt("max_rv_length")); // sets the max RV length allowed in the site
		s.setUtilities(row.getBoolean("utilities")); // sets the boolean whether the site has utilities hookup

		return s;
	}
}
