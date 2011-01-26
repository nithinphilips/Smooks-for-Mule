/**
 * Copyright (C) 2008 Maurice Zeijen <maurice@zeijen.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.milyn.smooks.mule;

import org.milyn.smooks.mule.SmooksRouter;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.tck.AbstractMuleTestCase;

/**
 *
 * @author <a href="mailto:maurice@zeijen.net">Maurice Zeijen</a>
 *
 */
public class RouterTest extends AbstractMuleTestCase {

	public void testInitWithoutSmooksConfigFile() throws InitialisationException
	{
		boolean thrown = false;
		try {
			SmooksRouter router = new SmooksRouter();
			router.setConfigFile( null );
			router.initialise();
		} catch (InitialisationException e) {
			thrown = true;
		}
		assertTrue("expected InitialisationException to be thrown", thrown);
	}

}
