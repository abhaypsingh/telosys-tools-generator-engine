package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;
import org.telosys.tools.generator.engine.GeneratorTemplate;

import tests.env.TestsEnv;

public class GeneratorTemplateTest {
	
	private final static String TEMPLATE1 = "We are using $project $name to render this.";
	
	//---------------------------------------------------------------------------------------	
	@Test
	public void testTemplateFromString() {
		System.out.println("Test template from string ");
		GeneratorTemplate template = new GeneratorTemplate("", "", TEMPLATE1);
		assertTrue(template.contentIsString());
		assertFalse(template.contentIsByteArray());
		assertEquals("", template.getFileName());
		assertEquals("", template.getFolderName());
		System.out.println("Content string length : " + template.getContentString().length() );
		System.out.println("Content byte array length : " + template.getContentByteArray().length );
		assertEquals(43, template.getContentString().length() );
		assertTrue( template.getContentByteArray().length == template.getContentString().length() );
		System.out.println("-----");
	}
	//---------------------------------------------------------------------------------------	
	@Test
	public void testTemplateFromByteArray() {
		System.out.println("Test template from byte array ");
		GeneratorTemplate template = new GeneratorTemplate("", "", TEMPLATE1.getBytes());
		assertFalse(template.contentIsString());
		assertTrue(template.contentIsByteArray());
		assertEquals("", template.getFileName());
		assertEquals("", template.getFolderName());
		System.out.println("Content string length : " + template.getContentString().length() );
		System.out.println("Content byte array length : " + template.getContentByteArray().length );
		assertEquals(43, template.getContentString().length() );
		assertTrue( template.getContentByteArray().length == template.getContentString().length() );
		System.out.println("-----");
	}
	//---------------------------------------------------------------------------------------	
	@Test
	public void testTemplateFileAndFolder() {
		System.out.println("Test template file and folder name ");
		GeneratorTemplate template = new GeneratorTemplate("myfolder", "myfile", TEMPLATE1);
		assertEquals("myfile", template.getFileName());
		assertEquals("myfolder", template.getFolderName());
		System.out.println("-----");
	}
	//---------------------------------------------------------------------------------------	
	@Test
	public void testTemplateFromFile() {
		System.out.println("Test template from file ");
		File file = TestsEnv.getTestFile("template1.vm") ;
		assertTrue(file.exists());
		System.out.println("File : " + file);
		GeneratorTemplate template = new GeneratorTemplate(file);
		assertFalse(template.contentIsString());
		assertTrue(template.contentIsByteArray());
		assertEquals("template1.vm", template.getFileName());
		assertTrue(template.getFolderName().startsWith("src"));
		assertTrue(template.getFolderName().endsWith("resources"));
		System.out.println("File length : " + file.length() );
		System.out.println("Content string length : " + template.getContentString().length() );
		System.out.println("Content byte array length : " + template.getContentByteArray().length );
		assertEquals(file.length(), template.getContentString().length() );
		assertEquals(file.length(), template.getContentByteArray().length );
		System.out.println("-----");
	}
	//---------------------------------------------------------------------------------------	
	@Test
	public void testTemplateFromFile2() {
		System.out.println("Test template from file 2");
		File file = TestsEnv.getTestFile("myfolder/template2.vm") ;
		assertTrue(file.exists());
		System.out.println("File : " + file);
		GeneratorTemplate template = new GeneratorTemplate(file);
		assertFalse(template.contentIsString());
		assertTrue(template.contentIsByteArray());
		assertEquals("template2.vm", template.getFileName());
		assertTrue(template.getFolderName().startsWith("src"));
		assertTrue(template.getFolderName().endsWith("myfolder"));
		System.out.println("File length : " + file.length() );
		System.out.println("Content string length : " + template.getContentString().length() );
		System.out.println("Content byte array length : " + template.getContentByteArray().length );
		assertEquals(file.length(), template.getContentString().length() );
		assertEquals(file.length(), template.getContentByteArray().length );
		System.out.println("-----");
	}
	//---------------------------------------------------------------------------------------	
	@Test
	public void testTemplateFromFile3() {
		System.out.println("Test template from file 3");
		File file = TestsEnv.getTmpFile("myfolder/template3.vm") ;
		assertFalse(file.exists());
		System.out.println("File : " + file);
		try {
			GeneratorTemplate template = new GeneratorTemplate(file);
			System.out.println("Template loaded : " + template.getFileName() );
			assertTrue(template == null);
		} catch (RuntimeException e) {
			System.out.println("Expected exception catched ");
			System.out.println(" " + e.getMessage());
		}
		System.out.println("-----");
	}
	//---------------------------------------------------------------------------------------	
	@Test
	public void testTemplateFromFile4() {
		System.out.println("Test template from file 4 (error : folder)");
		File file = TestsEnv.getTmpFile("myfolder") ;
		assertFalse(file.exists());
		System.out.println("File : " + file);
		try {
			GeneratorTemplate template = new GeneratorTemplate(file);
			System.out.println("Template loaded : " + template.getFileName() );
			assertTrue(template == null);
		} catch (RuntimeException e) {
			System.out.println("Expected exception catched ");
			System.out.println(" " + e.getMessage());
		}
		System.out.println("-----");
	}
	//---------------------------------------------------------------------------------------	
	@Test
	public void testTemplateFromFile5() {
		System.out.println("Test template from file 5 (error : existing folder)");
		File file = TestsEnv.getTestFolder("myfolder");
		assertTrue(file.exists());
		System.out.println("File : " + file);
		try {
			GeneratorTemplate template = new GeneratorTemplate(file);
			System.out.println("Template loaded : " + template.getFileName() );
			assertTrue(template == null);
		} catch (RuntimeException e) {
			System.out.println("Expected exception catched ");
			System.out.println(" " + e.getMessage());
		}
		System.out.println("-----");
	}
}
