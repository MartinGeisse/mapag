package name.martingeisse.mapag.codegen;

/**
 *
 */
public class MapagParserClassGenerator {

	private String packageName;
	private String className;

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void generate() {
		println("package " + packageName + ";\n");
		println();
		println("class " + className + " {");
		println("}");
	}

	private void print(Object o) {
		System.out.print(o);
	}

	private void println(Object o) {
		System.out.println(o);
	}

	private void println() {
		System.out.println();
	}

}
