/*
 * Copyright 2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package samples.methodhierarchy;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.powermock.PowerMock.createPartialMock;
import static org.powermock.PowerMock.expectPrivate;
import static org.powermock.PowerMock.replay;
import static org.powermock.PowerMock.verify;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.Whitebox;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Test that verifies that it's possible to invoke and create partial mocks of
 * private and/protected methods in a hierarchy.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest( { MethodInvocationDemo.class })
public class MethodInvocationDemoTest {

	@Test
	public void testCreatePartialMockForAProtectedMethodInASubclass()
			throws Exception {
		final String value = "another string";
		final String getTheStringMethodName = "getTheString";
		MethodInvocationDemo tested = createPartialMock(
				MethodInvocationDemo.class, getTheStringMethodName);

		expect(tested.getTheString()).andReturn(value);

		replay(tested);

		assertEquals(value, Whitebox.invokeMethod(tested, "getString"));

		verify(tested);
	}

	@Test
	@Ignore("Mabey this is impossible to achieve")
	public void testCreatePartialMockForAProtectedMethodInASpecificSubclass()
			throws Exception {
		final String value = "another string";
		final String getTheStringMethodName = "getTheString";
		MethodInvocationDemo tested = createPartialMock(
				MethodInvocationDemo.class,
				MethodInvocationDemoGrandChild.class, getTheStringMethodName);

		expectPrivate(tested, getTheStringMethodName,
				MethodInvocationDemoGrandChild.class).andReturn(value);

		replay(tested);

		assertEquals("MethodInvocationDemoChild wrapped " + value, tested
				.getTheString());

		verify(tested);
	}

	@Test
	public void testWhenClassUnderTestIsAnAnonymousInnerClass()
			throws Exception {
		MethodInvocationDemo tested = new MethodInvocationDemo() {

		};

		assertEquals(
				"MethodInvocationDemoChild wrapped a string from MethodInvocationDemoGrandChild",
				Whitebox.invokeMethod(tested, "getString"));
	}

	@Test
	public void testInvokePrivateMethodInSuperClassWhenClassUnderTestIsAnAnonymousInnerClass()
			throws Exception {
		MethodInvocationDemo tested = new MethodInvocationDemo() {

		};

		assertEquals("MethodInvocationDemoChild", Whitebox.invokeMethod(tested,
				MethodInvocationDemoChild.class, "getString"));
	}

	@Test
	public void testInvokeProtectedMethodWhenClassUnderTestIsAnAnonymousInnerClass()
			throws Exception {
		MethodInvocationDemo tested = new MethodInvocationDemo() {

		};

		assertEquals(
				"MethodInvocationDemoChild wrapped a string from MethodInvocationDemoGrandChild",
				tested.getTheString());
	}

	@Test
	public void testInvokeProtectedMethodWhenClassUnderTestIsAnAnonymousInnerClassUsingWithbox()
			throws Exception {
		MethodInvocationDemo tested = new MethodInvocationDemo() {

		};

		assertEquals(
				"MethodInvocationDemoChild wrapped a string from MethodInvocationDemoGrandChild",
				Whitebox.invokeMethod(tested, "getTheString"));
	}

	@Test
	public void testInvokeSpecificMethodInHierarchy() throws Exception {
		MethodInvocationDemo tested = new MethodInvocationDemo();

		assertEquals("MethodInvocationDemoGrandChild", Whitebox.invokeMethod(
				tested, MethodInvocationDemoGrandChild.class, "getString",
				new Class<?>[0]));
	}

	@Test
	public void testInvokeSpecificMethodInHierarchyWithArguments()
			throws Exception {
		MethodInvocationDemo tested = new MethodInvocationDemo();

		assertEquals("MethodInvocationDemoGrandChild: 2", Whitebox
				.invokeMethod(tested, MethodInvocationDemoGrandChild.class,
						"getString", new Class<?>[] { int.class }, 2));
	}
}
