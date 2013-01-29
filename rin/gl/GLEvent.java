package rin.gl;

import java.util.ArrayList;
import java.util.Iterator;

import rin.gl.GLEventListener.*;

public class GLEvent {
	public static final int KEY_EVENT = 0,
							MOUSE_EVENT = 1;

	public GLEvent() {}
	
	public static void addEventListener( int type, GLEventListener src ) {
		switch( type ) {
		
		case KEY_EVENT: KeyEvent.addListener( (KeyEventListener)src ); break;
		
		}
	}
	
	public static void removeEventListener( int type, GLEventListener src ) {
		switch( type ) {
		
		case KEY_EVENT: KeyEvent.removeListener( (KeyEventListener)src ); break;
		
		}
	}
	
	public static void fire( KeyEvent e ) { KeyEvent.fire( e ); }
	
	
	public static class KeyEvent extends GLEvent {
		private static ArrayList<KeyEventListener> listeners = new ArrayList<KeyEventListener>();
		
		public int key;
		
		public KeyEvent( int key ) {
			super();
			this.key = key;
		}
		
		public static void addListener( KeyEventListener el ) {
			if( KeyEvent.listeners.indexOf( el ) == -1 )
				KeyEvent.listeners.add( el );
		}
		
		public static void removeListener( KeyEventListener el ) {
			if( KeyEvent.listeners.indexOf( el ) != -1 )
				KeyEvent.listeners.remove( el );
		}
		
		public static void fire( KeyEvent e ) {
			Iterator<KeyEventListener> i = KeyEvent.listeners.iterator();
			while( i.hasNext() ) {
				( (KeyEventListener)i.next() ).processKeyEvent( e );
			}
		}
	}
	
	
	public static class MouseEvent extends GLEvent {
	}
}
