package rin.engine.scene;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL20.*;

import java.util.ArrayList;

import rin.engine.resource.Resource;
import rin.engine.resource.ResourceManager;
import rin.engine.scene.nodes.AbstractSceneNode;
import rin.engine.util.NodeTree;

public class GLScene3D implements Scene {

	private NodeTree<AbstractSceneNode> graph = new NodeTree<AbstractSceneNode>();
	private ArrayList<AbstractSceneNode> secondPass = new ArrayList<AbstractSceneNode>();
	
	private int vShader, fShader;
	private int program;
	
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
		
		glUseProgram( program );
		
		glEnable( GL_DEPTH_TEST );
		glDepthFunc( GL_LEQUAL );
		
		glClearColor( 1.0f, 1.0f, 1.0f, 1.0f );
		glEnable( GL_TEXTURE_2D );
		glUniform1i( glGetUniformLocation( program, "sampler" ), 0 );
	}
	
	@Override
	public void process() {
		for( AbstractSceneNode asn : graph ) {
			if( asn.isSecondPass() )
				secondPass.add( asn );
			else asn.process();
		}
		
		for( AbstractSceneNode asn : secondPass )
			asn.process();
		secondPass.clear();
	}

	@Override
	public void destroy() {
		System.out.println( "GLScene3D#destroy()" );
		if( vShader != -1 ) glDeleteShader( vShader );
		if( fShader != -1 ) glDeleteShader( fShader );
		if( program != -1 ) glDeleteProgram( program );
		
		for( AbstractSceneNode asn : graph )
			asn.destroy();
		graph.clear();
		secondPass.clear();
	}

	public void addModel( Resource resource ) {
		
	}
	
}
