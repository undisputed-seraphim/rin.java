package rin.gl.lib3d.properties;

public interface TransitionableProperty<T> {
	public T copy();

	public void doInterpolate( T from, T to, float dt );
}
