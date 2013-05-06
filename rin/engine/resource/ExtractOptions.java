package rin.engine.resource;

public class ExtractOptions extends ResourceOptions {
	
	private boolean deleteAfterExtract = false;
	private boolean notify = false;
	
	public boolean getDeleteAfterExtract() {
		return deleteAfterExtract;
	}
	
	public ExtractOptions setDeleteAfterExtract( boolean val ) {
		deleteAfterExtract = val;
		return this;
	}
	
	public boolean getNotify() {
		return notify;
	}
	
	public ExtractOptions setNotify( boolean val ) {
		notify = val;
		return this;
	}

}
