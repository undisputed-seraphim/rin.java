package rin.gl;

import java.util.ArrayList;
import java.util.Iterator;

import rin.gl.GLEventListener.*;

public class GLEvent {
	public GLEvent() {}
	
	public static void addKeyEventListener( KeyEventListener src ) { KeyEvent.addListener( src ); }
	public static void addPickEventListener( PickEventListener src, String code ) { PickEvent.addListener( src, code ); }
	
	public static void removeKeyEventListener( KeyEventListener src ) { KeyEvent.removeListener( src ); }
	
	public static void fire( KeyEvent e ) { KeyEvent.fire( e ); }
	public static void fire( PickEvent e ) { PickEvent.fire( e ); }
	
	
	public static class KeyEvent extends GLEvent {
		private static ArrayList<KeyEventListener> listeners = new ArrayList<KeyEventListener>();
		
		public int key;
		
		public KeyEvent( int key ) { this.key = key; }
		
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
	
	
	public static class PickEvent extends GLEvent {
		private static ArrayList<PickEventListener> listeners = new ArrayList<PickEventListener>();
		private static ArrayList<String> listening = new ArrayList<String>();
		
		public String code;
		
		public PickEvent( String code ) { this.code = code; }
		
		public static void addListener( PickEventListener el, String code ) {
			//System.out.println( code + " added as picklistener from " + ((Pickable)el).getName() );
			if( PickEvent.listeners.indexOf( el ) == - 1 ) {
				PickEvent.listeners.add( el );
				PickEvent.listening.add( code );
			}
		}
		
		//TODO: pick listeners need to be removed
		public static void removeListener( PickEventListener el ) {
		}
		
		public static void fire( PickEvent e ) {
			int pos = PickEvent.listening.indexOf( e.code );
			if( pos != -1 )
				( (PickEventListener)PickEvent.listeners.get( pos ) ).processPickEvent( e );
		}
	}
	
	
}
