package com.techelevator.parksystem.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.parksystem.model.Park;
import com.techelevator.parksystem.model.ParkDAO;

public class JDBCParkDAO implements ParkDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCParkDAO(DataSource source) {
		jdbcTemplate = new JdbcTemplate(source);
	}

	@Override
	public List<Park> getAllParks() {
		List<Park> parks = new ArrayList<>();

		String sql = "SELECT park_id, name, location, establish_date, area, visitors, description " + "FROM park "
				+ "ORDER BY name";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

		while (results.next()) {
			parks.add(mapRowToPark(results));
			// Gets 'results' from SQL query to find all parks in alph order from database
			// created out of campground.sql file.
			// adds Park objects as 'p' to "park" array using mapRowToPark method to set all
			// characteristics
		}

		return parks;
	}

	@Override
	public Park getParkById(int id) {
		Park park = null;

		String sql = "SELECT park_id, name, location, establish_date, area, visitors, description " + "FROM park "
				+ "WHERE park_id = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);

		if (results.next()) {
			park = mapRowToPark(results);
		}
		// Gets 'results' from SQL query to find a park by the parkId from database
		// created out of campground.sql file.
		// adds possible one line from Sql query to a 'park' object using mapRowToPark
		// method to set all characteristics

		return park;
	}

	@Override
	public Park getParkByName(String name) {
		Park park = null;

		String sql = "SELECT park_id, name, location, establish_date, area, visitors, description " + "FROM park "
				+ "WHERE name LIKE ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, name);

		if (results.next()) {
			park = mapRowToPark(results);
		}
		// Gets 'results' from SQL query to find park by name from database created out
		// of campground.sql file.
		// adds possible one line from Sql query to a 'park' object using mapRowToPark
		// method to set all characteristics

		return park;
	}

	/**
	 * @param row row of sql search data
	 * @return
	 */
	private Park mapRowToPark(SqlRowSet row) { // method to add characteristics to an object for given row of data
		Park p = new Park();
		p.setParkId(row.getInt("park_id")); // sets the id of the park
		p.setName(row.getString("name")); // sets name of park
		p.setLocation(row.getString("location")); // sets state location of park
		p.setEstablishDate(row.getDate("establish_date").toLocalDate()); // sets date park established
		p.setArea(row.getInt("area")); // sets area of area of entire park
		p.setVisitors(row.getInt("visitors")); // sets annual number of visitors annually
		p.setDescription(row.getString("description")); // sets long decription of Park

		return p;
	}
}
