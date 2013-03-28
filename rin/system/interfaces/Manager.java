package rin.system.interfaces;

import java.util.Map;

public interface Manager<T> {

	public Map<String, T> getManagedItems();
	
}
