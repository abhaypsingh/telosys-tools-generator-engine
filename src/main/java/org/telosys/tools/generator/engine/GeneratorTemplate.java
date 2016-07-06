/**
 *  Copyright (C) 2008-2015  Telosys project org. ( http://www.telosys.org/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.telosys.tools.generator.engine;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import org.apache.velocity.Template;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.generator.engine.directive.AssertFalseDirective;
import org.telosys.tools.generator.engine.directive.AssertTrueDirective;
import org.telosys.tools.generator.engine.directive.ErrorDirective;
import org.telosys.tools.generator.engine.directive.UsingDirective;

public class GeneratorTemplate {

	private final static String USER_DIRECTIVE_NAME  = "userdirective" ;
	private final static String USER_DIRECTIVE_VALUE = getDirectives() ;
	
	private final String folderName ;
	
	private final String fileName ;
	
	private final byte[] contentByteArray ;

	private final String contentString ;
	
	private Template velocityTemplate = null ;

	protected GeneratorTemplate(String folderName, String fileName, byte[] content) {
		super();
		this.folderName = folderName;
		this.fileName = fileName;
		this.contentByteArray = content ;
		this.contentString = null ;
	}

	protected GeneratorTemplate(String folderName, String fileName, String content) {
		super();
		this.folderName = folderName;
		this.fileName = fileName;
		this.contentByteArray = null ;
		this.contentString = content ;
	}

	public GeneratorTemplate(File file) {
		super(); 
		if ( ! file.exists() ) {
			throw new RuntimeException("Template file '" + file.getName() + "' not found");
		}
		if ( ! file.isFile() ) {
			throw new RuntimeException("'" + file.getName() + "' is not a file");
		}
		byte[] fileContent;
		try {
			fileContent = FileUtil.read(file);
		} catch (Exception e) {
			throw new RuntimeException("Cannot load template file", e);
		}
		this.folderName = file.getParent();
		this.fileName = file.getName();
		this.contentByteArray = fileContent ;
		this.contentString = null ;
	}

	protected String getFolderName() {
		return folderName;
	}

	protected String getFileName() {
		return fileName;
	}

	protected byte[] getContentByteArray() {
		if ( contentByteArray != null ) {
			return contentByteArray;
		}
		else if ( contentString != null ) {
			return contentString.getBytes() ;
		}
		else {
			return null ;
		}
	}
	
	protected String getContentString() {
		if ( contentString != null ) {
			return contentString ;
		}
		else if ( contentByteArray != null ) {
			return new String( contentByteArray) ;
		}
		else {
			return null ;
		}
	}
	
	protected boolean contentIsString() {
		return contentString != null ;
	}

	protected boolean contentIsByteArray() {
		return contentByteArray != null ;
	}
	
	protected Template getVelocityTemplate() throws Exception {
		if ( this.velocityTemplate == null ) {
			this.velocityTemplate = buildVelocityTemplate();
		}
		return this.velocityTemplate ;
	}

	private Template buildVelocityTemplate() throws Exception {
		if ( this.contentIsString() ) {
			Reader templateContentReader = new StringReader( this.getContentString() );
			return parseTemplate(this.getFileName(), templateContentReader ) ;
		}
		else if ( this.contentIsByteArray() ) {
			Reader templateContentReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(this.getContentByteArray())));
			return parseTemplate(this.getFileName(), templateContentReader ) ;
		}
		else {
			throw new Exception("Invalid template content (not String nor ByteArray)");
		}
	}
	
	private Template parseTemplate(String templateName, Reader templateContentReader) throws Exception {
        
		RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();
		
		//template.
		runtimeServices.setProperty(USER_DIRECTIVE_NAME, USER_DIRECTIVE_VALUE);
		//runtimeServices.init(arg0);

//		SimpleNode node;
//		try {
//			node = runtimeServices.parse(templateContentReader, templateName);
//		} catch (ParseException e) {			
//			e.printStackTrace();
//			throw new Exception("Parsing error in template",e);
//		}
		// 'parse' throws Velocity 'ParseException' ( extends 'java.lang.Exception' )
		SimpleNode node = runtimeServices.parse(templateContentReader, templateName); // v 3.0.0

		Template template = new Template();
		template.setRuntimeServices(runtimeServices);
		template.setData(node);
		
		// initializes the document.
		// init() is not longer dependent upon context, but we need to let the init() carry the template name down throught 
		// for VM namespace features
		template.initDocument();
		
		return template ;
	}
	
	private static String getDirectives() {
		return 
				UsingDirective.class.getCanonicalName() 
				+ ", " 
				+ AssertTrueDirective.class.getCanonicalName() 
				+ ", " 
				+ AssertFalseDirective.class.getCanonicalName() 
				+ ", " 
				+ ErrorDirective.class.getCanonicalName() 
				; // one or n directive(s) separated by a comma 
	}	
}
