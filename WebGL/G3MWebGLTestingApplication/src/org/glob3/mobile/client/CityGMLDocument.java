

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
      super(jso, null);
   }


   public ArrayList<CityGMLBuilding> parseLOD2Buildings() {


      final ArrayList<CityGMLBuilding> buildings = new ArrayList<CityGMLBuilding>();

      final ArrayList<XMLDocument> buildingsXML = evaluateXPathAsXMLDocuments("/CityModel/cityObjectMember/bldg:Building");
      ILogger.instance().logInfo("N Buildings %d", buildingsXML.size());
      for (final XMLDocument b : buildingsXML) {
         CityGMLBuilding building = null;

         //Name
         try {
            final String name = b.evaluateXPathAndGetTextContentAsText("/bldg:Building/gml:name/text()");
            building = new CityGMLBuilding(name);
         }
         catch (final Exception e) {
            ILogger.instance().logError("No name for building");
            building = new CityGMLBuilding("NO NAME");

            //ID
            try {
               final String id = b.getAttributeAsText("gml:id");
               building = new CityGMLBuilding(id);
            }
            catch (final Exception e2) {
               ILogger.instance().logError("No ID for building");
               building = new CityGMLBuilding("NO NAME");
            }
         }


         //RoofType
         try {
            final int roofType = b.evaluateXPathAndGetTextContentAsInteger("/bldg:Building/bldg:roofType/text()");
            building.setRoofTypeCode(roofType);
         }
         catch (final Exception e) {
            ILogger.instance().logError("No roof type for building");
         }

         //Walls
         final ArrayList<XMLDocument> wallsXML = b.evaluateXPathAsXMLDocuments("/bldg:Building/bldg:boundedBy/bldg:WallSurface/bldg:lod2MultiSurface//gml:posList");
         ILogger.instance().logInfo("N Walls %d", wallsXML.size());
         for (final XMLDocument s : wallsXML) {
            final ArrayList<Double> coor = s.getTextContentAsNumberArray(" ");
            building.addSurfaceWithPosLis(coor);
         }

         //Rooftops
         final ArrayList<XMLDocument> roofsXML = b.evaluateXPathAsXMLDocuments("/bldg:Building/bldg:boundedBy/bldg:RoofSurface/bldg:lod2MultiSurface//gml:posList");
         ILogger.instance().logInfo("N Roofs %d", roofsXML.size());
         for (final XMLDocument s : roofsXML) {
            final ArrayList<Double> coor = s.getTextContentAsNumberArray(" ");
            building.addSurfaceWithPosLis(coor);
         }

         //Ground
         final ArrayList<XMLDocument> groundXML = b.evaluateXPathAsXMLDocuments("/bldg:Building/bldg:boundedBy/bldg:GroundSurface/bldg:lod2MultiSurface//gml:posList");
         ILogger.instance().logInfo("N Roofs %d", roofsXML.size());
         for (final XMLDocument s : groundXML) {
            final ArrayList<Double> coor = s.getTextContentAsNumberArray(" ");
            building.addSurfaceWithPosLis(coor);
         }

         buildings.add(building);
      }

      return buildings;
   }
}
