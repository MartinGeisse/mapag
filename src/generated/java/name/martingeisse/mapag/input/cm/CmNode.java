package name.martingeisse.mapag.input.cm;

public interface CmNode {

	CmNode getCmParent();

	int compareStartOffset(CmNode other);

}
