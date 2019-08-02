package com.techelevator.parksystem.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.parksystem.model.Campground;
import com.techelevator.parksystem.model.CampgroundDAO;

public class JDBCCampgroundDAO implements CampgroundDAO {
	private JdbcTemplate jdbcTemplate;

	public JDBCCampgroundDAO(DataSource source) {
		this.jdbcTemplate = new JdbcTemplate(source);
	}

	@Override
	public List<Campground> getAllCampgrounds() {
		List<Campground> campgrounds = new ArrayList<>();

		String sql = "SELECT campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee " + "FROM campground "
				+ "ORDER BY name";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

		while (results.next()) {
			campgrounds.add(mapRowToCampground(results));
		}
		// Gets 'results' from SQL query to find all campgrounds from database created
		// out of campground.sql file.
		// adds campground objects as 'c' to "campground" array using mapRowToPark
		// method to set all characteristics

		return campgrounds;
	}

	@Override
	public Campground getCampgroundById(int id) {
		Campground campground = null;

		String sql = "SELECT campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee " + "FROM campground "
				+ "WHERE campground_id = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);

		if (results.next()) {
			campground = mapRowToCampground(results);
		}
		// Gets 'results' from SQL query to find a campground by the campgroundId from
		// database created out of campground.sql file.
		// adds possible one line from Sql query to a 'campground' object using
		// mapRowToPark method to set all characteristics

		return campground;
	}

	@Override
	public List<Campground> getCampgroundsForParkId(int parkId) {
		List<Campground> campgrounds = new ArrayList<>();

		String sql = "SELECT campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee " + "FROM campground "
				+ "WHERE park_id = ? " + "ORDER BY name";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, parkId);

		while (results.next()) {
			campgrounds.add(mapRowToCampground(results));
		}
		// Gets 'results' from SQL query to find campground by parkId from database
		// created out of campground.sql file.
		// adds campground objects as 'c' to "campground" array using mapRowToPark
		// method to set all characteristics

		return campgrounds;
	}

	private Campground mapRowToCampground(SqlRowSet row) { // method to add characteristics to an object for given row
															// of data
		Campground c = new Campground();
		c.setCampgroundId(row.getInt("campground_id")); // sets campground Id
		c.setParkId(row.getInt("park_id")); // sets park Id that the campground is in
		c.setName(row.getString("name")); // sets campground Name
		c.setOpenFromMM(row.getString("open_from_mm")); // sets month the park opens
		c.setOpenToMM(row.getString("open_to_mm")); // sets month the park closes
		c.setDailyFee(row.getBigDecimal("daily_fee")); // sets the daily fee for the campground based on park daily fee
		return c;
	}
}
