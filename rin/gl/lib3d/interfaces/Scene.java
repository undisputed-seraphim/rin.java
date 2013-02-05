package rin.gl.lib3d.interfaces;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

import rin.gl.event.GLEvent;
import rin.gl.event.GLEvent.*;
import rin.gl.lib3d.ActorList;
import rin.gl.lib3d.Camera;
import rin.gl.lib3d.DistanceComparator;
import rin.gl.Input;
import rin.gl.TextureManager;
import rin.util.Buffer;
import rin.util.IO;

public class Scene {
	private static final float VIEW_DISTANCE = 15.0f;
	
	private static int r = 0, g = 0, b = 0;
	public static float[] getNextColor() {
		if( Scene.r < 255 )
			Scene.r ++;
		else if( Scene.g < 255 ) {
			Scene.g ++;
			Scene.r = 0;
		} else {
			Scene.b ++;
			Scene.r = 0;
			Scene.g = 0;
		}
		
		float[] tmp = new float[] { Scene.r, Scene.g, Scene.b };
		return new float[]{ tmp[0] / 255, tmp[1] / 255, tmp[2] / 255 };
	}
	
	private boolean ready = false;
	
	private int vShader = -1, fShader = -1, program = -1;
	public int getProgram() { return this.program; }
	public int getAttrib( String str ) { return glGetAttribLocation( this.program, str ); }
	public int getUniform( String str ) { return glGetUniformLocation( this.program, str ); }
	
	private ArrayList<Actor> actors = new ArrayList<Actor>();
	private ActorList actor = new ActorList();
	private Camera camera = null;
	public Camera getCamera() { return this.camera; }
	
	public Scene( int width, int height ) {
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
		//glBindAttribLocation( this.program, 3, "unique" );
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
		
		glClearColor( 1.0f, 1.0f, 1.0f, 0.0f );
		glEnable( GL_TEXTURE_2D );
		glActiveTexture( GL_TEXTURE0 );
		glUniform1i( glGetUniformLocation( this.program, "sampler" ), 0 );
		
		glViewport( 0, 0, width, height );
		this.camera = new Camera( 45, width / height, 0.1f, VIEW_DISTANCE );
		this.ready = true;
	}
	
	public void addActor( Actor a ) {
		this.actors.add( a );
		this.actor.add( a );
		this.actor.sort();
	}
	
	public void update() {
		if( this.ready ) {
			glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT );
			Input.process();
			this.camera.update();
			
			for( Actor a : this.actors ) {
				( (Poly) a ).useUniqueColor( true );
				( (Poly) a ).render();
			}
			
			String tmp = Buffer.toString( this.camera.getMouseRGB() );
			glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT );

			for( Actor a : this.actors ) {
				( (Poly) a ).useUniqueColor( false );
				( (Poly) a ).render();
			}
			
			GLEvent.fire( new PickEvent( tmp ) );
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
	
	private String getShaderSource( String shader ) { return IO.file.asString( Engine.SHADER_DIR + shader ); }
	
	public Scene destroy() {
		this.ready = false;
		glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT );
		
		for( Actor a : this.actors )
			a = a.destroy();
		this.actors.clear();
		
		this.camera = this.camera != null ? camera.destroy() : null;
		
		TextureManager.reset();
		
		if( this.vShader != -1 ) glDeleteShader( this.vShader );
		if( this.fShader != -1 ) glDeleteShader( this.fShader );
		if( this.program != -1 ) glDeleteProgram( this.program );
		
		Scene.r = 0;
		Scene.g = 0;
		Scene.b = 0;
		
		return null;
	}
}
