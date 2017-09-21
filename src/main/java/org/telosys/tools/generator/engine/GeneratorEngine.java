/**
 *  Copyright (C) 2008-2017  Telosys project org. ( http://www.telosys.org/ )
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

import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.telosys.tools.generator.engine.events.GeneratorEvents;

public class GeneratorEngine {
	
	/**
	 * Constructor
	 */
	public GeneratorEngine() {
		super();
	}

	/**
	 * Generates using the given template and context. <br>
	 * The generation result is returned as a string. <br>
	 * 
	 * @param generatorTemplate
	 * @param generatorContext
	 * @return
	 * @throws Exception
	 */
	public String generate(GeneratorTemplate generatorTemplate, GeneratorContext generatorContext) throws Exception {

		GeneratorProperties.init();
		
		Template velocityTemplate = generatorTemplate.getVelocityTemplate() ;
		
		VelocityContext velocityContext = getVelocityContext(generatorContext);
		
        StringWriter result = new StringWriter();
        velocityTemplate.merge( velocityContext, result);
		return result.toString() ;
	}

	/**
	 * Creates a VelocityContext from the given GeneratorContext <br>
	 * and attach events to the VelocityContext <br>
	 * 
	 * @param generatorContext
	 * @return
	 */
	private VelocityContext getVelocityContext(GeneratorContext generatorContext ) {
		VelocityContext velocityContext = new VelocityContext(generatorContext.getMap());
		
//		//--- Make a cartridge to hold the event handlers 
//		EventCartridge ec = new EventCartridge();
//		
//		//--- Event handler for "Invalid Reference"
//		ec.addInvalidReferenceEventHandler( new InvalidReferenceEventImpl() );
//		
//		//ec.addNullSetEventHandler( new NullSetEventImpl() );
//		
//		//--- Finally let it attach itself to the context
//		ec.attachToContext( velocityContext );
		
		GeneratorEvents.attachEvents(velocityContext);
		
		return velocityContext ;
	}
	
}
