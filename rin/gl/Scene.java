package rin.gl;

import java.nio.IntBuffer;
import java.util.List;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

import rin.gl.lib3d.Actor;
import rin.gl.lib3d.Mesh;
import rin.gl.model.Model;
import rin.util.IO;

public class Scene {
	private boolean ready = false;
	public int items = 0;
	public Camera camera;
	
	/* scene specific program and shaders */
	private int width,
				height,
				program,
				vShader,
				fShader;

	/* hold all meshes within the scene */
	public List<Actor> actors = new ArrayList<Actor>();
	
	/* constructor */
	public Scene() {}
	
	/* getters */
	public boolean ready() { return this.ready; }
	public int getProgram() { return this.program; }
	public int getAttrib( String str ) { return glGetAttribLocation( this.program, str ); }
	public int getUniform( String str ) { return glGetUniformLocation( this.program, str ); }
	
	/* intialize shaders and program for the scene */
	public void init( int width, int height ) {
		System.out.println("in here");
		this.width = width;
		this.height = height;
		glClearColor( 1.0f, 1.0f, 1.0f, 0.0f );
		glEnable( GL_DEPTH_TEST );
		glDepthFunc( GL_LEQUAL );
		glViewport( 0, 0, this.width, this.height );
		glUniform1i( glGetUniformLocation( this.program, "sampler" ), 0 );
		glEnable( GL_TEXTURE_2D );
		glActiveTexture( GL_TEXTURE0 );
		
		int success = 0;
		/* create and compile vertex shader */
		this.vShader = this.createShader( GL_VERTEX_SHADER, this.getVertexShaderStr() );
		if( this.vShader == -1 )
			return;
		
		/* create and compile fragment shader */
		this.fShader = this.createShader( GL_FRAGMENT_SHADER, this.getFragmentShaderStr() );
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
		//glBindAttribLocation( this.program, 3, "bone" );
		//glBindAttribLocation( this.program, 4, "weight" );
		
		/* link the program to the gl context */
		glLinkProgram( this.program );
		success = glGetProgrami( this.program, GL_LINK_STATUS );
		if( success == 0 ) {
			System.out.println( "Program failed to link." );
			return;
		}
		
		/* init all actors that need to be initialized (textures / buffers) */
		for( Actor a : this.actors )
			if( a instanceof Mesh )
				( ( Mesh ) a ).init();
		
		glUseProgram( this.program );
		this.camera = new Camera( this, 45, this.width / this.height, 0.1f, 100.0f );
		this.ready = true;
	}
	
	/* get an actor from the actor stack */
	public Actor getActor( int index ) {
		return this.actors.get( index );
	}
	
	/* add an actor to the scene */
	public int addActor( Actor actor ) {
		actor.setScene( this );
		actor.setId( this.items++ );
		if( actor instanceof Mesh ) {
			( ( Mesh ) actor ).setPicking( true );
		}
		
		this.actors.add( actor );
		return this.actors.size() - 1;
	}
	
	/* add something to the scene */
	public Actor addModel( String name, Model.Format format ) {
		String file = GL.GL_DIR + "\\models\\" + name + "\\" + name;
		
		switch( format ) {
		case DAE:
			this.addActor( Model.create( format, file + ".dae" ) );
			break;
			
		default:
			break;
		}
		
		this.actors.get( this.actors.size() - 1 ).setName( name );
		return this.actors.get( this.actors.size() - 1 );
	}
	
	public int getWidth() { return this.width; }
	public int getHeight() { return this.height; }
	
	/* updates all items within range in the scene */
	public void update() {
		if( this.ready ) {
			/* clear the current scene and update the camera's position */
			glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT );
			this.camera.update();
			
			if( this.camera.picking ) {
				this.camera.startPicking();
			
				for( Actor a : this.actors ) {
					if( a instanceof Mesh )
						if( ( ( Mesh ) a ).isPicking() )
							( ( Mesh ) a ).render();
				}
				
				IntBuffer picking = this.camera.stopPicking();
				if( picking != null ) {
					/* get the targets that were picked and call their selected() methods */
					//TODO: properly loop through the buffer to get the hit results
					System.out.println( "something was hit: " + picking.get( 3 ) );
					( (Mesh) this.actors.get( picking.get( 3 ) ) ).selected();
				}
			}
			
			//TODO: add range checking for these items so that only those within range of camera are updated */
			for( Actor a : this.actors ) {
				if( a instanceof Mesh )
					( ( Mesh ) a ).render();
			}
		}
	}
	
	private int createShader( int type, String src ) {
		int shader = glCreateShader( type );
		glShaderSource( shader, src );
		glCompileShader( shader );
		if( glGetShaderi( shader, GL_COMPILE_STATUS ) == 0 ) {
			System.out.println( "Shader ["+ type +"] failed to compile." );
			return -1;
		}
		return shader;
	}
	
	/* return a string for the vertex shader code */
	private String getVertexShaderStr() { return IO.file.asString( GL.GL_DIR + "\\shaders\\vertex.shader" ); }
	
	/* return a string for the fragment shader code */
	private String getFragmentShaderStr() { return IO.file.asString( GL.GL_DIR + "\\shaders\\fragment.shader" ); }

}
