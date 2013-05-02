package rin.engine.resource.model.ism2;

import java.util.ArrayList;

public class Ism2Spec {

	public static final char[] MAGIC = new char[] { 'I', 'S', 'M', '2' };
	
	public static final int C_STRINGS = 0x21;
	public static final int C_TEXTURE_LIST = 0x2E;
	
	public static final int C_ANIMATION = 0x34;
	public static final int C_ANIMATION_FRAME = 0x50;
	public static final int C_FRAME_TRANSFORM = 0x0F;
	
	public static final int C_TRANSFORM_DATA = 0x44;
	
	public static class Ism2TransformData {
		public String name1;
		public String name2;
		public int type;
		public int count;
		public int stride;
		short[] time;
		float[][] data;
		
		public Ism2TransformData( String n1, String n2 ) {
			name1 = n1;
			name2 = n2;
		}
	}
	
	public static class Ism2KeyFrame {
		public String name;
		ArrayList<Ism2TransformData> transforms = new ArrayList<Ism2TransformData>();
		
		public Ism2KeyFrame( String n ) {
			name = n;
		}
		
		public Ism2TransformData addTransform( String name1, String name2 ) {
			Ism2TransformData transform = new Ism2TransformData( name1, name2 );
			transforms.add( transform );
			return transform;
		}
	}
	
	public static class Ism2Animation {
		public String name;
		ArrayList<Ism2KeyFrame> frames = new ArrayList<Ism2KeyFrame>();
		
		public Ism2Animation( String n ) {
			name = n;
		}
		
		public Ism2KeyFrame addFrame( String name ) {
			Ism2KeyFrame frame = new Ism2KeyFrame( name );
			frames.add( frame );
			return frame;
		}
		
		public void print() {
			System.out.println( "Animation: " + name );
			for( Ism2KeyFrame kf : frames ) {
				System.out.println( "\tFrame: " + kf.name );
				for( Ism2TransformData t : kf.transforms ) {
					System.out.println( "\t\tTransform: " + t.name1 );
					System.out.println( "\t\tTime: " + t.time.length );
					System.out.println( "\t\tData: " + t.data.length );
				}
			}
		}
	}
	
}
