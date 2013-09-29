

package org.glob3.mobile.client;

import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.Vector2I;


public class NasaBillElevationDataURL {

   public static URL compoundURL(Sector sector, Vector2I extent) {
	   return new URL("http://128.102.22.115/elev?REQUEST=GetMap&SERVICE=WMS&VERSION=1.3.0&LAYERS=srtm3&STYLES=&FORMAT=image/bil&BGCOLOR=0xFFFFFF&TRANSPARENT=TRUE&CRS=EPSG:4326&BBOX=-17.0232177085356,27.967811065876,-16.0019401695656,28.6103464294992&WIDTH=256&HEIGHT=256", false);
   }
}
