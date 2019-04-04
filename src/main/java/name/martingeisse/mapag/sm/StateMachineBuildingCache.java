package name.martingeisse.mapag.sm;

import name.martingeisse.mapag.grammar.canonical.Alternative;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public final class StateMachineBuildingCache {

	private final Map<Alternative, Map<String, StateElement>> cachedStartingStateElements = new HashMap<>();

	public StateElement buildStartingStateElement(String leftSide, Alternative alternative, String followTerminalOrEof) {
		Map<String, StateElement> innerMap = cachedStartingStateElements.computeIfAbsent(alternative, key -> new HashMap<>());
		return innerMap.computeIfAbsent(followTerminalOrEof, key -> new StateElement(leftSide, alternative, 0, followTerminalOrEof));
	}

}
