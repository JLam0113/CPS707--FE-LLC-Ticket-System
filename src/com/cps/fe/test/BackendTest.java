package com.cps.fe.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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

	@Test
	void testInputValidation() {
		String fail = new String("03 event9          admin           002 -010.00");
		String correct = new String("03 event9          admin           002 010.00");
		boolean res = be.validateInput(fail);
		assertEquals(false, res);
		res = be.validateInput(correct);
		assertEquals(true, res);
	}
	
	@Test
	void testError() {
		String error = new String(be.error(0));
		assertEquals("", error);
		String error2 = new String(be.error(1));
		assertEquals("", error2);
		String error3 = new String(be.error(3));
		assertEquals("Incorrect error code entered", error3);
	}
	
	@Test
	void testFileValidation() {
		try {
			File file = new File("resources/tickets.txt");
			boolean res = new Boolean(be.validateTickets(file));
			assertEquals(true,res);
			File file2 = new File("resources/accounts.txt");
			res = be.validateAccounts(file2);
			assertEquals(true,res);
			File file3 = new File("resources/accounts2.txt");
			res = be.validateAccounts(file3);
			assertEquals(false,res);
			File file4 = new File("resources/tickets2.txt");
			res = be.validateTickets(file4);
			assertEquals(false,res);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	void testStatementUpdateBackendCode00Logout() {
		try {
			// prepare
			populateDTF("00 admin           AA 001040.00");	
		
			// verify nothing has changed
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
					accFile.close();
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
	
	@Test
	void testStatementUpdateBackendCode01CreateUser() {
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
	
	@Test
	void testStatementUpdateBackendCode02DeleteUser() {
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
				
				if (curLine.equals("user2           BS 001080.00"))
				{
					fail("found deleted user");
					return;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(true);
	}
	
	@Test
	void testStatementUpdateBackendCode03SellEvent() {
		try {
			// prepare
			populateDTF("03 event9          admin           002 010.00");	
		
			// verify
			be.updateBackend();
			
			BufferedReader tickFile = new BufferedReader(new FileReader("resources/tickets.txt"));
			String curLine = "";
			while ((curLine = tickFile.readLine()) != null) 
			{
				if (curLine.equals("END                                         "))
					break;
				
				if (curLine.equals("event9              admin         2   10.00 "))
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
	
	@Test
	void testStatementUpdateBackendCode04SellEvent() {
		try {
			// prepare
			populateDTF("04 event2          admin           001 015.00");	
		
			// verify
			be.updateBackend();
			
			BufferedReader tickFile = new BufferedReader(new FileReader("resources/tickets.txt"));
			String curLine = "";
			while ((curLine = tickFile.readLine()) != null) 
			{
				if (curLine.equals("END                                         "))
					break;
				
				if (curLine.equals("event2              admin         2   15.00 "))
				{
					assertTrue(true);
					return;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fail("cannot find expected ticket change");
	}

	//TODO: fix empty string
	@Test
	void testStatementUpdateBackendCode05Refund() {
		try {
			// prepare
			populateDTF("05 user2           admin           000010.00");	
		
			// verify
			be.updateBackend();
			
			BufferedReader accFile = new BufferedReader(new FileReader("resources/accounts.txt"));
			String curLine = "";
			while ((curLine = accFile.readLine()) != null) 
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
		
		fail("cannot find expected event change");
	}
	
	@Test
	void testStatementUpdateBackendCode06AddCredit() {
		try {
			// prepare
			populateDTF("06 user2           BS 000010.00 ");	
		
			// verify
			be.updateBackend();
			
			BufferedReader tickFile = new BufferedReader(new FileReader("resources/accounts.txt"));
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
	
	@Test
	void tesDecisionUpdateBackendElseCatchAll() {
		try {
			// prepare
			populateDTF("99 admin           AA 001040.00");	
		
			// verify nothing has changed
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
