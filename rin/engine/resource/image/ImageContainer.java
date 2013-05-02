package rin.engine.resource.image;

import rin.engine.resource.ResourceContainer;
import rin.engine.util.ImageUtils;

public class ImageContainer extends ResourceContainer {
	
	private int w = 0;
	private int h = 0;
	
	private PixelFormat pixelFormat = PixelFormat.RGB888;
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
	
	public void test() {
		ImageUtils.test( w, h, pixelFormat.getStride(), pixelData );
	}
}
