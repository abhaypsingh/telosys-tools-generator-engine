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

import java.util.Properties;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.telosys.tools.generator.engine.directive.AssertFalseDirective;
import org.telosys.tools.generator.engine.directive.AssertTrueDirective;
import org.telosys.tools.generator.engine.directive.CheckIdDirective;
import org.telosys.tools.generator.engine.directive.ErrorDirective;
import org.telosys.tools.generator.engine.directive.UsingDirective;
import org.telosys.tools.generator.engine.include.IncludeEventImpl;

/**
 * This utility class is used to initialize the Velocity engine properties <br>
 * It is based on the Velocity 'RuntimeSingleton' <br>
 * 
 * RuntimeSingleton Javadoc :<br>
 * 
 * This is the Runtime system for Velocity. It is the single access point for all functionality in Velocity.
 * It adheres to the mediator pattern and is the only structure that developers need to be familiar with
 * in order to get Velocity to perform.
 *
 * The Runtime will also cooperate with external systems like Turbine. Runtime properties can
 * set and then the Runtime is initialized.
 *
 * Turbine for example knows where the templates are to be loaded from, and where the velocity
 * log file should be placed.
 *
 * So in the case of Velocity cooperating with Turbine the code might look something like the following:
 *
 * <pre>
 * RuntimeSingleton.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, templatePath);
 * RuntimeSingleton.setProperty(RuntimeConstants.RUNTIME_LOG, pathToVelocityLog);
 * RuntimeSingleton.init();
 * </pre>
 *
 * <pre>
 * -----------------------------------------------------------------------
 * N O T E S  O N  R U N T I M E  I N I T I A L I Z A T I O N
 * -----------------------------------------------------------------------
 * RuntimeSingleton.init()
 *
 *    If Runtime.init() is called by itself the Runtime will initialize with a set of default values.
 * -----------------------------------------------------------------------
 * RuntimeSingleton.init(String/Properties)
 *
 *    In this case the default velocity properties are layed down first to provide a solid base, then any properties provided
 *    in the given properties object will override the corresponding default property.
 * -----------------------------------------------------------------------
 * </pre>
 *
 */
public class GeneratorProperties {
	
	private final static String USER_DIRECTIVE_NAME  = "userdirective" ;
	private final static String USER_DIRECTIVE_VALUE = getDirectives() ;
	
	private static String getDirectives() {
		return 
				UsingDirective.class.getCanonicalName() 
				+ ", " 
				+ AssertTrueDirective.class.getCanonicalName() 
				+ ", " 
				+ AssertFalseDirective.class.getCanonicalName() 
				+ ", " 
				+ CheckIdDirective.class.getCanonicalName() 
				+ ", " 
				+ ErrorDirective.class.getCanonicalName() 
				; // one or n directive(s) separated by a comma 
	}	

	/**
	 * Initializes the Velocity engine properties <br>
	 * Can be called several times <br>
	 */
	public final static void init(String templateFolder) {

		Properties properties = new Properties();

		// Try to allow relative path for #include and #parse 
		// Added 2016/09/29 LGU
		// No regression
//		properties.setProperty(RuntimeConstants.EVENTHANDLER_INCLUDE, IncludeRelativePath.class.getName() );
//		properties.setProperty(RuntimeConstants.EVENTHANDLER_INCLUDE, IncludeRelativePath.class.getCanonicalName() );
//		properties.setProperty(RuntimeConstants.EVENTHANDLER_INCLUDE, "xxx.xxx" );
		properties.setProperty(RuntimeConstants.EVENTHANDLER_INCLUDE, IncludeEventImpl.class.getCanonicalName() );
		
		properties.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, "" );
		
		// Set all the "user directives" ( list of classes names ) : OK, it works
		properties.setProperty(USER_DIRECTIVE_NAME, USER_DIRECTIVE_VALUE);
		
		System.out.println("Properties size = " + properties.size() );
		printProperty(properties, RuntimeConstants.EVENTHANDLER_INCLUDE);
		printProperty(properties, RuntimeConstants.FILE_RESOURCE_LOADER_PATH);
		printProperty(properties, USER_DIRECTIVE_NAME);
		
		// Initialize the Velocity Runtime with a Properties object.
		// The source code shows that the 'init' method can be called several times
		// only the first call is processed 
		RuntimeSingleton.init(properties) ;
		
		// After RuntimeSingleton.init(properties) the configuration is as expected
		ExtendedProperties configuration = RuntimeSingleton.getConfiguration();
		printProperty(configuration, RuntimeConstants.EVENTHANDLER_INCLUDE);
		printProperty(configuration, RuntimeConstants.FILE_RESOURCE_LOADER_PATH);
		printProperty(configuration, USER_DIRECTIVE_NAME);
	}

	private final static void printProperty(Properties properties, String key) {
		//System.out.println(" - '" + key + "' --> '" + properties.getProperty(key) + "'");
	}
	private final static void printProperty(ExtendedProperties configuration, String key) {
		//System.out.println(" - '" + key + "' --> '" + configuration.getProperty(key) + "'");
	}
}
