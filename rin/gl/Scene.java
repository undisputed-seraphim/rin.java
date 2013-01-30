package rin.gl;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

import rin.gl.lib3d.Actor;
import rin.gl.lib3d.Mesh;
import rin.gl.lib3d.Poly;
import rin.gl.lib3d.shape.ComplexShape;
import rin.gl.model.Model;
import rin.util.IO;

public class Scene {
	public static final String LS = System.getProperty( "file.separator" );
	public static final String OS = System.getProperty( "os.name" ).toLowerCase().indexOf( "mac" ) != -1 ?
			LS+"Users"+LS+"Musashi"+LS+"Desktop"+LS+"Horo"+LS+"rin.java"+LS+"rin"+LS :
				"C:"+LS+"Users"+LS+"johall"+LS+"Desktop"+LS+"Horo"+LS+"rin.java"+LS+"rin"+LS;
	private static final String SHADER_DIR = OS+"inc"+LS+"shaders"+LS;
	private static final String MODEL_DIR = OS+"inc"+LS+"models"+LS;
	private static final float VIEW_DISTANCE = 15.0f;
	
	private boolean ready = false;
	private int items = 0;
	private Camera camera = null;
	
	private int width, height;
	private int program = -1, vShader = -1, fShader = -1;

	/* hold all meshes within the scene */
	private ArrayList<Actor> actors = new ArrayList<Actor>();

	/* getters */
	public int getProgram() { return this.program; }
	public int getAttrib( String str ) { return glGetAttribLocation( this.program, str ); }
	public int getUniform( String str ) { return glGetUniformLocation( this.program, str ); }
	
	public int getWidth() { return this.width; }
	public int getHeight() { return this.height; }
	public boolean isReady() { return this.ready; }
	
	/* get an actor from the actor stack */
	public Actor getActor( int id ) {
		for( Actor a : this.actors )
			if( a.getId() == id )
				return a;
		if( this.actors.size() > 0 )
			return this.actors.get( 0 );
		return null;
	}
	
	public void deleteActor( int id ) {
		if( this.actors.size() > id ) {
			if( this.getActor( id ).isMesh() )
				this.getActor( id ).toMesh().destroy();
			else if( this.getActor( id ).isPoly() )
				this.getActor( id ).toPoly().destroy();
			this.actors.remove( id );
		}
	}
	
	public int addComplexShape( ComplexShape shape ) {
		this.actors.add( shape );
		return shape.setId( this.items++ ).getId();
	}
	
	/* add something to the scene */
	public int addModel( String name, Model.Format format ) {
		String file = Scene.MODEL_DIR + name + LS + name;
		
		switch( format ) {
		case DAE:
			this.actors.add( Model.create( format, file + ".dae" ) );
			break;
			
		default:
			break;
		}
		
		return this.actors.get( this.actors.size() - 1 ).setName( name ).setId( this.items++ ).getId();
	}
	
	/* intialize shaders and program for the scene */
	public Scene() { this( 500, 500 ); }
	public Scene( int width, int height ) {
		TextureManager.reset();
		this.items = 0;
		
		this.width = width;
		this.height = height;
		
		this.camera = new Camera( 45, this.width / this.height, 0.1f, Scene.VIEW_DISTANCE );
	}
	
	public void show() {
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
		//glBindAttribLocation( this.program, 3, "bone" );
		//glBindAttribLocation( this.program, 4, "weight" );
		
		/* link the program to the gl context */
		glLinkProgram( this.program );
		if( glGetProgrami( this.program, GL_LINK_STATUS ) == 0 ) {
			System.out.println( "Program failed to link." );
			return;
		}
		
		/* init all actors that need to be initialized (textures / buffers) */
		for( Actor a : this.actors )
			if( a instanceof Mesh )
				( ( Mesh ) a ).init();
		
		glUseProgram( this.program );
		
		glClearColor( 1.0f, 1.0f, 1.0f, 0.0f );
		glEnable( GL_DEPTH_TEST );
		glDepthFunc( GL_LEQUAL );
		glViewport( 0, 0, this.width, this.height );
		glUniform1i( glGetUniformLocation( this.program, "sampler" ), 0 );
		glEnable( GL_TEXTURE_2D );
		glActiveTexture( GL_TEXTURE0 );
		
		this.camera.init();
		this.ready = true;
	}
	
	/* updates all items within range in the scene */
	public void update() {
		if( this.ready ) {
			if( Keyboard.isKeyDown( Keyboard.KEY_1 ) ) {
				this.camera.detach();
			} else if( Keyboard.isKeyDown( Keyboard.KEY_2 ) ) {
				this.camera.attach( this.actors.get( 0 ) );
			}
			
			glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT );
			//Input.process();
			this.camera.update();
			
			/*
			//TODO: fix so that only the top most bounding box can be selected
			float z = this.camera.getMouseZ(), w = 0;
			Actor picked = null;
			for( Actor a : this.actors ) {
				if( a.isMesh() && a.withinRange( Scene.VIEW_DISTANCE, this.camera.getPosition() ) ) {
					if( a.toMesh().isPickable() ) {
						a.toMesh().showBoundingBox();
						w = this.camera.getMouseZ();
						if( w != z ) {
							picked = a;
							z = w;
						} else a.toPickable().isMouseOver = false;
					} else if( a.toMesh().isPolyPickable() ) {
						for( Poly p : a.toMesh().getPolys() ) {
							p.showBoundingBox();
							w = this.camera.getMouseZ();
							if( w != z ) {
								picked = p;
								z = w;
							} else p.toPickable().isMouseOver = false;
						}
					}
				}
			}
			
			if( picked != null )
				picked.toPickable().isMouseOver = true;
			
			glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT );
			*/
			//TODO: this range check will falsely succeed if something is drawn after at the same z point
			for( Actor a : this.actors ) {
				if( a.isMesh() && a.withinRange( Scene.VIEW_DISTANCE, this.camera.getPosition() ) ) {
					a.toMesh().render();
					if( a.toMesh().isMouseOver && a.toMesh().isPickable() ) {
						a.toMesh().showBoundingBox( GL_LINE_STRIP );
						if( Mouse.isButtonDown( 0 ) )
							a.toMesh().clicked();
					} else if( a.toMesh().isPolyPickable() ) {
						for( Poly p : a.toMesh().getPolys() ) {
							if( p.toPickable().isMouseOver ) {
								p.toPickable().showBoundingBox( GL_LINE_STRIP );
								if( Mouse.isButtonDown( 0 ) )
									p.toPickable().clicked();
							}
						}
					}
				}
			}
		}
	}
	
	public Scene destroy() {
		this.ready = false;
		glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT );
		
		for( Actor a : this.actors )
			a = a.destroy();
		this.actors.clear();
		
		this.camera = this.camera != null ? camera.destroy() : null;
		
		if( this.vShader != -1 ) glDeleteShader( this.vShader );
		if( this.fShader != -1 ) glDeleteShader( this.fShader );
		if( this.program != -1 ) glDeleteProgram( this.program );
		
		return null;
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
	//private String getVertexShaderStr() { return IO.file.asString( GL.GL_DIR + "\\shaders\\vertex.shader" ); }
	private String getShaderSource( String shader ) { return IO.file.asString( Scene.SHADER_DIR + shader ); }
}
