package com.techelevator.parksystem.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.parksystem.model.Reservation;
import com.techelevator.parksystem.model.ReservationDAO;

public class JDBCReservationDAO implements ReservationDAO {
	private JdbcTemplate jdbcTemplate;

	public JDBCReservationDAO(DataSource source) {
		this.jdbcTemplate = new JdbcTemplate(source);
	}

	@Override
	public List<Reservation> getAllReservations() {
		List<Reservation> reservations = new ArrayList<>();

		String sql = "SELECT reservation_id, site_id, name, from_date, to_date, create_date " + "FROM reservation";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

		while (results.next()) {
			reservations.add(mapRowToReservation(results));
		}
		// Gets 'results' from SQL query to find all reservations from database created
		// out of campground.sql file.
		// adds reservation objects as 'r' to "reservations" array using
		// mapRowToReservation method to set all characteristics

		return reservations;
	}

	@Override
	public Reservation getReservationById(int id) {
		Reservation reservation = null;

		String sql = "SELECT reservation_id, site_id, name, from_date, to_date, create_date " + "FROM reservation "
				+ "WHERE reservation_id = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);

		if (results.next()) {
			reservation = mapRowToReservation(results);
		}
		// Gets 'results' from SQL query to find reservation by reservationId from
		// database created out of campground.sql file.
		// adds possible one line from Sql query to a 'reservation' object using
		// mapRowToReservation method to set all characteristics

		return reservation;
	}

	@Override
	public List<Reservation> getReservationsForSiteId(int siteId) {
		List<Reservation> reservations = new ArrayList<>();

		String sql = "SELECT reservation_id, site_id, name, from_date, to_date, create_date " + "FROM reservation "
				+ "WHERE site_id = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, siteId);

		while (results.next()) {
			reservations.add(mapRowToReservation(results));
		}
		// Gets 'results' from SQL query to find all reservations by siteId from
		// database created out of campground.sql file.
		// adds reservation objects as 'r' to "reservations" array using
		// mapRowToReservation method to set all characteristics

		return reservations;
	}

	@Override
	public Reservation storeReservation(Reservation r) { // stores the reservation in the database creating
															// automatically and returning reservation Id.

		String sql = "INSERT INTO reservation(site_id, name, from_date, to_date, create_date) "
				+ "VALUES (?, ?, ?, ?, ?) RETURNING reservation_id";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, r.getSiteId(), r.getName(), r.getArrivalDate(),
				r.getDepartureDate(), r.getCreationDate());
		// gets the requested reservation data
		if (results.next()) {
			r.setReservationId(results.getInt("reservation_id")); // sets the new reservation Id
		}

		return r;
	}

	private Reservation mapRowToReservation(SqlRowSet row) { // method to add characteristics to an object for given row
																// of data
		Reservation r = new Reservation();
		r.setReservationId(row.getInt("reservation_id")); // sets the id of the reservation
		r.setSiteId(row.getInt("site_id")); // sets the site Id
		r.setName(row.getString("name")); // sets the name reservation listed under
		r.setArrivalDate(row.getDate("from_date").toLocalDate()); // sets the arrival date of the reservation
		r.setDepartureDate(row.getDate("to_date").toLocalDate()); // sets the departure date of the reservation
		r.setCreationDate(row.getDate("create_date").toLocalDate()); // sets the date at which the rervation was created

		return r;
	}
}
