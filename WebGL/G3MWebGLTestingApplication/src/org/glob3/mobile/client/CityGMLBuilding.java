

package org.glob3.mobile.client;

import java.util.ArrayList;

import org.glob3.mobile.generated.ILogger;


public class CityGMLBuilding {

   private class Wall {
      final ArrayList<Double> _coordinates;


      public Wall(final ArrayList<Double> coordinates) {
         _coordinates = coordinates;
      }
   }

   public String          _name;
   public ArrayList<Wall> _walls = new ArrayList<CityGMLBuilding.Wall>();


   public CityGMLBuilding(final String name) {
      _name = name;
      ILogger.instance().logInfo("Creating buidling with name " + name);
   }


   public void addWallWithPosLis(final ArrayList<Double> coordinates) {
      ILogger.instance().logInfo("Creating wall with coordinates -> FC:" + coordinates.get(0));
      _walls.add(new Wall(coordinates));
   }


}
