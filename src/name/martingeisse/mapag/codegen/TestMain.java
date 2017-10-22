package name.martingeisse.mapag.codegen;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.log.NullLogChute;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;

/**
 *
 */
public class TestMain {

	public static void main(String[] args) throws Exception {

		VelocityEngine engine = new VelocityEngine();
		engine.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM, new NullLogChute());
		engine.setProperty(VelocityEngine.INPUT_ENCODING, "UTF-8");
		engine.setProperty(VelocityEngine.OUTPUT_ENCODING, "UTF-8");
		engine.setProperty(VelocityEngine.RESOURCE_LOADER, "classpath");
		engine.setProperty("classpath.loader.description", "classpath-based resource loader");
		engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		engine.setProperty("classpath.resource.loader.cache", true);
		engine.init();

		Template template = engine.getTemplate("Parser.vm");

		VelocityContext context = new VelocityContext();
		context.put("name", new String("Foobar"));

		StringWriter sw = new StringWriter();
		template.merge(context, sw);
		System.out.println(sw);

	}

}
