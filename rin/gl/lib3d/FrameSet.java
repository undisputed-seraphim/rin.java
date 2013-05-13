package rin.gl.lib3d;

public class FrameSet {

	private float[] rFramesTime = new float[0];
	private float[] rFramesData = new float[0];
	private float[] sFramesTime = new float[0];
	private float[] sFramesData = new float[0];
	private float[] tFramesTime = new float[0];
	private float[] tFramesData = new float[0];
	
	public FrameSet() {}

	public float[] getRotationTime() { return rFramesTime; }
	public float[] getRotationData() { return rFramesData; }
	public void setRotationData( float[] times, float[] data ) {
		if( times != null ) rFramesTime = times;
		if( data != null ) rFramesData = data;
	}
	
	public float[] getScaleTime() { return sFramesTime; }
	public float[] getScaleData() { return sFramesData; }
	public void setScaleData( float[] times, float[] data ) {
		if( times != null ) sFramesTime = times;
		if( data != null ) sFramesData = data;
	}
	
	public float[] getTranslationTime() { return tFramesTime; }
	public float[] getTranslationData() { return tFramesData; }
	public void setTranslationData( float[] times, float[] data ) {
		if( times != null ) tFramesTime = times;
		if( data != null ) tFramesData = data;
	}
}
