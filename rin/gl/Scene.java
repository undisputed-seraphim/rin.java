package rin.gl;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

import rin.gl.lib3d.Actor;
import rin.gl.lib3d.Mesh;
import rin.gl.model.Model;
import rin.util.IO;

public class Scene {
	private static final String OS = System.getProperty( "file.separator" );
	private static final String SHADER_DIR = OS+"Users"+OS+"Musashi"+OS+"Desktop"+OS+"Horo"+OS+"rin.java"+OS+"rin"+OS+"inc"+OS+"shaders"+OS;
	private static final String MODEL_DIR = OS+"Users"+OS+"Musashi"+OS+"Desktop"+OS+"Horo"+OS+"rin.java"+OS+"rin"+OS+"inc"+OS+"models"+OS;
	private static final float VIEW_DISTANCE = 15.0f;
	
	private static boolean ready = false;
	private static boolean destroyRequested = false;
	private static int items = 0;
	private static Camera camera = null;
	
	private static int width, height;
	private static int program = -1, vShader = -1, fShader = -1;

	/* hold all meshes within the scene */
	private static ArrayList<Actor> actors = new ArrayList<Actor>();

	/* getters */
	public static int getProgram() { return Scene.program; }
	public static int getAttrib( String str ) { return glGetAttribLocation( Scene.program, str ); }
	public static int getUniform( String str ) { return glGetUniformLocation( Scene.program, str ); }
	
	public static int getWidth() { return Scene.width; }
	public static int getHeight() { return Scene.height; }
	public static boolean isReady() { return Scene.ready; }
	
	/* get an actor from the actor stack */
	public static Actor getActor( int id ) {
		for( Actor a : Scene.actors )
			if( a.getId() == id )
				return a;
		if( Scene.actors.size() > 0 )
			return Scene.actors.get( 0 );
		return null;
	}
	
	public static void deleteActor( int id ) {
		if( Scene.actors.size() > id ) {
			if( Scene.getActor( id ).isMesh() )
				Scene.getActor( id ).toMesh().destroy();
			else if( Scene.getActor( id ).isPoly() )
				Scene.getActor( id ).toPoly().destroy();
			Scene.actors.remove( id );
		}
	}
	
	/* add something to the scene */
	public static int addModel( String name, Model.Format format ) {
		//String file = GL.GL_DIR + "\\models\\" + name + "\\" + name;
		String file = Scene.MODEL_DIR + name + "/" + name;
		
		switch( format ) {
		case DAE:
			Scene.actors.add( Model.create( format, file + ".dae" ) );
			break;
			
		default:
			break;
		}
		
		return Scene.actors.get( Scene.actors.size() - 1 ).setName( name ).setId( Scene.items++ ).getId();
	}
	
	/* intialize shaders and program for the scene */
	public static void create() { Scene.create( 500, 500 ); }
	public static void create( int width, int height ) {
		TextureManager.reset();
		Scene.items = 0;
		Scene.destroyRequested = false;
		
		Scene.width = width;
		Scene.height = height;
		
		int success = 0;
		/* create and compile vertex shader */
		Scene.vShader = Scene.createShader( GL_VERTEX_SHADER, Scene.getShaderSource( "vertex.shader" ) );
		if( Scene.vShader == -1 )
			return;
		
		/* create and compile fragment shader */
		Scene.fShader = Scene.createShader( GL_FRAGMENT_SHADER, Scene.getShaderSource( "fragment.shader" ) );
		if( Scene.fShader == -1 )
			return;
		
		/* create program and attach shaders */
		Scene.program = glCreateProgram();
		glAttachShader( Scene.program, Scene.vShader );
		glAttachShader( Scene.program, Scene.fShader );
		
		/* bind attribute locations */
		glBindAttribLocation( Scene.program, 0, "vertex" );
		glBindAttribLocation( Scene.program, 1, "normal" );
		glBindAttribLocation( Scene.program, 2, "texture" );
		//glBindAttribLocation( this.program, 3, "bone" );
		//glBindAttribLocation( this.program, 4, "weight" );
		
		/* link the program to the gl context */
		glLinkProgram( Scene.program );
		success = glGetProgrami( Scene.program, GL_LINK_STATUS );
		if( success == 0 ) {
			System.out.println( "Program failed to link." );
			return;
		}
		
		/* init all actors that need to be initialized (textures / buffers) */
		for( Actor a : Scene.actors )
			if( a instanceof Mesh )
				( ( Mesh ) a ).init();
		
		glUseProgram( Scene.program );
		
		glClearColor( 1.0f, 1.0f, 1.0f, 0.0f );
		glEnable( GL_DEPTH_TEST );
		glDepthFunc( GL_LEQUAL );
		glViewport( 0, 0, Scene.width, Scene.height );
		glUniform1i( glGetUniformLocation( Scene.program, "sampler" ), 0 );
		glEnable( GL_TEXTURE_2D );
		glActiveTexture( GL_TEXTURE0 );
		
		Scene.camera = new Camera( 45, Scene.width / Scene.height, 0.1f, Scene.VIEW_DISTANCE );
		Scene.ready = true;
	}
	
	/* updates all items within range in the scene */
	public static void update() {		
		if( Scene.ready ) {
			/* clear the current scene and update the camera's position */
			glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT );
			Scene.camera.update();
			
			/* render bounding boxes to see if mouse is over anything */
			float z = Scene.camera.getMouseZ(), w = 0;
			int hitId = -1;
			for( Actor a : Scene.actors ) {
				if( a.isMesh() && a.withinRange( Scene.VIEW_DISTANCE, Scene.camera.getPosition() ) ) {
					if( a.toMesh().isPickable() ) {
						a.toMesh().showBoundingBox();
						w = Scene.camera.getMouseZ();
						if( w != z ) {
							hitId = a.getId();
							z = w;
						} else a.toMesh().isMouseOver = false;
					}
				}
			}
			if( hitId != -1 )
				Scene.getActor( hitId ).toMesh().isMouseOver = true;
			
			glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT );
			
			//TODO: this range check will falsely succeed if something is drawn after at the same z point
			for( Actor a : Scene.actors ) {
				if( a.isMesh() && a.withinRange( Scene.VIEW_DISTANCE, Scene.camera.getPosition() ) ) {
					a.toMesh().render();
					if( a.toMesh().isMouseOver ) {
						a.toMesh().showBoundingBox( GL_LINE_STRIP );
						if( Mouse.isButtonDown( 0 ) )
							a.toMesh().clicked();
					}
				}
			}
		}
		
		if( Scene.destroyRequested ) {
			Scene.ready = false;
			Scene.destroyRequested = false;
			glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT );
			Scene.destroy();
			return;
		}
	}
	
	public static void requestDestroy() { Scene.destroyRequested = true; }
	public static void forceDestroy() { Scene.destroy(); }
	private static void destroy() {
		Scene.ready = false;
		
		for( Actor a : Scene.actors )
			a = a.destroy();
		Scene.actors.clear();
		
		Scene.camera = Scene.camera != null ? camera.destroy() : null;
		
		if( Scene.vShader != -1 ) glDeleteShader( Scene.vShader );
		if( Scene.fShader != -1 ) glDeleteShader( Scene.fShader );
		if( Scene.program != -1 ) glDeleteProgram( Scene.program );
	}
	
	private static int createShader( int type, String src ) {
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
	//private String getVertexShaderStr() { return IO.file.asString( GL.GL_DIR + "\\shaders\\vertex.shader" ); }
	private static String getShaderSource( String shader ) { return IO.file.asString( Scene.SHADER_DIR + shader ); }
}
