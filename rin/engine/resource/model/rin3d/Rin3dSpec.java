package rin.engine.resource.model.rin3d;

/**
 * Fairly generic 3d model container format. Created so i could convert many different
 *  game model files in various formats to one singular format, for storage.
 * @author Fluttershy
 */
public class Rin3dSpec {
	
	public static final char[] SIGNATURE = new char[] { 'R', 'i', '3' ,'D' };
	
	public static final int C_HEAD = 0xC1;
	public static final int C_LIST = 0xC2;
	public static final int C_FILE = 0xC3;
	public static final int C_MODL = 0xC4;
	public static final int C_TEXR = 0xC5;
	
}
