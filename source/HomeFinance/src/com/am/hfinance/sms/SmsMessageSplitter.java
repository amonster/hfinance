package com.am.hfinance.sms;

import java.util.ArrayList;
import java.util.List;

import android.telephony.SmsMessage;

public class SmsMessageSplitter {
	private static final int MULTIPART_MESSAGE_LIMIT = 3;

	public List<String> split(List<String> source) {
		ArrayList<String> result = new ArrayList<String>();
		if(source == null || source.size() == 0) {
			return result;
		}
		
		StringBuilder smsBuilder = new StringBuilder();
		for (String item : source) {

			// Item does not fit into 3 messages.
			if(calculateLength(item).msgCount > MULTIPART_MESSAGE_LIMIT) {
				// TODO: Throw exception.
				// throw new Exception("Size of the element exceeds size of the message");
				break;
			}

			int initialSmsLength = smsBuilder.length();

			if(initialSmsLength != 0) {
				smsBuilder.append('*');
			}
			
			smsBuilder.append(item);

			TextEncodingDetails ted = calculateLength(smsBuilder.toString());
			
			if(ted.msgCount > 3 && initialSmsLength > 0) {
				result.add(smsBuilder.substring(0, initialSmsLength));
				smsBuilder.delete(0, initialSmsLength + 1);
			}
		}
		
		// Add last part of the message.
		result.add(smsBuilder.toString());
		
		return result;
	}

	public TextEncodingDetails calculateLength(String sms) {
		return new TextEncodingDetails(SmsMessage.calculateLength(sms, false));
	}

	public class TextEncodingDetails {
		public int msgCount;
		public int codeUnitCount;
		public int codeUnitsRemaining;
		public int codeUnitSize;

		public TextEncodingDetails(int[] arr) {
			msgCount = arr[0];
			codeUnitCount = arr[1];
			codeUnitsRemaining = arr[2];
			codeUnitSize = arr[3];
		}
	}
}
