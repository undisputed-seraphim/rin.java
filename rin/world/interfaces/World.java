package rin.world.interfaces;

import java.util.Map;

import rin.system.interfaces.Manager;
import rin.world.entity.interfaces.Entity;

public interface World {
	
	public Manager<Entity> getManager();
	public Map<String, Entity> getEntities();
	
}
