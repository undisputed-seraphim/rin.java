package rin.gl.model;

import rin.gl.lib3d.interfaces.Actor;

/**
 * Interface used to ensure any {@link Model} formats return the proper data
 *  to build and return an {@link Actor}.
 *  
 *  @see {@link Actor}, {@link Model}
 */
public interface ModelManager {
	
	/**
	 * Creates an Actor from a string filename. The instructions for any format
	 *  can be added effeciently using this setup.
	 *  
	 * @param file	String filename of the model file to be loaded
	 * @return		{@link Actor} representing the model in {@param file}
	 */
	public Actor fromFile( String file );
}
