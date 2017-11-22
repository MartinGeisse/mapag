package name.martingeisse.mapag.util;

import java.util.Objects;

/**
 * TODO check if we can use the Pair class from apache commons lang.
 */
public final class Pair<A, B> {

	private final A left;
	private final B right;

	public Pair(A left, B right) {
		this.left = left;
		this.right = right;
	}

	public A getLeft() {
		return left;
	}

	public B getRight() {
		return right;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Pair) {
			Pair other = (Pair) obj;
			return (Objects.equals(left, other.left) && Objects.equals(right, other.right));
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (left == null ? 0 : left.hashCode()) ^ (right == null ? 0 : right.hashCode());
	}

	@Override
	public String toString() {
		return "(" + left + ", " + right + ")";
	}

}
