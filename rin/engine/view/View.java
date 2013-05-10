package rin.engine.view;

public interface View {

	/**
	 * Initialize anything having to deal with this view. After this call,
	 * the view should be ready for {@link #show(int, int)}.
	 */
	public void init();
	
	/**
	 * Set the title of the current view. Differs by implementation, but
	 * should commonly set the title of the title bar. 
	 * @param title desired view title
	 */
	public void setTitle( String title );
	
	/**
	 * Set the size of this view.
	 * @param width width of graphical display
	 * @param height height of graphical display
	 */
	public void setSize( int width, int height );
	
	/**
	 * Display the graphical representation of this view.
	 */
	public void show();
	
	/**
	 * Update a view to show the most current scene data.
	 */
	public void update();
	
	/**
	 * Check if the view was closed.
	 * @return true if view has been closed
	 */
	public boolean isClosed();
	
	/**
	 * Destroy all resources belonging to this view.
	 */
	public void destroy();
}
