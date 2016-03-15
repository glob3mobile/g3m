

package org.glob3.mobile.client;

import java.util.ArrayList;

import org.glob3.mobile.generated.ILogger;

import com.google.gwt.core.client.JavaScriptObject;


public class CityGMLDocument
         extends
            XMLDocument {

   public CityGMLDocument(final String doc) {
      super(doc);
   }


   public CityGMLDocument(final JavaScriptObject jso) {
      super(jso);
   }


   //   public double getNumberOfBuildings() {
   //      final XPathResult res = xpath("count(/CityModel/cityObjectMember/bldg:Building)");
   //      final double i = res.getAsNumber();
   //      return i;
   //   }


   public void logAllBuildingNames() {
      final XPathResult res2 = xpath("/CityModel/cityObjectMember/bldg:Building/gml:name");
      final ArrayList<XMLDocument> docs = res2.getAsXMLDocuments();
      ILogger.instance().logInfo("We have %d buildings", docs.size());

      for (int j = 0; j < docs.size(); j++) {
         ILogger.instance().logInfo(docs.get(j).getTextContent());
      }
   }


   public int getBuildingNumberOfWalls(final int n) {
      final XPathResult res = xpath("count(/CityModel/cityObjectMember/bldg:Building[" + n + "]/bldg:boundedBy)");
      return (int) res.getAsNumber();
   }


   public ArrayList<CityGMLBuilding> parseLOD2Buildings() {

      final ArrayList<CityGMLBuilding> buildings = new ArrayList<CityGMLBuilding>();

      final int nBuildings = (int) evaluateXPathAndGetNumberValueAsDouble("count(/CityModel/cityObjectMember/bldg:Building)");

      for (int i = 1; i <= nBuildings; i++) { //Check number of buildings TEST
         final String bPath = "/CityModel/cityObjectMember[" + i + "]/bldg:Building";

         final String name = evaluateXPathAndGetTextContentAsText(bPath + "/gml:name/text()");

         final CityGMLBuilding building = new CityGMLBuilding(name);

         final int roofType = evaluateXPathAndGetTextContentAsInteger(bPath + "/bldg:roofType/text()");
         building.setRoofTypeCode(roofType);

         //Walls
         final int nwalls = xpath(bPath + "/bldg:boundedBy/bldg:WallSurface/bldg:lod2MultiSurface").getAsXMLDocuments().size();
         for (int j = 1; j <= nwalls; j++) {
            final String xpathcoord = bPath + "/bldg:boundedBy[" + j
                     + "]/bldg:WallSurface/bldg:lod2MultiSurface//gml:posList/text()";
            final XPathResult wc = xpath(xpathcoord);
            final ArrayList<Double> coor = wc.getTextContentAsNumberArray(" ");
            if ((coor.size() % 3) != 0) {
               ILogger.instance().logError("Coordinates incorrect (%d) in building " + name, coor.size());
            }
            building.addSurfaceWithPosLis(coor);
         }

         //Rooftops
         final int nroof = (int) evaluateXPathAndGetNumberValueAsDouble("count("
                  + bPath
                  + "/bldg:boundedBy/bldg:RoofSurface/bldg:lod2MultiSurface)");
         ILogger.instance().logInfo("Building with %d roof surfaces", nroof);
         //         for (int j = 1; j <= nwalls; j++) {
         //            final String xpathcoord = bPath + "/bldg:boundedBy[" + j
         //                     + "]/bldg:WallSurface/bldg:lod2MultiSurface//gml:posList/text()";
         //            final XPathResult wc = xpath(xpathcoord);
         //            final ArrayList<Double> coor = wc.getTextContentAsNumberArray(" ");
         //            if ((coor.size() % 3) != 0) {
         //               ILogger.instance().logError("Coordinates incorrect (%d) in building " + name, coor.size());
         //            }
         //            building.addWallWithPosLis(coor);
         //         }

         buildings.add(building);
      }

      return buildings;
   }
}
