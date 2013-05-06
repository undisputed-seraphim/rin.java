package rin.engine.resource.image;

public class ImageOptions {

	private boolean saveOnDecode = false;
	private boolean deleteOnSave = false;
	private String saveFormat = "";
	
	public boolean getSaveOnDecode() {
		return saveOnDecode;
	}
	
	public ImageOptions setSaveOnDecode( boolean val ) {
		saveOnDecode = val;
		return this;
	}
	
	public boolean getDeleteOnSave() {
		return deleteOnSave;
	}
	
	public ImageOptions setDeleteOnSave( boolean val ) {
		deleteOnSave = val;
		return this;
	}
	
	public String getSaveFormat() {
		return saveFormat;
	}
	
	public ImageOptions saveAs( String format ) {
		saveFormat = format;
		setSaveOnDecode( true );
		return this;
	}
}
