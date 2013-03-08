package rin.gl.gui;

import rin.gl.event.GLEvent.PickInEvent;
import rin.gl.lib3d.Poly;
import rin.gl.lib3d.properties.Properties;

public class GLGUIComponent<T> extends Poly {

	public GLGUIComponent( String id, Properties p ) {
		super( id );
	}
	
	private GLGUIFactory.GLGUIEvent runOnMouseIn = null;
	@Override public void processPickInEvent( PickInEvent e ) {
		System.out.println( "picked!" );
	}
	
}
