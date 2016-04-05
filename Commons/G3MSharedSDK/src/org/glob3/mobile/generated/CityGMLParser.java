

package org.glob3.mobile.generated;

//
//  CityGMLParser.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 30/3/16.
//
//

//
//  CityGMLParser.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 30/3/16.
//
//


//class MarksRenderer;
//class MeshRenderer;

public class CityGMLParser {

   public static java.util.ArrayList<CityGMLBuilding> parseLOD2Buildings2(final IXMLNode cityGMLDoc) {

      final java.util.ArrayList<CityGMLBuilding> buildings = new java.util.ArrayList<CityGMLBuilding>();

      final java.util.ArrayList<IXMLNode> buildingsXML = cityGMLDoc.evaluateXPathAsXMLNodes("//*[local-name()='Building']");
      //      ILogger.instance().logInfo("N Buildings %d", buildingsXML.size());
      for (int i = 0; i < buildingsXML.size(); i++) {

         final IXMLNode b = buildingsXML.get(i);

         //Name
         String name = b.getAttribute("id");
         if (name == null) {
            name = new String("NO NAME");
         }
         ILogger.instance().logInfo("PARSE BUILDING " + name);

         final java.util.ArrayList<CityGMLBuildingSurface> surfaces = new java.util.ArrayList<CityGMLBuildingSurface>();

         //Grounds
         final java.util.ArrayList<IXMLNode> grounds = b.evaluateXPathAsXMLNodes("*[local-name()='boundedBy']//*[local-name()='GroundSurface']//*[local-name()='posList']");
         for (int j = 0; j < grounds.size(); j++) {
            String str = grounds.get(j).getTextContent();
            if (str != null) {
               //ILogger::instance()->logInfo("%s", str->c_str() );

               final java.util.ArrayList<Double> coors = IStringUtils.instance().parseDoubles(str, " ");
               if ((coors.size() % 3) != 0) {
                  ILogger.instance().logError("Problem parsing wall coordinates.");
               }

               surfaces.add(CityGMLBuildingSurface.createFromArrayOfCityGMLWGS84Coordinates(coors,
                        CityGMLBuildingSurfaceType.GROUND));

               str = null;
            }

            if (grounds.get(j) != null) {
               grounds.get(j).dispose();
            }
         }

         //Walls
         final java.util.ArrayList<IXMLNode> walls = b.evaluateXPathAsXMLNodes("*[local-name()='boundedBy']//*[local-name()='WallSurface']//*[local-name()='posList']");
         for (int j = 0; j < walls.size(); j++) {
            String str = walls.get(j).getTextContent();
            if (str != null) {
               //ILogger::instance()->logInfo("%s", str->c_str() );

               final java.util.ArrayList<Double> coors = IStringUtils.instance().parseDoubles(str, " ");
               if ((coors.size() % 3) != 0) {
                  ILogger.instance().logError("Problem parsing wall coordinates.");
               }

               surfaces.add(CityGMLBuildingSurface.createFromArrayOfCityGMLWGS84Coordinates(coors,
                        CityGMLBuildingSurfaceType.WALL));

               str = null;
            }

            if (walls.get(j) != null) {
               walls.get(j).dispose();
            }
         }

         //Roofs
         final java.util.ArrayList<IXMLNode> roofs = b.evaluateXPathAsXMLNodes("*[local-name()='boundedBy']//*[local-name()='RoofSurface']//*[local-name()='posList']");
         for (int j = 0; j < roofs.size(); j++) {
            String str = roofs.get(j).getTextContent();
            if (str != null) {
               //ILogger::instance()->logInfo("%s", str->c_str() );

               final java.util.ArrayList<Double> coors = IStringUtils.instance().parseDoubles(str, " ");
               if ((coors.size() % 3) != 0) {
                  ILogger.instance().logError("Problem parsing wall coordinates.");
               }

               surfaces.add(CityGMLBuildingSurface.createFromArrayOfCityGMLWGS84Coordinates(coors,
                        CityGMLBuildingSurfaceType.ROOF));

               str = null;
            }

            if (roofs.get(j) != null) {
               roofs.get(j).dispose();
            }
         }

         final CityGMLBuilding nb = new CityGMLBuilding(name, 1, surfaces);

         buildings.add(nb);

         name = null;
         if (b != null) {
            b.dispose();
         }

      }

      return buildings;
   }


   public static void addLOD2MeshAndMarksFromFile(final String url,
                                                  final IDownloader downloader,
                                                  final Planet planet,
                                                  final MeshRenderer meshRenderer,
                                                  final MarksRenderer marksRenderer) {
      downloader.requestBuffer(new URL(url), DownloadPriority.HIGHEST, TimeInterval.fromHours(1), true,
               new G3MCityGMLDemoScene_BufferDownloadListener(planet, meshRenderer, marksRenderer), true);
   }

}
