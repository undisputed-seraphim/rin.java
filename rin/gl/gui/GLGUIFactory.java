package rin.gl.gui;

import rin.gl.event.TransitionEvent;
import rin.gl.lib3d.properties.Scale;

public class GLGUIFactory {
	private static GLGUI gui = new GLGUI();
	
	public static class Transitions {
		public static final GLGUIEvent SCALE_BURST_SHOW = new GLGUIEvent() {
			@Override public void event() {
				this.target.addScaleTransition( new Scale( 0, 0.02f, 1 ), 1500L ).onFinish( new TransitionEvent() {
					@Override public void run() {
						this.getActor().addScaleTransition( new Scale( 1, 0.02f, 1 ), 600L ).onFinish( new TransitionEvent() {
							@Override public void run() {
								this.getActor().addScaleTransition( new Scale( 1, 1, 1 ), 300L ).onFinish( new TransitionEvent() {
									@Override public void run() {
										GLGUIFactory.getPane( "root" ).hide();
									}
								});
							}
						});
					}
				});
			}
		};
		
		public static final GLGUIEvent SCALE_BURST_HIDE = new GLGUIEvent() {
			@Override public void event() {
				this.target.addScaleTransition( new Scale( 1, 0.02f, 1 ), 300L ).onFinish( new TransitionEvent() {
					@Override public void run() {
						this.getActor().addScaleTransition( new Scale( 0, 0.02f, 1 ), 600L ).onFinish( new TransitionEvent() {
							@Override public void run() {
								System.out.println( "finished hiding" );
								GLGUIFactory.getPane( "root" ).setVisible( false );
							}
						});
					}
				});
			}
		};
	}
	
	public static enum Position {
		BOTTOM
	}
	
	public static GLPane getPane( String id ) { return (GLPane)GLGUIFactory.gui.get( id ); }
	public static GLPane createPane( String id ) {
		GLGUIFactory.gui.add( new GLPane( id ) );
		return GLGUIFactory.getPane( id );
	}
	
 	public static abstract class GLGUIEvent implements Runnable {
 		public GLGUIComponent<?> target = null;
 		public GLGUIEvent setTarget( GLGUIComponent<?> target ) { this.target = target; return this; }
 		
		@Override public final void run() { this.event(); this.finished(); }
		private Runnable runOnFinish = null;
		public GLGUIEvent onFinish( Runnable r ) {
			this.runOnFinish = r;
			return this;
		}
		
		public void finished() {
			if( this.runOnFinish != null )
				this.runOnFinish.run();
		}
		
		public abstract void event();
	}
 	
 	public static void render( long dt ) { GLGUIFactory.gui.render( dt ); }

}
