package rin.engine.scene.gl;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL20.*;

import rin.engine.resource.Resource;
import rin.engine.resource.ResourceManager;
import rin.engine.scene.Scene;

public abstract class GLScene implements Scene {

	private int vShader = -1;
	private int fShader = -1;
	private int program = -1;
	
	protected int getProgram() { return program; }
	protected int getAttribute( String str ) { return glGetAttribLocation( program, str ); }
	protected int getUniform( String str ) { return glGetUniformLocation( program, str ); }
	
	private int createShader( int type, Resource res ) {
		int shader = glCreateShader( type );
		glShaderSource( shader, res.toString() );
		glCompileShader( shader );
		if( glGetShaderi( shader, GL_COMPILE_STATUS ) == 0 ) {
			System.out.println( "Shader ["+ (type == GL_VERTEX_SHADER ? "vertex" : "fragment") +"] failed to compile." );
			return -1;
		}
		return shader;
	}
	
	@Override
	public void init() {
		System.out.println( "GLScene3D#init()" );
		/* create and compile vertex shader */
		vShader = createShader( GL_VERTEX_SHADER, ResourceManager.getPackResource( "rin", "shaders", "vertex.shader" ) );
		if( vShader == -1 )
			return;
		
		/* create and compile fragment shader */
		fShader = createShader( GL_FRAGMENT_SHADER, ResourceManager.getPackResource( "rin", "shaders", "fragment.shader" ) );
		if( fShader == -1 )
			return;
		
		/* create program and attach shaders */
		program = glCreateProgram();
		glAttachShader( program, vShader );
		glAttachShader( program, fShader );
		
		/* bind attribute locations */
		glBindAttribLocation( program, 0, "vertex" );
		glBindAttribLocation( program, 1, "normal" );
		glBindAttribLocation( program, 2, "texture" );
		glBindAttribLocation( program, 3, "color" );
		glBindAttribLocation( program, 4, "bone" );
		glBindAttribLocation( program, 5, "weight" );
		
		/* link the program to the gl context */
		glLinkProgram( program );
		if( glGetProgrami( program, GL_LINK_STATUS ) == 0 ) {
			System.out.println( "Program failed to link." );
			return;
		}
		
		GL.setScene( this );
		glUseProgram( program );
		
		glEnable( GL_DEPTH_TEST );
		glDepthFunc( GL_LEQUAL );
		
		glClearColor( 1.0f, 1.0f, 1.0f, 1.0f );
		glEnable( GL_TEXTURE_2D );
		glUniform1i( getUniform( "sampler" ), 0 );
	}
	
	@Override
	public abstract void process();

	@Override
	public void destroy() {
		System.out.println( "GLScene#destroy()" );
		if( vShader != -1 ) glDeleteShader( vShader );
		if( fShader != -1 ) glDeleteShader( fShader );
		if( program != -1 ) glDeleteProgram( program );
	}
	
}
