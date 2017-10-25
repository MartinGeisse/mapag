package name.martingeisse.mapag.sm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import name.martingeisse.mapag.grammar.canonical.Alternative;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class StateTest {

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNull() {
		new State(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorEmpty() {
		new State(ImmutableSet.of());
	}

	@Test
	public void testConstructorGetter() {
		Alternative alternative = new Alternative(ImmutableList.of("r1", "r2", "r3"), "prec1");
		StateElement stateElement = new StateElement("lll", alternative, 0, "foo");
		State state = new State(ImmutableSet.of(stateElement));
		Assert.assertEquals(ImmutableSet.of(stateElement), state.getElements());
	}

	@Test
	public void testEqualsAndHashCode() {

		Alternative alternative1 = new Alternative(ImmutableList.of("r1", "r2", "r3"), "prec1");
		StateElement stateElement1 = new StateElement("aaa", alternative1, 0, "foo");

		Alternative alternative2 = new Alternative(ImmutableList.of("foo", "bar"), null);
		StateElement stateElement2 = new StateElement("bbb", alternative2, 1, "baz");

		State[] states = new State[]{
			new State(ImmutableSet.of(stateElement1)),
			new State(ImmutableSet.of(stateElement2)),
			new State(ImmutableSet.of(stateElement1, stateElement2)),
			new State(ImmutableSet.of(stateElement1, stateElement2))
		};

		for (int i = 0; i < states.length; i++) {
			for (int j = 0; j < states.length; j++) {
				if (i == j || (i >= 2 && j >= 2)) {
					Assert.assertEquals(states[i], states[j]);
					Assert.assertEquals(states[i].hashCode(), states[j].hashCode());
				} else {
					Assert.assertNotEquals(states[i], states[j]);
				}
			}
		}

	}

	// TODO

}
