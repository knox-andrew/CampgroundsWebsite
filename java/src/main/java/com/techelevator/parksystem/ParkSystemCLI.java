package com.techelevator.parksystem;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.parksystem.exception.InvalidDateRangeException;
import com.techelevator.parksystem.exception.MonthOutOfRangeException;
import com.techelevator.parksystem.model.Campground;
import com.techelevator.parksystem.model.CampgroundDAO;
import com.techelevator.parksystem.model.Park;
import com.techelevator.parksystem.model.ParkDAO;
import com.techelevator.parksystem.model.Reservation;
import com.techelevator.parksystem.model.ReservationDAO;
import com.techelevator.parksystem.model.Site;
import com.techelevator.parksystem.model.SiteDAO;
import com.techelevator.parksystem.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.parksystem.model.jdbc.JDBCParkDAO;
import com.techelevator.parksystem.model.jdbc.JDBCReservationDAO;
import com.techelevator.parksystem.model.jdbc.JDBCSiteDAO;
import com.techelevator.parksystem.view.Menu;

public class ParkSystemCLI {
	
	private static final String INTRO_IMAGE = "                  .e$c\"*eee...                      \n" + 
			"                z$$$$$$.  \"*$$$$$$$$$.                    \n" + 
			"            .z$$$$$$$$$$$e. \"$$$$$$$$$$c.                 \n" + 
			"         .e$$P\"\"  $$  \"\"*$$$bc.\"$$$$$$$$$$$e.             \n" + 
			"     .e$*\"\"       $$         \"**be$$$***$   3             \n" + 
			"     $            $F              $    4$r  'F            \n" + 
			"     $           4$F              $    4$F   $            \n" + 
			"    4P   \\       4$F              $     $$   3r           \n" + 
			"    $\"    r      4$F              3     $$r   $           \n" + 
			"    $     '.     $$F              4F    4$$   'b          \n" + 
			"   dF      3     $$    ^           b     $$L   \"L         \n" + 
			"   $        .    $$   %            $     ^$$r   \"c        \n" + 
			"  JF             $$  %             4r     '$$.   3L       \n" + 
			" .$              $$ \"               $      ^$$r\"\"         \n" + 
			" $%              $$P                3r  .e*\"              \n" + 
			"'*=*********************************$$P\"";

	private static final String PARK_INFO_MENU_OPTION_VIEW_CAMPGROUNDS = "View Campgrounds";
	private static final String PARK_INFO_MENU_OPTION_RESERVATION_BY_CAMPGROUND = "Search for Reservation By Campground";
	private static final String PARK_INFO_MENU_OPTION_RESERVATION_PARK_WIDE = "Search for Park-wide Reservation";
	private static final String PARK_INFO_MENU_OPTION_RETURN = "Return to Previous Screen";
	private static final String[] PARK_INFO_MENU_OPTIONS = { PARK_INFO_MENU_OPTION_VIEW_CAMPGROUNDS,
			PARK_INFO_MENU_OPTION_RESERVATION_BY_CAMPGROUND, PARK_INFO_MENU_OPTION_RESERVATION_PARK_WIDE,
			PARK_INFO_MENU_OPTION_RETURN };

	private static final String PARK_CAMP_MENU_OPTION_SEARCH_AVAILABLE = "Search for Available Reservation";
	private static final String PARK_CAMP_MENU_OPTION_RETURN = "Return to Previous Screen";
	private static final String[] PARK_CAMP_MENU_OPTIONS = { PARK_CAMP_MENU_OPTION_SEARCH_AVAILABLE,
			PARK_CAMP_MENU_OPTION_RETURN };

	private static final String ENTER_ARRIVAL_DATE_PROMPT = "What is the arrival date (ex. 5/21/2020)?";
	private static final String ENTER_DEPARTURE_DATE_PROMPT = "What is the departure date (ex. 5/21/2020)?";
	
	private static Scanner scan = new Scanner(System.in);
	private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("M/d/yyyy");

	private Menu menu;
	
	private boolean returnToStart = false;

	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;
	private SiteDAO siteDAO;
	private ReservationDAO reservationDAO;

	public ParkSystemCLI(DataSource dataSource, Menu menu) {
		this.menu = menu;

		this.parkDAO = new JDBCParkDAO(dataSource);
		this.campgroundDAO = new JDBCCampgroundDAO(dataSource);
		this.siteDAO = new JDBCSiteDAO(dataSource);
		this.reservationDAO = new JDBCReservationDAO(dataSource);
	}

	public void run() {
		boolean repeat = true;

		while (repeat) {
			returnToStart = false;
			System.out.println(INTRO_IMAGE);
			System.out.println("\nView Parks Interface");
			System.out.println(  "--------------------");
			
			System.out.println("Select a Park for Further Details");
			Object choice = menu.getChoiceFromOptionsWithQuit(parkDAO.getAllParks().toArray());

			
			if (choice.equals("quit")) {
				System.out.println("\nGoodbye");
				repeat = false;
			} else {
				Park park = (Park) choice;
				goToParkInfoMenu(park);
			}
		}
	}

	private void goToParkInfoMenu(Park park) {
		boolean repeat = true;

		while (repeat && returnToStart == false) {
			System.out.println("\nPark Information Screen");
			System.out.println(  "-----------------------");
			System.out.println(park.getAllInfo());

			String choice = (String) menu.getChoiceFromOptions(PARK_INFO_MENU_OPTIONS);

			switch (choice) {
			case (PARK_INFO_MENU_OPTION_VIEW_CAMPGROUNDS):
				goToParkCampMenu(park);
				break;
			case (PARK_INFO_MENU_OPTION_RESERVATION_BY_CAMPGROUND):
				goToCampgroundSearchMenu(campgroundDAO.getCampgroundsForParkId(park.getParkId()));
				break;
			case (PARK_INFO_MENU_OPTION_RESERVATION_PARK_WIDE):
				goToParkWideSearchMenu(park);
				break;
			case (PARK_INFO_MENU_OPTION_RETURN):
				repeat = false;
				break;
			}
		}
	}

	private void goToParkCampMenu(Park park) {
		boolean repeat = true;

		while (repeat && returnToStart == false) {
			System.out.println("\nPark Campgrounds");
			System.out.println(  "----------------");
			System.out.println(park.getName() + " National Park Campgrounds");

			List<Campground> campgrounds = campgroundDAO.getCampgroundsForParkId(park.getParkId());
			displayCampgroundsInfo(campgrounds);

			String choice = (String) menu.getChoiceFromOptions(PARK_CAMP_MENU_OPTIONS);

			switch (choice) {
			case PARK_CAMP_MENU_OPTION_SEARCH_AVAILABLE:
				goToCampgroundSearchMenu(campgrounds);
				break;
			case PARK_CAMP_MENU_OPTION_RETURN:
				repeat = false;
				break;
			}
		}
	}

	private void goToParkWideSearchMenu(Park park) {
		boolean repeat = true;

		while (repeat && returnToStart == false) {
			String input = "";
			try {
				System.out.println("\nSearch for Park-wide Reservation");
				System.out.println(  "--------------------------------");

				LocalDate arrivalDate = this.readInDate(ENTER_ARRIVAL_DATE_PROMPT);
				if (arrivalDate.isBefore(LocalDate.now())) {
					throw new InvalidDateRangeException(InvalidDateRangeException.ARRIVAL_DATE_IS_PASSED_ERROR_MESSAGE);
				}

				LocalDate departureDate = this.readInDate(ENTER_DEPARTURE_DATE_PROMPT);
				if (departureDate.isBefore(arrivalDate)) {
					throw new InvalidDateRangeException(
							InvalidDateRangeException.DEPARTURE_IS_BEFORE_ARRIVAL_ERROR_MESSAGE);
				}

				List<Campground> campsInPark = campgroundDAO.getCampgroundsForParkId(park.getParkId());

				List<Site> sites = new ArrayList<>();
				for (Campground camp : campsInPark) {
					sites.addAll(siteDAO.getAvailableSitesForCampgroundId(camp.getCampgroundId(), arrivalDate,
							departureDate));
				}

				if (sites.size() == 0) {
					System.out.println("\nNo available camp sites for those dates\n");
					System.out.print("Press enter to continue...");
					scan.nextLine();
					repeat = false;
				} else {
					System.out.println("\nResults Matching Your Search Criteria");
					displaySitesInfoWithCost(sites, ChronoUnit.DAYS.between(arrivalDate, departureDate), true);

					System.out.print("\nWhich site should be reserved (enter 0 to cancel)? ");
					input = scan.nextLine();
					try {
						int selection = Integer.parseInt(input);
						if (selection != 0) {
							finalizeReservation(sites.get(selection - 1), arrivalDate, departureDate);
						}
						
						repeat = false;
					} catch (NumberFormatException | IndexOutOfBoundsException e) {
						System.out.println("\n*** " + input + " is not a valid option ***\n");
						System.out.print("Press enter to continue...");
						scan.nextLine();
					}
				}
			} catch (DateTimeParseException e) {
				System.out.println("\n*** " + input + " is not a valid date ***\n");
				System.out.print("Press enter to continue...");
				scan.nextLine();
			} catch (InvalidDateRangeException e) {
				System.out.println("\n*** " + e.getMessage() + " ***\n");
				System.out.print("Press enter to continue...");
				scan.nextLine();
			}
		}
	}

	private void goToCampgroundSearchMenu(List<Campground> campsOfPark) {
		boolean repeat = true;

		while (repeat && returnToStart == false) {
			System.out.println("\nSearch for Campground Reservation");
			System.out.println(  "---------------------------------");
			displayCampgroundsInfo(campsOfPark);

			System.out.print("\nWhich campground (enter 0 to cancel)? ");
			String input = scan.nextLine();

			try {
				int selection = Integer.parseInt(input);
				if (selection == 0) {
					repeat = false;
				} else {
					Campground selectedCamp = campsOfPark.get(selection - 1);

					LocalDate arrivalDate = this.readInDate(ENTER_ARRIVAL_DATE_PROMPT);
					if (arrivalDate.isBefore(LocalDate.now())) {
						throw new InvalidDateRangeException(
								InvalidDateRangeException.ARRIVAL_DATE_IS_PASSED_ERROR_MESSAGE);

					} else if (!selectedCamp.isOpenOnDate(arrivalDate)) {
						throw new MonthOutOfRangeException(selectedCamp.getName() + " is not open in "
								+ Campground.monthNumToMonthName(arrivalDate.getMonthValue()));
					}

					LocalDate departureDate = readInDate(ENTER_DEPARTURE_DATE_PROMPT);
					if (departureDate.isBefore(arrivalDate)) {
						throw new InvalidDateRangeException(
								InvalidDateRangeException.DEPARTURE_IS_BEFORE_ARRIVAL_ERROR_MESSAGE);

					} else if (!selectedCamp.isOpenOnDate(departureDate)) {
						throw new MonthOutOfRangeException(selectedCamp.getName() + " is not open in "
								+ Campground.monthNumToMonthName(departureDate.getMonthValue()));
					}

					List<Site> sites = siteDAO.getAvailableSitesForCampgroundId(selectedCamp.getCampgroundId(),
							arrivalDate, departureDate);

					if (sites.size() == 0) {
						System.out.println("\nNo available camp sites for those dates\n");
						System.out.print("Press enter to continue...");
						scan.nextLine();
						repeat = false;
					} else {
						goToSearchResultsMenu(sites, arrivalDate, departureDate);
					}
				}

			} catch (NumberFormatException | IndexOutOfBoundsException e) {
				System.out.println("\n*** " + input + " is not a valid option ***\n");
				System.out.print("Press enter to continue...");
				scan.nextLine();
			} catch (DateTimeParseException e) {
				System.out.println("\n*** " + input + " is not a valid date ***\n");
				System.out.print("Press enter to continue...");
				scan.nextLine();
			} catch (MonthOutOfRangeException e) {
				System.out.println("\n*** " + e.getMessage() + " ***\n");
				System.out.print("Press enter to continue...");
				scan.nextLine();
			} catch (InvalidDateRangeException e) {
				System.out.println("\n*** " + e.getMessage() + " ***\n");
				System.out.print("Press enter to continue...");
				scan.nextLine();
			}
		}
	}

	private void goToSearchResultsMenu(List<Site> sites, LocalDate arrivalDate, LocalDate departureDate) {
		long numDays = ChronoUnit.DAYS.between(arrivalDate, departureDate);
		boolean repeat = true;
		while (repeat && returnToStart == false) {
			System.out.println("\nResults Matching Your Search Criteria");
			System.out.println(  "-------------------------------------");

			displaySitesInfoWithCost(sites, numDays, false);

			System.out.print("\nWhich site should be reserved (enter 0 to cancel)? ");
			String input = scan.nextLine();
			try {
				int selection = Integer.parseInt(input);

				if (selection != 0) {
					finalizeReservation(sites.get(selection - 1), arrivalDate, departureDate);
				}
				repeat = false;
			} catch (NumberFormatException | IndexOutOfBoundsException e) {
				System.out.println("\n*** " + input + " is not a valid option ***\n");
				System.out.print("Press enter to continue...");
				scan.nextLine();
			}
		}
	}

	private void finalizeReservation(Site site, LocalDate arrivalDate, LocalDate departureDate) {
		System.out.print("What name should the reservation be made under? ");
		String name = scan.nextLine();

		Reservation newRes = new Reservation();
		newRes.setSiteId(site.getSiteId());
		newRes.setName(name);
		newRes.setArrivalDate(arrivalDate);
		newRes.setDepartureDate(departureDate);
		newRes.setCreationDate(LocalDate.now());

		reservationDAO.storeReservation(newRes);

		System.out.println("\nThe reservation has been made and the confirmation id is " + newRes.getReservationId());
		System.out.print("\nPress enter to continue...");
		scan.nextLine();
		returnToStart = true;
	}

	
	/*
	 * STORE INPUTS IN ARRAY 
	 */
	private void displaySitesInfoWithCost(List<Site> sites, long numDays, boolean showCampground) {
		String format = "%-5s";
		format += showCampground ? "%-30s" : "";
		format += "%-20s%-20s%-20s%-20s%-20s%-20s\n";
		

		if (showCampground) {
			System.out.format(format, "", "Campground", "Site No.", "Max Occup.", "Accessible?", "Max RV Length",
					"Utility", "Cost");
		} else {
			System.out.format(format, "", "Site No.", "Max Occup.", "Accessible?", "Max RV Length", "Utility", "Cost");
		}

		for (int i = 0; i < sites.size(); i++) {
			Campground c = campgroundDAO.getCampgroundById(sites.get(i).getCampgroundId());

			String isAccessible = sites.get(i).isAccessible() ? "Yes" : "No";
			String hasUtilities = sites.get(i).isUtilities() ? "Yes" : "No";
			String rvLength = sites.get(i).getMaxRvLength() == 0 ? "N/A"
					: Integer.toString(sites.get(i).getMaxRvLength());
			BigDecimal cost = c.getDailyFee().multiply(BigDecimal.valueOf(numDays));

			if (showCampground) {
				System.out.format(format, (i + 1) + ") ", c.getName(), sites.get(i).getSiteNumber(),
						sites.get(i).getMaxOccupancy(), isAccessible, rvLength, hasUtilities, "$" + cost);
			} else {
				System.out.format(format, (i + 1) + ") ", sites.get(i).getSiteNumber(), sites.get(i).getMaxOccupancy(),
						isAccessible, rvLength, hasUtilities, "$" + cost);
			}
		}
	}

	
	private void displayCampgroundsInfo(List<Campground> campgrounds) {
		System.out.println(Campground.DISPLAY_HEADER);

		Campground c;
		for (int i = 0; i < campgrounds.size(); i++) {
			c = campgrounds.get(i);
			
			System.out.format("%-5s" + c + "\n", "#" + (i + 1));
		}
	}

	private LocalDate readInDate(String message) throws DateTimeParseException {
		System.out.print(message + " ");
		String input = scan.nextLine();
		return LocalDate.parse(input, dateFormat);
	}

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		Menu menu = new Menu(System.in, System.out);
		ParkSystemCLI application = new ParkSystemCLI(dataSource, menu);
		application.run();
	}
}
