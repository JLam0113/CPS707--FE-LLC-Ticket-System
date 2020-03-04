/*
 * The following is our prototype ticket system application for CPS 707 Assignment 2. It is able to do all the required functionalities and handles some error checking.
 * It still doesn't meet all requirements and pass all tests as that is leftover for assignment 3, but it does the bare minimum of performing all functionalities to some degree.
 * @author FE LLC
 * @author Jessye Lam 500702091
 * @author Christopher Seow 500782570
 * @author Michael Tsao 500694108
 * CPS707 - Assignment 2
 */
package com.cps.fe.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.io.PrintWriter;
import java.util.Scanner;

/*
 * This class is used to handle all things related to the accounts.txt file.
 */
public class User {
	
	String username;
	String userType;
	int credit;

	private String url = "accounts.txt";

	/*
	 * This is the constructor for setting up the user.
	 * @param username Username of person logging in.
	 */
	public User(String username, String accountsPath) {
		this.username = username;
		try {
		
		//Get the local path for accounts.txt
		url = accountsPath;
			
		File file = new File(url);
		Scanner sc = new Scanner(file);
		while (sc.hasNextLine()) {
			String temp = sc.nextLine();
			String[] temp2 = temp.split(" ");
			if(temp2[0].equals(this.username)) {
				this.userType = temp2[1];
			//if(Double.parseDouble(temp2[2]) != 0.00)
				this.credit = (int) Double.parseDouble(temp2[2]);
			}
		}
		sc.close();
		}
		 catch (FileNotFoundException e) {
			 System.out.println(e);
			}
	}
	
	/*
	 * This returns the username of the user.
	 * @return String This is the username.
	 */
	public String getUser() {
		return this.username;
	}
	
	/*
	 * This returns the usertype of the user.
	 * @return String This is the usertype.
	 */
	public String getUserType() {
		return this.userType;
	}
	
	/*
	 * This returns the amount of credits of the user.
	 * @return int This is the amount of credits.
	 */
	public int getCredit() {
		return this.credit;
	}
	
	/*
	 * This method is used as an alternative to the constructor. Used to change the user to a different user.
	 * @param username This is the username of the user to be changed to.
	 */
	public void setUser(String username) {
		this.username = username;
		try {

		File file = new File(url);
		Scanner sc = new Scanner(file);
		while (sc.hasNextLine()) {
			String temp = sc.nextLine();
			String[] temp2 = temp.split(" ");
			if(temp2[0].equals(this.username)) {
				this.userType = temp2[1];
			//if(Double.parseDouble(temp2[2]) != 0.00)
				this.credit = (int) Double.parseDouble(temp2[2]);
			}
		}
		sc.close();
		}
		 catch (FileNotFoundException e) {
			 System.out.println(e);
			}
	}
	
	/*
	 * This method is used to see if the user logging in is real.
	 * @return true if the user exists, false otherwise.
	 */
	public boolean exists() {
		try {
		

			
		File file = new File(url);	
		Scanner sc = new Scanner(file);
		while (sc.hasNextLine()) {
			String temp = sc.nextLine();
			String[] temp2 = temp.split(" ");
			if(temp2[0].equals(this.username))
				sc.close();
				return true;
			}
		sc.close();
		}
		 catch (FileNotFoundException e) {
			 System.out.println(e);
			}
		return false;
	}
	
	/*
	 * This method is used to write the file with the updated credit value.
	 * @param credit This is the credit to be added or subtracted to the current credit value.
	 */
	public void updateCredit(int credit) {
		try {
			
			//Get the local path for accounts.txt
			java.net.URL url2 = User.class.getClassLoader().getResource("resources/temp.txt");
				
			File file = new File(url);
			File file2 = new File(url2.getPath());
			
			Scanner sc = new Scanner(file);
			FileWriter fr;
			try {
				fr = new FileWriter(file2,true);
				while (sc.hasNextLine()) {
					String temp = sc.nextLine();
					String[] temp2 = temp.split(" ");
					if(temp2[0].equals(this.username))
					{
						//Write to file with new credit value
						this.credit = (int) Double.parseDouble(temp2[2]);
						this.credit += credit;
						fr.write(this.username + " " + this.userType + " " + this.credit + ".00\n");
					}
					else
						fr.write(temp + "\n");
				}
				// Rename file
				PrintWriter pw = new PrintWriter(file);
				pw.print("");
				pw.close();
				fr.close();
				sc.close();
				sc = new Scanner(file2);
				fr = new FileWriter(file,true);
				while (sc.hasNextLine()) {
					String temp = sc.nextLine();
					String[] temp2 = temp.split(" ");
					if(temp2[0].equals(this.username))
					{
						//Write to file with new credit value
						fr.write(this.username + " " + this.userType + " " + this.credit + ".00\n");
					}
					else
						fr.write(temp + "\n");
				}
		
				pw = new PrintWriter(file2);
				pw.print("");
				pw.close();
				
				
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sc.close();
			}
			 catch (FileNotFoundException e) {
				 System.out.println(e);
				}
	}
	
	/*
	 * This method is used to delete the account from accounts.txt file.
	 */
	public void delete() {
		try {
			
			//Get the local path for accounts.txt
			java.net.URL url2 = User.class.getClassLoader().getResource("resources/temp.txt");
				
			File file = new File(url);
			File file2 = new File(url2.getPath());
			
			Scanner sc = new Scanner(file);
			FileWriter fr;
			try {
				fr = new FileWriter(file2,true);
				while (sc.hasNextLine()) {
					String temp = sc.nextLine();
					String[] temp2 = temp.split(" ");
					if(temp2[0].equals(this.username))
						fr.write("");
					else
						fr.write(temp + "\n");
				}
				// Rename file
				PrintWriter pw = new PrintWriter(file);
				pw.print("");
				pw.close();
				fr.close();
				sc.close();
				sc = new Scanner(file2);
				fr = new FileWriter(file,true);
				while (sc.hasNextLine()) {
					String temp = sc.nextLine();
					String[] temp2 = temp.split(" ");
					if(temp2[0].equals(this.username))
					{
						//Write to file with new credit value
						fr.write("");
					}
					else
						fr.write(temp + "\n");
				}
		
				pw = new PrintWriter(file2);
				pw.print("");
				pw.close();
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sc.close();
			}
			 catch (FileNotFoundException e) {
				 System.out.println(e);
				}
	}
	
	/*
	 * This method is used to create an account.
	 * @param username This is username to be created.
	 * @param type This is the type to set the user to.
	 */
	public void createAccount(String username, String type){
		if (username.length() >= 16) {
			System.out.println("Username is too long (max 15), please enter a command");
			return;
		}
		if (this.userType.equals("AA")) {
			try 
			{

					
				File file = new File(url);
				Scanner sc = new Scanner(file);
				
				while (sc.hasNextLine()) 
				{
					String temp = sc.nextLine();
					String[] temp2 = temp.split(" ");
					if(temp2[0].equals(username))
					{
						//An account with this name already exists
						System.out.println("Invalid username (username already taken). Please enter a command.");
						return;
					}
				}
				sc.close();
				
				//User name is not taken.
				FileWriter fr;
				try {
					fr = new FileWriter(file,true);
					fr.write("\n" + username + " " + type + " 0.00");
					fr.close();
					this.writeToDTF("01 " + username + " " + type + " 0.00\n");
					System.out.println("Transaction successful, please enter a command.");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			catch (FileNotFoundException e) 
			{
				 System.out.println(e);
			}	
		}
		else 
			System.out.println("You are not authorized to do that. please enter a command");
	}
	
	/*
	 * This method is used when addcredit is called, this method is mainly for verification on the constraints.
	 * @param user Username to add credit to.
	 * @param credit Amount of credit to be added.
	 */
	public void addCredit(String user, int credit) {
		if (user.equals(this.username)) {
			this.updateCredit(credit);
			this.writeToDTF("06 " + this.username + " " + credit + " \n");
			System.out.println("Transaction successful, please enter a command.");
		}
		else if (this.userType.equals("AA")) {
			try 
			{
				//TODO: Check if entered username exists in accounts.txt
					
				File file = new File(url);
				Scanner sc = new Scanner(file);
				boolean userExists = false;
				
				while (sc.hasNextLine()) 
				{
					String temp = sc.nextLine();
					String[] temp2 = temp.split(" ");
					if(temp2[0].equals(user))
					{
						//user exists within system
						userExists = true;
					}
				}
				sc.close();
				
				if(!userExists)
				{
					//An account with this name does not exist in account.txt
					System.out.println("Invalid username (user does not exist). Session ended.");
					System.exit(0);
				}
				else
				{
					//Update the users credit
					User user2 = new User(user, url);
					user2.updateCredit(credit);	
					this.writeToDTF("06 " + user2.username + " " + credit + " \n");
					System.out.println("Transaction successful, please enter a command.");
				}
			}
			catch (FileNotFoundException e) 
			{
				 System.out.println(e);
			}	
		}
		else if (this.userType.equals("FS")) {
			User user2 = new User(user, url);
			if(user2.userType.equals("AA"))
				System.out.println("Transaction cancelled (user is invalid), please enter a command");
			else {
				user2.updateCredit(credit);
				this.writeToDTF("06 " + user2.username + " " + credit + " \n");
				System.out.println("Transaction successful, please enter a command.");
			}
		}
		else 
			System.out.println("Transaction cancelled (user is invalid), please enter a command");
	}
	
	/*
	 * This method is called when delete is entered. This method is mainly used for verification on the constraints.
	 * @param user User to be deleted.
	 */
	public void deleteAccount(String user) {
		if (this.userType.equals("AA")) {
			try 
			{
				//TODO: Check if entered username exists in accounts.txt
				//Get the local path for accounts.txt

				File file = new File(url);
				Scanner sc = new Scanner(file);
				boolean userExists = false;
				
				while (sc.hasNextLine()) 
				{
					String temp = sc.nextLine();
					String[] temp2 = temp.split(" ");
					if(temp2[0].equals(user))
					{
						//user exists within system
						userExists = true;
					}
				}
				sc.close();
				
				if(!userExists)
				{
					//An account with this name does not exist in account.txt
					System.out.println("Invalid username (user does not exist). Session ended.");
					System.exit(0);
				}
				else
				{
					//Update the users credit
					User user2 = new User(user, url);
					user2.delete();
					this.writeToDTF("02 " + user2.username + " " + user2.userType + " " + user2.credit + " \n");
					System.out.println("Transaction successful, please enter a command.");
				}
			}
			catch (FileNotFoundException e) 
			{
				 System.out.println(e);
			}	
		}
		else 
			System.out.println("Transaction cancelled (user is invalid), please enter a command");
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

	}
}
