package au.com.cba.nopowersms;

import android.test.AndroidTestCase;
import au.com.cba.nopowersms.base.Globals;
import au.com.cba.nopowersms.database.ResellerAgent;

public class ResellerAgentTest extends AndroidTestCase {

	final static String resellerPhoneNumber = "+1404555817";
	final static String newResellerPhoneNumber = "+14447634037";
	final static String ownerPhoneNumberReal = "+61404653790";
	final static String ownerPhoneNumberTest = "+61404555817";
	
	public final void test0SettersAndGetters() {

		// Arrange
		ResellerAgent subject = new ResellerAgent(this.getContext());

		// Reseller Message
		
		// Arrange
		subject.setResellerMessage("lorem ipsum");
		
		// Assert
		Assert.areEqual(subject.getResellerMessage(), "lorem ipsum",
				"set and get for resellers message");
		
		// Reseller Committed
		
		// Arrange
		subject.setResellerMessageCommitted(true);
		
		// Assert
		Assert.areEqual(subject.getResellerMessageCommitted(), true,
				"set and get for resellers message committed");
		
		// Reseller Phone Number
		
		// Arrange
		subject.setResellerPhoneNumber(resellerPhoneNumber);
		
		// Assert
		Assert.areEqual(subject.getResellerPhoneNumber(), resellerPhoneNumber,
				"set and get for resellers message committed");
		
	}
	
	// Clear the reseller settings on the phone
	private void resetPhoneSettings() {
		ResellerAgent subject = new ResellerAgent(this.getContext());
		subject.setResellerMessage("");
		subject.setResellerPhoneNumber("");
		
		// This has the effect that next time a reseller message is received, the reseller phone number will 
		// be set to the sending phone number
		subject.setResellerMessageCommitted(false);
	}
	
	public final void testChangeResellerMessageCommand() {
		
		// Valid command, from valid sender, is accepted

		// Arrange
		resetPhoneSettings();
		
		ResellerAgent subject = new ResellerAgent(this.getContext());
		String initialResellerPhoneNumber = subject.getResellerPhoneNumber();
		
		Assert.isTrue((initialResellerPhoneNumber == null) ||
						initialResellerPhoneNumber.equals(""), "ResellerAgent reseller number is null");
		subject.handleResellerMsgCommand("@", resellerPhoneNumber);
		Assert.areEqual(subject.getResellerMessage(), "",
				"Reseller Msg Command precondition");
		Assert.areEqual(subject.getResellerPhoneNumber(),resellerPhoneNumber,
							"subject reseller phone number is test reseller phone number");

		// Act
		String command = "@Happy Christmas!!";
		String sender = resellerPhoneNumber;
		
		subject.handleResellerMsgCommand(command,sender);
	
		// Assert
		Assert.areEqual(subject.getResellerMessage(), "Happy Christmas!!",
				"Reseller Msg Command accepts new msg");

		// Long command is truncated

		// Act
		command = "@0_________1_________2_________3_________4_________5_________6_________7123456789";
		sender = resellerPhoneNumber;
		
		subject.handleResellerMsgCommand(command,sender);
	
		// Assert
		Assert.areEqual(subject.getResellerMessage(), 
				"0_________1_________2_________3_________4_________5_________6_________71234",
				"Reseller Msg Command accepts new msg");
	
	
	}
	
	public final void testChangeResellerPhoneCommand() {
		
		final String ownerPhoneNumber = Globals.USE_TEST_PHONE_NUMBER ? ownerPhoneNumberTest : ownerPhoneNumberReal;
		final String changeResellerPhoneNumberCommand = "#"+newResellerPhoneNumber;
		
		// Arrange

		// Arrange
		resetPhoneSettings();

		ResellerAgent subject = new ResellerAgent(this.getContext());
		subject.handleResellerMsgCommand("@", resellerPhoneNumber);
		Assert.areEqual(subject.getResellerMessage(), "",
				"Reseller Msg Command precondition");
		Assert.areEqual(subject.getResellerPhoneNumber(), resellerPhoneNumber, "Reseller Msg Command precondition 2");
		
		// Get command from other number. Rejected
		
		// Act
		
		subject.handleResellerChangePhoneCommand(changeResellerPhoneNumberCommand,"+112345678" );
		
		// Assert
		Assert.areEqual(subject.getResellerPhoneNumber(), resellerPhoneNumber, "Reseller phone number not changed from non-owner");
		
		// Get command from owner. Accepted
		
		// Act
		
		subject.handleResellerChangePhoneCommand(changeResellerPhoneNumberCommand,ownerPhoneNumber );
		
		// Assert
		Assert.areEqual(subject.getResellerPhoneNumber(), newResellerPhoneNumber, "Reseller phone number from owner");
		
		

	}


}
