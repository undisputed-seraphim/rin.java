package rin.engine.game.ability;

public abstract class AbstractAbility {

	public abstract AbstractAbility setTarget( AbilityTarget target );
	public abstract AbstractAbility setEffect( AbstractAbilityEffect effect );
	public abstract void execute();
	
}
