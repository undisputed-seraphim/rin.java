package rin.engine.resource.formats.gmo;

public class GMOSpec {

	public static final char[] MAGIC = new char[] {
		'O', 'M', 'G', '.', '0', '0', '.', '1', 'P', 'S', 'P', '\0', '\0', '\0', '\0'
	};
	
	public static final short C_ROOT = 0x0002;
	public static final short C_LIST = 0x0003;
}
