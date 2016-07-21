package au.com.cba.nopowersms;

import java.util.Calendar;

import au.com.cba.nopowersms.productactivation.*;
import au.com.cba.nopowersms.base.Utils;
import junit.framework.TestCase;

public class UtilsTest extends TestCase {

	public final void testSmsTruncation() {
		String longMessage = 
		"01234567891123456789212345678931234567894123456789L123467896123456789712345678981234567899123456789C1234567891123456789212345678931234567894123456789L1234567896123456789";
		String shortMessage = Utils.truncatedToMaximumSms(longMessage);
		Assert.areEqual(shortMessage.length(), 153, "Message is truncated");
	}

	public final void testPhoneNumberComparison() {
		String baseNumber9 = "+1404555817"; // Has nine sig digits (current standard i'ntl)
		String equal9 = "0404555817";
		String equal8 = "404555817";
		String diff10 = "5404555817";
		String diff9 = "504555817";
		String diff8 = "74555817";
		String diff7 = "6555817";
		String same6 = "555817";
		String same5 = "55817";
		String same3 = "817";
		String same1 = "7";
		
		boolean actual = Utils.comparePhoneNumbers(baseNumber9,baseNumber9);
		
		Assert.areEqual(actual,true,"Identical numbers return IS match");
		
		actual = Utils.comparePhoneNumbers(baseNumber9, equal9);
	
		Assert.areEqual(actual,true,"Similar numbers return IS match");

		actual = Utils.comparePhoneNumbers(baseNumber9, equal8);
		
		Assert.areEqual(actual,true,"Similar numbers return IS match");
		
		actual = Utils.comparePhoneNumbers(baseNumber9, diff10);
		
		Assert.areEqual(actual,true,"Diff on pos 10 numbers return IS match");
		
		actual = Utils.comparePhoneNumbers(baseNumber9, diff9);
		
		Assert.areEqual(actual,false,"Diff on pos 9 numbers return NOT match");

		actual = Utils.comparePhoneNumbers(baseNumber9, diff8);
		
		Assert.areEqual(actual,false,"Diff on pos 8 numbers return NOT match");
		
		actual = Utils.comparePhoneNumbers(baseNumber9, diff7);
		
		Assert.areEqual(actual,false,"Diff on pos 7 Different numbers return NOT match");

		actual = Utils.comparePhoneNumbers(baseNumber9, same6);
		
		Assert.areEqual(actual,true,"Short numbers, same 6, return IS match");

		actual = Utils.comparePhoneNumbers(baseNumber9, same5);
		
		Assert.areEqual(actual,true,"Short numbers, same 5, return IS match");
		
		actual = Utils.comparePhoneNumbers(baseNumber9, same3);
		
		Assert.areEqual(actual,true,"Short numbers, same 3, return IS match");
		
		actual = Utils.comparePhoneNumbers(baseNumber9, same1);
		
		Assert.areEqual(actual,false,"Short numbers, same 1, return IS match");
		// 
	}
	
	public final void testDayNextDayOfWeek() {
		
		final int SUNDAY = Calendar.SUNDAY;
		final int MONDAY = Calendar.MONDAY;
		final int TUESDAY = Calendar.TUESDAY;
		final int WEDNESDAY = Calendar.WEDNESDAY;
		final int SATURDAY = Calendar.SATURDAY;
		
		Calendar testDay = Calendar.getInstance();
		testDay.set(2014, Calendar.OCTOBER, 12);
				
		// Assert this is a Sunday
		Assert.areEqual(testDay.get(Calendar.DAY_OF_WEEK), Calendar.SUNDAY, "Based day is a Sunday");
		
		// On Sunday,
		// Set Sunday, expect +7 days
		// Set Monday, expect +1
		// Set Saturday, expect +6
		int actual = Utils.daysToNextDayOfWeek(testDay, SUNDAY);
		int expected = 7;
		Assert.areEqual(actual,expected,"");
		
		actual = Utils.daysToNextDayOfWeek(testDay, MONDAY);
		expected = 1;
		Assert.areEqual(actual,expected,"");
		
		actual = Utils.daysToNextDayOfWeek(testDay, SATURDAY);
		expected = 6;
		Assert.areEqual(actual,expected,"");
		
		
		// On Wednesday, 
		// Set Sunday, expect + 4
		// Set Tue, expect    +6
		// Set Wed, expect    +7
		// Set Sat, expect    +3
		testDay.set(2014, Calendar.OCTOBER, 15);

		Assert.areEqual(testDay.get(Calendar.DAY_OF_WEEK), Calendar.WEDNESDAY, "Based day is a wednesday");

		actual = Utils.daysToNextDayOfWeek(testDay, SUNDAY);
		expected = 4;
		Assert.areEqual(actual,expected,"");
		
		actual = Utils.daysToNextDayOfWeek(testDay, TUESDAY);
		expected = 6;
		Assert.areEqual(actual,expected,"");
		
		actual = Utils.daysToNextDayOfWeek(testDay, WEDNESDAY);
		expected = 7;
		Assert.areEqual(actual,expected,"");
		
		actual = Utils.daysToNextDayOfWeek(testDay, SATURDAY);
		expected = 3;
		Assert.areEqual(actual,expected,"");
		
		// On Saturday, 
		// Set Sundary, expect + 1
		// Set Tue, expect    + 3
		// Set Saturday, expect + 7
		testDay.set(2014, Calendar.OCTOBER, 18);

		Assert.areEqual(testDay.get(Calendar.DAY_OF_WEEK), Calendar.SATURDAY, "Based day is a wednesday");

		actual = Utils.daysToNextDayOfWeek(testDay, SUNDAY);
		expected = 1;
		Assert.areEqual(actual,expected,"");
		
		actual = Utils.daysToNextDayOfWeek(testDay, TUESDAY);
		expected = 3;
		Assert.areEqual(actual,expected,"");
		
		actual = Utils.daysToNextDayOfWeek(testDay, SATURDAY);
		expected = 7;
		Assert.areEqual(actual,expected,"");
		
	}

}
