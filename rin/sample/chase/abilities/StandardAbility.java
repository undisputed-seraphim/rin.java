package rin.sample.chase.abilities;

import rin.engine.game.ability.AbstractAbility;
import rin.engine.game.ability.AbstractAbilityEffect;
import rin.engine.game.ability.AbilityTarget;

public class StandardAbility extends AbstractAbility {

	private AbilityTarget abilityTarget;
	private AbstractAbilityEffect abilityEffect;
	
	@Override
	public StandardAbility setTarget( AbilityTarget target ) {
		abilityTarget = target;
		return this;
	}

	@Override
	public StandardAbility setEffect( AbstractAbilityEffect effect ) {
		abilityEffect = effect;
		return this;
	}

	@Override
	public void execute() {
		abilityTarget.apply( abilityEffect );
	}

}
