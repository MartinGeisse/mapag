package name.martingeisse.mapag.util;

/**
 *
 */
public class ProfilingTimer {

	public static final long THRESHOLD = 100;

	private final String description;
	private final long startTime;
	private int index = 0;
	private long lastTime;

	public ProfilingTimer(String description) {
		this.description = description;
		startTime = time();
		lastTime = startTime;
	}

	public void tick() {
		long now = time();
		check(now - lastTime, false);
		lastTime = now;
		index++;
	}

	public void end() {
		tick();
		check(time() - startTime, true);
	}

	private void check(long amount, boolean end) {
		if (amount > THRESHOLD) {
			show(amount, end);
		}
	}

	private void show(long amount, boolean end) {
		System.out.println(description + " / " + index + ": " + amount + (end ? " (end)" : ""));
	}

	private static long time() {
		return System.currentTimeMillis();
	}

}
