package rin.sample.chase.abilities;

import rin.engine.game.ability.Ability;
import rin.engine.game.ability.AbilityEffect;
import rin.engine.game.ability.AbilityTarget;

public class StandardAbility implements Ability {

	private AbilityTarget abilityTarget;
	private AbilityEffect<?> abilityEffect;
	
	@Override
	public StandardAbility setTarget( AbilityTarget target ) {
		abilityTarget = target;
		return this;
	}

	@Override
	public StandardAbility setEffect( AbilityEffect<?> effect ) {
		abilityEffect = effect;
		return this;
	}

	@Override
	public void execute() {
		abilityTarget.apply( abilityEffect );
	}

}
