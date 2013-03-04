package rin.gl.lib3d.data;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import rin.engine.Engine;
import rin.gl.Input;
import rin.gl.Scene;
import rin.gl.lib3d.Actor;
import rin.gl.lib3d.ActorList;
import rin.gl.lib3d.Camera;
import rin.gl.lib3d.Poly;
import rin.gl.model.ModelManager;
import rin.util.Buffer;
import rin.util.IO;

public class GLRenderThread extends Thread {
	private static GLRenderThread _instance = null;
	
	private static ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<Runnable>();
	private static boolean destroyRequested = false;
	public static void requestDestroy() { GLRenderThread.destroyRequested = true; }
	
	private int width, height;
	public GLRenderThread( int width, int height ) {
		super( "rin.ai | Render Thread" );
		GLRenderThread.destroyRequested = false;
		this.width = width;
		this.height = height;
		this.start();
	}
	
	public static void init( int width, int height ) { GLRenderThread._instance = new GLRenderThread( width, height ); }
	public static GLRenderThread get() { return GLRenderThread._instance; }
	
	private Camera camera = null;
	private int program, vShader, fShader;
	public void run() {
		try {
			Display.setDisplayMode( new DisplayMode( this.width, this.height ) );
			Display.create();
			Display.setVSyncEnabled( true );
			/*Engine.scene = new Scene( width, height );
			if( Engine.scene.isReady() ) {
				Engine.scene.getCamera().init();
				Engine.started = true;

				Engine.createDebugWindow();
			}*/
		} catch( LWJGLException e ) {
			System.out.println( "lwjgl instance failed to display [" + this.width + "x" + this.height + "]" );
		}
		
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
		
		glViewport( 0, 0, this.width, this.height );

		this.camera = new Camera( 45, this.width / this.height, 0.1f, 15f );
		this.camera.init();
		this.loop();
	}
	
	public int getAttrib( String str ) { return glGetAttribLocation( this.program, str ); }
	public int getUniform( String str ) { return glGetUniformLocation( this.program, str ); }
	
	public static void addActor( final ModelManager.Format format, final String file ) {
		GLRenderThread.queue.add( new Runnable() {
			public void run() {
				ActorList.add( ModelManager.create( format, file ) );
				//ActorList.get().getActors().get( 0 ).addEvent( new Transition( (Transitionable)ActorList.get().getActors().get(0) ) );
			}
		});
	}
	
	private void loop() {
		Runnable current = null;
		while( !GLRenderThread.destroyRequested && !Display.isCloseRequested() ) {
			while( ( current = GLRenderThread.queue.poll() ) != null )
				current.run();
			
			glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT );
			this.camera.update();
			Input.process();
			
			glUniform1i( this.getUniform( "useUnique" ), GL_TRUE );
			for( Actor a : ActorList.get().getActors() )
				( (Poly) a ).render( true );
			glUniform1i( this.getUniform( "useUnique" ), GL_FALSE );
			
			Scene.uniqueAtMouse = Buffer.toString( this.camera.getMouseRGB() );
			glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT );
			
			for( Actor a : ActorList.get().getActors() )
				( (Poly) a ).render();
			
			Display.update();
			Display.sync( 60 );
			
			try {
				Thread.sleep( 1L );
			} catch( InterruptedException e ) {
				
			}
		}
		this.destroy();
	}
	
	public void destroy() {
		Display.destroy();
		Engine.destroy();
	}
	
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

}
