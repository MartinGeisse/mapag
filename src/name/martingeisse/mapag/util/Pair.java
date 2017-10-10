package name.martingeisse.mapag.util;

/**
 *
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

}
