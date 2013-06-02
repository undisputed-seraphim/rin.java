package rin.engine.view.gl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import rin.engine.resource.Resource;
import rin.engine.resource.ResourceManager;

public class GL {
	public static final int VERTEX_SHADER = GL_VERTEX_SHADER;
	public static final int FRAGMENT_SHADER = GL_FRAGMENT_SHADER;

	private static int vShader = -1;
	private static int fShader = -1;
	private static int program = -1;
	
	public static int getProgram() { return program; }
	public static int getAttribute( String attr ) { return glGetAttribLocation( program, attr ); }
	public static int getUniform( String uniform ) { return glGetUniformLocation( program, uniform ); }
	
	private static int createShader( int type, Resource res ) {
		int shader = glCreateShader( type );
		glShaderSource( shader, res.toString() );
		glCompileShader( shader );
		if( glGetShaderi( shader, GL_COMPILE_STATUS ) == 0 ) {
			System.out.println( "Shader ["+ (type == GL_VERTEX_SHADER ? "vertex" : "fragment") +"] failed to compile." );
			return -1;
		}
		return shader;
	}
	
	public static void initDefaults() {
		System.out.println( "GL#initDefaults()" );
		
		/* create and compile vertex shader */
		vShader = createShader( GL_VERTEX_SHADER, ResourceManager.getPackResource( "rin", "shaders", "default.vert" ) );
		if( vShader == -1 ) return;
		
		/* create and compile fragment shader */
		fShader = createShader( GL_FRAGMENT_SHADER, ResourceManager.getPackResource( "rin", "shaders", "default.frag" ) );
		if( fShader == -1 ) return;
		
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
		
		glUseProgram( program );
		
		glEnable( GL_DEPTH_TEST );
		glDepthFunc( GL_LEQUAL );
		
		glClearColor( 1.0f, 1.0f, 1.0f, 1.0f );
		glEnable( GL_TEXTURE_2D );
		glUniform1i( getUniform( "sampler" ), 0 );
	}
	
	public static void setSize( int width, int height ) { glViewport( 0, 0, width, height ); }
	
	public static void destroy() {
		System.out.println( "GL#destroy()" );
		if( vShader != -1 ) glDeleteShader( vShader );
		if( fShader != -1 ) glDeleteShader( fShader );
		if( program != -1 ) glDeleteProgram( program );
	}
	
}
