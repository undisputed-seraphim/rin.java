package rin.engine.game.ability;

public interface Ability {

	public Ability setTarget( AbilityTarget target );
	public Ability setEffect( AbilityEffect<?> effect );
	public void execute();
	
}
