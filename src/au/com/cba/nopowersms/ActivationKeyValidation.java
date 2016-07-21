package au.com.cba.nopowersms;

import au.com.cba.nopowersms.productactivation.ActivationKey;
import junit.framework.TestCase;

public class ActivationKeyValidation extends TestCase {

	public final void testValidCharsAreAccepted() {
		String[] validCodes = {
		
		"DFG422",
		"HKM422",
		"PQR422",
		"TVY422",
		"GFD422",
		"MKH422",
		"RPQ422",
		"YVT422"		};
		for (String validCode : validCodes) {
			if ((ActivationKey.validate(validCode) == null) ||
					!(ActivationKey.validate(validCode)).getKey().equals(validCode)) {
				fail("valid code failed validation: " + validCode);
			}
		}
	}
	public final void testInvalidLengthIsRejected() {
		String[] invalidCodes = {
		
		"DF422",
		"HKM4222",
		"DFGDFG",
		"844844"// No numbers
		};
		for (String invalidCode : invalidCodes) {
			Assert.isNull(ActivationKey.validate(invalidCode),
								"Invalid code length fails validation: " + invalidCode);
			}
	}
	
	public final void testInvalidCharIsRejected() {
		String[] invalidCodes = {
		
				"AFG422",  // INvalid first char
				"BFG422",
				"CFG422",
				"IFG422",
				"JFG422",
				"LFG422",
				"NFG422",
				"OFG422",
				"SFG422",
				"UFG422",
				"WFG422",
				"XFG422",
				"ZFG422",
				"DAG422", // Invalid 2nd char
				"DZG422",
				"DFC422", // Invalid 3rd char
				"DFO422",
				"DDG422", // First char repeated.
				"DFF422", // 2nd char repested
	
		};
		for (String invalidCode : invalidCodes) {
			Assert.isNull(ActivationKey.validate(invalidCode),
					"Invalid chars fail validation: " + invalidCode);
			}
		}

	
	public final void testValidDigitsAreAccepted() {
		String[] validCodes = {
		
		"DFG422",
		"DFG442",
		"DFG452",
		"DFG462",
		"DFG482",
		"DFG522",
		"DFG524",
		"DFG544",
		"DFG552",
		"DFG554",
		"DFG562",
		"DFG564",
		"DFG622",
		"DFG624",
		"DFG625",
		"DFG642",
		"DFG644",
		"DFG645",
		"DFG652",
		"DFG654",
		"DFG655",
		"DFG662",
		"DFG664",
		"DFG665",
		"DFG822",
		"DFG824",
		"DFG825",
		"DFG826",
		"DFG842",
		"DFG844",
		"DFG845",
		"DFG846",
		"DFG852",
		"DFG854",
		"DFG855",
		"DFG856",
		"DFG862",
		"DFG864",
		"DFG865",
		"DFG866",
		"DFG882",
		"DFG884",
		"DFG885",
		"DFG886"};

		
		
		for (String validCode : validCodes) {
			if ((ActivationKey.validate(validCode) == null) ||
					!(ActivationKey.validate(validCode)).getKey().equals(validCode)) {
				fail("valid codes pass activation: " + validCode);
			}
		}
	}

	public final void testInvalidCodesAreRejected() {
		String[] invalidCodes = {
				"DFG222",  // Valid numbers. last number not less than.
				"DFG888",
				"DFG525",
				"DFG528",
				"DFG888",
				"DFG122",  // INvalid first number
				"DFG322",
				"DFG322",
				"DFG722",
				"DFG922",
				"DFG812", // INvalid second number
				"DFG832",
				"DFG872",
				"DFG892",
				"DFG821", // Invalid third number
				"DFG823",
				"DFG823",
				"DFG823",
				"DFG823",
				"DFG453", // One from Sargent
				"HKG577" // Problem report. Sargent. March 2015
			};
		
		for (String invalidCode : invalidCodes) {
			Assert.isNull(ActivationKey.validate(invalidCode),
					"Invalid number fails validation: " + invalidCode);
		}
	}

}
