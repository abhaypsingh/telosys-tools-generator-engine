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
package org.telosys.tools.generator.engine.include;

import org.apache.velocity.app.event.IncludeEventHandler;
import org.telosys.tools.commons.FileUtil;

public class IncludeEventImpl implements IncludeEventHandler {

    /**
     * Return path relative to the current template's path.
     * 
     * @param includeResourcePath  the path as given in the include directive.
     * @param currentResourcePath the path of the currently rendering template that includes the
     *            include directive.
     * @param directiveName  name of the directive used to include the resource. (With the
     *            standard directives this is either "parse" or "include").

     * @return new path relative to the current template's path
     */
    public String includeEvent(
        String includeResourcePath,
        String currentResourcePath,
        String directiveName)
    {
//    	System.out.println(" - includeResourcePath = " + includeResourcePath);
//    	System.out.println(" - currentResourcePath = " + currentResourcePath); // template.name
//    	System.out.println(" - directiveName = " + directiveName);
    	
    	
//    	// Doesn't work due to invalid path
//    	// see http://stackoverflow.com/questions/12669375/velocity-different-template-paths 
//    	//  ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, "path/to/templates");
//    	//String s = RuntimeConstants.FILE_RESOURCE_LOADER_PATH ; // = "file.resource.loader.path"
//
//    	
//    	// JUST FOR TEST :
//    	String dir = "D:\\TMP\\TMP-TT-PROJECTS\\project1\\TelosysTools\\templates\\basic-templates-samples-T300\\" ;
////    	String dir = "\\TMP\\TMP-TT-PROJECTS\\project1\\TelosysTools\\templates\\basic-templates-samples-T300\\" ;
//    	return dir + includeResourcePath ;
//    	
////    	return includeResourcePath ;
//    	

//        // if the resource name starts with a slash, it's not a relative path
//        if (includeResourcePath.startsWith("/") || includeResourcePath.startsWith("\\") ) {
//            return includeResourcePath;
//        }
//
        int lastSlashPosition = Math.max(
                currentResourcePath.lastIndexOf("/"),
                currentResourcePath.lastIndexOf("\\")
                );

        if (lastSlashPosition == -1) {
            // No '/' or '\' character => we are at the root of the tree
            return includeResourcePath;
        }
        else {
            // cut at the last '/' or '\'
        	String folder = currentResourcePath.substring(0,lastSlashPosition);
        	return FileUtil.buildFilePath(folder, includeResourcePath);
//            return currentResourcePath.substring(0,lastslashpos) + "/" + includeResourcePath;
        }
    }
}
