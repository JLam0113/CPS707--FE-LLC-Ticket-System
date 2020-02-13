package com.cps.fe.user;

import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JOptionPane;

import com.cps.fe.console.Console;

public class User {
	String username;
	String userType;
	int credit;


	public User(String username) {
		this.username = username;
		try {
		
		//Get the local path for accounts.txt
		java.net.URL url = User.class.getClassLoader().getResource("resources/accounts.txt");
			
		File file = new File(url.getPath());
		Scanner sc = new Scanner(file);
		while (sc.hasNextLine()) {
			String temp = sc.nextLine();
			String[] temp2 = temp.split(" ");
			if(temp2[0].equals(this.username))
				this.userType = temp2[1];
				this.credit = (int) Double.parseDouble(temp2[2]);
			}
		sc.close();
		}
		 catch (FileNotFoundException e) {
			 System.out.println(e);
			}
	}
	
	public boolean exists() {
		try {
		
		//Get the local path for accounts.txt
		java.net.URL url = User.class.getClassLoader().getResource("resources/accounts.txt");
			
		File file = new File(url.getPath());	
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
	
	public void updateCredit(int credit) {
		try {
			
			//Get the local path for accounts.txt
			java.net.URL url = User.class.getClassLoader().getResource("resources/accounts.txt");
			java.net.URL url2 = User.class.getClassLoader().getResource("resources/temp.txt");
				
			File file = new File(url.getPath());
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
	
	public void createAccount(String username, String type){
		if (this.userType.equals("AA")) {
			try 
			{
				//Write to file with new account info
				//Get the local path for accounts.txt
				java.net.URL url = User.class.getClassLoader().getResource("resources/accounts.txt");
					
				File file = new File(url.getPath());
				Scanner sc = new Scanner(file);
				
				while (sc.hasNextLine()) 
				{
					String temp = sc.nextLine();
					String[] temp2 = temp.split(" ");
					if(temp2[0].equals(username))
					{
						//An account with this name already exists
						System.out.println("Invalid username (username already taken). Session ended.");
						System.exit(0);
					}
				}
				sc.close();
				
				//User name is not taken.
				FileWriter fr;
				try {
					fr = new FileWriter(file,true);
					fr.write("\n" + username + " " + type + " 0.00");
					fr.close();
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
	
	public void addCredit(String user, int credit) {
		if (user.equals(this.username))
			this.updateCredit(credit);
		else if (this.userType.equals("AA")) {
			try 
			{
				//TODO: Check if entered username exists in accounts.txt
				//Get the local path for accounts.txt
				java.net.URL url = User.class.getClassLoader().getResource("resources/accounts.txt");
					
				File file = new File(url.getPath());
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
					User user2 = new User(user);
					user2.updateCredit(credit);	
				}
			}
			catch (FileNotFoundException e) 
			{
				 System.out.println(e);
			}	
		}
		else if (this.userType.equals("FS")) {
			User user2 = new User(user);
			if(user2.userType.equals("AA"))
				System.out.println("Transaction cancelled (user is invalid), please enter a command");
			else
				user2.updateCredit(credit);
		}
		else 
			System.out.println("Transaction cancelled (user is invalid), please enter a command");
	}

}
