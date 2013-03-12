package rin.gl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import rin.engine.Engine;
import static rin.engine.Engine.*;
import rin.gl.gui.GLGUI;
import rin.gl.gui.GLGUIFactory;
import static rin.gl.gui.GLGUIFactory.*;
import rin.gl.lib3d.Actor;
import rin.gl.lib3d.Camera;
import rin.gl.lib3d.Poly;
import rin.gl.lib3d.properties.Scale;
import rin.gl.lib3d.shape.Shape;
import rin.gl.lib3d.shape.Sphere;
import rin.gl.model.ModelLoader;
import rin.gl.model.ModelManager;
import rin.engine.Engine.ModelParams;
import rin.gui.GUIFactory;
import rin.sample.States;
import rin.system.Loader;
import rin.system.SingletonThread;
import rin.util.IO;
import rin.util.math.Mat4;

public class GL extends SingletonThread<GL> {
	private static GL instance = null;
	public static GL get() { return GL.instance; }
	
	private static int width = 900, height = 600;
	public int getWidth() { return GL.width; }
	public int getHeight() { return GL.height; }

	public static ConcurrentLinkedQueue<Runnable> sources = new ConcurrentLinkedQueue<Runnable>();
	
	private volatile Camera camera;
	public Camera getCamera() { return this.camera; }
	
	private int vShader = -1, fShader = -1, program = -1;
	public int getProgram() { return this.program; }
	public static int getAttrib( String str ) { return GL.get().getAttributeLocation( str ); }
	public static int getUniform( String str ) { return GL.get().getUniformLocation( str ); }
	
	public int getAttributeLocation( String str ) { return glGetAttribLocation( this.program, str ); }
	public int getUniformLocation( String str ) { return glGetUniformLocation( this.program, str ); }
	
	private String getShaderSource( String shader ) { return IO.file.asString( Engine.SHADER_DIR + shader ); }
	private int createShader( int type, String src ) {
		int shader = glCreateShader( type );
		glShaderSource( shader, src );
		glCompileShader( shader );
		if( glGetShaderi( shader, GL_COMPILE_STATUS ) == 0 ) {
			System.out.println( "Shader ["+ (type == GL_VERTEX_SHADER ? "vertex" : "fragment") +"] failed to compile." );
			return -1;
		}
		return shader;
	}
	
	private String actorAtMouse = "";
	public static String getUniqueAtMouse() { return GL.get().actorAtMouse; }
	public static void setUniqueAtMouse( String actor ) { GL.get().actorAtMouse = actor; }
	
	private static int r = 0, g = 0, b = 0;
	public static float[] getNextColor() {
		if( GL.r < 255 )
			GL.r ++;
		else if( GL.g < 255 ) {
			GL.g ++;
			GL.r = 0;
		} else {
			GL.b ++;
			GL.r = 0;
			GL.g = 0;
		}
		
		float[] tmp = new float[] { GL.r, GL.g, GL.b };
		return new float[]{ tmp[0] / 255, tmp[1] / 255, tmp[2] / 255 };
	}

	public static void init() { GL.instance = new GL( GL.width, GL.height ); }
	public static void init( int width, int height ) { GL.instance = new GL( width, height ); }
	
	public static Loader<Actor> addModel( final ModelParams p ) {
		final Loader<Actor> lr = new Loader<Actor>();
		GL.sources.add( new Runnable() {
			public void run() {
				Actor actor = ModelManager.create( p.format, p.name );
				synchronized( GLScene.getActors() ) {
					GLScene.getActors().add( actor );
					lr.setTarget( actor ).loaded();
				}
			}
		});
		return lr;
	}
	
	public static Loader<Actor> addShape( final ShapeParams p ) {
		final Loader<Actor> lr = new Loader<Actor>();
		GL.sources.add( new Runnable() {
			public void run() {
				Actor actor = Shape.create( p );
				
				synchronized( GLScene.getActors() ) {
					GLScene.getActors().add( actor );
					lr.setTarget( actor ).loaded();
				}
			}
		});
		return lr;
	}
	
	public GL( int width, int height ) {
		super( "rin.ai | Render Thread" );
		GL.width = width;
		GL.height = height;
	}
	
	@Override public void preload() {
		int width = GL.width;
		int height = GL.height;
		
		try {
			Display.setDisplayMode( new DisplayMode( width, height ) );
			//Display.setParent( GUIFactory.getCanvas( "canvas" ).getCanvas() );
			Display.create();
			Display.setVSyncEnabled( true );
		} catch( LWJGLException e ) {
			System.out.println( "lwjgl instance failed to display [" + width +" "+ height + "]" );
			return;
		}
		
		GL.r = 0;
		GL.g = 0;
		GL.b = 0;
		
		/* create and compile vertex shader */
		this.vShader = this.createShader( GL_VERTEX_SHADER, this.getShaderSource( "vertex.shader" ) );
		if( this.vShader == -1 )
			return;
		
		/* create and compile fragment shader */
		this.fShader = this.createShader( GL_FRAGMENT_SHADER, this.getShaderSource( "fragment.shader" ) );
		if( this.fShader == -1 )
			return;
		
		/* create program and attach shaders */
		this.program = glCreateProgram();
		glAttachShader( this.program, this.vShader );
		glAttachShader( this.program, this.fShader );
		
		/* bind attribute locations */
		glBindAttribLocation( this.program, 0, "vertex" );
		glBindAttribLocation( this.program, 1, "normal" );
		glBindAttribLocation( this.program, 2, "texture" );
		glBindAttribLocation( this.program, 3, "color" );
		//glBindAttribLocation( this.program, 3, "bone" );
		//glBindAttribLocation( this.program, 4, "weight" );
		
		/* link the program to the gl context */
		glLinkProgram( this.program );
		if( glGetProgrami( this.program, GL_LINK_STATUS ) == 0 ) {
			System.out.println( "Program failed to link." );
			return;
		}
		
		/* init all actors that need to be initialized (textures / buffers) */
		/*for( Actor a : this.actors )
			if( a instanceof Mesh )
				( ( Mesh ) a ).init();*/
		
		glUseProgram( this.program );
		
		glEnable( GL_DEPTH_TEST );
		glDepthFunc( GL_LEQUAL );
		
		glEnable( GL_BLEND );
		glBlendFunc( GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA );
		
		glClearColor( 1.0f, 1.0f, 1.0f, 1.0f );
		glEnable( GL_TEXTURE_2D );
		//glActiveTexture( GL_TEXTURE0 );
		glUniform1i( glGetUniformLocation( this.program, "sampler" ), 0 );
		
		//this.font = new Font( Engine.FONT_DIR + "ff6.png" );
		//TextureManager.init();
		//glUniform1i( this.getUniform( "samplerA" ), TextureManager.array );
		
		glViewport( 0, 0, width, height );

		this.camera = new Camera( 45, width / height, 0.1f, 15.0f );
		this.camera.init();
		
		GLGUIFactory.init( GL.width, GL.height );
		
		GLGUIFactory.createPane( "root" )
				.onShow( Transitions.SCALE_BURST_SHOW )
				.onHide( Transitions.SCALE_BURST_HIDE )
				.onClick( new GLGUIFactory.GLGUIEvent() {
					
					@Override public void event() {
						System.out.println( "clicked" );
					}
					
				})
				.show();
	}
	
	@Override public void main() {
		Runnable tmp;
		if( !Display.isCloseRequested() ) {
			while( (tmp = GL.sources.poll() ) != null )
				tmp.run();
			
			glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT );
			/* draw the GLGUI object */
			glUniformMatrix4( GL.getUniform( "vMatrix" ), false, Mat4.GUI.gl() );
			glUniformMatrix4( GL.getUniform( "mMatrix" ), false, Mat4.IDENTITY.gl() );
			GLGUIFactory.render( this.getDt() );
			
			Input.process();
			//System.out.println( Keyboard.isKeyDown( Keyboard.KEY_W ) );
			this.camera.update( this.getDt() );

			for( Actor a : GLScene.getActors() ) {
				((Poly)a).render();
			}
			
			Display.update();
			Display.sync( 60 );
		} else {
			this.requestDestroy();
		}
		
	}
	
	@Override public void destroy() {
		GL.r = 0;
		GL.g = 0;
		GL.b = 0;
		
		for( Actor a : GLScene.getActors() )
			a = a.destroy();
		GLScene.get().requestDestroy();
		
		TextureManager.destroy();
		Display.destroy();
	}
}