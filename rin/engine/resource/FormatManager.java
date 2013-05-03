package rin.engine.resource;

import java.util.HashMap;

import rin.engine.resource.image.ImageContainer;
import rin.engine.resource.image.ImageDecoder;
import rin.engine.resource.image.ImageEncoder;
import rin.engine.resource.image.ImageOptions;
import rin.engine.resource.image.tid.TidDecoder;
import rin.engine.resource.model.ModelContainer;
import rin.engine.resource.model.ModelDecoder;
import rin.engine.resource.model.ModelEncoder;
import rin.engine.resource.model.ModelOptions;
import rin.engine.resource.model.brres.BrresDecoder;
import rin.engine.resource.model.gmo.GmoDecoder;
import rin.engine.resource.model.ism2.Ism2Decoder;
import rin.engine.resource.model.pssg.PssgDecoder;

public class FormatManager {
	
	private static <T> T createInstance( Class<T> cls ) {
		try {
			return cls.newInstance();
		} catch( InstantiationException ex ) {
			// TODO Auto-generated catch block
		} catch( IllegalAccessException ex ) {
			// TODO Auto-generated catch block
		}
		
		return null;
	}
	
	/* ----------------- IMAGES --------------- */

	private static HashMap<String, ImageDecoder> imageDecoders = new HashMap<String, ImageDecoder>();
	private static HashMap<String, ImageEncoder> imageEncoders = new HashMap<String, ImageEncoder>();
	
	static {
		if( !addImageDecoder( TidDecoder.class ) )
			System.err.println( "Unable to load TID Image Decoder." );
	}
	
	public static <T extends ImageDecoder> boolean addImageDecoder( Class<T> decoderClass ) {
		// create instance
		T decoder = createInstance( decoderClass );
		if( decoder == null )
			return false;
		
		// check if format exists
		if( imageDecoders.containsKey( decoder.getExtensionName().toUpperCase() ) ) {
			//TODO: format exists exception?
			return false;
		}
		
		// add format
		imageDecoders.put( decoder.getExtensionName().toUpperCase(), decoder );
		return true;
	}
	
	public static ImageDecoder getImageDecoder( String extension ) {
		if( imageDecoders.containsKey( extension.toUpperCase() ) )
			return imageDecoders.get( extension.toUpperCase() );
		
		//TODO: decodernotfoundexception
		return null;
	}
	
	public static ImageContainer decodeImage( Resource resource ) {
		return decodeImage( resource, null, resource.getExtension() );
	}
	
	public static ImageContainer decodeImage( Resource resource, String format ) {
		return decodeImage( resource, null, format );
	}
	
	public static ImageContainer decodeImage( Resource resource, ImageOptions opts, String format ) {
		ImageDecoder decoder = getImageDecoder( format );
		if( decoder == null )
			return null;
		
		decoder.clear();
		return decoder.decode( resource, opts );
	}
	
	public static <T extends ImageEncoder> boolean addImageEncoder( Class<T> encoderClass ) {
		// create instance
		T encoder = createInstance( encoderClass );
		if( encoder == null )
			return false;
		
		// check if format exists
		if( imageEncoders.containsKey( encoder.getExtensionName().toUpperCase() ) ) {
			//TODO: format exists exception?
			return false;
		}
		
		// add format
		imageEncoders.put( encoder.getExtensionName().toUpperCase(), encoder );
		return true;
	}
	
	public static ImageEncoder getImageEncoder( String extension ) {
		return null;
	}
	
	/* ---------------- MODELS ----------------- */
	
	private static HashMap<String, ModelDecoder> modelDecoders = new HashMap<String, ModelDecoder>();
	private static HashMap<String, ModelEncoder> modelEncoders = new HashMap<String, ModelEncoder>();
	
	static {
		if( !addModelDecoder( BrresDecoder.class ) )
			System.err.println( "Unable to load BRRES Model Decoder." );
		if( !addModelDecoder( GmoDecoder.class ) )
			System.err.println( "Unable to load GMO Model Decoder." );
		if( !addModelDecoder( Ism2Decoder.class ) )
			System.err.println( "Unable to load ISM2 Model Decoder." );
		if( !addModelDecoder( PssgDecoder.class ) )
			System.err.println( "Unable to load ISM2 Model Decoder." );
	}
	
	public static <T extends ModelDecoder> boolean addModelDecoder( Class<T> decoderClass ) {
		// create instance
		T decoder = createInstance( decoderClass );
		if( decoder == null )
			return false;
		
		// check if format exists
		System.out.println( decoderClass.toString() );
		if( modelDecoders.containsKey( decoder.getExtensionName().toUpperCase() ) ) {
			//TODO: format exists exception?
			return false;
		}
		
		// add format
		modelDecoders.put( decoder.getExtensionName().toUpperCase(), decoder );
		return true;
	}
	
	public static ModelDecoder getModelDecoder( String extension ) {
		if( modelDecoders.containsKey( extension.toUpperCase() ) )
			return modelDecoders.get( extension.toUpperCase() );
		
		//TODO: decodernotfoundexception
		return null;
	}
	
	public static ModelContainer decodeModel( Resource resource ) {
		return decodeModel( resource, null, resource.getExtension() );
	}
	
	public static ModelContainer decodeModel( Resource resource, String format ) {
		return decodeModel( resource, null, format );
	}
	
	public static ModelContainer decodeModel( Resource resource, ModelOptions opts, String format ) {
		ModelDecoder decoder = getModelDecoder( format );
		if( decoder == null )
			return null;
		
		decoder.clear();
		return decoder.decode( resource, opts );
	}
	
	public static <T extends ModelEncoder> boolean addModelEncoder( Class<T> encoderClass ) {
		// create instance
		T encoder = createInstance( encoderClass );
		if( encoder == null )
			return false;
		
		// check if format exists
		if( modelEncoders.containsKey( encoder.getExtensionName().toUpperCase() ) ) {
			//TODO: format exists exception?
			return false;
		}
		
		// add format
		modelEncoders.put( encoder.getExtensionName().toUpperCase(), encoder );
		return true;
	}
	
	public static ModelEncoder getModelEncoder( String extension ) {
		return null;
	}
	
}
