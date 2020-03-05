/*
 * The following is our prototype ticket system application for CPS 707 Assignment 2. It is able to do all the required functionalities and handles some error checking.
 * It still doesn't meet all requirements and pass all tests as that is leftover for assignment 3, but it does the bare minimum of performing all functionalities to some degree.
 * @author FE LLC
 * @author Jessye Lam 500702091
 * @author Christopher Seow 500782570
 * @author Michael Tsao 500694108
 * CPS707 - Assignment 2
 */
package com.cps.fe.tickets;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.Scanner;

import com.cps.fe.user.User;

/*
 * This class is mainly used to handle all interactions with the available tickets file.
 */
public class Tickets {
	private User user;
	private int ticCount = 0;
	private boolean soldItemThisSession;
	private Scanner consoleScanner;
	private static String url = "tickets.txt";
	private static String url2 = "accounts.txt";

	/*
	 * Initial constructor
	 */
	public Tickets(User user, Scanner consoleScanner, String ticketsPath, String accountsPath)
	{
		this.user = user;
		this.consoleScanner = consoleScanner;

		soldItemThisSession = false;
        url = ticketsPath;
        url2 = accountsPath;
	}

	/*
	 * This method is called when the buy command is entered.
	 * @param eventTitle Event to be bought from.
	 * @param numOfTickets Number of tickets being bought.
	 * @param sellersUsername Username of the seller.
	 */
	public void buy(String eventTitle, int numOfTickets, String sellersUsername)
	{
		if(this.user.getUserType() != "AA" && ticCount >= 4)
		{
			System.out.println("Only 4 tickets can be purhcased per session. Please exit and return to purchase more, please enter a command.");
		}
		else
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
	
				File file = new File(url);
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
				//int resPrice = user.getCredit() - (int) (price*numOfTickets);
				int resPrice = (int) (price*numOfTickets);
				User sellerUser = new User(sellersUsername, url2);
				sellerUser.updateCredit(resPrice);
				user.updateCredit(resPrice * -1);
	
				System.out.println("Transaction confirmed, please enter a command.");
				ticCount += numOfTickets;
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
	}

	/*
	 * This method is used when the sell command is entered.
	 * @param eventTitle Event to be sold to.
	 * @param sellPrice Amount for each ticket.
	 * @param numOfTickets Number of tickets being sold.
	 */
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
	
	/*
	 * This method is used when refund command is entered.
	 * @param buyer This is the user who bought.
	 * @param seller This is the seller who sold.
	 * @param credit This is the amount of credits transaction.
	 */
	public void refund(String buyer, String seller, int credit) throws IOException {
		User userBuyer = new User(buyer, url2);
		User userSeller = new User(seller, url2);
		if(userBuyer.exists() && userSeller.exists()) {
		userBuyer.updateCredit(credit);
		userSeller.updateCredit(credit * -1);
		writeToDTF("05 " + buyer + " " + seller + " " + credit + "\n");
		}
	}

	/*
	 * This method is used to write to the tickets.txt file.
	 * @param findEvtName This is the name of the event.
	 * @param findSeller This is the seller.
	 * @param updateQty This is the number of tickets.
	 */
	public static void updateTicketsValues(String findEvtName, String findSeller, String updateQty) throws IOException {

		BufferedReader file = new BufferedReader(new FileReader(new File(url)));
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
		FileOutputStream fos = new FileOutputStream(new File(url));
		fos.write(inputBuffer.toString().getBytes());
		fos.close();
	}

	/*
	 * This method is used to write to the tickets.txt file.
	 * @param eventName This is the event title.
	 * @param sellerName This is the seller.
	 * @param quantity This is the amount.
	 * @param price This is the price.
	 */
	public static void addNewTicket(String eventName, String sellerName, String quantity, String price) throws IOException {

		BufferedReader file = new BufferedReader(new FileReader(new File(url)));
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
		FileOutputStream fos = new FileOutputStream(new File(url));
		fos.write(inputBuffer.toString().getBytes());
		fos.close();
	}
	
	/*
	 * This method is used to write to the daily transaction file.
	 * @param msg This is the string written to the file.
	 */
	public void writeToDTF(String msg) {

			LocalDate localDate = LocalDate.now();
			String date = new String("resources/" + localDate + ".dtf");
			/*
			java.net.URL url = User.class.getClassLoader().getResource(date);
			URL url2 = User.class.getClassLoader().getResource("resources/");

			if(url == null) {
				File temp = new File(url2.getPath(), localDate + ".dtf");
				temp.createNewFile();
				url = User.class.getClassLoader().getResource(date);
			}
			 */
			File file = new File(date);

			FileWriter fr;
			try {
				fr = new FileWriter(file,true);
				fr.write(msg);
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}


	}

}
