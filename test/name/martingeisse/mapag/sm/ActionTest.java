package name.martingeisse.mapag.sm;

import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import name.martingeisse.mapag.grammar.canonical.TestUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class ActionTest {

	private static final Alternative alternative = new Alternative("a1", TestUtil.expansion("abc", "def"), null);

	@Test
	public void testShift() {
		StateElement nextStateElement = new StateElement("nt1", alternative, 0, "bar");
		State nextState = new State(ImmutableSet.of(nextStateElement));
		Action.Shift shift = new Action.Shift(nextState);
		Assert.assertSame(nextState, shift.getNextState());
	}

	@Test
	public void testReduce() {
		Action.Reduce reduce = new Action.Reduce("nt1", alternative);
		Assert.assertEquals("nt1", reduce.getNonterminal());
		Assert.assertEquals(alternative, reduce.getAlternative());
	}

	@Test
	public void testAccept() {
		Assert.assertTrue(Action.Accept.INSTANCE instanceof Action.Accept);
	}

}
