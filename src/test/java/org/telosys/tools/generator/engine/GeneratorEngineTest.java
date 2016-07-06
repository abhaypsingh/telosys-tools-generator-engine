package org.telosys.tools.generator.engine;

import org.junit.Test;
import org.telosys.tools.generator.engine.GeneratorContext;
import org.telosys.tools.generator.engine.GeneratorContextException;
import org.telosys.tools.generator.engine.GeneratorEngine;
import org.telosys.tools.generator.engine.GeneratorTemplate;
import org.telosys.tools.generator.engine.directive.DirectiveException;

public class GeneratorEngineTest {
	
	private final static String TEMPLATE1 = "We are using $project $name to render this.";
	private final static String TEMPLATE2_INVALID = "We are using #if( aaa   $name to render this.";
	private final static String TEMPLATE3_INVALID_REF = "We are using $foo to render this.";
	
	private GeneratorContext getContext1() {
		GeneratorContext context = new GeneratorContext();
        context.put("name",    "Velocity");
        context.put("project", "Jakarta");
        return context ;
	}
	private GeneratorContext getContext2() {
		GeneratorContext context = new GeneratorContext();
        context.put("name",    "Foo");
        context.put("project", "My project");
        return context ;
	}
	private GeneratorContext getContext3() {
		GeneratorContext context = new GeneratorContext();
        context.put("name",    "Foo");
        context.put("project", "My project");
        context.put("flagTrue", true);
        context.put("flagFalse", false);
        return context ;
	}

	@Test
	public void test1() throws Exception {

		GeneratorTemplate template = new GeneratorTemplate("", "", TEMPLATE1);

		GeneratorEngine generatorEngine = new GeneratorEngine();
		String r1 = generatorEngine.generate(template, getContext1() );
		System.out.println("Generation result 1 : " + r1 );

		String r2 = generatorEngine.generate(template, getContext2() );
		System.out.println("Generation result 2 : " + r2 );
	}

	@Test(expected=Exception.class)
	public void test2() throws Exception {

		GeneratorTemplate template = new GeneratorTemplate("", "", TEMPLATE2_INVALID);

		GeneratorEngine generatorEngine = new GeneratorEngine();
		String r1 = generatorEngine.generate(template, getContext1() );
		System.out.println("Generation result 1 : " + r1 );
	}

	@Test (expected=GeneratorContextException.class)
	public void test3() throws Exception {

		GeneratorTemplate template = new GeneratorTemplate("", "", TEMPLATE3_INVALID_REF);

		GeneratorEngine generatorEngine = new GeneratorEngine();
		String r1 = generatorEngine.generate(template, getContext1() );
		System.out.println("Generation result 1 : " + r1 );
	}

	//---------------------------------------------------------------------------------------
	
	private final static String TEMPLATE4 = 
			"We are using $project $name to render this.\n" +
			"#error('My error message')" +
			"End of template";
	
	@Test(expected=DirectiveException.class)
	public void test4DirectiveError() throws Exception {

		System.out.println("Test directive '#error' : DirectiveException expected "  );
		GeneratorTemplate template = new GeneratorTemplate("", "", TEMPLATE4);

		GeneratorEngine generatorEngine = new GeneratorEngine();
		String r1 = generatorEngine.generate(template, getContext1() );
		System.out.println("Generation result 1 : " + r1 );
	}

	//---------------------------------------------------------------------------------------
	
	private final static String TEMPLATE5 = 
			"We are using $project $name to render this.\n" +
			"#error(badName)" +
			"End of template";
	
	@Test(expected=Exception.class)
	public void test5DirectiveError() throws Exception {

		System.out.println("Test directive '#error' : DirectiveException expected "  );
		GeneratorTemplate template = new GeneratorTemplate("", "", TEMPLATE5);

		GeneratorEngine generatorEngine = new GeneratorEngine();
		String r1 = generatorEngine.generate(template, getContext1() );
		System.out.println("Generation result 1 : " + r1 );
	}

	//---------------------------------------------------------------------------------------
	
	private final static String TEMPLATE6 = 
			"We are using $project $name to render this.\n" +
			"#assertTrue(true, 'Must be true') \n" +
			"#assertTrue($flagTrue, 'The flag must be true') \n" +
			"End of template";
	
	//@Test(expected=DirectiveException.class)
	@Test
	public void test6DirectiveAssertTrue() throws Exception {

		System.out.println("Test directive '#assertTrue' : OK expected "  );
		GeneratorTemplate template = new GeneratorTemplate("", "", TEMPLATE6);

		GeneratorEngine generatorEngine = new GeneratorEngine();
		String r1 = generatorEngine.generate(template, getContext3() );
		System.out.println("Generation result 1 : " + r1 );
	}

	private final static String TEMPLATE7 = 
			"We are using $project $name to render this.\n" +
			"#assertTrue($flagTrue, 'The flag must be true') \n" +
			"#assertTrue(false, 'Must be true') \n" +
			"End of template";
	
	@Test(expected=DirectiveException.class)
	public void test7DirectiveAssertTrue() throws Exception {

		System.out.println("Test directive '#assertTrue' : OK expected "  );
		GeneratorTemplate template = new GeneratorTemplate("", "", TEMPLATE7);

		GeneratorEngine generatorEngine = new GeneratorEngine();
		String r1 = generatorEngine.generate(template, getContext3() );
		System.out.println("Generation result 1 : " + r1 );
	}

	//---------------------------------------------------------------------------------------
	
	private final static String TEMPLATE8 = 
			"We are using $project $name to render this.\n" +
			"#assertFalse(false, 'Must be false') \n" +
			"#assertFalse($flagFalse, 'The flag must be FALSE') \n" +
			"End of template";
	
	//@Test(expected=DirectiveException.class)
	@Test
	public void test8DirectiveAssertFalse() throws Exception {

		System.out.println("Test directive '#assertFalse' : OK expected "  );
		GeneratorTemplate template = new GeneratorTemplate("", "", TEMPLATE8);

		GeneratorEngine generatorEngine = new GeneratorEngine();
		String r1 = generatorEngine.generate(template, getContext3() );
		System.out.println("Generation result 1 : " + r1 );
	}

	//---------------------------------------------------------------------------------------
	
	private final static String TEMPLATE9 = 
			"#using('$project') \n" +
			"#using('project', 'name') \n" +
			"We are using $project $name to render this.\n" +
			"End of template";
	
	@Test
	public void test9DirectiveUsing() throws Exception {

		System.out.println("Test directive '#using' : OK expected "  );
		GeneratorTemplate template = new GeneratorTemplate("", "", TEMPLATE9);

		GeneratorEngine generatorEngine = new GeneratorEngine();
		String r1 = generatorEngine.generate(template, getContext3() );
		System.out.println("Generation result 1 : " + r1 );
	}

	//---------------------------------------------------------------------------------------
	
	private final static String TEMPLATE10 = 
			"#using('$project') \n" +
			"#using('$project', '$name') \n" +
			"#using('$project', '$name', '$zzzz', 'yyyy' ) \n" +    // $zzzz not defined in context
			"We are using $project $name to render this.\n" +
			"End of template";
	
	@Test(expected=DirectiveException.class)
	//@Test
	public void test10DirectiveUsing() throws Exception {

		System.out.println("Test directive '#using' : Exception expected "  );
		GeneratorTemplate template = new GeneratorTemplate("", "", TEMPLATE10);

		GeneratorEngine generatorEngine = new GeneratorEngine();
		String r1 = generatorEngine.generate(template, getContext3() );
		System.out.println("Generation result 1 : " + r1 );
	}

}
