package org.glob3.mobile.generated; 
public class GlobalMembersCityGMLBuilding
{

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