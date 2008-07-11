package org.milyn.smooks.mule;


import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.milyn.io.StreamUtils;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.transformer.TransformerException;
import org.mule.tck.AbstractMuleTestCase;


/**
 * Unit test for {@link Transformer}
 * <p/>
 * The test in the class intentionally only test the configuration and <br>
 * execution of {@link Transformer} and not the actual tranformations<br>
 * that Smooks performs as these are covered in the Smooks project.
 *
 * @author <a href="mailto:maurice@zeijen.net">Maurice Zeijen</a>
 *
 */
public class TransformerTest extends AbstractMuleTestCase
{
	private Transformer transformer;

	private final String smooksConfigFile = "/transformer-smooks-config.xml";

	public void testInitWithoutSmooksConfigFile() throws InitialisationException
	{
		boolean thrown = false;
		try {
			transformer.setConfigFile( null );
			transformer.initialise();
		} catch (InitialisationException e) {
			thrown = true;
		}
		assertTrue("expected InitialisationException to be thrown", thrown);
	}

	public void testIllegalResultType()
	{
		boolean thrown = false;
		try {
			transformer.setConfigFile( smooksConfigFile );
			transformer.setResultType( "badResultType" );
			transformer.initialise();
		} catch (InitialisationException e) {
			thrown = true;
		}
		assertTrue("expected InitialisationException to be thrown", thrown);
	}

	public void testJavaResultBeanId()
	{
		transformer.setConfigFile( smooksConfigFile );
		transformer.setResultType( "JAVA" );
		transformer.setJavaResultBeanId( "beanId" );
		try
		{
			transformer.initialise();
		}
		catch (InitialisationException e)
		{
			fail( "Should not have thrown A InitializationException");
		}
	}

	public void testDoTransformationExecContextAttrNotDefinend() throws Exception {
		testDoTransformation(null, null);
	}

	public void testDoTransformationWithoutExecContextAttr() throws Exception {
		testDoTransformation(false, null);
	}

	public void testDoTransformationWithExecContextAttr() throws Exception {
		testDoTransformation(true, null);
	}

	public void testDoTransformationWithExecContextAttrWithOwnAttrKey() throws Exception {
		testDoTransformation(true, "executionContextSmooks");
	}

	public void testDoTransformation(Boolean setExecuctionContextAsMessageKey, String executionContextMessagePropertyKey) throws TransformerException
	{

		transformer.setConfigFile( smooksConfigFile );
		transformer.setExcludeNonSerializables( false );
		if(setExecuctionContextAsMessageKey != null) {
			transformer.setExecutionContextAsMessageProperty(setExecuctionContextAsMessageKey);
		}
		if(executionContextMessagePropertyKey != null) {
			transformer.setExecutionContextMessagePropertyKey(executionContextMessagePropertyKey);
		} else {
			executionContextMessagePropertyKey = Transformer.MESSAGE_PROPERTY_KEY_EXECUTION_CONTEXT;
		}

		byte[] inputMessage = readInputMessage();

		//Is this correct?
		MuleMessage message = new DefaultMuleMessage(inputMessage);
		Object transformedObject = transformer.transform( message );
		assertNotNull ( transformedObject );

		Object attributes = message.getProperty( executionContextMessagePropertyKey );

		if(setExecuctionContextAsMessageKey != null && setExecuctionContextAsMessageKey) {
			assertNotNull( attributes );
		} else {
			assertNull( attributes );
		}
	}

	public void testDoTransformationWithSmooksReportGeneration() throws InitialisationException, TransformerException
	{
		File reportFile = new File ( "target" + File.separator + "smooks-report.html" );
		transformer.setConfigFile( smooksConfigFile );
		transformer.setReportPath( reportFile.getAbsolutePath() );
		transformer.initialise();
		byte[] inputMessage = readInputMessage();
		try
		{
    		Object transformedObject = transformer.transform( inputMessage );
    		assertNotNull ( transformedObject );
			assertTrue( reportFile.exists() );
		}
		finally
		{
			if ( reportFile.exists() )
			{
				reportFile.delete();
			}
		}
	}

	@Override
	@Before
	public void doSetUp() throws Exception
	{
    	transformer = new Transformer();
		transformer.setConfigFile( smooksConfigFile );
		transformer.initialise();
	}

	//	private

	private static byte[] readInputMessage()
	{
        try
        {
            return StreamUtils.readStream( TransformerTest.class.getResourceAsStream( "/transformer-input-message.xml"));
        }
        catch (IOException e)
        {
        	e.printStackTrace();
            return "<no-message/>".getBytes();
        }
    }
}
