package rin.engine.view.lib3d;

public class Frame {

	protected JointNode target;
	
	protected float[] tTime;
	protected float[] tData;
	
	protected float[] sTime;
	protected float[] sData;
	
	protected float[] rxTime;
	protected float[] rxData;
	
	protected float[] ryTime;
	protected float[] ryData;
	
	protected float[] rzTime;
	protected float[] rzData;
	
	public Frame( JointNode jn ) { target = jn; }
}
