package com.techelevator.parksystem.model;

import java.util.List;

public interface ReservationDAO {
	public List<Reservation> getAllReservations();

	public Reservation getReservationById(int id);

	public List<Reservation> getReservationsForSiteId(int siteId);

	public Reservation storeReservation(Reservation reservation);
}
