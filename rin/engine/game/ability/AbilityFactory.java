package rin.engine.game.ability;

public class AbilityFactory {

	public static AbstractAbility createAbility( AbilityTarget target, AbstractAbilityEffect effect ) {
		return new AbstractAbility() {
			private AbilityTarget tar;
			private AbstractAbilityEffect eff;
			public AbstractAbility setTarget( AbilityTarget target ) { tar = target; return this; }
			public AbstractAbility setEffect( AbstractAbilityEffect effect ) { eff = effect; return this; }
			public void execute() { tar.apply( eff ); }
		}.setTarget( target ).setEffect( effect );
	}
	
}
