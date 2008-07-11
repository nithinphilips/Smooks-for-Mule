package org.milyn.smooks.mule.component;

import java.util.Date;
import java.util.Map;

import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;

public class TestReplyComponent implements Callable {


	@SuppressWarnings("unchecked")
	public Object onCall(MuleEventContext eventContext) throws Exception {
		MuleMessage message = eventContext.getMessage();
		Map<String, Object> payload = (Map<String, Object>) message.getPayload();

		String test = (String) message.getProperty("test");
		String test2 = (String) message.getProperty("test2");
		Integer testInt = (Integer) message.getProperty("testInt");
		Date testDate = (Date) message.getProperty("testDate");
		String xmlTest1 = (String) message.getProperty("xmlTest1");
		String xmlTest2 = (String) message.getProperty("xmlTest2");
		String testOverwritten = (String) message.getProperty("testOverwritten");

		return payload.get("value").toString() + "," +
				test  + "," +
				test2 + "," +
				testInt + "," +
				testDate.getTime() + "," +
				xmlTest1 + "," +
				xmlTest2 + "," +
				testOverwritten;
	}

}
