package rin.gl.model;

import rin.engine.resource.Resource;
import rin.engine.resource.model.ModelContainer;
import rin.gl.lib3d.Actor;

/**
 * Interface used to ensure any {@link Model} formats return the proper data
 *  to build and return an {@link Actor}.
 *  
 *  @see {@link Actor}, {@link Model}
 */
public interface Model {
	
	/**
	 * Creates an Actor from a string filename. The instructions for any format
	 *  can be added effeciently using this setup.
	 *  
	 * @param file	String filename of the model file to be loaded
	 * @return		{@link Actor} representing the model in {@param file}
	 */
	public Actor fromResource( Resource resource );
	public Actor fromContainer( ModelContainer container );
}
