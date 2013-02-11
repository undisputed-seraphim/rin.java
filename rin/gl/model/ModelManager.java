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
	
	/**
	 * Enumeration for the various {@link Model} formats allowed in the engine.
	 *  New formats can be added via implementation file and inserting an entry
	 *  for the new format here (e.g. {@link ModelDAE} implementation and
	 *  {@link #DAE} enumeration).
	 *  <p>
	 *  Each format consists of a single {@link ModelManager} used by
	 *   {@link Model#create} to staticly create models of a given format.
	 *
	 * @see {@link Model}, {@link ModelManager}
	 */
	public static enum Format {
		DAE		( new ModelDAE() ),
		OBJ		( new ModelOBJ() );
		
		private Model manager;
		
		Format( Model manager ) {
			this.manager = manager;
		}
	}
	
	public static Actor create( ModelManager.Format format, String file ) {
		String model = Engine.MODEL_DIR + file + Engine.LS + file + "." + format.toString();
		return format.manager.fromFile( model );
	}
}
