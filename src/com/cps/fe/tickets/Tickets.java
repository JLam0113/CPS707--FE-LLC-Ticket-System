package com.cps.fe.tickets;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.cps.fe.user.User;

public class Tickets {
	
	public Tickets()
	{
		
		//Get path for tickets.txt
		try {
			//Get the local path for accounts.txt
			java.net.URL url = User.class.getClassLoader().getResource("resources/tickets.txt");
				
			File file = new File(url.getPath());
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()) {
				String temp = sc.nextLine();
				System.out.println(temp);
				
			}
			sc.close();
			
			}
			 catch (FileNotFoundException e) {
				 System.out.println(e);
			}
		
		
	}

}
