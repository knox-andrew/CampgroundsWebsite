package com.techelevator.parksystem.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class Menu {

	private PrintWriter out;
	private Scanner in;

	public Menu(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options, false);
			choice = getChoiceFromUserInput(options, false);     //sends choice object to method to get user option, with hasQuit boolean set to false. 
		}
		return choice;
	}
	
	public Object getChoiceFromOptionsWithQuit(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options, true);
			choice = getChoiceFromUserInput(options, true);    //sends choice object to method to get user option, with hasQuit boolean set to true . 
		}
		return choice;
	}
	
	private Object getChoiceFromUserInput(Object[] options, boolean hasQuitOption) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];    //choice set to user menu selected. 
			}
		} catch (NumberFormatException e) {   //if menu has a quit option, and user types 'q' or 'Q', returns Quit and exits
			if(hasQuitOption && userInput.toUpperCase().equals("Q")) {
				choice = "quit";
			}
		}
		if (choice == null) {
			out.println("\n*** " + userInput + " is not a valid option ***\n");
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options, boolean hasQuitOption) { //if sent hasQuitOption, adds a Q)uit option to the menu
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i]);
		}
		if (hasQuitOption) {
			out.println("Q) quit");
		}
		out.print("\nPlease choose an option >>> ");
		out.flush();
	}
}
