package com.cps.fe.backend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

public class Backend {
	
	private String url = "accounts.txt";

	public Backend(String accountsPath) {
		url = accountsPath;
		// TODO Auto-generated constructor stub
	}

	public void updateBackend() throws IOException {
		//find todays DTF
		LocalDate localDate = LocalDate.now();
		String date = new String("resources/DTF-" + localDate + ".dtf");
		File file = new File(date);
		//read through each line of dtf
		Scanner sc = new Scanner(file);
		while (sc.hasNextLine()) 
		{
			String temp = sc.nextLine();
			String action = temp.substring(0,2);
			//tickets file
			//System.out.println("TICKETS: " + temp + "\n");
			if(action.equals("03"))
			{
				//sell
			}
			else if(action.equals("02"))
			{
				//buy
			}
			else if(action.equals("05"))
			{
				//refund
			}
			//accounts file
			//System.out.println("ACCOUNTS: " + temp + "\n");
			else if(action.equals("00"))
			{
				//logout
				//does not affect accounts file
			}
			else if(action.equals("01"))
			{
				//create
				//append user to accounts file
				//System.out.println("test\n");
				String username = temp.substring(3,18).trim();
				String credit = temp.substring(22,31).trim();
				//String  = creditTemp.replaceFirst("^0+(?!$)", "");
				String type = temp.substring(19,21).trim();

				//System.out.println(username + "\n");
				BufferedReader accFile = new BufferedReader(new FileReader(new File(url)));
				StringBuffer inputBuffer = new StringBuffer();
				
				Scanner accSc = new Scanner(accFile);
				while (accSc.hasNextLine()) 
				{
					String accTemp = accSc.nextLine();
					String curr_user = accTemp.substring(0,15);
					String curr_user2 = curr_user.trim();
					if(curr_user2.equals(username))
					{
						//An account with this name already exists (probably redundant now)
						//return;
					}
					else if(curr_user2.equals("END"))
					{
						break;
					}
					else
						inputBuffer.append(temp +"\n");
				}
				String usernameDTF = String.format("%-15s", username);
				//String creditDTF = String.format("%06s", credit);
				//System.out.println(usernameDTF + " " + type + " "  + credit + "\n");
				accSc.close();
				accFile.close();
				inputBuffer.append(usernameDTF + " " + type + " "  + credit + "\n");
				FileOutputStream fos = new FileOutputStream(new File(url));
				fos.write(inputBuffer.toString().getBytes());
				fos.close();
				//inputBuffer.append("END                000000.00\n");
				
			}
			else if(action.equals("02"))
			{
				//delete
				//remove user from accounts file
				//System.out.println("test\n");
				String username = temp.substring(3,18).trim();
				String credit = temp.substring(22,31).trim();
				//String  = creditTemp.replaceFirst("^0+(?!$)", "");
				String type = temp.substring(19,21).trim();

				//System.out.println(username + "\n");
				BufferedReader accFile = new BufferedReader(new FileReader(new File(url)));
				StringBuffer inputBuffer = new StringBuffer();
				
				Scanner accSc = new Scanner(accFile);
				while (accSc.hasNextLine()) {
					String accTemp = accSc.nextLine();
					String curr_user = temp.substring(0,15);
					String curr_user2 = curr_user.trim();
					if(curr_user2.equals(username))
					{
						//do nothing
					}
					else
						inputBuffer.append(temp +"\n");
				}
				FileOutputStream fos = new FileOutputStream(new File(url));
				fos.write(inputBuffer.toString().getBytes());
				fos.close();
				accSc.close();
			}
			else if(action.equals("06"))
			{
				//addCredit
				//update credits of user in accounts file
				//System.out.println("test\n");
				String username = temp.substring(3,18).trim();
				String credit = temp.substring(22,31).trim();
				//String  = creditTemp.replaceFirst("^0+(?!$)", "");
				String type = temp.substring(19,21).trim();
				int credit_i = Integer.parseInt(credit);
				//System.out.println(username + "\n");
				BufferedReader accFile = new BufferedReader(new FileReader(new File(url)));
				StringBuffer inputBuffer = new StringBuffer();
				
				Scanner accSc = new Scanner(accFile);
				while (accSc.hasNextLine()) {
					String accTemp = sc.nextLine();
					String[] temp2 = accTemp.split(" ");
					String curr_user = accTemp.substring(0,15);
					String curr_type = accTemp.substring(16,18);
					String curr_credit = accTemp.substring(19,28);
					String curr_user2 = curr_user.trim();
					if(curr_user2.equals(username))
					{
						//Write to file with new credit value
						int credit_ii = (int) Double.parseDouble(curr_credit);
						credit_i += credit_ii;
						String usernameDTF = String.format("%-15s", username);
						String creditDTF = String.format("%06d", credit_i);
						inputBuffer.append(usernameDTF+ " " + type + " " + creditDTF + ".00\n");
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
		}
		sc.close();
	}
}
