package osmBuildings;

import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.Vector3D;

public class OSMBuildings {

	public String getBuildingsInSector(Sector sector, Planet planet,
			float verticalExaggeration, double delta) {

		Vector3D lowerBound = planet.toCartesian(sector._lower);
		Vector3D upperBound = planet.toCartesian(sector._upper);

		return null;
	}
}
