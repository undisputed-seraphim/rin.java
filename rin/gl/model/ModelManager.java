package rin.gl.model;

import rin.gl.lib3d.Actor;
import rin.engine.Engine;

/**
 * Class for creating any of the implemented model formats. Other formats can be added
 *  by including an implementation file which implements {@link ModelManager} and
 *  an entry in the {@link Model.Format} enumeration.
 *
 * @see {@link ModelManager}
 */
public class ModelManager {

	public static Actor create( Engine.ModelFormat format, String file ) {
		String model = Engine.MODEL_DIR + file + Engine.LS + file + "." + format.toString();
		return format.manager.fromFile( model );
	}
	
}
