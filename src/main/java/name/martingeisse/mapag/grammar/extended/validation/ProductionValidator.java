package name.martingeisse.mapag.grammar.extended.validation;

import name.martingeisse.mapag.grammar.extended.Production;

/**
 * This object can only be used for a single grammar, but should be used for all productions of that grammar to detect
 * properties of the whole set of productions, e.g. whether a production for the start symbol exists.
 */
interface ProductionValidator {

	void validateProduction(Production production, ErrorReporter errorReporter);

	void finish(ErrorReporter errorReporter);

}
