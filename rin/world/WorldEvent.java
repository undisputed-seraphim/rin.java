package rin.world;

import rin.system.interfaces.Event;

public abstract class WorldEvent<T> extends Event<T> {

	@Override
	public abstract T handle();
}
