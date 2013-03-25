package rin.system.interfaces;

public interface DestroyRequestable extends Destructor {
	public void requestDestroy();
	public boolean isDestroyRequested();
	@Override public void destroy();
}
