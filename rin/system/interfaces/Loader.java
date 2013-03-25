package rin.system.interfaces;

public interface Loader<T> {
	
	public Loader<T> setTarget( T target );
	
	public Loader<T> onLoad( Event<T> e );
	
	/** Method called when this loader's target has finished loading.
	 * <p>
	 *  NOTE: This method is responsible for setting the target of its onLoad event, if one exists.
	 */
	public void loaded();
	
}
