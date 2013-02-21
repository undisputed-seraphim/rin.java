package rin.gl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

import rin.engine.Engine;
import rin.gl.event.GLEvent;
import rin.gl.event.GLEventThread;
import rin.gl.event.Transition;
import rin.gl.event.GLEvent.*;
import rin.gl.font.Font;
import rin.gl.lib3d.ActorList;
import rin.gl.lib3d.Camera;
import rin.gl.lib3d.DistanceComparator;
import rin.gl.lib3d.Actor;
import rin.gl.lib3d.Poly;
import rin.gl.lib3d.data.GLRenderStream;
import rin.gl.Input;
import rin.gl.TextureManager;
import rin.util.Buffer;
import rin.util.IO;
import rin.util.math.Vec3;

public class Scene {
	private static final float VIEW_DISTANCE = 15.0f;
	public static String uniqueAtMouse = "";
	
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
	public boolean isReady() { return this.ready; }
	
	private int vShader = -1, fShader = -1, program = -1;
	public int getProgram() { return this.program; }
	public int getAttrib( String str ) { return glGetAttribLocation( this.program, str ); }
	public int getUniform( String str ) { return glGetUniformLocation( this.program, str ); }

	private List<Actor> actors = Collections.synchronizedList( new ArrayList<Actor>() );
	private Camera camera = null;
	public Camera getCamera() { return this.camera; }
	
	private Font font = null;
	public Font getFont() { return this.font; }
	
	public Scene( int width, int height ) {
		Scene.r = 0;
		Scene.g = 0;
		Scene.b = 0;
		
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
		
		this.font = new Font( Engine.FONT_DIR + "ff6.png" );
		//TextureManager.init();
		//glUniform1i( this.getUniform( "samplerA" ), TextureManager.array );
		
		glViewport( 0, 0, width, height );

		this.camera = new Camera( 45, width / height, 0.1f, VIEW_DISTANCE );
		this.ready = true;
	}
	
	public Actor getActor( int index ) {
		return this.actors.get( index );
	}
	
	public void addActor( Actor a ) {
		if( a != null ) {
			this.actors.add( a );
			//this.actor.add( a );
			//this.actor.sort();
		}
	}
	
	private String prev = "";
	public void update() {
		if( this.ready ) {
			glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT );
			this.camera.update();
			
			glUniform1i( this.getUniform( "useUnique" ), GL_TRUE );
			for( Actor a : this.actors ) {
				( (Poly) a ).render( true );
			}
			glUniform1i( this.getUniform( "useUnique" ), GL_FALSE );
			
			Scene.uniqueAtMouse = Buffer.toString( this.camera.getMouseRGB() );

			glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT );
			GLEvent.fire( new TickEvent( 0.0015f ) );
			Input.process();
			/*if( !this.prev.equals( "" ) ) {
				if( !this.prev.equals( tmp ) ) {
					GLEvent.fire( new PickOutEvent( this.prev ) );
					GLEvent.fire( new PickInEvent( tmp ) );
				} else {
					GLEvent.fire( new PickRepeatEvent( tmp ) );
				}
			}
			this.prev = tmp;*/
			
			//glUniform1i( this.getUniform( "use3D" ), GL_TRUE );
			for( Actor a : this.actors ) {
				( (Poly) a ).render();
			}
			
			//this.font.draw3D( "testing", 0.3f, 0.05f, new Vec3() );
		}
	}
	
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
	
	/*vec3 light = vec3( 0.6, 0.6, 0.6 );
	vec3 directionColor = vec3( 0.2, 0.2, 0.2 );
	vec3 direction = vec3( 0.0, 0.0, 1.0 );
	float directional = max( dot( vNormal.xyz, direction ), 0.0 );
	vec3 ambient = light + ( directionColor * directional );
	
	vec3 specularWeight = vec3( 0.0, 0.0, 0.0 );
	vec3 eyeDirection = normalize( -vVertex.xyz );
	vec3 reflectionDirection = reflect( -direction, vNormal.xyz );
	float specularBrightness = pow( max( dot( reflectionDirection, eyeDirection ), 0.0 ), 0.5 );
	specularWeight = vec3( 0.3, 0.3, 0.3 ) * specularBrightness;
	
	texel = vec4( texel.rgb * ambient, 1.0 );*/
	
	private String getShaderSource( String shader ) { return IO.file.asString( Engine.SHADER_DIR + shader ); }
	
	public Scene destroy() {
		this.ready = false;
		//Input.requestDestroy();
		glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT );
		
		for( Actor a : this.actors )
			a = a.destroy();
		this.actors.clear();
		
		if( this.camera != null )
			this.camera = this.camera.destroy();
		
		TextureManager.destroy();
		
		if( this.vShader != -1 ) glDeleteShader( this.vShader );
		if( this.fShader != -1 ) glDeleteShader( this.fShader );
		if( this.program != -1 ) glDeleteProgram( this.program );
		
		return null;
	}
}