package engine;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.junit.Test;

import com.trabajo.utils.Strings;

public class MSWTest {

	@SuppressWarnings("serial")
	@Test
	public void resources() throws Exception {
		
		
		VelocityEngine ve = new VelocityEngine();

		Map<String, String>	map=new HashMap<String, String>() {{put("jar0_unzip", "C:\\Users\\colin\\.trabajo\\filecache\\classloaders\\bugfix-0.0.3\\jar0_unzip");}};
		
		
	//	ve.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM, this);
		ve.setProperty("resource.loader", Strings.toCSV(map.keySet())); 
		
		Properties p=new Properties();
		for(String name: map.keySet()) {
			p.setProperty(name+".resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
			p.setProperty(name+".resource.loader.path", map.get(name)); 
			p.setProperty(name+".resource.loader.cache", "true");
			p.setProperty(name+".resource.loader.modificationCheckInterval", "10000000");
		}		

		ve.init(p);
		
		VelocityContext context = new VelocityContext();

		context.put("magic", this);

		Template template = null;
		StringWriter sw=new StringWriter();

		try {
			template = ve.getTemplate("html\\et1.html");
		} catch (ResourceNotFoundException e) {
			throw new RuntimeException(e);
		} catch (ParseErrorException e) {
			throw new RuntimeException(e);
		} catch (MethodInvocationException e) {
			throw new RuntimeException(e);
		}


		template.merge(context, sw);
		
		System.out.print(sw.toString());
		
	}
}
