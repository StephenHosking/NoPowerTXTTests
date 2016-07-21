package au.com.cba.nopowersms;

import android.content.Context;
import android.test.AndroidTestCase;
import au.com.cba.nopowersms.database.ContactsAdapter;
import au.com.cba.nopowersms.database.NotificationAgent;

public class NotificationAgentTest extends AndroidTestCase {

	String[] sampleNumbers = new String[] { "0404555817", "0423333333",
			"+61404555817" };
	private static final String NOT_A_NOTIFIER = "0404555555";

	public final void testListNotifications() {

		// Arrange
		// get ContactsAdapter to interact with the SQLite database
		ContactsAdapter adapter = new ContactsAdapter(this.getContext());
		adapter.open();

		adapter.purge();

		NotificationAgent subject = new NotificationAgent(this.getContext(),
				adapter);

		// Assert
		Assert.areEqual(subject.getAllContactsStatus(), "",
				"When no contacts, empty string is returned");

		// Arrange
		boolean success = subject.setNotifying("11111", true);
		Assert.areEqual(success, false,
				"When no contacts, setNotifying returns false");

		// Arrange.
		adapter.insertContact("Name1", "11111", true);
		String actualList = subject.getAllContactsStatus();

		// Assert
		Assert.areEqual(actualList, "11111+",
				"List for one contacts, is notifying.");

		// Arrange
		success = subject.setNotifying("11111", false);
		actualList = subject.getAllContactsStatus();

		// Assert
		Assert.areEqual(success, true,
				"When one contact, setNotifying returns true");
		Assert.areEqual(actualList, "11111-",
				"One contacts, set not notifying.");

		// Arrange. Add second contacts
		success = subject.setNotifying("11111", true); // restore
		adapter.insertContact("Name2", "22222", true);

		// Assert.
		actualList = subject.getAllContactsStatus();
		Assert.areEqual(actualList, "11111+,22222+",
				"List for two contacts, both notifying.");

		// Act. Stop first contact
		subject.setNotifying("11111", false);

		// Assert.
		actualList = subject.getAllContactsStatus();
		Assert.areEqual(actualList, "11111-,22222+",
				"Stop notifying first contact");

		// Act. Stop second contact
		subject.setNotifying("22222", false);

		// Assert.
		actualList = subject.getAllContactsStatus();
		Assert.areEqual(actualList, "11111-,22222-",
				"Stop notifying second contact.");

		// Act.
		subject.setNotifying("22222", true);

		// Assert.
		actualList = subject.getAllContactsStatus();
		Assert.areEqual(actualList, "11111-,22222+",
				"Restart notifying second contact.");

		// Act. Add a contact
		subject.addNotifying("33333");

		// Assert.
		actualList = subject.getAllContactsStatus();
		Assert.areEqual(actualList, "11111-,22222+,33333+", "Contact is added");

		// Act.
		subject.setNotifying("33333", false);

		// Assert.
		actualList = subject.getAllContactsStatus();
		Assert.areEqual(actualList, "11111-,22222+,33333-",
				"New contact is deactivated");

		// Act. Use different number formats
		subject.addNotifying("+61404555817");
		subject.addNotifying("0404555817");

		// Assert.
		actualList = subject.getAllContactsStatus();
		Assert.areEqual(actualList,
				"11111-,22222+,33333-,+61404555817+,0404555817+",
				"Contact is added");

		adapter.close();

	}

	public final void testCommandParsing() {

		NotificationAgent subject = new NotificationAgent(this.getContext(),
				null);

		Context context = this.getContext();

		Assert.isFalse(NotificationAgent.isNotifyingCommandList(context, null),
				"Null string commmand list");

		Assert.isFalse(NotificationAgent.isNotifyingCommandList(context, ""),
				"Empty string commmand list");

		Assert.isFalse(NotificationAgent.isNotifyingCommandList(context, "44"),
				"Short string commmand list");

		Assert.isFalse(
				NotificationAgent.isNotifyingCommandList(context, "44448889"),
				"No commmanNotificationAgent.t");

		Assert.isTrue(NotificationAgent.isNotifyingCommandList(context,
				"1234567890+"), "One notifying on commmand.");

		Assert.isTrue(NotificationAgent.isNotifyingCommandList(context,
				"1234567890-"), "One notifying off command");

		Assert.isTrue(NotificationAgent.isNotifyingCommandList(context,
				"1234567890++"), "One notifying add command");

		Assert.isTrue(NotificationAgent.isNotifyingCommandList(context,
				"+1234567890++"), "international number");

		Assert.isFalse(NotificationAgent.isNotifyingCommandList(context,
				"1234ab67890+"), "One jumbled on command");

		Assert.isTrue(NotificationAgent.isNotifyingCommandList(context,
				"1234567890+,1234567890++,1234567890-"), "Three in command");

		Assert.isFalse(NotificationAgent.isNotifyingCommandList(context,
				"1234567890+,1234567890,1234567890-"),
				"Three in command, one invalid");

	}

	public final void testNumberValidation() {
		// if (false ) {
		//
		// this.Populate(2);
		//
		// NotificationAgent subject = new NotificationAgent(this.getContext(),
		// null);
		//
		// Assert.areEqual(subject.getUnknownNumber(null),"",
		// "Null string number list");
		//
		// Assert.areEqual(subject.getUnknownNumber(""),"",
		// "Empty string commmand list");
		//
		// // Known numbers. One in numbers.
		// Assert.areEqual(subject.getUnknownNumber("123456000+"),"",
		// "One known notifying on commmand.");
		//
		// Assert.areEqual(subject.getUnknownNumber("123456000-"),"",
		// "One known notifying off command");
		//
		// Assert.areEqual(subject.getUnknownNumber("123456000++"),"",
		// "One known notifying add command");
		//
		// Assert.areEqual(subject.getUnknownNumber("+123456000++"),"",
		// "One known international number");
		//
		// // Unknown numbers. One in numbers.
		// Assert.areEqual(subject.getUnknownNumber("123456999+"),"123456999",
		// "One unknown notifying on commmand.");
		//
		// Assert.areEqual(subject.getUnknownNumber("123456999-"),"123456999",
		// "One unknown notifying off command");
		//
		// Assert.areEqual(subject.getUnknownNumber("123456999++"),"123456999",
		// "One unknown notifying add command");
		//
		// Assert.areEqual(subject.getUnknownNumber("+123456999++"),"+123456999",
		// "One unknown international number");
		//
		// // Multiple numbers.
		//
		// Assert.areEqual(subject
		// .getUnknownNumber("123456789+,123456789++,123456789-"),"",
		// "Three in command");
		//
		// Assert.areEqual(subject
		// .getUnknownNumber("123456789+,123456789,123456789-"),"",
		// "Three in command, one invalid");
		// }

	}

	public final void testHandlingOfNotifiers() {

		ContactsAdapter adapter = new ContactsAdapter(this.getContext());
		adapter.open();

		adapter.purge();

		NotificationAgent subject = new NotificationAgent(this.getContext(),
				adapter);

		// Arrange
		adapter.insertContact("Self", "0404555817", true);

		// Act
		subject.handleNotificationsMessage("0404555817-");

		// Assert
		Assert.areEqual(subject.getAllContactsStatus(), "0404555817-",
				"Valid command is executed. One contact.");

		// // Arrange
		Populate(3);
		String asPopulated = String.format("%s+,%s+,%s+", sampleNumbers[0],
				sampleNumbers[1], sampleNumbers[2]);
		Assert.areEqual(subject.getAllContactsStatus(), asPopulated,
				"Precondition for extended noti test");

		// Act
		String msg = String.format("%s-,%s+,%s-,%s++", sampleNumbers[0],
				sampleNumbers[1], sampleNumbers[2], "0404555555");
		subject.handleNotificationsMessage(msg);

		// Assert
		String expected = String.format("%s-,%s+,%s-,%s+", sampleNumbers[0],
				sampleNumbers[1], sampleNumbers[2], "0404555555");
		Assert.areEqual(subject.getAllContactsStatus(), expected,
				"Valid command is executed. Multiple contacts.");

		adapter.close();
	}

	public final void testPowerStateService() {

		// // Doesn't work, because PowerStateService is created without our App
		// Context.
		// // Need mocking.
		// // Use ServiceTestCase. See
		// http://developer.android.com/tools/testing/service_testing.html
		// PowerStateService service = new PowerStateService();
		//
		// Intent intent = new Intent();
		//
		// service.onHandleIntent(intent);
	}

	protected void tearDown() throws Exception {
		ContactsAdapter adapter = new ContactsAdapter(this.getContext());
		adapter.open();

		adapter.purge();
		adapter.insertContact("Self", "0404555817", true);
		adapter.close();

	}

	public final void testGetSelectedPhoneNumbers() {

		// Arrange
		// get ContactsAdapter to interact with the SQLite database
		ContactsAdapter adapter = new ContactsAdapter(this.getContext());
		adapter.open();

		adapter.purge();

		NotificationAgent subject = new NotificationAgent(this.getContext(),
				adapter);
		
		Populate(3);
		
		// Remove selection from item 2
		subject.setNotifying(sampleNumbers[2], false);
		
		// Act
		String[] actual = subject.getSelectedPhoneNumbers();
		
		// Assert
		Assert.areEqual(actual.length, 2, "List of Notifications includes nofitying, and excludes not notifyint");
		Assert.areEqual(actual[0], sampleNumbers[0], "First notifier is correct");
		Assert.areEqual(actual[1], sampleNumbers[1], "Second notifier is correct");
		
		
	}

	public final void testIsNotifierPhoneNumber() {

		// Arrange
		// get ContactsAdapter to interact with the SQLite database
		ContactsAdapter adapter = new ContactsAdapter(this.getContext());
		adapter.open();

		adapter.purge();

		NotificationAgent subject = new NotificationAgent(this.getContext(),
				adapter);
		
		Populate(3);
		
		// Remove selection from item 2
		subject.setNotifying(sampleNumbers[2], false);
		
		// Act
		
		// Assert
		Assert.assertTrue("First subject is a notifier", subject.isNotifier(sampleNumbers[0]));
		Assert.assertTrue("Non-selected subject is a notifier", subject.isNotifier(sampleNumbers[2]));	
		Assert.assertFalse("test number is not a notifier", subject.isNotifier(NOT_A_NOTIFIER));	
		
		adapter.close();
	}
	
	protected void testGetSelectedNumbers() {

		// Arrange
		// get ContactsAdapter to interact with the SQLite database
		ContactsAdapter adapter = new ContactsAdapter(this.getContext());
		adapter.open();

		adapter.purge();

		NotificationAgent subject = new NotificationAgent(this.getContext(),
				adapter);
		
		Populate(3);
		
		// Assert
		String[] actual = subject.getSelectedPhoneNumbers();
		
		Assert.areEqual(actual.length, 3, "All subjects selected. Got right count");
		Assert.areEqual(actual[0], sampleNumbers[0], "First selected number correct");
		Assert.areEqual(actual[1], sampleNumbers[1], "2nd selected number correct");
		Assert.areEqual(actual[2], sampleNumbers[2], "3rd selected number correct");
		
		// Act. Remove one number remotely
		subject.handleNotificationsMessage(String.format("%s-",sampleNumbers[2]));
		
		// Assert
		actual = subject.getSelectedPhoneNumbers();
		
		Assert.areEqual(actual.length, 2, "One subjects de-selected. Got right count");
		Assert.areEqual(actual[0], sampleNumbers[0], "First selected number correct");
		Assert.areEqual(actual[1], sampleNumbers[1], "First selected number correct");
		
		// Act. Remove all numbers remotely
		subject.handleNotificationsMessage(String.format("%s-,%s+",sampleNumbers[1],sampleNumbers[0]));
		
		// Assert
		actual = subject.getSelectedPhoneNumbers();
		
		Assert.areEqual(actual.length, 0, "One subjects de-selected. Got right count");
		
		// Act. Restore one number remotely
		subject.handleNotificationsMessage(String.format("%s+",sampleNumbers[0]));
		
		// Assert
		actual = subject.getSelectedPhoneNumbers();
		
		Assert.areEqual(actual.length, 1, "One subjects re-selected. Got right count");
		Assert.areEqual(actual[0], sampleNumbers[0], "First selected number correct");
		
		// Act. Add one number remotely
		String newNoti = "01234568888";
		subject.handleNotificationsMessage(String.format("%s+",newNoti));
		
		// Assert
		actual = subject.getSelectedPhoneNumbers();
		
		Assert.areEqual(actual.length, 2, "One subjects re-selected. Got right count");
		Assert.areEqual(actual[0], sampleNumbers[0], "First selected number correct");
		Assert.areEqual(actual[1], sampleNumbers[1], "2nd selected number correct");

	}
	protected void Populate(int numberOfNames) {
		ContactsAdapter adapter = new ContactsAdapter(this.getContext());
		adapter.open();

		adapter.purge();
		for (int i = 0; i < numberOfNames; i++) {
			adapter.insertContact(String.format("Name%d", i),
			// Had problems with numbers begining with 0. Use them here
			// to ensure they are tested.
					sampleNumbers[i], true);
		}
		adapter.close();

	}

}
