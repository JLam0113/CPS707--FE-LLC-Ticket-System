/*
 * The following is our prototype ticket system application for CPS 707 Assignment 2. It is able to do all the required functionalities and handles some error checking.
 * It still doesn't meet all requirements and pass all tests as that is leftover for assignment 3, but it does the bare minimum of performing all functionalities to some degree.
 * @author FE LLC
 * @author Jessye Lam 500702091
 * @author Christopher Seow 500782570
 * @author Michael Tsao 500694108
 * CPS707 - Assignment 2
 */
package com.cps.fe.console;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.Scanner;

import com.cps.fe.backend.Backend;
import com.cps.fe.tickets.Tickets;
import com.cps.fe.user.User;

/*
 * This class is used to handle the frontend portion of the system. The user will mainly interact with this class.
 */
public class Console {
	private static Tickets tickets;
	private static String accountsPath = "resources/accounts.txt";
	private static String ticketsPath = "resources/tickets.txt";

	/*
	 * This is the main method to handle the inputs.
	 * @param args Unused.
	 */
	public static void main(String[] args) throws IOException {

		// set the proper input mode
		Scanner sc = new Scanner(System.in);
		if (args.length == 2)
		{
			accountsPath = args[0];
			ticketsPath = args[1];
		}

		// start saving the output to file
		//System.setOut(new PrintStream(new FileOutputStream(new File("outputs/" + args[0]))));

		System.out.println("Welcome, please enter a command.");

		while(true) {
			User user1 = login(sc);
			tickets = new Tickets(user1, sc, ticketsPath, accountsPath);
			Backend be = new Backend(accountsPath,ticketsPath);
			postLogin(user1,be, sc);
			logout(user1);
		}
	}
	
	/*
	 * This method is used to handle interactions after logging in.
	 * @param user1 This is the User that is logged in.
	 * @param sc This is the scanner to read inputs.
	 */
	public static void postLogin(User user1,Backend be, Scanner sc) throws IOException {
		String next = sc.nextLine();
		while(!next.equals("logout")){
			if(next.equals("login"))
				System.out.println("Invalid command (cannot login while logged in), please enter a command");
			if(next.equals("addcredit")) {
				System.out.println("AddCredit selected, Please enter username:");
				String username2 = sc.nextLine();
				System.out.println("Please enter amount of credit:");
				int credit = sc.nextInt();
				user1.addCredit(username2, credit);
			}
			if(next.equals("create")) {
				System.out.println("Create account selected, please enter the username:");
				String username2 = sc.nextLine();
				System.out.println("Please enter the account type:");
				String type = sc.nextLine();
				System.out.println("Please enter the initial credit amount:");
				int credit = sc.nextInt();
				user1.createAccount(username2, type, credit);
			}
			if(next.equals("delete")) {
				System.out.println("Delete account selected, please enter the username:");
				String username2 = sc.nextLine();
				user1.deleteAccount(username2, ticketsPath);
			}
			if(next.equals("buy")) {
				
				System.out.println("Buy tickets selected, please enter the event title:");
				String eventTitle = sc.nextLine();
				System.out.println("Please enter the number of tickets:");
				int numOfTickets = Integer.parseInt(sc.nextLine());
				System.out.println("Please enter the seller's username:");
				String sellersUsername = sc.nextLine();
				tickets.buy(eventTitle, numOfTickets, sellersUsername);
			}
			if(next.equals("sell")) {

				System.out.println("Sell tickets selected, please enter an event:");
				String eventTitle = sc.nextLine();
				System.out.println(eventTitle + " selected, please enter the price:");
				float pricef = Float.parseFloat(sc.nextLine());
				//TEMP FIX*******
				int price = Math.round(pricef);
				System.out.println("Please enter the amount of tickets:");
				int numOfTickets = Integer.parseInt(sc.nextLine());
				tickets.sell(eventTitle, price, numOfTickets);
			}
			if(next.equals("refund")) {
				if(user1.getUserType().equals("AA")) {
				System.out.println("Refund selected, please enter the buyer's username:");
				String buyer = sc.nextLine();
				System.out.println("Please enter the seller's username:");
				String seller = sc.nextLine();
				System.out.println(buyer + " is the buyer, and " + seller + " is the seller, please enter the amount of credit:");
				int credit = Integer.parseInt(sc.nextLine());
				tickets.refund(buyer, seller, credit);
				}
				else
					System.out.println("User is non privileged, please enter a command:");
			}
			if(next.equals("exit")) {
				System.out.println("Session ended");
				System.exit(0);
			}
			if(next.equals("update")) {
				be.updateBackend();
			}
			next = sc.nextLine();
		}
	}
	
	/*
	 * This is the method to logout after the user enters logout, this is called.
	 * @param user1 This is the user that is logging out. 
	 */
	public static void logout(User user1) throws FileNotFoundException {
		String usernameDTF = String.format("%-15s", user1.getUser());
		String creditDTF = String.format("%06d", user1.getCredit());
		writeToDTF("00 " +  usernameDTF + " " + user1.getUserType() + " " + creditDTF + ".00\n");
		System.out.println("You're now logged out, please enter a command");
	}
	
	/*
	 * This is the method that handles the login portion.
	 * @param sc This is the scanner used to handle inputs.
	 * @return User This returns the user logging in.
	 */
	public static User login(Scanner sc) {
		String command = sc.nextLine();
		while (!command.equals("login")) {
			if(command.equals("exit")) {
				System.out.println("Session ended");
				System.exit(0);
			}
			System.out.println("Invalid command (must be logged in first). Please try again.");
			command = sc.nextLine();
		}
		System.out.println("Please enter your username");
		String username = sc.nextLine();
		User user1 = new User(username, accountsPath);
		while (!user1.exists()) {
			username = sc.nextLine();
			if(username.equals("exit")) {
				System.out.println("Session ended");
				System.exit(0);
			}
			user1.setUser(username);
		}
		if(user1.getUserType().equals("AA"))
			System.out.println("Privileged Login successful, now logged in as " + user1.getUser() + ", please enter a command");
		else
			System.out.println("Non-privileged Login successful, now logged in as " + user1.getUser() + ", please enter a command");
		return user1;
	}
	
	/*
	 * This method is used to write to the daily transaction file.
	 * @param msg This is the string written to the file.
	 */
	public static void writeToDTF(String msg) {

			LocalDate localDate = LocalDate.now();
			String date = new String("resources/DTF-" + localDate + ".dtf");
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

}
