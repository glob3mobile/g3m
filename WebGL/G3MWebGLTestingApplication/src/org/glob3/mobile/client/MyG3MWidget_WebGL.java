package org.glob3.mobile.client;

import org.glob3.mobile.generated.Sector;
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
			$wnd.G3M.addLODAugmented = $entry(function(factor) {
				that.@org.glob3.mobile.client.MyG3MWidget_WebGL::addLODAugmented(D)(factor);
			});
	   }-*/;

	   private int doble(final int numero) {
	      return 2*numero;
	   }
	   
	   private void addLODAugmented (final double factor) {
		   getPlanetRenderer().addLODAugmentedForSector(_sector, factor);
	   }
}
