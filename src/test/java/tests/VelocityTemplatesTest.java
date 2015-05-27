package tests;

import java.io.StringReader;
import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.junit.Test;

public class VelocityTemplatesTest {
	
	private final static String TEMPLATE1 = "We are using $project $name to render this.";
	private final static String TEMPLATE2_INVALID = "We are using #if( aaa   $name to render this.";
	
	private VelocityContext getContext1() {
        VelocityContext context = new VelocityContext();
        context.put("name",    "Velocity");
        context.put("project", "Jakarta");
        return context ;
	}
	private VelocityContext getContext2() {
        VelocityContext context = new VelocityContext();
        context.put("name",    "Foo");
        context.put("project", "My project");
        return context ;
	}

	private Template getTemplate(String templateName, String templateContent) {
        RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();
        
		StringReader templateStringReader = new StringReader(templateContent);
		SimpleNode node;
		try {
			node = runtimeServices.parse(templateStringReader, templateName);
		} catch (ParseException e) {			
			e.printStackTrace();
			throw new RuntimeException("Parsing error in template.",e);
		}
		Template template = new Template();
		template.setRuntimeServices(runtimeServices);
		template.setData(node);
		
		// initializes the document.
		// init() is not longer dependant upon context, but we need to let the init() carry the template name down throught 
		// for VM namespace features
		template.initDocument();
		
		return template ;
	}
	
	@Test
	public void test1() {
		/* first, we init the runtime engine.  Defaults are fine. */

        Velocity.init();

        /* lets make a Context and put data into it */
//        VelocityContext context = new VelocityContext();
//        context.put("name", "Velocity");
//        context.put("project", "Jakarta");
        VelocityContext context = getContext1() ;
        
//        /* lets render a template */
//
//        StringWriter w = new StringWriter();
//
//        Velocity.mergeTemplate("testtemplate.vm", context, w );
//        System.out.println(" template : " + w );

        /* lets make our own string to render */
        String templateString = "We are using $project $name to render this.";
        StringWriter result = new StringWriter();
        Velocity.evaluate( context, result, "[MyLOG]", templateString );
        System.out.println("Result : ");
        System.out.println( result );
        
        
	}

	@Test
	public void test2() {

//        String templateString = "We are using $project $name to render this.";
//
//        RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();
//        
//		StringReader templateStringReader = new StringReader(templateString);
//		SimpleNode node;
//		try {
//			node = runtimeServices.parse(templateStringReader, "Template name");
//		} catch (ParseException e) {			
//			e.printStackTrace();
//			throw new RuntimeException("Parsing error in template.",e);
//		}
//		Template template = new Template();
//		template.setRuntimeServices(runtimeServices);
//		template.setData(node);
//		
//		// initializes the document.
//		// init() is not longer dependant upon context, but we need to let the init() carry the template name down throught 
//		// for VM namespace features
//		template.initDocument();		
		
		Template template = getTemplate("TEMPLATE1", TEMPLATE1);
		
		System.out.println("Generation result : " + generate(template, getContext1()));
		System.out.println("Generation result : " + generate(template, getContext2()));
	}

	
	@Test(expected=RuntimeException.class)
	public void test3() {
		Template template = getTemplate("TEMPLATE2", TEMPLATE2_INVALID);
		
		System.out.println("Generation result : " + generate(template, getContext1()));
		System.out.println("Generation result : " + generate(template, getContext2()));
	}
	
	private String generate(Template template, VelocityContext context ) {
		System.out.println("generate(..,..)");
        StringWriter result = new StringWriter();
		template.merge( context, result);
		return result.toString() ;
	}
}
