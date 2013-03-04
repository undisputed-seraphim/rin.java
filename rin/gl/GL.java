package rin.gl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import rin.engine.Engine;
import rin.gl.font.Font;
import rin.gl.lib3d.Camera;
import rin.gl.lib3d.data.GLSource;
import rin.sample.GSGL;
import rin.sample.States;
import rin.system.SingletonThread;
import rin.util.IO;

public class GL extends SingletonThread {
	private static GL instance;
	private static int width = 900, height = 600;
	public static GL get() {
		if( GL.instance == null )
			GL.instance = new GL( GL.width, GL.height );
		
		return GL.instance;
	}
	
	public static ConcurrentLinkedQueue<GLSource> sources = new ConcurrentLinkedQueue<GLSource>();
	
	private Camera camera;
	
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

	public static void init() { GL.instance = new GL(); }
	public static void init( int width, int height ) { GL.instance = new GL( width, height ); }
	
	public GL() { this( 900, 600 ); }
	public GL( int width, int height ) {
		super( "rin.ai | Render Thread" );
		GL.width = width;
		GL.height = height;
		super.start();
	}
	
	@Override public void preload() {
		int width = GL.width;
		int height = GL.height;
		
		try {
			Display.setDisplayMode( new DisplayMode( width, height ) );
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
	}
	
	@Override public void main() {
		if( !Display.isCloseRequested() ) {
			System.out.println( "gl loop" );
			glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT );
			this.camera.update();
		
			Display.update();
			Display.sync( 60 );
		} else {
			this.requestDestroy();
		}
	}
}