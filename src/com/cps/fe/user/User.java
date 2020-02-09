package com.cps.fe.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class User {
	String username;
	String userType;
	int credit;

	public User(String username) {
		this.username = username;
		try {
		File file = new File("C:\\Users\\Jessye\\eclipse-workspace\\CPS707- FE LLC Ticket System\\src\\resources\\accounts.txt");
		Scanner sc = new Scanner(file);
		while (sc.hasNextLine()) {
			String temp = sc.nextLine();
			String[] temp2 = temp.split(" ");
			if(temp2[0].equals(this.username))
				this.userType = temp2[1];
				this.credit = Integer.parseInt(temp2[2]);
				sc.close();
			}
		sc.close();
		}
		 catch (FileNotFoundException e) {
			 System.out.println(e);
			}
	}
	
	public boolean exists() {
		try {
		File file = new File("C:\\Users\\Jessye\\eclipse-workspace\\CPS707- FE LLC Ticket System\\src\\resources\\accounts.txt");
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
			File file = new File("C:\\Users\\Jessye\\eclipse-workspace\\CPS707- FE LLC Ticket System\\src\\resources\\accounts.txt");
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()) {
				String temp = sc.nextLine();
				String[] temp2 = temp.split(" ");
				if(temp2[0].equals(this.username))
					//Write to file with new credit value
					sc.close();
				}
			sc.close();
			}
			 catch (FileNotFoundException e) {
				 System.out.println(e);
				}
	}
	
	public void createAccount(String username, String type) {
		if (this.userType.equals("AA")) {
			//Write to file with new account info
		}
		else 
			System.out.println("You are not authorized to do that. please enter a command");
	}
	
	public void addCredit(String user, int credit) {
		if (user.equals(this.username))
			this.updateCredit(credit);
		else if (this.userType.equals("AA")) {
			User user2 = new User(user);
			user2.updateCredit(credit);
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
