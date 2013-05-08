package rin.engine.resource.model.rin3d;

/**
 * Fairly generic 3d model container format. Created so i could convert many different
 *  game model files in various formats to one singular format, for storage.
 * @author Fluttershy
 */
public class Rin3dSpec {
	
	public static final char[] SIGNATURE = new char[] { 'R', 'i', '3' ,'D' };
	
	public static final int C_HEAD = 0x01;
	public static final int C_LIST = 0x02;
	public static final int C_FILE = 0x03;
	public static final int C_MODL = 0x04;
	public static final int C_TXTR = 0x05;
	
	
	
	
	
}
