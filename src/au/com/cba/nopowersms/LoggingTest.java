package au.com.cba.nopowersms;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;

import javax.security.auth.login.LoginException;

import android.test.AndroidTestCase;
import android.util.Log;
import au.com.cba.nopowersms.database.LogConstants;
import au.com.cba.nopowersms.database.LogReader;
import au.com.cba.nopowersms.database.LogWriter;
import au.com.cba.nopowersms.database.Logger;
import au.com.cba.nopowersms.database.LogConstants.LogEvent;
import au.com.cba.nopowersms.database.LogReader.LogItem;
import au.com.cba.nopowersms.services.PowerStateReceiver;
import au.com.cba.nopowersms.services.PowerStateService;
import au.com.cba.nopowersms.services.PowerStateService.PowerEvent;
import junit.framework.TestCase;

public class LoggingTest extends AndroidTestCase {

	final static String TAG = "LoggingTest";

	public final void testPowerEventLogging() throws Exception {

		// Read from empty log file.

		try {

			// ARRANGE
			LogWriter writer = new LogWriter(this.getContext());
			LogReader reader = new LogReader(this.getContext());
			writer.clearLogFile();

			// ACT
			reader.openInputStream();
			LogItem item = reader.readItem();
			// ASSERT
		} catch (EOFException e) {
			// All good!
			Log.v("LoggingTest.testPowerEventLogging",
					"Passed: Read empty log file");
		} catch (FileNotFoundException e) {
			fail("When open empty log expect EOF exception. got: " + e);
		} catch (IOException e) {
			fail("When open empty log expect EOF exception. got: " + e);
		} catch (Exception e) {
			fail("When open empty log expect EOF exception. got: " + e);
		}

		// Write and Read an Item

		// ARRANGE

		LogWriter writer = new LogWriter(this.getContext());
		LogReader reader = new LogReader(this.getContext());

		// ACT

		final int POWER_EVENT = LogEvent.POWER_EVENT.ordinal();
		final int SMS_SENT_EVENT = LogEvent.SMS_SUBMITTED_EVENT.ordinal();
		final int POWER_OFF_DETAIL = PowerStateService.PowerEvent.POWER_OFF
				.ordinal();
		final int POWER_ON_DETAIL = PowerStateService.PowerEvent.POWER_ON
				.ordinal();
		final String POWER_DISCONNECTED_MESSAGE = "0404555817,0447634037";

		writer.writeEvent(LogEvent.POWER_EVENT, POWER_OFF_DETAIL);

		reader.openInputStream();
		LogItem readItem = reader.readItem();

		// ASSERT
		Assert.areEqual(readItem.Event.ordinal(), POWER_EVENT,
				"Write and Read One PowerEvent to log. event is correct");
		Assert.areEqual(readItem.Detail, POWER_OFF_DETAIL,
				"Write and Read One PowerEvent to log. detail is correct");
		Assert.areEqual(readItem.Text, "",
				"Write and Read One PowerEvent to log. text is correct");

		Log.v("LoggingTest.testPowerEventLogging",
				"Passed: Read and write an Item");

		reader.closeInputStream();

		// ACT

		// Write and Read another 2 Items. A Power On event, and an SMS

		writer.writeEvent(LogEvent.POWER_EVENT, POWER_ON_DETAIL);
		writer.writeEvent(LogEvent.SMS_SUBMITTED_EVENT,
				LogConstants.SMS_DETAIL_POWER_DISCONNECTED,
				POWER_DISCONNECTED_MESSAGE);

		try {

			reader.openInputStream();
			LogItem readItem1 = reader.readItem();
			LogItem readItem2 = reader.readItem();
			LogItem readItem3 = reader.readItem();

			// ASSERT

			Assert.areEqual(readItem1.Event.ordinal(), POWER_EVENT,
					"First PowerEvent in log. event is correct");
			Assert.areEqual(readItem1.Detail, POWER_OFF_DETAIL,
					"First PowerEvent in log. detail is correct");
			Assert.areEqual(readItem1.Text, "",
					"First PowerEvent in log. text is correct");

			Assert.areEqual(readItem2.Event.ordinal(), POWER_EVENT,
					"2nd PowerEvent in log. event is correct");
			Assert.areEqual(readItem2.Detail, POWER_ON_DETAIL,
					"2nd PowerEvent in log. detail is correct");
			Assert.areEqual(readItem2.Text, "",
					"2nd PowerEvent in log. text is correct");

			Assert.areEqual(readItem3.Event.ordinal(), SMS_SENT_EVENT,
					"3rd PowerEvent in log. event is correct");
			Assert.areEqual(readItem3.Detail, 1,
					"3rd PowerEvent in log. detail is correct");
			Assert.isNotNull(readItem3.Text,
					"3rd PowerEvent in log. text is correct");

			// ACT. Read next item
			try {
				readItem3 = reader.readItem();

				// ASSERT
				fail("Get null item when read past end of file");
			} catch (EOFException e) {
				// All good!
			}

			reader.closeInputStream();

		} catch (StreamCorruptedException e) {
			fail(e.toString());
		} catch (IOException e) {
			fail(e.toString());
		} catch (Exception e) {
			fail(e.toString());
		}

	}

	@SuppressWarnings("unused")
	public final void testZPadTheLog() {

		LogWriter writer = new LogWriter(this.getContext());

		writer.clearLogFile();

		int eventIndex = 1;

		if (1 == 0) {
			// Test ACRA reporting

			String.format("%d", "hello");
		}

		if (1 == 1) {
			// Pad with 1000 items

			long startTime = System.nanoTime();

			writer.writeEvent(LogEvent.POWER_EVENT, 0);
			writer.writeEvent(LogEvent.SMS_SUBMITTED_EVENT,
					LogConstants.SMS_DETAIL_POWER_DISCONNECTED,
					"1:0404555817,0447634037");
			writer.writeEvent(LogEvent.POWER_EVENT, 1);
			Log.v(TAG,
					String.format("time to log 3 event: %d ms",
							(System.nanoTime() - startTime) / 1000000));

			writer.clearLogFile();

			startTime = System.nanoTime();
			for (int i = 0; i < 333; i++) {
				writer.writeEvent(LogEvent.POWER_EVENT, 0);
				eventIndex++;
				writer.writeEvent(LogEvent.SMS_SUBMITTED_EVENT,
						LogConstants.SMS_DETAIL_POWER_DISCONNECTED, eventIndex
								+ ":0404555817,0447634037 ms");
				eventIndex++;
				writer.writeEvent(LogEvent.POWER_EVENT, 1);
				eventIndex++;
			}
			Log.v(TAG,
					String.format("time to log 1000 events: %d",
							(System.nanoTime() - startTime) / 1000000));

		} else {
			writer.writeEvent(LogEvent.POWER_EVENT, 0);
			writer.writeEvent(LogEvent.SMS_SUBMITTED_EVENT,
					LogConstants.SMS_DETAIL_POWER_DISCONNECTED,
					"1:0404555817,0447634037");
			writer.writeEvent(LogEvent.POWER_EVENT, 1);
		}
	}

}
