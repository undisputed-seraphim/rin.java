package rin.engine.resource;

import java.util.HashMap;

import rin.engine.resource.image.ImageDecoder;
import rin.engine.resource.image.ImageEncoder;
import rin.engine.resource.model.ModelContainer;
import rin.engine.resource.model.ModelDecoder;
import rin.engine.resource.model.ModelEncoder;
import rin.engine.resource.model.ModelOptions;
import rin.engine.resource.model.brres.BrresDecoder;

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
		return null;
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
		addModelDecoder( BrresDecoder.class );
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
		return decodeModel( resource, null );
	}
	
	public static ModelContainer decodeModel( Resource resource, ModelOptions opts ) {
		ModelDecoder decoder = modelDecoders.get( resource.getExtension() );
		if( decoder == null ) {
			//TODO: decodernotfoundexception
			return null;
		}
		
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
