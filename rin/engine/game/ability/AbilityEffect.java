package rin.engine.game.ability;

public interface AbilityEffect<T extends AbilityTarget> {

	public void process( T target );
	
}
