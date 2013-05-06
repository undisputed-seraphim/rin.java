package rin.engine.resource;

import java.util.HashMap;

import rin.engine.resource.audio.acb.AcbExtractor;
import rin.engine.resource.image.ImageContainer;
import rin.engine.resource.image.ImageDecoder;
import rin.engine.resource.image.ImageEncoder;
import rin.engine.resource.image.ImageOptions;
import rin.engine.resource.image.png.PngDecoder;
import rin.engine.resource.image.png.PngEncoder;
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
		if( !addImageDecoder( PngDecoder.class ) )
			System.err.println( "Unable to load PNG Image Decoder." );
		
		if( !addImageEncoder( PngEncoder.class ) )
			System.err.println( "Unable to load PNG Image Encoder." );
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
	
	private static ImageDecoder getImageDecoder( String extension ) {
		if( imageDecoders.containsKey( extension.toUpperCase() ) )
			return imageDecoders.get( extension.toUpperCase() );
		
		//TODO: decodernotfoundexception
		return null;
	}
	
	public static ImageContainer decodeImage( Resource resource ) {
		return decodeImage( resource, resource.getExtension(), null );
	}
	
	public static ImageContainer decodeImage( Resource resource, String format ) {
		return decodeImage( resource, format, null );
	}
	
	public static ImageContainer decodeImage( Resource resource, ImageOptions opts ) {
		return decodeImage( resource, resource.getExtension(), opts );
	}
	
	public static ImageContainer decodeImage( Resource resource, String format, ImageOptions opts ) {
		ImageDecoder decoder = getImageDecoder( format );
		if( decoder == null )
			return null;
		
		decoder.clear();
		if( opts == null ) opts = decoder.getDefaultOptions();
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
	
	private static ImageEncoder getImageEncoder( String extension ) {
		if( imageEncoders.containsKey( extension.toUpperCase() ) )
			return imageEncoders.get( extension.toUpperCase() );
		
		//TODO: encodernotfoundexception
		return null;
	}
	
	public static boolean encodeImage( ImageContainer from, Resource to ) {
		return encodeImage( from, to, to.getExtension(), null );
	}
	
	public static boolean encodeImage( ImageContainer from, Resource to, String format ) {
		return encodeImage( from, to, format, null );
	}
	
	public static boolean encodeImage( ImageContainer from, Resource to, ImageOptions opts ) {
		return encodeImage( from, to, to.getExtension(), opts );
	}
	
	public static boolean encodeImage( ImageContainer from, Resource to, String format, ImageOptions opts ) {
		ImageEncoder encoder = getImageEncoder( format );
		if( encoder == null )
			return false;
		
		//encoder.clear();
		if( opts == null ) opts = encoder.getDefaultOptions();
		return encoder.encode( from, to, opts );
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
		if( modelDecoders.containsKey( decoder.getExtensionName().toUpperCase() ) ) {
			//TODO: format exists exception?
			return false;
		}
		
		// add format
		modelDecoders.put( decoder.getExtensionName().toUpperCase(), decoder );
		return true;
	}
	
	private static ModelDecoder getModelDecoder( String extension ) {
		if( modelDecoders.containsKey( extension.toUpperCase() ) )
			return modelDecoders.get( extension.toUpperCase() );
		
		//TODO: decodernotfoundexception
		return null;
	}
	
	public static ModelContainer decodeModel( Resource resource ) {
		return decodeModel( resource, resource.getExtension(), null );
	}
	
	public static ModelContainer decodeModel( Resource resource, String format ) {
		return decodeModel( resource, format, null );
	}
	
	public static ModelContainer decodeModel( Resource resource, ModelOptions opts ) {
		return decodeModel( resource, resource.getExtension(), opts );
	}
	
	public static ModelContainer decodeModel( Resource resource, String format, ModelOptions opts ) {
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
	
	private static ModelEncoder getModelEncoder( String extension ) {
		if( modelEncoders.containsKey( extension.toUpperCase() ) )
			return modelEncoders.get( extension.toUpperCase() );
		
		//TODO: encodernotfoundexception
		return null;
	}
	
	public static boolean encodeModel( ModelContainer from, Resource to ) {
		return encodeModel( from, to, to.getExtension(), null );
	}
	
	public static boolean encodeModel( ModelContainer from, Resource to, String format ) {
		return encodeModel( from, to, format, null );
	}
	
	public static boolean encodeModel( ModelContainer from, Resource to, ModelOptions opts ) {
		return encodeModel( from, to, to.getExtension(), opts );
	}
	
	public static boolean encodeModel( ModelContainer from, Resource to, String format, ModelOptions opts ) {
		ModelEncoder encoder = getModelEncoder( format );
		if( encoder == null )
			return false;
		
		//encoder.clear();
		//if( opts == null ) opts = encoder.getDefaultOptions();
		return encoder.encode( from, to, opts );
	}
	
	/* ---------------- EXTRACTORS ----------------- */
	
	private static HashMap<String, ResourceExtractor> resourceExtractors = new HashMap<String, ResourceExtractor>();
	
	static {
		if( !addResourceExtractor( AcbExtractor.class ) )
			System.err.println( "Unable to load ACB Resource Extractor." );
	}
	
	public static <T extends ResourceExtractor> boolean addResourceExtractor( Class<T> extractorClass ) {
		// create instance
		T extractor = createInstance( extractorClass );
		if( extractor == null )
			return false;
		
		// check if format exists
		if( resourceExtractors.containsKey( extractor.getExtensionName().toUpperCase() ) ) {
			//TODO: format exists exception?
			return false;
		}
		
		// add format
		resourceExtractors.put( extractor.getExtensionName().toUpperCase(), extractor );
		return true;
	}
	
	private static ResourceExtractor getResourceExtractor( String extension ) {
		if( resourceExtractors.containsKey( extension.toUpperCase() ) )
			return resourceExtractors.get( extension.toUpperCase() );
		
		//TODO: encodernotfoundexception
		return null;
	}
	
	public static boolean extractResource( Resource resource ) {
		return extractResource( resource, resource.getExtension(), null );
	}
	
	public static boolean extractResource( Resource resource, String format ) {
		return extractResource( resource, format, null );
	}
	
	public static boolean extractResource( Resource resource, ExtractOptions opts ) {
		return extractResource( resource, resource.getExtension(), opts );
	}
	
	public static boolean extractResource( Resource resource, String format, ExtractOptions opts ) {
		ResourceExtractor extractor = getResourceExtractor( format );
		if( extractor == null )
			return false;
		
		//extractor.clear();
		if( opts == null ) opts = extractor.getDefaultOptions();
		return extractor.extract( resource, opts );
	}
	
	public static boolean extractAll( Directory dir, String format ) {
		return extractAll( dir, format, null );
	}
	
	public static boolean extractAll( Directory dir, String format, ExtractOptions opts ) {
		ResourceExtractor extractor = getResourceExtractor( format );
		if( opts == null ) opts = extractor.getDefaultOptions();
		boolean valid = true;
		
		for( Resource r : dir.getResourcesByExtension( format ) )
			valid &= extractor.extract( r, opts );
		
		return valid;
	}
}
