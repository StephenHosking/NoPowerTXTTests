package au.com.cba.nopowersms;

import java.util.Calendar;

import android.content.Context;
import android.test.AndroidTestCase;
import au.com.cba.nopowersms.productactivation.*;
import au.com.cba.nopowersms.base.Utils;
import au.com.cba.nopowersms.database.ResellerAgent;
import au.com.cba.nopowersms.services.PowerStateReceiver;
import junit.framework.TestCase;

public class ActivationSettingsTest extends AndroidTestCase {

	public final void testGetSimSerialNumber() {
		Context context = getContext();
		String sn = new ActivationSettings(context)
				.getSimSerialNumber();
		if ((sn.length() < 12) || (sn.length() > 20)) {
			// Normally 19,20 digits. this number (12) is a guess at the
			// absolute minimum lenght in
			// practice
			fail(String.format("getSimSerialNumber fail"));
		}
	}

	public final void testSettersAndGettters() {

		// Assemble
		Context context = getContext();
		ActivationSettings subject = new ActivationSettings(context);
		SendActivationReport sendActivationReport = new SendActivationReport(context);
		// (An anomaly. This is the only test for SendActivationReport)

		// Act
		String serialNumberNotSet = subject.getLockedSimSerialNumber();

		// Act
		{
			subject.setActivationKey("DFG422");
			subject.setLockedSimSerialNumber("12345678901234567890");
			subject.setIsActivated(true);
			sendActivationReport.setSavedAlarmTime(450);

			if (!(subject.getActivationKey().equals("DFG422"))
					&& (subject.getLockedSimSerialNumber()
							.equals("12345678901234567890"))
					&& (subject.getIsActivated() == true)
					&& (sendActivationReport.getSavedAlarmTime() == 450)) {
				fail("Getter/Setter fail");
			}
		}

	}

	public final void testSimCardLocking() {
		// Assemble
		Context context = getContext();
		ActivationSettings subject = new ActivationSettings(context);
		
		// Setup reseller agent, because locking/unlocking change the reseller msg and need to be
		// tested here.
		ResellerAgent resellerAgent = new ResellerAgent(this.getContext());
		resellerAgent.setResellerMessageCommitted(false);
		String resellerPhoneNumber = ResellerAgentTest.resellerPhoneNumber;
		resellerAgent.handleResellerMsgCommand("@Buy from Me!", resellerPhoneNumber);
		Assert.areEqual(resellerAgent.getResellerMessage(), "Buy from Me!",
				"Reseller Msg Command precondition");

		// Assert. SIM is present
		Assert.isTrue(subject.isSimPresent(),
				"SIM not present reported when it is present");
		// Sim is not locked
		Assert.isFalse(subject.isSimLocked(), "Initially, sim is not unlocked");
		
		// Act. Set null SIM number.
		subject.setLockedSimSerialNumber(null);

		// Assert
		Assert.isTrue(subject.isSimValid(),
				"sim number is not valid, when it is expected to be valid");

		// Act. Set "" SIM number.
		subject.setLockedSimSerialNumber("");

		// Assert
		Assert.isTrue(subject.isSimValid(),
				"sim number is not valid, when it is expected to be valid");

		// Act - Lock to sim. Current number is valid
		subject.lockSim(true);

		// Assert
		Assert.isTrue(subject.isSimValid(),
				"sim number is not valid, when it is expected to be valid");
		Assert.isTrue(subject.isSimLocked(), "After sim lock, sim is not locked");

		Assert.areEqual(resellerAgent.getResellerMessage(), "Buy from Me!",
				"After lock, Reseller msg lost");

		// Act. Change the saved number (because we can't change the actual 
		// number on the test phone.
		// Afterwards, the same SIM, which was valid, should be invalid
		
		
		subject.setLockedSimSerialNumber("09876543210987654321");

		// Assert
		Assert.isFalse(subject.isSimValid(),
				"sim number is not valid, when it is expected to be valid");

		// Act. Unlock the SIM
		subject.lockSim(false);
		
		// Attempt to set reseller message

		// Act
		resellerAgent.handleResellerMsgCommand("@Come back to Me!", resellerPhoneNumber);
		
		Assert.areEqual(resellerAgent.getResellerMessage(), "",
				"After unlock, reseller is still able to set message");
		

		// Assert
		// SIM is now valid, Phone is unlocked
		Assert.isTrue(subject.isSimValid(),
				"sim number is not valid, when it is expected to be valid");
		Assert.isFalse(subject.isSimLocked(), "After sim unlock, sim is not unlocked");

		Assert.areEqual(resellerAgent.getResellerMessage(), "",
				"After unlock, Reseller msg not removed");

		// Act. Lock the SIM
		subject.lockSim(true);

		// Assert
		// SIM is now valid, Phone is locked
		Assert.isTrue(subject.isSimValid(),
				"sim number is not valid, when it is expected to be valid");
		Assert.isTrue(subject.isSimLocked(), "After sim lock, sim is unlocked");

		// Set reseller message

		// Act
		resellerAgent.handleResellerMsgCommand("@Come back to Me!", resellerPhoneNumber);
		
		Assert.areEqual(resellerAgent.getResellerMessage(), "Come back to Me!",
				"After lock, reseller cannot set message");
		

	}

	@Override
	protected void tearDown() throws Exception {
		Context context = getContext();
		
		ActivationSettings activationSettings = new ActivationSettings(context);
		activationSettings.lockSim(false);
	}

}
