package name.martingeisse.mapag.codegen.old;

import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

/**
 * This class is needed because a plain ClasspathResourceLoader would try to load resources from its own class
 * loader, not from ours.
 */
public class MyResourceLoader extends ClasspathResourceLoader {
}
