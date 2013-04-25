package rin.gl.model;

import rin.gl.lib3d.Actor;
import rin.engine.Engine;
import rin.engine.resource.Resource;
import rin.engine.resource.ResourceIdentifier;

/**
 * Class for creating any of the implemented model formats. Other formats can be added
 *  by including an implementation file which implements {@link ModelManager} and
 *  an entry in the {@link Model.Format} enumeration.
 *
 * @see {@link ModelManager}
 */
public class ModelManager {

	/*public static Actor create( Engine.ModelFormat format, String name, String pack ) {
		String model = Engine.PACK_DIR + pack + Engine.LS + name + Engine.LS + name + "." + format.toString();
		return format.manager.fromFile( model );
	}*/
	
	public static Actor create( Engine.ModelFormat format, Resource resource ) {
		return format.manager.fromResource( resource );
	}
	
	/*public static Actor create( Engine.ModelFormat format, String name ) {
		String model = Engine.MODEL_DIR + name + Engine.LS + name + "." + format.toString();
		return format.manager.fromFile( model );
	}*/
	
}
