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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
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
	boolean foundUser;

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
		foundUser = false;
		while (sc.hasNextLine()) {
			String temp = sc.nextLine();
			String[] temp2 = temp.split(" ");
			String curr_user = temp.substring(0,15);
			String curr_type = temp.substring(16,18);
			String curr_credit = temp.substring(19,28);
			String curr_user2 = curr_user.trim();
			//System.out.println(curr_user2 + " | " + curr_type + " | " + curr_credit + "\n");
			//if(temp2[0].equals(this.username)) {
			if(curr_user2.equals(this.username)) {
				foundUser = true;
				this.userType = curr_type;
			//if(Double.parseDouble(temp2[2]) != 0.00)
				this.credit = (int) Double.parseDouble(curr_credit);
			}
		}
		sc.close();
		if(foundUser == false)
		{
			System.out.println("Login unsuccessful (user not found). Please enter your username.");
		}
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
			String curr_user = temp.substring(0,15);
			String curr_type = temp.substring(16,18);
			String curr_credit = temp.substring(19,28);
			String curr_user2 = curr_user.trim();
			String[] temp2 = temp.split(" ");
			//if(temp2[0].equals(this.username)) {
			if(curr_user2.equals(this.username)) {
				foundUser = true;
				this.userType = curr_type;
			//if(Double.parseDouble(temp2[2]) != 0.00)
				this.credit = (int) Double.parseDouble(curr_credit);
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
		return foundUser;
		/*	
		try {

		File file = new File(url);	
		Scanner sc = new Scanner(file);
		while (sc.hasNextLine()) {
			String temp = sc.nextLine();
			String curr_user = temp.substring(0,15);
			String curr_user2 = curr_user.trim();
			String[] temp2 = temp.split(" ");
			if(curr_user2.equals(this.username))
				sc.close();
				return true;
			}
		sc.close();
		}
		 catch (FileNotFoundException e) {
			 System.out.println(e);
			}
			*/
	}
	
	/*
	 * This method is used to write the file with the updated credit value.
	 * @param credit This is the credit to be added or subtracted to the current credit value.
	 */
	public void updateCredit(int credit) throws IOException {
			
			//Get the local path for accounts.txt
			//java.net.URL url2 = User.class.getClassLoader().getResource("resources/temp.txt");
				
			//File file = new File(url);
			BufferedReader file = new BufferedReader(new FileReader(new File(url)));
			StringBuffer inputBuffer = new StringBuffer();
			//File file2 = new File(url2.getPath());
			
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()) {
				String temp = sc.nextLine();
				String[] temp2 = temp.split(" ");
				String curr_user = temp.substring(0,15);
				String curr_type = temp.substring(16,18);
				String curr_credit = temp.substring(19,28);
				String curr_user2 = curr_user.trim();
				if(curr_user2.equals(this.username))
				{
					//Write to file with new credit value
					this.credit = (int) Double.parseDouble(curr_credit);
					this.credit += credit;
					String usernameDTF = String.format("%-15s", this.username);
					String creditDTF = String.format("%06d", this.credit);
					inputBuffer.append(usernameDTF+ " " + this.userType + " " + creditDTF + ".00\n");
					//fr.write(this.username + " " + this.userType + " " + this.credit + ".00\n");
				}
				else
				{
					inputBuffer.append(temp +"\n");
					//do nothing
				}
			}
			FileOutputStream fos = new FileOutputStream(new File(url));
			fos.write(inputBuffer.toString().getBytes());
			fos.close();
			sc.close();
	}
	
	/*
	 * This method is used to delete the account from accounts.txt file.
	 */
	public void delete() throws IOException {
		//Get the local path for accounts.txt
		//java.net.URL url = User.class.getClassLoader().getResource("resources/accounts.txt");
			
		//File file = new File(url.getPath());
		//File file2 = new File(url2.getPath());
		BufferedReader file = new BufferedReader(new FileReader(new File(url)));
		StringBuffer inputBuffer = new StringBuffer();
		
		Scanner sc = new Scanner(file);
		FileWriter fr;
		//fr = new FileWriter(file2,true);
		while (sc.hasNextLine()) {
			String temp = sc.nextLine();
			String[] temp2 = temp.split(" ");
			String curr_user = temp.substring(0,15);
			String curr_user2 = curr_user.trim();
			if(curr_user2.equals(this.username))
			{
				//do nothing
			}
			else
				inputBuffer.append(temp +"\n");
		}
		FileOutputStream fos = new FileOutputStream(new File(url));
		fos.write(inputBuffer.toString().getBytes());
		fos.close();
		sc.close();
	}
	
	/*
	 * This method is used to create an account.
	 * @param username This is username to be created.
	 * @param type This is the type to set the user to.
	 */
	public void createAccount(String username, String type, int credit) throws IOException{
		if (username.length() >= 16) {
			System.out.println("Username is too long (max 15), please enter a command");
			return;
		}
		if (credit > 999999 || credit < 0)
		{
			System.out.println("Transaction unsuccessful (credit exceeded $999,999 or was less than 0), please enter a command.");
			return;
		}
		if (this.userType.equals("AA")) {
			try 
			{
				//Write to file with new account info
				//Get the local path for accounts.txt
				//java.net.URL url = User.class.getClassLoader().getResource("resources/accounts.txt");
					
				//File file = new File(url.getPath());
				BufferedReader file = new BufferedReader(new FileReader(new File(url)));
				StringBuffer inputBuffer = new StringBuffer();
				Scanner sc = new Scanner(file);
				
				while (sc.hasNextLine()) 
				{
					String temp = sc.nextLine();
					String[] temp2 = temp.split(" ");
					String curr_user = temp.substring(0,15);
					String curr_user2 = curr_user.trim();
					if(curr_user2.equals(username))
					{
						//An account with this name already exists
						System.out.println("Invalid username (username already taken). Please enter a command.");
						return;
					}
					else if(curr_user2.equals("END"))
					{
						break;
					}
					else
						inputBuffer.append(temp +"\n");
				}
				

				String usernameDTF = String.format("%-15s", username);
				String creditDTF = String.format("%06d", credit);
				inputBuffer.append(usernameDTF + " " + type + " "  + creditDTF + ".00\n");
				inputBuffer.append("END                000000.00\n");
				this.writeToDTF("01 " + usernameDTF + " " + type + " " + creditDTF + ".00 \n");
				//this.writeToDTF("01 " + username + " " + type + " 0.00\n");
				System.out.println("Transaction successful, please enter a command.");
				FileOutputStream fos = new FileOutputStream(new File(url));
				fos.write(inputBuffer.toString().getBytes());
				fos.close();
				sc.close();
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
	public void addCredit(String user, int credit) throws IOException {
		if (credit > 1000 || credit < 0)
		{
			System.out.println("Transaction unsuccessful (credit exceeded $1,000 or was less than 0), please enter a command.");
			return;
		}
		if (user.equals(this.username)) {
				this.updateCredit(credit);
				String usernameDTF = String.format("%-15s", this.username);
				String creditDTF = String.format("%06d", credit);
				this.writeToDTF("06 " + usernameDTF + " " + this.userType + " " + creditDTF + ".00\n");
				System.out.println("Transaction successful, please enter a command.");
		}
		else if (this.userType.equals("AA")) {
			try 
			{
				//TODO: Check if entered username exists in accounts.txt
				//Get the local path for accounts.txt
				//java.net.URL url = User.class.getClassLoader().getResource("resources/accounts.txt");
					
				File file = new File(url);
				Scanner sc = new Scanner(file);
				boolean userExists = false;
				
				while (sc.hasNextLine()) 
				{
					String temp = sc.nextLine();
					String[] temp2 = temp.split(" ");
					String curr_user = temp.substring(0,15);
					String curr_user2 = curr_user.trim();
					if(curr_user2.equals(user))
					{
						//user exists within system
						userExists = true;
					}
				}
				sc.close();
				
				if(!userExists)
				{
					//An account with this name does not exist in account.txt
					System.out.println("Invalid username (user does not exist). Please enter a command.");
					return;
				}
				else
				{
					//Update the users credit
					User user2 = new User(user,url);
						user2.updateCredit(credit);	
						String usernameDTF = String.format("%-15s", user2.username);
						String creditDTF = String.format("%06d", credit);
						this.writeToDTF("06 " + usernameDTF + " " + user2.userType + " " + creditDTF + ".00 \n");
						System.out.println("Transaction successful, please enter a command.");
				}
			}
			catch (FileNotFoundException e) 
			{
				 System.out.println(e);
			}	
		}
		else 
		{
			System.out.println("Transaction cancelled (user is invalid), please enter a command");
			return;
		}
	}
	
	/*
	 * This method is called when delete is entered. This method is mainly used for verification on the constraints.
	 * @param user User to be deleted.
	 */
	public void deleteAccount(String user, String ticketsPath) throws IOException {
		if (this.username.equals(user)) {
			System.out.println("Invalid username (user is current user). Please enter a command.");
			return;
		}
		if (this.userType.equals("AA")) {
			try 
			{
				//TODO: Check if entered username exists in accounts.txt
				//Get the local path for accounts.txt
				//java.net.URL url = User.class.getClassLoader().getResource("resources/accounts.txt");
					
				File file = new File(url);
				Scanner sc = new Scanner(file);
				boolean userExists = false;
				
				//BufferedReader fileB = new BufferedReader(new FileReader(new File(ticketsPath)));
				StringBuffer inputBuffer = new StringBuffer();
				
				while (sc.hasNextLine()) 
				{
					String temp = sc.nextLine();
					String[] temp2 = temp.split(" ");
					String curr_user = temp.substring(0,15);
					String curr_user2 = curr_user.trim();
					if(curr_user2.equals(user))
					{
						//user exists within system
						userExists = true;
						break;
					}
				}
				sc.close();
				
				if(!userExists)
				{
					//An account with this name does not exist in account.txt
					System.out.println("Invalid username (user does not exist). Please enter a command.");
					return;
				}
				else
				{
					File file2 = new File(ticketsPath);
					Scanner sc2 = new Scanner(file2);
					
					while (sc2.hasNextLine()) 
					{
						String temp = sc2.nextLine();
						String[] temp2 = temp.split(" ");
						String seller = temp.substring(20,34).trim();
						if(seller.equals(user))
						{
							//remove these tickets from list
						}
						else
							inputBuffer.append(temp +"\n");
					}
					
					FileOutputStream fos = new FileOutputStream(new File(ticketsPath));
					fos.write(inputBuffer.toString().getBytes());
					fos.close();
					sc2.close();
					
					//delete user
					User user2 = new User(user,url);
					String usernameDTF = String.format("%-15s", user2.username);
					String creditDTF = String.format("%06d", user2.credit);
					this.writeToDTF("02 " + usernameDTF + " " + user2.userType + " " + creditDTF + ".00\n");
					user2.delete();
					System.out.println("Transaction successful, please enter a command.");
				}
			}
			catch (FileNotFoundException e) 
			{
				 System.out.println(e);
			}	
		}
		else 
		{
			System.out.println("Transaction cancelled (user is invalid), please enter a command");
			return;
		}
	}
	
	/*
	 * This method is used to write to the daily transaction file.
	 * @param msg This is the string written to the file.
	 */
	public void writeToDTF(String msg) {

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
