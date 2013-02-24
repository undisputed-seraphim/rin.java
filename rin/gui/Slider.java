package rin.gui;

import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

import rin.gui.GUIFactory.SliderEvent;

public class Slider extends GUIComponent<Slider, SliderEvent> {
	private static int items = 0;
	
	private Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
	private boolean dynamic = true;
	
	public Slider() { this( "Slider-" + Slider.items++ ); }
	public Slider( String id ) {
		this.id = id;
		this.canHaveChildren = false;
		this.target = new JSlider();
		this.target.setFont( new GUIFactory.Font( "Arial", GUIFactory.Font.PLAIN, 10 ) );
		
		this.setRange( 0, 100 );
		this.setMajorTickSize( 50 );
		this.setMinorTickSize( 10 );
		this.setShowTrack( true );
		this.setShowLabels( true );
		this.setShowTicks( true );
		
		this.onWindowLoad( new GUIFactory.OnLoadEvent() {
			@Override public void run() {
				((JSlider)this.component.target).addChangeListener( this.component );
			}
		}.setTargets( this.target, this ) );
	}
	
	private JSlider real() { return (JSlider)this.target; }
	
	public Slider setOrientation( GUIFactory.Orientation orientation ) { this.real().setOrientation( orientation.value ); return this.update(); }
	
	public Slider setMajorTickSize( int size ) { this.real().setMajorTickSpacing( size ); return this.update(); }
	public Slider setMinorTickSize( int size ) { this.real().setMinorTickSpacing( size ); return this.update(); }
	public Slider setTickSize( int major, int minor ) { this.setMajorTickSize( major ); this.setMinorTickSize( minor ); return this.update(); }
	
	public Slider setMinimum( int min ) { this.real().setMinimum( min ); return this.update(); }
	public Slider setMaximum( int max ) { this.real().setMaximum( max ); return this.update(); }
	public Slider setRange( int min, int max ) { this.setMinimum( min ); this.setMaximum( max ); return this; }
	
	public Slider setSnap( boolean snap ) { this.real().setSnapToTicks( snap ); return this.update(); }
	public Slider setDynamic( boolean dynamic ) { this.dynamic = dynamic; return this; }
	
	public Slider setShowTicks( boolean show ) { this.real().setPaintTicks( show ); return this.update(); }
	public Slider setShowTrack( boolean show ) { this.real().setPaintTrack( show ); return this.update(); }
	public Slider setShowLabels( boolean show ) { this.real().setPaintLabels( show ); return this.update(); }
	
	public Slider setLabel( int value, String text ) {
		this.labels.put( value, new JLabel( text ) );
		this.real().setLabelTable( this.labels );
		return this.update();
	}
	
	private SliderEvent runOnChange = null;
	public Slider onChange( SliderEvent e ) {
		this.runOnChange = e.<SliderEvent>setTarget( this );
		return this;
	}
	
	@Override public void stateChanged( ChangeEvent e ) {
		if( !this.real().getValueIsAdjusting() || this.dynamic ) {
			if( this.runOnChange != null ) {
				this.runOnChange.value = this.real().getValue();
				this.runOnChange.run();
			}
		}
	}
	
	@Override protected Slider destroy() {
		this.real().removeChangeListener( this );
		super.destroy();
		
		return null;
	}
}
