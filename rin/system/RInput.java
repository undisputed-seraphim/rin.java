package rin.system;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import javax.swing.JFrame;

public class RInput extends Thread implements KeyListener {
	private static JFrame dummy = new JFrame();
	private static enum Poll { KEYBOARD, MOUSE, BOTH; };
	private static RInput _instance = null;
	private static RInput get() { return RInput._instance; }
	
	public static void trackInput() {
		/* if there already is an instance, ensure it is listening to both */
		if( RInput._instance != null ) {
		} else {
			RInput._instance = new RInput( "rin.ai | Input Thread", RInput.Poll.BOTH );
		}
	}
	
	private boolean destroyRequested = false;
	public void requestDestroy() { this.destroyRequested = true; }
	public boolean isDestroyRequested() { return this.destroyRequested; }
	
	private HashMap<Integer, Boolean> keys = new HashMap<Integer, Boolean>();
	private Poll poll;
	
	private RInput( String name, RInput.Poll poll ) {
		super.setName( name );
		this.poll = poll;
		super.start();
	}
	
	@Override public void run() {
		RInput.dummy.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		RInput.dummy.addKeyListener( this );
		RInput.dummy.setVisible( true );
		
		while( !this.destroyRequested ) {
			try {
				Thread.sleep( 1L );
			} catch( InterruptedException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		this.destroy();
	}
	
	public void destroy() {
		RInput.dummy.removeKeyListener( this );
	}
	
	public void listenForMouse() {}
	
	public static void initMouse() {}
	
	public void listenForKeyboard() {}
	
	public static void initKeyboard() {
	}
	
	public static class Keyboard {
		public static int KEY_A = KeyEvent.VK_A;
		
		public static boolean isKeyDown( int key ) {
			if( RInput._instance != null )
				return RInput.get().keys.containsKey( key ) ? RInput.get().keys.get( key ) : false;
				
			System.out.println( "RInput must be initialized first. Use RInput.init();." );
			return false;
		}
	}

	@Override public final void keyPressed( KeyEvent e ) {
		System.out.println( "Key pressed" );
		this.keys.put( e.getKeyCode(), true );
	}

	@Override public final void keyReleased( KeyEvent e ) {
		System.out.println( "Key released" );
		this.keys.put( e.getKeyCode(), false );
	}
	
	@Override public final void keyTyped( KeyEvent e ) {}
}
