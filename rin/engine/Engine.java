package rin.engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import rin.engine.game.Game;
import rin.engine.game.scene.Scene;
import rin.engine.system.Processor;
import rin.engine.view.View;
import rin.engine.view.gui.GUIManager;
import rin.engine.view.opengl.OpenGLView;
import rin.gl.GL;
import rin.gl.GLScene;
import rin.gl.event.GLEventThread;
import rin.gl.lib3d.properties.Properties;
import rin.gl.model.Model;
import rin.gl.model.ModelDAE;
import rin.gl.model.ModelISM2;
import rin.gl.model.ModelManager;
import rin.gl.model.ModelOBJ;
import rin.gl.model.ModelPSSG;
import rin.gui.*;
import rin.sample.States;
import rin.util.Buffer;
import rin.world.WorldController;

public class Engine {
	
	private volatile static View view = null;
	public static View getView() {
		if( Engine.view == null )
			Engine.view = new OpenGLView();
		
		return Engine.view;
	}
	
	public static void setView( View view ) {
		//TODO: allow switching view's dynamically, just because
		Engine.view = view;
	}

	public static GUIManager getGUIManager() {
		return GUIManager.get();
	}
	
	private static Scene scene = null;
	/*public static Scene getScene() {
		if( Engine.scene == null )
			Engine.scene = new Scene();
		
		return Engine.scene;
	}*/
	
	private static Processor processor = null;
	public static Processor getProcessor() {
		if( Engine.processor == null )
			Engine.processor = new Processor( 10 );
		
		return Engine.processor;
	}
	
	public static void doWhenever( Runnable process ) {
		Engine.getProcessor().execute( process );
	}
	
	private static volatile boolean destroyRequested = false;
	public static void requestDestroy() { Engine.destroyRequested = true; }
	
	private static long dt = 0L;
	private static long start = 0L;
	public static long getDt() {
		long current = System.nanoTime();
		Engine.dt = current - Engine.start;
		Engine.start = current;
		return Engine.dt;
	}
	
	public static void start( final Game game ) {
		/*Engine.dt = System.nanoTime();
		new Thread( new Runnable() {
			@Override public void run() {
				game.preload();
				getScene().setView( game.getView() );
				game.initGUI();
				game.load();
				
				while( !Engine.destroyRequested ) {
					getScene().update( Engine.getDt() );
					getScene().render();
				}
				game.destroy();
			}
		}).start();*/
	}
	
	public static final String LS = System.getProperty( "file.separator" );
	public static final String OS = System.getProperty( "os.name" ).toLowerCase();
	public static final String ROOT = OS.indexOf( "window" ) != -1 ? "C:" : "";
	public static final String USER = OS.indexOf( "window" ) != -1 ? "johall" : "Musashi";
	public static final String MAINDIR = ROOT + LS + "Users"+LS+USER+LS+"Desktop"+LS+"Horo"+LS+"rin.java"+LS;
	public static final String ROOTDIR = OS.indexOf( "linux" ) != -1 ? LS+"media"+LS+"sf_Horo"+LS+"rin.java"+LS : MAINDIR;
	public static final String PACK_DIR = ROOTDIR+"packs"+LS;
	public static final String SHADER_DIR = ROOTDIR+"rin"+LS+"inc"+LS+"shaders"+LS;
	public static final String MODEL_DIR = ROOTDIR+"rin"+LS+"inc"+LS+"models"+LS;
	public static final String FONT_DIR = ROOTDIR+"rin"+LS+"inc"+LS+"fonts"+LS;
	public static final String IMG_DIR = ROOTDIR+"rin"+LS+"inc"+LS+"img"+LS;
	
	private static WorldController wc;
	public static WorldController getWorldController() { return Engine.wc; }
	
	public static GLScene getScene() { return GLScene.get(); }
	
	public static enum ModelFormat {
		DAE		( new ModelDAE() ),
		OBJ		( new ModelOBJ() ),
		PSSG	( new ModelPSSG() ),
		ISM2	( new ModelISM2() );
		
		public Model manager;
		
		ModelFormat( Model manager ) {
			this.manager = manager;
		}
	}
	
	public static class ModelParams {
		public String name = "noire_v";
		public String pack = "";
		public ModelFormat format = ModelFormat.DAE;
		
		public ModelParams( ModelFormat format, String pack, String name ) {
			this.format = format;
			this.name = name;
			this.pack = pack;
		}
		
		public ModelParams( ModelFormat format, String name ) {
			this.format = format;
			this.name = name;
		}
	}
	
	public static class ShapeParams {
		public Properties properties = new Properties();
		public boolean wire = false;
	}
	
	public static class TetrahedronParams extends ShapeParams {
		public float radius = 0.5f;
		
		public TetrahedronParams() {}
		public TetrahedronParams( Float radius ) { this( radius, null, null ); }
		public TetrahedronParams( Float radius, Properties p ) { this( radius, p, null ); }
		public TetrahedronParams( Float radius, Properties p, Boolean wire ) {
			if( radius != null ) this.radius = radius;
			if( p != null ) this.properties = p;
			if( wire != null ) this.wire = wire;
		}
	}
	
	public static class CuboidParams extends ShapeParams {
		public float width = 1.0f;
		public float height = 1.0f;
		public float depth = 1.0f;
		
		public CuboidParams() {}
		public CuboidParams( Float width ) { this( width, null, null, null, null ); }
		public CuboidParams( Float width, Float height ) { this( width, height, null, null, null ); }
		public CuboidParams( Float width, Float height, Float depth ) { this( width, height, depth, null, null ); }
		public CuboidParams( Float width, Float height, Float depth, Properties p ) { this( width, height, depth, p, null ); }
		public CuboidParams( Float width, Float height, Float depth, Properties p, Boolean wire ) {
			if( width != null ) this.width = width;
			if( height != null ) this.height = height;
			if( depth != null ) this.depth = depth;
			if( p != null ) this.properties = p;
			if( wire != null ) this.wire = wire;
		}
	}

	public static class DodecahedronParams extends ShapeParams {}
	public static class OctahedronParams extends ShapeParams {}
	public static class IcosahedronParams extends ShapeParams {}
	public static class SphereParams extends ShapeParams {
		public float radius = 0.5f;
		public int slices = 10;
		public int stacks = 10;
		
		public SphereParams() {}
		public SphereParams( Float radius ) { this( radius, null, null, null, null ); }
		public SphereParams( Float radius, Integer slices ) { this( radius, slices, null, null, null ); }
		public SphereParams( Float radius, Integer slices, Integer stacks ) { this( radius, slices, stacks, null, null ); }
		public SphereParams( Float radius, Integer slices, Integer stacks, Properties p ) { this( radius, slices, stacks, p, null ); }
		public SphereParams( Float radius, Integer slices, Integer stacks, Properties p, Boolean wire ) {
			if( radius != null ) this.radius = radius;
			if( slices != null ) this.slices = slices;
			if( stacks != null ) this.stacks = stacks;
			if( p != null ) this.properties = p;
			if( wire != null ) this.wire = wire;
		}
	}
	
	public static void init( int width, int height ) {
		Engine.wc = new WorldController();
		GL.init( width, height );
		GLScene.init();
	}
	
	public static void start() {
		GL.get().run();
	}
				
	/* You're Fun. */
	
	public static void stop() { Engine.destroy(); }
	
	public static void destroy() {
		GL.get().requestDestroy();
	}
}