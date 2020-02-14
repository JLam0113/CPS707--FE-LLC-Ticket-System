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
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		System.out.println("Welcome, please enter a command.");
		String command = sc.nextLine();
		if (command.equals("login")) {
			System.out.println("Please enter your username");
		}
		else {
			System.out.println("Invalid command (must be logged in first). Session ended.");
			System.exit(0);
		}
		String username = sc.nextLine();
		User user1 = new User(username);
		if (!user1.exists()) {
			System.out.println("Login unsuccessful (user not found). Session ended.");
			System.exit(0);
		}
		System.out.println("Login successful, please enter a command");
		String next = sc.nextLine();
		while(!next.equals("logout")){
			if(next.equals("addcredit")) {
				System.out.println("Please enter username:");
				String username2 = sc.nextLine();
				System.out.println("Please enter amount of credit:");
				int credit = sc.nextInt();
				user1.addCredit(username2, credit);
			}
			if(next.equals("create")) {
				System.out.println("Create account selected, pelase enter the username:");
				String username2 = sc.nextLine();
				System.out.println("Please enter the account type:");
				String type = sc.nextLine();
				user1.createAccount(username2, type);
			}
			if(next.equals("delete")) {
				System.out.println("Delete account selected, pelase enter the username:");
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
		writeToDTF("00 000000 0.00\n");
		System.out.println("Session ended");
		System.exit(0);
		
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
