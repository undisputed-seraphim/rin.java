package rin.gl.gui;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.glUniform1i;

import java.util.HashMap;

import rin.gl.GL;
import rin.gl.lib3d.properties.Scale;
import rin.util.Buffer;

public class GLGUI {

	private HashMap<String, GLGUIComponent<?>> components = new HashMap<String, GLGUIComponent<?>>();
	
	public GLGUI() {
		GLGUIComponent<GLPane> pane = new GLPane();
		pane.addScaleTransition( new Scale( 1.0f, 0.0f, 1.0f ), 5000L );
		
		this.addComponent( pane );
	}
	
	public void addComponent( GLGUIComponent<?> component ) {
		this.components.put( component.getName(), component );
	}
	
	public GLGUIComponent<?> getComponent( String name ) {
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
