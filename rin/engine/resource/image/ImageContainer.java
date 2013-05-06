package rin.engine.resource.image;

import rin.engine.resource.FormatManager;
import rin.engine.resource.Resource;
import rin.engine.resource.ResourceContainer;
import rin.engine.util.ImageUtils;

public class ImageContainer extends ResourceContainer {
	
	private String n = "";
	
	private int w = 0;
	private int h = 0;
	
	private PixelFormat pixelFormat = PixelFormat.RGB;
	private short[] pixelData = new short[0];
	
	public ImageContainer() {}
	
	public ImageContainer( int width, int height ) {
		w = width;
		h = height;
	}
	
	public ImageContainer( int width, int height, short[] data ) {
		this( width, height );
		pixelData = data;
	}
	
	public ImageContainer( int width, int height, short[] data, PixelFormat format ) {
		this( width, height, data );
		pixelFormat = format;
	}
	
	public String getName() {
		return n;
	}
	
	public ImageContainer setName( String name ) {
		n = name;
		return this;
	}
	
	public int getWidth() {
		return w;
	}
	
	public ImageContainer setWidth( int width ) {
		w = width;
		return this;
	}
	
	public int getHeight() {
		return h;
	}
	
	public ImageContainer setHeight( int height ) {
		h = height;
		return this;
	}
	
	public PixelFormat getFormat() {
		return pixelFormat;
	}
	
	public ImageContainer setFormat( PixelFormat format ) {
		pixelFormat = format;
		return this;
	}
	
	public short[] getData() {
		return pixelData;
	}
	
	public ImageContainer setData( short[] data ) {
		pixelData = data;
		return this;
	}
	
	public boolean hasAlpha() {
		return pixelFormat.toString().toUpperCase().indexOf( "A" ) != -1;
	}
	
	public boolean save( Resource resource ) {
		return FormatManager.encodeImage( this, resource );
	}
	
	public boolean save( Resource resource, String format ) {
		return FormatManager.encodeImage( this, resource, format );
	}
	
	public void test() {
		ImageUtils.test( w, h, pixelFormat.getStride(), pixelData );
	}
	
	public ImageContainer flipY() {
		short[] flipped = new short[pixelData.length];
		int rowLength = w * pixelFormat.getStride();
		for( int i = 0, j = pixelData.length - rowLength; i < pixelData.length; i += rowLength, j -= rowLength ) {
			for( int k = 0; k < rowLength; k++ ) {
				flipped[i+k] = pixelData[j+k];
			}
		}
		pixelData = flipped;
		return this;
	}
	
	/*public ImageContainer flipX() {
		short[] flipped = new short[pixelData.length];
		int rowLength = w * 3;
		for( int i = 0, j = rowLength - 3; i < w; i+=3, j-=3 ) {
			for( int k = 0; k < h; k++ ) {
				flipped[i+k*rowLength] = pixelData[j+k*rowLength];
			}
		}
		pixelData = flipped;
		return this;
	}*/
}
