package org.glob3.mobile.client;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.EffectTarget;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.specific.G3MWidget_WebGL;


public class MyG3MWidget_WebGL extends G3MWidget_WebGL {
	
	private Sector _sector;
	

	   public MyG3MWidget_WebGL() {
		   super();
		   exportMyJSFunctions();
	   }
	   
	   public void setSectorForLODAugmented(final Sector sector) {
		   _sector = sector;
	   }
		   
	   private native void exportMyJSFunctions() /*-{
			var that = this;
			if (!$wnd.G3M) {
				$wnd.G3M = {};
			}

			// here we set all my function headings
			$wnd.G3M.doble = $entry(function(numero) {
				return that.@org.glob3.mobile.client.MyG3MWidget_WebGL::doble(I)(numero);
			});
			$wnd.G3M.setLODAugmentedFactor = $entry(function(factor) {
				that.@org.glob3.mobile.client.MyG3MWidget_WebGL::setLODAugmentedFactor(D)(factor);
			});
			$wnd.G3M.orbiCameratToPitch = $entry(function(pitch, seconds) {
				that.@org.glob3.mobile.client.MyG3MWidget_WebGL::orbitCameraToPitch(DD)(pitch, seconds);
			});
	   }-*/;

	   private int doble(final int numero) {
	      return 2*numero;
	   }
	   
	   public void setLODAugmentedFactor (final double factor) {
		   getPlanetRenderer().addLODAugmentedForSector(_sector, factor);
	   }
	   
	   public void orbitCameraToPitch(double pitch, double seconds) {
		   /*
		   VerticalOrbitEffect effect = new VerticalOrbitEffect(Angle.fromDegrees(pitch),
				   TimeInterval.fromSeconds(seconds));
		   EffectTarget target = getNextCamera().getEffectTarget();
		   getG3MContext().getEffectsScheduler().startEffect(effect, target);*/
		   getG3MContext().getWidget().orbitCameraToPitch(Angle.fromDegrees(pitch), 
				   TimeInterval.fromSeconds(seconds));
	   }

}
