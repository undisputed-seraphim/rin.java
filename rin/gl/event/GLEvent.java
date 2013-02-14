package rin.gl.event;

import java.util.ArrayList;
import java.util.Iterator;

import rin.gl.event.GLEventListener.*;
import rin.gl.lib3d.interfaces.Animatable;
import rin.gl.lib3d.interfaces.Pickable;

public class GLEvent {
	public static enum State {
		IN,
		OUT,
		UP,
		DOWN,
		REPEAT,
		MOVED,
		WHEEL,
		TICK;
	}
	
	public GLEvent() {}
	
	public static void addKeyEventListener( KeyEventListener src ) { KeyEvent.addListener( src ); }
	public static void addMouseEventListener( MouseEventListener src ) { MouseEvent.addListener( src ); }
	public static void addPickEventListener( Pickable src ) { PickEvent.addListener( src ); }
	public static void addAnimationEventListener( Animatable src ) { AnimationEvent.addListener( src ); }
	
	public static void removeKeyEventListener( KeyEventListener src ) { KeyEvent.removeListener( src ); }
	public static void removeMouseEventListener( MouseEventListener src ) { MouseEvent.removeListener( src ); }
	public static void removePickEventListener( Pickable src ) { PickEvent.removeListener( src ); }

	public static void removeAllKeyEventListeners() { KeyEvent.removeAll(); }
	public static void removeAllMouseEventListeners() {}
	public static void removeAllPickEventListeners() {}
	
	public static void fire( KeyEvent e ) { KeyEvent.fire( e ); }
	public static void fire( MouseEvent e ) { MouseEvent.fire( e ); }
	public static void fire( PickEvent e ) { PickEvent.fire( e ); }
	public static void fire( AnimationEvent e ) { AnimationEvent.fire( e ); }
	
	
	public static class KeyEvent extends GLEvent {		
		private static ArrayList<KeyEventListener> listeners = new ArrayList<KeyEventListener>();
		
		public int key;
		public State state;
		
		public KeyEvent( int key, State state ) { this.key = key; this.state = state; }
		
		public static void addListener( KeyEventListener el ) {
			if( KeyEvent.listeners.indexOf( el ) == -1 )
				KeyEvent.listeners.add( el );
		}
		
		public static void removeListener( KeyEventListener el ) {
			if( KeyEvent.listeners.indexOf( el ) != -1 )
				KeyEvent.listeners.remove( el );
		}
		
		public static void removeAll() {
			
		}
		
		public static void fire( KeyEvent e ) {
			Iterator<KeyEventListener> i = KeyEvent.listeners.iterator();
			while( i.hasNext() ) {
				switch( e.state ) {
				
				case UP: ( (KeyEventListener)i.next() ).processKeyUpEvent( (KeyUpEvent)e ); break;
				case DOWN: ( (KeyEventListener)i.next() ).processKeyDownEvent( (KeyDownEvent)e ); break;
				case REPEAT: ( (KeyEventListener)i.next() ).processKeyRepeatEvent( (KeyRepeatEvent)e ); break;
				default: i.next(); break;
				
				}
			}
		}
	}
	public static class KeyDownEvent extends KeyEvent { public KeyDownEvent( int key ) { super( key, State.DOWN ); } }
	public static class KeyUpEvent extends KeyEvent { public KeyUpEvent( int key ) { super( key, State.UP ); } }
	public static class KeyRepeatEvent extends KeyEvent { public KeyRepeatEvent( int key ) { super( key, State.REPEAT ); } }
	
	
	public static class MouseEvent extends GLEvent {
		private static ArrayList<MouseEventListener> listeners = new ArrayList<MouseEventListener>();
		public static final int BUTTON_LEFT = 0,
								BUTTON_RIGHT = 1,
								WHEEL = 500;
		
		public int button;
		public State state;
		public int x;
		public int y;
		
		public MouseEvent( int button, State state, int x, int y ) {
			this.button = button;
			this.state = state;
			this.x = x;
			this.y = y;
		}
		
		public static void addListener( MouseEventListener el ) {
			if( MouseEvent.listeners.indexOf( el ) == -1 )
				MouseEvent.listeners.add( el );
		}
		
		public static void removeListener( MouseEventListener el ) {
			if( MouseEvent.listeners.indexOf( el ) != -1 )
				MouseEvent.listeners.remove( el );
		}
		
		public static void fire( MouseEvent e ) {
			Iterator<MouseEventListener> i = MouseEvent.listeners.iterator();
			while( i.hasNext() ) {
				switch( e.state ) {
				
				case UP: ( (MouseEventListener)i.next() ).processMouseUpEvent( (MouseUpEvent)e ); break;
				case DOWN: ( (MouseEventListener)i.next() ).processMouseDownEvent( (MouseDownEvent)e ); break;
				case MOVED: ( (MouseEventListener)i.next() ).processMouseMoveEvent( (MouseMoveEvent)e ); break;
				case REPEAT: ( (MouseEventListener)i.next() ).processMouseRepeatEvent( (MouseRepeatEvent)e ); break;
				case WHEEL: ( (MouseEventListener)i.next() ).processMouseWheelEvent( (MouseWheelEvent)e ); break;
				default: i.next(); break;
				
				}
			}
		}
	}
	public static class MouseDownEvent extends MouseEvent {
		public MouseDownEvent( int button, int x, int y ) { super( button, State.DOWN, x, y ); }
	}
	public static class MouseUpEvent extends MouseEvent {
		public MouseUpEvent( int button, int x, int y ) { super( button, State.UP, x, y ); }
	}
	public static class MouseMoveEvent extends MouseEvent {
		public int dx, dy;
		public MouseMoveEvent( int button, int x, int y, int dx, int dy ) {
			super( button, State.MOVED, x, y );
			this.dx = dx;
			this.dy = dy;
		}
	}
	public static class MouseRepeatEvent extends MouseEvent {
		public MouseRepeatEvent( int button, int x, int y ) { super( button, State.REPEAT, x, y ); }
	}
	public static class MouseWheelEvent extends MouseEvent {
		public int delta;
		public MouseWheelEvent( int x, int y, int delta ) {
			super( MouseEvent.WHEEL, State.WHEEL, x, y );
			this.delta = delta;
		}
	}
	
	
	public static class PickEvent extends GLEvent {
		private static ArrayList<PickEventListener> listeners = new ArrayList<PickEventListener>();
		private static ArrayList<String> listening = new ArrayList<String>();
		
		public String code;
		public State state;
		
		public PickEvent( String code, State state ) { this.code = code; this.state = state; }
		
		public static void addListener( Pickable el ) {
			float tmp[] = el.getUniqueColor();
			if( PickEvent.listeners.indexOf( el ) == - 1 ) {
				PickEvent.listeners.add( el );
				PickEvent.listening.add( "FloatArray[ "+(float)((int)(tmp[0] * 255) & 0xff)+" "+
						(float)((int)(tmp[1] * 255) & 0xff)+" "+(float)((int)(tmp[2] * 255) & 0xff)+" ]" );
				System.out.println( "added " + "FloatArray[ "+(float)((int)(tmp[0] * 255) & 0xff)+" "+
						(float)((int)(tmp[1] * 255) & 0xff)+" "+(float)((int)(tmp[2] * 255) & 0xff)+" ]" );
			}
		}
		
		public static void removeListener( Pickable el ) {
			if( PickEvent.listeners.indexOf( el ) != - 1 ) {
				PickEvent.listening.remove( PickEvent.listeners.indexOf( el ) );
				PickEvent.listeners.remove( el );
			}
		}
		
		public static void fire( PickEvent e ) {
			int pos = PickEvent.listening.indexOf( e.code );
			if( pos != -1 ) {
				switch( e.state ) {
				
				case IN: ( (PickEventListener)PickEvent.listeners.get( pos ) ).processPickInEvent( (PickInEvent)e ); break;
				case OUT: ( (PickEventListener)PickEvent.listeners.get( pos ) ).processPickOutEvent( (PickOutEvent)e ); break;
				case REPEAT: ( (PickEventListener)PickEvent.listeners.get( pos ) ).processPickRepeatEvent( (PickRepeatEvent)e ); break;
				default: break;
				
				}
			}
		}
	}
	public static class PickInEvent extends PickEvent { public PickInEvent( String code ) { super( code, State.IN ); } }
	public static class PickOutEvent extends PickEvent { public PickOutEvent( String code ) { super( code, State.OUT ); } }
	public static class PickRepeatEvent extends PickEvent { public PickRepeatEvent( String code ) { super( code, State.REPEAT ); } }
	
	public static class AnimationEvent extends GLEvent {
		private static ArrayList<AnimationEventListener> listeners = new ArrayList<AnimationEventListener>();

		public State state;
		
		public AnimationEvent( State state ) { this.state = state; }
		
		public static void addListener( Animatable a ) {
			AnimationEvent.listeners.add( a );
		}
		
		public static void fire( AnimationEvent e ) {
			Iterator<AnimationEventListener> i = AnimationEvent.listeners.iterator();
			while( i.hasNext() ) {
				switch( e.state ) {
				
				case TICK: ( (AnimationEventListener)i.next() ).processTickEvent( (TickEvent)e ); break;
				default: i.next(); break;
				
				}
			}
		}
		
	}
	public static class TickEvent extends AnimationEvent {
		public float dt;
		public TickEvent( float dt ) { super( State.TICK); this.dt = dt; }
	}
}
