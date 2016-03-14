

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


   public double getNumberOfBuildings() {
      final XPathResult res = xpath("count(/CityModel/cityObjectMember/bldg:Building)");
      final double i = res.getAsNumber();
      return i;
   }


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

      final int nBuildings = (int) getNumberOfBuildings();
      for (int i = 1; i <= nBuildings; i++) { //Check number of buildings TEST
         final String bPath = "/CityModel/cityObjectMember[" + i + "]/bldg:Building";

         final XPathResult bs = xpath(bPath + "/gml:name/text()");
         final String name = bs.getTextContentAsText();

         final CityGMLBuilding building = new CityGMLBuilding(name);

         //Walls
         final int nwalls = xpath(bPath + "/bldg:boundedBy/bldg:WallSurface/bldg:lod2MultiSurface").getAsXMLDocuments().size();
         for (int j = 1; j <= nwalls; j++) {
            final String xpathcoord = bPath + "/bldg:boundedBy[" + j
                                      + "]/bldg:WallSurface/bldg:lod2MultiSurface//gml:posList/text()";
            final XPathResult wc = xpath(xpathcoord);
            building.addWallWithPosLis(wc.getTextContentAsNumberArray(" "));
         }

         buildings.add(building);
      }

      return buildings;
   }
}
