package com.cps.fe.console;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Scanner;

import com.cps.fe.user.User;

public class Console {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Welcome, please enter a command.");
		while(true) {
		User user1 = login(sc);
		postLogin(user1, sc);
		logout(user1);
		}
	}
	
	public static void postLogin(User user1, Scanner sc) {
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
				user1.createAccount(username2, type);
			}
			if(next.equals("delete")) {
				System.out.println("Delete account selected, please enter the username:");
				String username2 = sc.nextLine();
				user1.deleteAccount(username2);
			}
			if(next.equals("buy")) {
				
			}
			if(next.equals("sell")) {
				
			}
			if(next.equals("refund")) {
				
			}
			next = sc.nextLine();
		}
	}
	
	public static void logout(User user1) {
		writeToDTF("00 " +  user1.getUser() + " " + user1.getUserType() + " " + user1.getCredit() + "\n");
		System.out.println("You're now logged out, please enter a command");
	}
	
	public static User login(Scanner sc) {
		String command = sc.nextLine();
		while (!command.equals("login")) {
			System.out.println("Invalid command (must be logged in first). Please try again.");
			command = sc.nextLine();
		}
		System.out.println("Please enter your username");
		String username = sc.nextLine();
		User user1 = new User(username);
		while (!user1.exists()) {
			System.out.println("Login unsuccessful (user not found). Please enter your username.");
			username = sc.nextLine();
			user1.setUser(username);
		}
		if(user1.getUserType().equals("AA"))
			System.out.println("Privileged Login successful, now logged in as " + user1.getUser() + ", please enter a command");
		else
			System.out.println("Non-privileged Login successful, now logged in as " + user1.getUser() + ", please enter a command");
		return user1;
	}
	
	public static void writeToDTF(String msg) {
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
