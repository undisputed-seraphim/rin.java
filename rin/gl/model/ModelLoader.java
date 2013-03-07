package rin.gl.model;

public class ModelLoader {
	private Runnable runOnFinish = null;
	public void onFinish( Runnable r ) { this.runOnFinish = r; }
	
	/** System method called when the model is finished loading. Do not call yourself. */
	public void finished() {
		if( this.runOnFinish != null )
			this.runOnFinish.run();
	}
}
