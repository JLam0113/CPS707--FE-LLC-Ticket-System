package com.cps.fe.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.cps.fe.backend.Backend;

class BackendTest {

	private static String accounts = "";
	private static String tickets = "";
	
	private Backend be;
	
	@BeforeEach
	public void runBeforeEachTest()
	{
		try {
			resetInputFiles();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		be = new Backend("resources/accounts.txt", "resources/tickets.txt");
	}
	
	@AfterEach
	public void runAfterEachTest()
	{
		be = null;
	}

	//missing functionality - buy 04?
	
	//sell
	@Disabled("borked")
	@Test
	void testStatementUpdateBackendCode03() {
		try {
			// prepare
			populateDTF("03 event1          admin           002 010.00");	
		
			// verify
			be.updateBackend();
			
			BufferedReader tickFile = new BufferedReader(new FileReader("resources/tickets.txt"));
			String curLine = "";
			while ((curLine = tickFile.readLine()) != null) 
			{
				if (curLine.equals("END                                         "))
					break;
				
				if (curLine.equals("event1              admin         2   10.00 "))
				{
					assertTrue(true);
					return;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fail("cannot find expected event change");
	}
	
	@Disabled("borked")
	@Test
	void testStatementUpdateBackendCode02() {
		try {
			// prepare
			populateDTF("02 user2           BS 999959.00");	
		
			// verify
			be.updateBackend();
			
			BufferedReader tickFile = new BufferedReader(new FileReader("resources/tickets.txt"));
			String curLine = "";
			while ((curLine = tickFile.readLine()) != null) 
			{
				if (curLine.equals("END                                         "))
					break;
				
				if (curLine.equals("event1              admin         2   10.00 "))
				{
					assertTrue(true);
					return;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fail("cannot find expected event change");
	}
	
	@Disabled("borked")
	@Test
	void testStatementUpdateBackendCode05() {
		try {
			// prepare
			populateDTF("05 user2           admin           000010.00");	
		
			// verify
			be.updateBackend();
			
			BufferedReader tickFile = new BufferedReader(new FileReader("resources/tickets.txt"));
			String curLine = "";
			while ((curLine = tickFile.readLine()) != null) 
			{
				if (curLine.equals("END                                         "))
					break;
				
				if (curLine.equals("event1              admin         2   10.00 "))
				{
					assertTrue(true);
					return;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fail("cannot find expected event change");
	}
	
	// logout
	@Test
	void testStatementUpdateBackendCode00() {
		try {
			// prepare
			populateDTF("00 admin           AA 001040.00");	
		
			// verify
			be.updateBackend();
			String[] ts = {"event1              admin         1   10.00 ",
			               "event2              admin         3   15.00 ",
			               "event3              admin         3   20.0  ",
			               "END                                         "};
			
			String[] as = {"admin           AA 001080.00",
							"user2           BS 001080.00",
							"END                000000.00"};

			int curIndex = 0;
			
			BufferedReader tickFile = new BufferedReader(new FileReader("resources/tickets.txt"));
			String curLine = "";
			while ((curLine = tickFile.readLine()) != null) 
			{
				if (!curLine.equals(ts[curIndex]))
				{
					fail("failed ticket file mismatch - no change expected");
				}
				curIndex++;
			}
			tickFile.close();
			
			curIndex = 0;
			BufferedReader accFile = new BufferedReader(new FileReader("resources/accounts.txt"));
			curLine = "";
			while ((curLine = accFile.readLine()) != null) 
			{
				if (!curLine.equals(as[curIndex]))
				{
					fail("failed account file mismatch - no change expected");
				}
				curIndex++;
			}		
			accFile.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(true);
		
	}
	
	// create
	@Test
	void testStatementUpdateBackendCode01() {
		try {
			// prepare
			populateDTF("01 user1           FS 000100.00 ");	
		
			// verify
			be.updateBackend();
			
			BufferedReader tickFile = new BufferedReader(new FileReader("resources/accounts.txt"));
			String curLine = "";
			while ((curLine = tickFile.readLine()) != null) 
			{
				if (curLine.equals("END                                         "))
					break;
				
				if (curLine.equals("user1           FS 000100.00"))
				{
					assertTrue(true);
					return;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fail("entry not found in accounts");

	}
	
	// add credit
	@Disabled("borked")
	@Test
	void testStatementUpdateBackendCode06() {
		try {
			// prepare
			populateDTF("06 user2           BS 000010.00 ");	
		
			// verify
			be.updateBackend();
			
			BufferedReader tickFile = new BufferedReader(new FileReader("resources/tickets.txt"));
			String curLine = "";
			while ((curLine = tickFile.readLine()) != null) 
			{
				if (curLine.equals("END                                         "))
					break;
				
				if (curLine.equals("user2           BS 001090.00"))
				{
					assertTrue(true);
					return;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fail("entry not found in accounts");
	}
	
	// helper function that will populate the dtf for the next test
	private void populateDTF(String command) throws IOException
	{
		FileOutputStream dtf = new FileOutputStream(new File("resources/DTF-" + LocalDate.now() + ".dtf"), false);
		dtf.write(command.getBytes());
		dtf.close();
	}
	
	// helper function that will reset the input files
	private void resetInputFiles() throws IOException
	{
		FileOutputStream tick = new FileOutputStream(new File("resources/tickets.txt"), false);
		tick.write("event1              admin         1   10.00 \n".getBytes());
		tick.write("event2              admin         3   15.00 \n".getBytes());
		tick.write("event3              admin         3   20.0  \n".getBytes());
		tick.write("END                                         \n".getBytes());
		tick.close();		
		
		FileOutputStream acc = new FileOutputStream(new File("resources/accounts.txt"), false);
		acc.write("admin           AA 001080.00\n".getBytes());
		acc.write("user2           BS 001080.00\n".getBytes());
		acc.write("END                000000.00\n".getBytes());
		acc.close();
	}

}
