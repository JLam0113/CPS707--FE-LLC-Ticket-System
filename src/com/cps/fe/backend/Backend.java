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
	private String url2 = "tickets.txt";

	public Backend(String accountsPath, String ticketsPath) {
		url = accountsPath;
		url2 = ticketsPath;
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
				BufferedReader tickFile = new BufferedReader(new FileReader(file));
				StringBuffer inputBuffer = new StringBuffer();
				String curLine = "";
				
				String eventName = temp.substring(4,18).trim();
				String sellerName = temp.substring(19,34).trim();
				String quantity = temp.substring(35,38).trim();
				String price = temp.substring(39,45).trim();

				// go to end
				while ((curLine = tickFile.readLine()) != null) {

					if (curLine.equals("END                                         "))
						break;

					inputBuffer.append(curLine);
					inputBuffer.append('\n');
				}

				// update
				String eventRes = String.format("%1$-19s", eventName);
				String sellerRes = String.format("%1$-13s", sellerName);
				String qtyRes = quantity;
				String priceRes = price;

				inputBuffer.append(eventRes + " " + sellerRes + " " + qtyRes + " " + priceRes);
				inputBuffer.append('\n');

				inputBuffer.append(curLine);
				inputBuffer.append('\n');

				// overwrite entire file
				FileOutputStream fos = new FileOutputStream(new File(url2));
				fos.write(inputBuffer.toString().getBytes());
				fos.close();
				tickFile.close();
				return;
			}
			else if(action.equals("02"))
			{
				//buy
				BufferedReader tickFile = new BufferedReader(new FileReader(file));
				StringBuffer inputBuffer = new StringBuffer();
				String curLine = "";
				String evtName= "";
				String seller= "";
				String findEvtName = temp.substring(3,18).trim();
				String findSeller = temp.substring(19,34).trim();
				String updateQty = temp.substring(35,38).trim();
				// go to line to update
				while ((curLine = tickFile.readLine()) != null) {

					evtName = curLine.substring(0,20).trim();
					seller = curLine.substring(20,34).trim();

					if (evtName.equalsIgnoreCase(findEvtName) && seller.equalsIgnoreCase(findSeller))
						break;

					inputBuffer.append(curLine);
					inputBuffer.append('\n');
				}

				// update
				String newQtyAvaliable = String.format("%1$-3s", updateQty);
				inputBuffer.append(curLine.substring(0,34) + newQtyAvaliable + " " + curLine.substring(38));
				inputBuffer.append('\n');

				// write the rest of file
				while ((curLine = tickFile.readLine()) != null) {

					inputBuffer.append(curLine);
					inputBuffer.append('\n');
				}

				// overwrite entire file
				FileOutputStream fos = new FileOutputStream(new File(url));
				fos.write(inputBuffer.toString().getBytes());
				fos.close();
				tickFile.close();
				return;
			}
			else if(action.equals("05"))
			{
				//refund
				BufferedReader tickFile = new BufferedReader(new FileReader(file));
				StringBuffer inputBuffer = new StringBuffer();
				String curLine = "";
				String evtName= "";
				String seller= "";
				String findEvtName = temp.substring(3,18).trim();
				String findSeller = temp.substring(19,34).trim();
				String updateQty = temp.substring(35,38).trim();
				// go to line to update
				while ((curLine = tickFile.readLine()) != null) {

					evtName = curLine.substring(0,20).trim();
					seller = curLine.substring(20,34).trim();

					if (evtName.equalsIgnoreCase(findEvtName) && seller.equalsIgnoreCase(findSeller))
						break;

					inputBuffer.append(curLine);
					inputBuffer.append('\n');
				}

				// update
				String newQtyAvaliable = String.format("%1$-3s", updateQty);
				inputBuffer.append(curLine.substring(0,34) + newQtyAvaliable + " " + curLine.substring(38));
				inputBuffer.append('\n');

				// write the rest of file
				while ((curLine = tickFile.readLine()) != null) {

					inputBuffer.append(curLine);
					inputBuffer.append('\n');
				}

				// overwrite entire file
				FileOutputStream fos = new FileOutputStream(new File(url));
				fos.write(inputBuffer.toString().getBytes());
				fos.close();
				tickFile.close();
				return;
				
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
						inputBuffer.append(accTemp +"\n");
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
				accSc.close();
				return;
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
				return;
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
				accSc.close();
				return;
			}
		}
		sc.close();
	}
}
