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

}
