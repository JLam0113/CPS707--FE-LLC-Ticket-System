package com.cps.fe.tickets;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.Scanner;

import com.cps.fe.user.User;

public class Tickets {
	private User user;
	private boolean soldItemThisSession;
	private Scanner consoleScanner;

	public Tickets(User user, Scanner consoleScanner)
	{
		this.user = user;
		this.consoleScanner = consoleScanner;

		soldItemThisSession = false;
	}

	public void buy(String eventTitle, int numOfTickets, String sellersUsername)
	{
		boolean foundEvent = false;
		boolean foundSeller = false;

		String curLine = "";
		int lineIndex = 0;

		// check for validity of purchase amount
		if (user.getUserType().equalsIgnoreCase("SS"))
		{
			System.out.println("Invalid command (account not privileged), please enter a command.");
			return;
		}


		// check for validity of purchase amount
		if (numOfTickets > 4)
		{
			System.out.println("Invalid number of tickets, please enter a command.");
			return;
		}

		//Get path for tickets.txt
		try {
			//Get the local path for accounts.txt
			java.net.URL url = User.class.getClassLoader().getResource("resources/tickets.txt");

			File file = new File(url.getPath());
			Scanner sc = new Scanner(file);

			String seller = "";

			boolean curFoundEvent = false;
			boolean curFoundUser = false;

			while (sc.hasNextLine()) {
				curLine = sc.nextLine();
				String evtName = curLine.substring(0,20).trim();
				seller = curLine.substring(20,34).trim();
				lineIndex++;

				if (evtName.equalsIgnoreCase(eventTitle))
				{
					foundEvent = true;
					curFoundEvent = true;
				}

				if (seller.equalsIgnoreCase(sellersUsername))
				{
					foundSeller = true;
					curFoundUser = true;
				}

				if (curFoundEvent && curFoundUser)
				{
					break;
				}

				curFoundEvent = false;
				curFoundUser = false;
			}

			if (!foundEvent)
			{
				System.out.println("Event not found, please enter a command.");
				sc.close();
				return;
			}

			if (!foundSeller)
			{
				System.out.println("Invalid username, please enter a command.");
				sc.close();
				return;
			}

			int qtyAvaliable = Integer.parseInt(curLine.substring(34,38).trim());
			float price = Float.parseFloat(curLine.substring(38,44));

			if (numOfTickets > qtyAvaliable)
			{
				System.out.println("Invalid Quantity");
				sc.close();
				return;
			}

			// get confirmation
			while (true)
			{
				System.out.println("You are buying "+numOfTickets+" tickets for "+ eventTitle +" from "+ seller +" for $"+price+", can you please confirm with yes/no?");
				String confirmation = consoleScanner.nextLine();
				confirmation = confirmation.trim();

				if (confirmation.equalsIgnoreCase("no"))
				{
					System.out.println("Transaction cancelled, please enter a command.");
					sc.close();
					return;
				}
				else if (confirmation.equalsIgnoreCase("yes")) break;
			}

			// update tickets.txt and balance
			int resTickets = qtyAvaliable - numOfTickets;
			updateTicketsValues(eventTitle, seller, String.valueOf(resTickets));
			int resPrice = user.getCredit() - (int) (price*numOfTickets);
			user.updateCredit(resPrice);

			System.out.println("Transaction confirmed, please enter a command.");
			sc.close();

			writeToDTF("04 " + eventTitle + " " + user.getUser() + " " + numOfTickets + " " + price + " \n");

		}
		catch (FileNotFoundException e) {
			System.out.println(e);
		}
		catch (IOException e) {
			System.out.println(e);
		}

	}

	public void sell(String eventTitle, float sellPrice, int numOfTickets)
	{
		// check for validity of purchase amount
		if (user.getUserType().equalsIgnoreCase("BS"))
		{
			System.out.println("Invalid command (account not privileged), please enter a command.");
			return;
		}

		if (eventTitle.length() > 25)
		{
			System.out.println("Invalid event title (too long), please enter a command.");
			return;
		}

		if (sellPrice > 999.99)
		{
			System.out.println("Invalid ticket price (max price is 999.99), please enter a command.");
			return;
		}

		if (numOfTickets > 100)
		{
			System.out.println("Invalid number of tickets (max 100), please enter a command.");
			return;
		}

		if (!soldItemThisSession)
		{
			System.out.println(numOfTickets + " tickets have been sold at $"+sellPrice+" to "+eventTitle+", please enter a command.");

			try {
				addNewTicket(eventTitle, user.getUser(), String.valueOf(numOfTickets), String.valueOf(sellPrice));
				writeToDTF("03 " + eventTitle + " " + user.getUser() + " " + numOfTickets + " " + sellPrice + " \n");
				soldItemThisSession = true;
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		else
		{
			System.out.println("Invalid command (please start a new session to sell tickets), please enter a command.");
			return;
		}

	}

	public static void updateTicketsValues(String findEvtName, String findSeller, String updateQty) throws IOException {
		java.net.URL url = User.class.getClassLoader().getResource("resources/tickets.txt");

		BufferedReader file = new BufferedReader(new FileReader(new File(url.getPath())));
		StringBuffer inputBuffer = new StringBuffer();
		String curLine = "";
		String evtName= "";
		String seller= "";

		// go to line to update
		while ((curLine = file.readLine()) != null) {

			evtName = curLine.substring(0,20).trim();
			seller = curLine.substring(20,34).trim();

			if (evtName.equalsIgnoreCase(findEvtName) && seller.equalsIgnoreCase(findSeller))
				break;

			inputBuffer.append(curLine);
			inputBuffer.append('\n');
		}

		// update
		String newQtyAvaliable = String.format("%1$-3s", updateQty);
		inputBuffer.append(curLine.substring(0,34) + newQtyAvaliable + " " + curLine.substring(38));
		inputBuffer.append('\n');

		// write the rest of file
		while ((curLine = file.readLine()) != null) {

			inputBuffer.append(curLine);
			inputBuffer.append('\n');
		}

		// overwrite entire file
		FileOutputStream fos = new FileOutputStream(new File(url.getPath()));
		fos.write(inputBuffer.toString().getBytes());
		fos.close();
	}

	public static void addNewTicket(String eventName, String sellerName, String quantity, String price) throws IOException {
		java.net.URL url = User.class.getClassLoader().getResource("resources/tickets.txt");

		BufferedReader file = new BufferedReader(new FileReader(new File(url.getPath())));
		StringBuffer inputBuffer = new StringBuffer();
		String curLine = "";

		// go to end
		while ((curLine = file.readLine()) != null) {

			if (curLine.equals("END                                         "))
				break;

			inputBuffer.append(curLine);
			inputBuffer.append('\n');
		}

		// update
		String eventRes = String.format("%1$-19s", eventName);
		String sellerRes = String.format("%1$-13s", sellerName);
		String qtyRes = String.format("%1$-3s", quantity);
		String priceRes = String.format("%1$-6s", price);

		inputBuffer.append(eventRes + " " + sellerRes + " " + qtyRes + " " + priceRes);
		inputBuffer.append('\n');

		inputBuffer.append(curLine);
		inputBuffer.append('\n');

		// overwrite entire file
		FileOutputStream fos = new FileOutputStream(new File(url.getPath()));
		fos.write(inputBuffer.toString().getBytes());
		fos.close();
	}

	public void writeToDTF(String msg) {
		try
		{
			LocalDate localDate = LocalDate.now();
			String date = new String("resources/" + localDate + ".dtf");
			java.net.URL url = User.class.getClassLoader().getResource(date);
			URL url2 = User.class.getClassLoader().getResource("resources/");

			if(url == null) {
				File temp = new File(url2.getPath(), localDate + ".dtf");
				temp.createNewFile();
				url = User.class.getClassLoader().getResource(date);
			}
			File file = new File(url.getPath());

			FileWriter fr;
			try {
				fr = new FileWriter(file,true);
				fr.write(msg);
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		catch (FileNotFoundException e)
		{
			System.out.println(e);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
