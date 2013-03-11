package rin.gl.gui;

import rin.gl.event.TransitionEvent;
import rin.gl.lib3d.Poly;
import rin.gl.lib3d.properties.Scale;

public class GLGUIFactory {
	private static GLGUI gui;
	public static GLGUI getRootGUI() { return GLGUIFactory.gui; }
	
	public static void init( int width, int height ) {
		GLGUIFactory.gui = new GLGUI( width, height );
	}
	
	public static class Transitions {
		public static final GLGUIEvent SCALE_BURST_SHOW = new GLGUIEvent() {
			@Override public void event() {
				this.target.addScaleTransition( new Scale( 0, 0.02f, 1 ), 1500L ).onFinish( new TransitionEvent() {
					@Override public void run() {
						this.getActor().addScaleTransition( new Scale( 1, 0.02f, 1 ), 600L ).onFinish( new TransitionEvent() {
							@Override public void run() {
								this.getActor().addScaleTransition( new Scale( 1, 1, 1 ), 300L ).onFinish( new TransitionEvent() {
									@Override public void run() {
										//GLGUIFactory.getPane( "root" ).hide();
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
		TOP,
		BOTTOM,
		CENTER,
		LEFT,
		RIGHT
	}
	
	public static enum Alignment {
		LEFT,
		CENTER,
		RIGHT
	}
	
	public static class GLGUIParams {

		public Integer width, height;
		public int padding, margin;
		public Position position;
		public Alignment alignment;
		
		public GLGUIParams() {
			this.setWidth( null ).setHeight( null );
			this.setPadding( 20 ).setMargin( 20 );
			this.setPosition( Position.RIGHT ).setAlignment( Alignment.CENTER );
		}
		
		public GLGUIParams setWidth( Integer width ) { this.width = width; return this; }
		public GLGUIParams setHeight( Integer height ) { this.height = height; return this; }
		public GLGUIParams setPadding( int padding ) {this.padding = padding; return this; }
		public GLGUIParams setMargin( int margin ) { this.margin = margin; return this; }
		public GLGUIParams setPosition( Position position ) { this.position = position; return this; }
		public GLGUIParams setAlignment( Alignment alignment ) { this.alignment = alignment; return this; }
		
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
 	
	public static GLPane getPane( String id ) { return (GLPane)GLGUIFactory.gui.get( id ); }
	public static GLPane createPane( String id ) {
		GLGUIFactory.gui.add( new GLPane( id ) );
		return GLGUIFactory.getPane( id );
	}
 	
 	public static void render( long dt ) { GLGUIFactory.gui.render( dt ); }

}
