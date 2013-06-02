package rin.gl.lib3d;

import java.util.ArrayList;
import java.util.HashMap;

import rin.gl.lib3d.properties.Point;
import rin.gl.lib3d.properties.Properties;
import rin.gl.lib3d.properties.Scale;
import rin.gl.lib3d.properties.Transformation;
import rin.gl.lib3d.interfaces.Positionable;
import rin.gl.lib3d.interfaces.Controllable;
import rin.gl.lib3d.interfaces.Animatable;
import rin.gl.lib3d.interfaces.Transitionable;
import rin.gl.event.GLEvent;
import rin.gl.event.Transition;
import rin.gl.event.GLEvent.*;
import rin.gl.lib3d.properties.Position;
import rin.util.math.Mat4;
import rin.util.math.Quat4;
import rin.util.math.Vec3;

public class Actor implements Positionable, Controllable, Animatable, Transitionable {
	private static int items = 0;

	/** Name describing Actor */
	private String name = "No Name";
	public String getName() { return this.name; }
	
	/** Unique color in the format of [ r, g, b ]. Used for Picking. */
	private float[] uniqueColor = new float[] { 0.0f, 0.0f, 0.0f };
	public float[] getUniqueColor() { return this.uniqueColor; }
	public void setUniqueColor( float[] color ) { this.uniqueColor = color; }
	
	public Actor() { this( "Actor-" + Actor.items++, new Properties() ); }
	public Actor( String name ) { this( name, new Properties() ); }
	public Actor( Properties p ) { this( "Actor-" + Actor.items++, p ); }
	public Actor( String name, Properties p ) {
		this.name = name;
		this.setTransformation( p.getTransformation() );
		this.uniqueColor = new float[] { 0.0f, 0.0f, 0.0f };
	}
	
	public Poly asPoly() { return (Poly)this; }
	public Mesh asMesh() { return (Mesh)this; }
	//public SkinnedMesh asSkinnedMesh() { return (SkinnedMesh)this; }
	
	/* -------------- positionable implementation ------------------ */
	private volatile Position position = new Position();
	private volatile Scale scale = new Scale();
	
	private Vec3 rotation = new Vec3();
	private Quat4 qbuf = new Quat4();
	private volatile Mat4 translate = new Mat4(), rotate = new Mat4(), scaled = new Mat4(), matrix = new Mat4();
	
	public Transformation getTransformation() { return new Transformation( this.position.asVec3(), this.rotation, this.scale.asVec3() ); }
	public void setTransformation( Transformation t ) {
		this.setPosition( t.getPosition() );
		this.setRotation( t.getRotation() );
		this.setScale( t.getScale() );
	}
	
	@Override public Mat4 getMatrix() { return this.matrix; }
	@Override public void setMatrix( Mat4 m ) { this.matrix = m; }
	
	@Override public Position getPosition() { return this.position; }
	@Override public Mat4 getPositionMatrix() { return this.translate; }
	@Override public void resetPosition() { this.setPosition( 0.0f, 0.0f, 0.0f ); }
	
	public void setPosition( Vec3 p ) { this.setPosition( p.x, p.y, p.z ); }
	public void setPosition( Position p ) { this.setPosition( p.x, p.y, p.z ); }
	@Override public void setPosition( float x, float y, float z ) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
		this.transform();
	}
	
	private void updatePosition() {
		//this.translate = Mat4.translate( new Mat4(), this.position.asVec3() );
		translate.identity();
		translate.translate( this.position.asVec3() );
	}

	@Override public Vec3 getRotation() { return this.rotation; }
	@Override public Mat4 getRotationMatrix() { return this.rotate; }
	@Override public void resetRotation() { this.setRotation( 0.0f, 0.0f, 0.0f ); }
	
	public void setRotation( Vec3 r ) { this.setRotation( r.x, r.y, r.z ); }
	@Override public void setRotation( float x, float y, float z ) {
		this.rotation.redefine( x * Quat4.PIOVER180, y * Quat4.PIOVER180, z * Quat4.PIOVER180 );
		this.transform();
	}
	
	private void updateRotation() {
		/*Quat4	rotateX = Quat4.create( Vec3.X_AXIS, this.rotation.x * Quat4.PIUNDER180 ),
				rotateY = Quat4.create( Vec3.Y_AXIS, this.rotation.y * Quat4.PIUNDER180 ),
				rotateZ = Quat4.create( Vec3.Z_AXIS, this.rotation.z * Quat4.PIUNDER180 );
		this.rotate = Quat4.multiply( Quat4.multiply( rotateX, rotateY ), rotateZ ).toMat4();*/
		qbuf.identity();
		qbuf.applyOrientationRad( Vec3.X_AXIS, rotation.x );
		qbuf.applyOrientationRad( Vec3.Y_AXIS, rotation.y );
		qbuf.applyOrientationRad( Vec3.Z_AXIS, rotation.z );
		qbuf.intoMat4( rotate );
	}

	@Override public Scale getScale() { return this.scale; }
	@Override public Mat4 getScaleMatrix() { return this.scaled; }
	@Override public void resetScale() { this.setScale( 1.0f, 1.0f, 1.0f ); }
	
	public void setScale( Vec3 s ) { this.setScale( s.x, s.y, s.z ); }
	@Override public void setScale( float x, float y, float z ) {
		this.scale.x = x;
		this.scale.y = y;
		this.scale.z = z;
		this.transform();
	}
	
	private void updateScale() { this.scaled = Mat4.scale( new Mat4(), this.scale.asVec3() ); }
	
	@Override public void spin( float xaxis, float yaxis, float zaxis ) {
		this.rotation.x += xaxis;
		this.rotation.y += yaxis;
		this.rotation.z += zaxis;
		this.transform();
	}
	
	@Override public void move( float step, float side, float rise ) {
		this.position.x += this.rotate.m[ 8] * step + ( this.rotate.m[0] * side ) + ( this.rotate.m[4] * rise );
		this.position.y += this.rotate.m[ 9] * step + ( this.rotate.m[1] * side ) + ( this.rotate.m[5] * rise );
		this.position.z += this.rotate.m[10] * step + ( this.rotate.m[2] * side ) + ( this.rotate.m[6] * rise );
		this.transform();
	}
	
	public void lookAt( float x, float y, float z ) {
		float posx = this.position.x;
		float posy = this.position.y;
		float posz = this.position.z;

		//this.setMatrix( Mat4.multiply( GL.get().getCamera().getPerspectiveMatrix(), Mat4.lookAt(posx, posy, posz, x, y, z, 0, 1, 0) ) );
		Mat4 m = Mat4.lookAt(posx, posy, posz, x, y, z, 0, 1, 0);
        double rx = Math.atan2( m.m[6], m.m[10] );
        double tmp2 = Math.sqrt( m.m[0] * m.m[0] + m.m[1] * m.m[1] );
        double ry = Math.atan2( -m.m[2], tmp2 );
        double rz = Math.atan2( m.m[1], m.m[0] );
        System.out.println( "angles?: " + rx + " " + ry + " " + rz );
        this.setRotation( (float)rx, (float)ry, this.getRotation().z );
	}
	
	public void applyTranslation( float x, float y, float z ) {
		position.x += z;
		position.y += y;
		position.z += z;
		updatePosition();
	}
	
	public void applyRotation( float x, float y, float z, float w ) {
		rotate = new Quat4( x, y, z, w ).toMat4();
	}
	
	public void applyScale( float x, float y, float z ) {
		Mat4.scale( new Mat4(), new Vec3( x, y, z ) );
	}
	
	@Override public void transform() {
		this.updatePosition();
		this.updateRotation();
		this.updateScale();
		//this.matrix = Mat4.multiply( Mat4.multiply( Mat4.multiply( new Mat4(), this.scaled ), this.rotate ), this.translate );
		matrix.identity();
		matrix.multiply( scaled );
		matrix.multiply( rotate );
		matrix.multiply( translate );
	}

	@Override public Actor destroy() {
		this.setKeyboardControlled( false );
		this.setMouseControlled( false );
		
		return null;
	}
	
	
	/* ------------- controllable implementation ------------- */
	private boolean keyboardListening = false;	
	private boolean keyboardControlled = false;
	@Override public boolean isKeyboardControlled() { return this.keyboardControlled; }
	@Override public void setKeyboardControlled( boolean val ) {
		this.keyboardControlled = val;
		if( val && !this.keyboardListening ) {
			GLEvent.addKeyEventListener( this );
			this.keyboardListening = true;
		} else if( this.keyboardListening ) {
			GLEvent.removeKeyEventListener( this );
			this.keyboardListening = false;
		}
	}
	
	private boolean mouseListening = false;
	private boolean mouseControlled = false;
	@Override public boolean isMouseControlled() { return this.mouseControlled; }
	@Override public void setMouseControlled( boolean val ) {
		this.mouseControlled = val;
		if( val && !this.mouseListening ) {
			GLEvent.addMouseEventListener( this );
			this.mouseListening = true;
		} else if( this.mouseListening ) {
			GLEvent.removeMouseEventListener( this );
			this.mouseListening = false;
		}
	}

	@Override public void processKeyUpEvent( KeyUpEvent e ) {}
	@Override public void processKeyDownEvent( KeyDownEvent e ) {}
	@Override public void processKeyRepeatEvent( KeyRepeatEvent e ) {}
	@Override public void processMouseUpEvent( MouseUpEvent e ) {}
	@Override public void processMouseDownEvent( MouseDownEvent e ) {}
	@Override public void processMouseMoveEvent( MouseMoveEvent e ) {}
	@Override public void processMouseRepeatEvent( MouseRepeatEvent e ) {}
	@Override public void processMouseWheelEvent( MouseWheelEvent e ) {}
	
	/* ----------------- animatable implementation ------------------- */
	private ArrayList<GLEvent> events = new ArrayList<GLEvent>();
	
	private boolean animating = false;
	@Override public boolean isAnimatable() { return this.animating; }
	@Override public void setAnimatable( boolean val ) {
		GLEvent.addAnimationEventListener( this );
		this.setIgnoreAnimations( false );
	}
	
	private boolean animationIgnore = true;
	public boolean isIgnoringAnimations() { return this.animationIgnore; }
	public void setIgnoreAnimations( boolean val ) {
		this.animationIgnore = val;
	}
	
	@Override public void processTickEvent( TickEvent e ) {
		if( !animationIgnore ) {
			System.out.println( "test tick" );
		}
	}
	
	public void addEvent( GLEvent e ) {
		synchronized( this.events ) {
			this.events.add( e );
		}
	}
	
	public void update( long dt ) {
		
		boolean updated = false;
		
		for( String t : this.transitions.keySet() ) {
			updated = true;
			synchronized( this.transitions.get( t ) ) {
				this.transitions.get( t ).update( dt );
			}
		}
		
		if( updated )
			this.transform();
		
		/*this.spin( -0.01f, 0, 0 );
		synchronized( this.events ) {
			for( GLEvent e : this.events ) {
				if( e instanceof Transition ) {
					System.out.println( "transition" );
					if( this.position.y >= 0.0f )
						this.move( 0.0f, 0.0f, -0.01f );
				}
				else if( e instanceof KeyEvent ) {
					System.out.println( "keyevent" );
					if( this.position.y <= 0.0f ) {
						this.move( 0.0f, 0.0f, 1.0f );
					}
				}
			}
		}*/
	}
	
	/* ---------------- transitionable implementation ------------ */
	private HashMap<String, Transition<?>> transitions = new HashMap<String, Transition<?>>();
	
	public Transition<?> addPositionTransition( Position to, long duration ) {
		if( this.transitions.get( "position" ) != null ) {
			synchronized( this.transitions.get( "position" ) ) {
				this.transitions.put( "position", new Transition.Interpolate<Point>( this.getPosition(), to, duration ).setActor( this ) );
			}
		} else {
			this.transitions.put( "position", new Transition.Interpolate<Point>( this.getPosition(), to, duration ).setActor( this ) );
		}
		return this.transitions.get( "position" );
	}
	
	public void removePositionTransition() {
		if( this.transitions.get( "position" ) != null ) {
			synchronized( this.transitions.get( "position" ) ) {
				this.transitions.remove( "position" );
			}
		}
	}
	
	public Transition<?> addScaleTransition( Scale to, long duration ) {
		if( this.transitions.get( "scale" ) != null ) {
			synchronized( this.transitions.get( "scale" ) ) {
				this.transitions.put( "scale", new Transition.Interpolate<Point>( this.getScale(), to, duration ).setActor( this ) );
			}
		} else {
			this.transitions.put( "scale", new Transition.Interpolate<Point>( this.getScale(), to, duration ).setActor( this ) );
		}
		return this.transitions.get( "scale" );
	}
	
	public void removeScaleTransition() {
		if( this.transitions.get( "scale" ) != null ) {
			synchronized( this.transitions.get( "scale" ) ) {
				this.transitions.remove( "scale" );
			}
		}
	}
	
	@Override public void applyTransition( Transition<?> t, long dt ) {
	}
	
	@Override public void onTransitionBegin( Transition<?> t ) {
		System.out.println( "Transition starting!" );
	}
	
	@Override public void onTransitionEnd( Transition<?> t ) {
		System.out.println( "Transition ending!" );
	}
}
