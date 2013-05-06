package rin.engine.resource.image.tid;

import static rin.engine.resource.image.tid.TidSpec.*;

import rin.engine.resource.Resource;
import rin.engine.resource.image.ImageContainer;
import rin.engine.resource.image.ImageDecoder;
import rin.engine.resource.image.ImageOptions;
import rin.engine.resource.image.PixelFormat;
import rin.engine.resource.image.dds.DdsUtils;
import rin.util.bio.BaseBinaryReader;

public class TidDecoder extends BaseBinaryReader implements ImageDecoder {

	private int magic;
	
	private ImageContainer header() {
		magic = readInt32();
		
		boolean valid = false;
		valid |= magic == TTYPE_0;
		valid |= magic == TTYPE_1;
		valid |= magic == TTYPE_2;
		valid |= magic == TTYPE_3;
		valid |= magic == TTYPE_4;
		valid |= magic == TTYPE_5;
		valid |= magic == TTYPE_6;
		valid |= magic == TTYPE_7;
		valid |= magic == TTYPE_8;
		valid |= magic == TTYPE_9;
		if( !valid ) return exitWithError( "Invalid TID image format." );
		
		readInt32(); //filesize
		int offset = readInt32();
		if( readInt32() != 1 ) return exitWithError( "Multiple Textures in TID file...?" );
		
		String name = "";
		int width = 0;
		int height = 0;
		int size = 0;
		int format = 0;
		switch( magic ) {
		
		case TTYPE_0:
			System.out.println( "type 0" );
			name = readString( 32 ).trim();
			readInt32();
			width = readInt32();
			height = readInt32();
			readInt32();
			readInt16();
			readInt16();
			readInt32();
			size = readInt32();
			readInt32();
			
			position( offset );
			return new ImageContainer( width, height, readUInt8( size ), PixelFormat.ARGB );
			
		case TTYPE_1:
			//System.out.println( "type 1" );
			readInt32( 4 );
			name = readString( 32 ).trim();
			readInt32(); //unknown
			width = readInt32();
			height = readInt32();
			readInt32(); //unknown
			readInt16(); //unknown
			readInt16(); //unknown
			readInt32(); //unknown
			size = readInt32();
			readInt32(); //unknown
			readInt32(); //unknown
			format = readInt32();
			readInt32(); //unknown
			readInt32(); //unknown
			
			position( offset );
			switch( format ) {
			
			case DXT1: return DdsUtils.fromRawDXT1( width, height, readInt8( size ) );
			case DXT5: return exitWithError( "DXT5 not yet implemented." );
				
			default:
				return exitWithError( "Uknown compression format " + format );
			}
			
		case TTYPE_2:
			//System.err.println( "type 2" );
			readInt32( 4 );
			name = readString( 32 ).trim();
			readInt32(); //unknown
			width = readInt32();
			height = readInt32();
			readInt32(); //unknown
			readInt16(); //unknown
			readInt16(); //unknown
			readInt32(); //unknown
			size = readInt32();
			readInt32(); //unknown
			
			position( offset );
			return new ImageContainer( width, height, readUInt8( size ), PixelFormat.ARGB );
			
		case TTYPE_3:
			System.err.println( "type 3" );
			break;
			
		case TTYPE_4:
			System.err.println( "type 4" );
			break;
			
		case TTYPE_5:
			//System.err.println( "type 5" );
			readInt32( 4 );
			name = readString( 32 ).trim();
			readInt32();
			width = readInt32();
			height = readInt32();
			readInt32(); //unknown
			readInt16(); //unknown
			readInt16(); //unknown
			readInt32(); //unknown
			size = readInt32();
			readInt32( 2 );
			format = readInt32();
			readInt32( 2 );
			
			position( offset );
			switch( format ) {
			
			case DXT1: return DdsUtils.fromRawDXT1( width, height, readInt8( size ) );
			case DXT5: return exitWithError( "DXT5 not yet implemented." );
				
			default:
				return exitWithError( "Uknown compression format " + format );
			}
			
		case TTYPE_6:
			//System.err.println( "type 6" );
			readInt32( 4 );
			name = readString( 32 ).trim();
			readInt32();
			width = readInt32();
			height = readInt32();
			readInt32();
			readInt16();
			readInt16();
			readInt32();
			size = readInt32();
			readInt32();
			
			position( offset );
			return new ImageContainer( width, height, readUInt8( size ), PixelFormat.ARGB );
			
		case TTYPE_7:
			System.err.println( "type 7" );
			break;
			
		case TTYPE_8:
			//System.err.println( "type 8" );
			readInt32( 4 );
			name = readString( 32 ).trim();
			readInt32();
			width = readInt32();
			height = readInt32();
			readInt32( 3 );
			size = readInt32();
			readInt32( 2 );
			format = readInt32();
			readInt32( 2 );
			
			position( offset );
			switch( format ) {
			
			case DXT1: return DdsUtils.fromRawDXT1( width, height, readInt8( size ) );
			case DXT5: return exitWithError( "DXT5 not yet implemented." );
			
			default:
				System.err.println( "unknown type " + format );
				break;
			}
			break;
			
		case TTYPE_9:
			System.err.println( "type 9" );
			break;
			
		default:
			System.err.println( "unkonwn ttype " + magic );
			break;
		}
		
		return null;
	}
	
	@Override
	public String getExtensionName() { return "tid"; }

	@Override
	public TidOptions getDefaultOptions() { return new TidOptions(); }
	
	@Override
	public ImageContainer decode( Resource resource, ImageOptions opts ) {
		load( resource );
		
		ImageContainer ic = header();
		if( ic == null ) return null;
		
		// if the user wants to save this image once decoded
		if( opts.getSaveOnDecode() && !opts.getSaveFormat().equals( "" ) ) {
			Resource tid = resource.getDirectory().createResource( resource.getBaseName() + "." + opts.getSaveFormat(), true );
			if( tid != null ) {
				if( ic.save( tid ) ) {
					if( opts.getDeleteOnSave() ) resource.delete();
				}
			}
		}
		
		return ic;
	}
	
	public void clear() {
		//TODO: clear all data used by this decoder
	}

}
