package rin.gl.lib3d;

public interface Pickable {
	public boolean isPicking();
	public Actor setPicking( boolean val );
	
	public void selected();
}
