package rin.engine.resource.image;

import rin.engine.resource.Resource;

public interface ImageDecoder {

	/**
	 * Defines the extension, or format, that uniquely identifies this Decoder.
	 * @return String extenstion
	 */
	public String getExtensionName();
	
	/**
	 * Return the default options for decoding this image format.
	 * @return ImageOptions depicting default options
	 */
	public ImageOptions getDefaultOptions();
	
	/**
	 * Decode an image, returning an universal ImageContainer object.
	 * @param resource Resource pointer to an image file
	 * @param opts ImageOptions for the specified image format
	 * @return ImageContainer representing the decoded image
	 */
	public ImageContainer decode( Resource resource, ImageOptions opts );
	
	/**
	 * Clears all data associated with this ImageDecoder.
	 */
	public void clear();
	
}
