package org.glob3.mobile.generated; 
public class GlobalMembersCityGMLBuilding
{

       public static int checkWallsVisibility(java.util.ArrayList<CityGMLBuilding> buildings)
       {
         int nInvisibleWalls = 0;
         for (int i = 0; i < buildings.size() - 1; i++)
         {
           CityGMLBuilding b1 = buildings.get(i);

           for (int j = i+1; j < i+30 && j < buildings.size() - 1; j++)
           {
             CityGMLBuilding b2 = buildings.get(j);
             nInvisibleWalls += GlobalMembersCityGMLBuilding.checkWallsVisibility(b1, b2);
           }

         }
         return nInvisibleWalls;
       }

       public static CityGMLBuildingTessellatorData getTessllatorData()
       {
         return _tessellatorData;
       }

       public static void setTessellatorData(CityGMLBuildingTessellatorData data)
       {
         _tessellatorData = null;
         _tessellatorData = data;
       }

       public static int getNumberOfVertex()
       {
         int n = 0;
         for (int i = 0; i < _surfaces.size(); i++)
         {
           n += (int)_surfaces[i]._geodeticCoordinates.size();
         }
         return n;
       }
     }