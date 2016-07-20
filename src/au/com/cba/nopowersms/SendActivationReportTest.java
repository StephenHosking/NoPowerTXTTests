package au.com.cba.nopowersms;

import java.util.Calendar;

import android.content.Context;
import android.test.AndroidTestCase;
import au.com.cba.nopowersms.productactivation.*;
import au.com.cba.nopowersms.base.Utils;
import au.com.cba.nopowersms.services.PowerStateReceiver;
import junit.framework.TestCase;

public class SendActivationReportTest extends AndroidTestCase {

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
		ActivationSettings activationSettings = new ActivationSettings(context);
		SendActivationReport report = new SendActivationReport(context);

		// Act
		String serialNumberNotSet = activationSettings.getLockedSimSerialNumber();

		// Act
		{
			activationSettings.setActivationKey("DFG422");
			activationSettings.setLockedSimSerialNumber("12345678901234567890");
			activationSettings.setIsActivated(true);
			report.setSavedAlarmTime(450);

			if (!(activationSettings.getActivationKey().equals("DFG422"))
					&& (activationSettings.getLockedSimSerialNumber()
							.equals("12345678901234567890"))
					&& (activationSettings.getIsActivated() == true)
					&& (report.getSavedAlarmTime() == 450)) {
				fail("Getter/Setter fail");
			}
		}

	}

	public final void testSimCardLocking() {
		// Assemble
		Context context = getContext();
		ActivationSettings subject = new ActivationSettings(context);

		// Assert. SIM is present
		Assert.isTrue(subject.isSimPresent(),
				"SIM not present reported when it is present");

		// Act - Lock to sim. Current number is valid
		subject.lockToSim();

		// Assert
		Assert.isTrue(subject.isSimValid(),
				"sim number is not valid, when it is expected to be valid");

		// Act. Change the saved number (because we can't change the actual
		// number on
		// the test phone.
		subject.setTestLockedSimNumber("09876543210987654321");

		// Assert
		Assert.isFalse(subject.isSimValid(),
				"sim number is not valid, when it is expected to be valid");

		// Act. Unlock the SIM
		subject.unlockSim();

		// Assert
		Assert.isTrue(subject.isSimValid(),
				"sim number is not valid, when it is expected to be valid");

		// Act. Set null SIM number.
		subject.setTestLockedSimNumber(null);

		// Assert
		Assert.isTrue(subject.isSimValid(),
				"sim number is not valid, when it is expected to be valid");

	}

	@Override
	protected void tearDown() throws Exception {
		Context context = getContext();
		SendActivationReport subject = SendActivationReport
				.getInstance(context);
		
		ActivationSettings activationSettings = new ActivationSettings(context);
		activationSettings.unlockSim();
	}

}
