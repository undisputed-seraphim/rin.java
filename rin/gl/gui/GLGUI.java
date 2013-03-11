package rin.gl.gui;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.glUniform1i;

import java.util.HashMap;

import rin.gl.GL;
import rin.util.Buffer;

public class GLGUI {

	private HashMap<String, GLGUIComponent<?>> components = new HashMap<String, GLGUIComponent<?>>();
	
	private int width = 0, height = 0;
	public int getWidth() { return this.width; }
	public int getHeight() { return this.height; }
	
	public GLGUI( int width, int height ) {
		this.width = width;
		this.height = height;
	}
	
	public void add( GLGUIComponent<?> component ) {
		this.components.put( component.getName(), component );
	}
	
	public GLGUIComponent<?> get( String name ) {
		if( this.components.get( name ) != null )
			return this.components.get( name );
		
		return null;
	}
	
	public void render( long dt ) {
		glUniform1i( GL.getUniform( "useUnique" ), GL_TRUE );
		for( String id : this.components.keySet() ) {
			this.components.get( id ).update( dt );
			this.components.get( id ).render( true );
		}
		glUniform1i( GL.getUniform( "useUnique" ), GL_FALSE );
		
		GL.setUniqueAtMouse( Buffer.toString( GL.get().getCamera().getMouseRGB() ) );
		
		for( String id : this.components.keySet() ) {
			this.components.get( id ).render();
		}
	}
}
