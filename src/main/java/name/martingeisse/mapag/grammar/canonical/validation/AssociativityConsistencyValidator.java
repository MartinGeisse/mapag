package name.martingeisse.mapag.grammar.canonical.validation;

import name.martingeisse.mapag.grammar.Associativity;
import name.martingeisse.mapag.grammar.canonical.TerminalDefinition;
import name.martingeisse.mapag.util.ListUtil;
import name.martingeisse.mapag.util.ParameterUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

/**
 * This validator checks that terminals with the same precedence index have equal associativity.
 */
class AssociativityConsistencyValidator {

	static final Comparator<Integer> NULLABLE_INTEGER_COMPARATOR = (x, y) -> {
		if (x == null) {
			if (y == null) {
				return 0;
			} else {
				return -1;
			}
		} else {
			if (y == null) {
				return 1;
			} else {
				return x.compareTo(y);
			}
		}
	};

	static final Comparator<TerminalDefinition> PRECEDENCE_COMPARATOR =
		Comparator.comparing(TerminalDefinition::getPrecedenceIndex, NULLABLE_INTEGER_COMPARATOR);

	void validate(Collection<TerminalDefinition> terminalDefinitions) {
		ParameterUtil.ensureNotNull(terminalDefinitions, "terminalDefinitions");
		Integer previousPrecedence = null;
		Associativity previousAssociativity = null;
		for (TerminalDefinition terminalDefinition : ListUtil.sorted(terminalDefinitions, PRECEDENCE_COMPARATOR)) {
			if (Objects.equals(previousPrecedence, terminalDefinition.getPrecedenceIndex())) {
				if (previousAssociativity != null && previousAssociativity != terminalDefinition.getAssociativity()) {
					throw new IllegalStateException("found two terminals with the same precedence index but different associativity");
				}
			}
			previousPrecedence = terminalDefinition.getPrecedenceIndex();
			previousAssociativity = terminalDefinition.getAssociativity();
		}
	}

}
