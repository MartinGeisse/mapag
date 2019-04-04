package external;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

/**
 *
 */
public class ImmutableListTest {

	@Test(expected = NullPointerException.class)
	public void testImmutableListDoesntAcceptNullElement() {
		ImmutableList.of("foo", null, "bar");
	}

}
