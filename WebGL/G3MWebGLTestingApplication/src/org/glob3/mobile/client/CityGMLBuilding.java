

package org.glob3.mobile.client;

import java.util.ArrayList;


public class CityGMLBuilding {

   private class Wall {
      public final ArrayList<Double> _coordinates;


      public Wall(final ArrayList<Double> coordinates) {
         _coordinates = coordinates;
      }
   }

   public String          _name;
   public ArrayList<Wall> _walls = new ArrayList<CityGMLBuilding.Wall>();


   public CityGMLBuilding(final String name) {
      _name = name;
   }


   public void addWallWithPosLis(final ArrayList<Double> coordinates) {
      _walls.add(new Wall(coordinates));
   }


   public String description() {
      String s = "Building Name: " + _name;
      for (int i = 0; i < _walls.size(); i++) {
         s += "\n Wall: Coordinates: ";
         for (int j = 0; j < _walls.get(i)._coordinates.size(); j += 3) {
            s += "(" + _walls.get(i)._coordinates.get(j) + ", " + _walls.get(i)._coordinates.get(j + 1) + ", "
                     + _walls.get(i)._coordinates.get(j + 1) + ") ";
         }
      }
      return s;
   }

}
