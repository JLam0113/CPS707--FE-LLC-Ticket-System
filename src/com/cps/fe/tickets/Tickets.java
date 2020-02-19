package com.cps.fe.tickets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Scanner;

import com.cps.fe.user.User;

public class Tickets {
	private User user;
	private boolean soldItemThisSession;

	public Tickets(User user)
	{
		this.user = user;

		soldItemThisSession = false;
	}

	public void buy(String eventTitle, int numOfTickets, String sellersUsername)
	{
		boolean foundEvent = false;
		boolean foundSeller = false;

		String curLine = "";
		int lineIndex = 0;

		// check for validity of purchase amount
		// TODO: wait to get userType from user
		/*
		if (user)
		{
			System.out.println("Invalid command (account not privileged), please enter a command.");
			return;
		}
		*/

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
			String price = curLine.substring(38,44);

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
				String confirmation = sc.nextLine().trim();

				if (confirmation.equalsIgnoreCase("no"))
				{
					System.out.println("Transaction cancelled, please enter a command.");
					sc.close();
					return;
				}
				else if (confirmation.equalsIgnoreCase("yes")) break;
			}

			// TODO: make file changes
			/*
			int resTickets = qtyAvaliable - numOfTickets;
			FileWriter fr;
			try {
				fr = new FileWriter(file,true);
				//fr.write(msg);
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			*/

			System.out.println("Transaction confirmed, please enter a command.");
			sc.close();

		}
		catch (FileNotFoundException e) {
			System.out.println(e);
		}

	}

	public void sell(String eventTitle, float sellPrice, int numOfTickets, String username)
	{
		// check for validity of purchase amount
		// TODO: wait to get userType from user
		/*
		if (user)
		{
			System.out.println("Invalid command (account not privileged), please enter a command.");
			return;
		}
		*/

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

			//TODO: make file changes

			soldItemThisSession = true;
		}
		else
		{
			System.out.println("Invalid command (please start a new session to sell tickets), please enter a command.");
			return;
		}

	}

	//TODO: use method
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		catch (FileNotFoundException e)
		{
			System.out.println(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
