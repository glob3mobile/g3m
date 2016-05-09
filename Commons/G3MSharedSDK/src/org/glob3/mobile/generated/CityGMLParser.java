package org.glob3.mobile.generated; 
public class CityGMLParser
{

  private static final IThreadUtils _threadUtils = null;
  private static IDownloader _downloader = null;



  public static void initialize(G3MContext context)
  {
    _threadUtils = context.getThreadUtils();
    _downloader = context.getDownloader();
  }

  public static java.util.ArrayList<CityGMLBuilding> parseLOD2Buildings2(IXMLNode cityGMLDoc)
  {
  
    ILogger.instance().logInfo("CityGMLParser starting parse");
  
    java.util.ArrayList<CityGMLBuilding> buildings = new java.util.ArrayList<CityGMLBuilding>();
  
    final java.util.ArrayList<IXMLNode> buildingsXML = cityGMLDoc.evaluateXPathAsXMLNodes("//*[local-name()='Building']");
    //      ILogger.instance().logInfo("N Buildings %d", buildingsXML.size());
    for (int i = 0; i < buildingsXML.size(); i++)
    {
  
      IXMLNode b = buildingsXML.get(i);
  
      //Name
      String name = b.getAttribute("id");
      if (name == null)
      {
        name = new String("NO NAME");
      }
  
      java.util.ArrayList<CityGMLBuildingSurface> surfaces = new java.util.ArrayList<CityGMLBuildingSurface>();
  
      //Grounds
      final java.util.ArrayList<IXMLNode> grounds = b.evaluateXPathAsXMLNodes(" *[local-name()='boundedBy']//*[local-name()='GroundSurface']//*[local-name()='posList']");
      for (int j = 0; j < grounds.size(); j++)
      {
        String str = grounds.get(j).getTextContent();
        if (str != null)
        {
          //ILogger::instance()->logInfo("%s", str->c_str() );
  
          java.util.ArrayList<Double> coors = IStringUtils.instance().parseDoubles(str, " ");
          if (coors.size() % 3 != 0)
          {
            ILogger.instance().logError("Problem parsing wall coordinates.");
          }
  
          surfaces.add(CityGMLBuildingSurface.createFromArrayOfCityGMLWGS84Coordinates(coors, CityGMLBuildingSurfaceType.GROUND));
  
          str = null;
        }
  
        if (grounds.get(j) != null)
           grounds.get(j).dispose();
      }
  
      //Walls
      final java.util.ArrayList<IXMLNode> walls = b.evaluateXPathAsXMLNodes("*[local-name()='boundedBy']//*[local-name()='WallSurface']//*[local-name()='posList']");
      for (int j = 0; j < walls.size(); j++)
      {
        String str = walls.get(j).getTextContent();
        if (str != null)
        {
          //ILogger::instance()->logInfo("%s", str->c_str() );
  
          java.util.ArrayList<Double> coors = IStringUtils.instance().parseDoubles(str, " ");
          if (coors.size() % 3 != 0)
          {
            ILogger.instance().logError("Problem parsing wall coordinates.");
          }
  
          surfaces.add(CityGMLBuildingSurface.createFromArrayOfCityGMLWGS84Coordinates(coors, CityGMLBuildingSurfaceType.WALL));
  
          str = null;
        }
  
        if (walls.get(j) != null)
           walls.get(j).dispose();
      }
  
      //Roofs
      final java.util.ArrayList<IXMLNode> roofs = b.evaluateXPathAsXMLNodes("*[local-name()='boundedBy']//*[local-name()='RoofSurface']//*[local-name()='posList']");
      for (int j = 0; j < roofs.size(); j++)
      {
        String str = roofs.get(j).getTextContent();
        if (str != null)
        {
          //ILogger::instance()->logInfo("%s", str->c_str() );
  
          java.util.ArrayList<Double> coors = IStringUtils.instance().parseDoubles(str, " ");
          if (coors.size() % 3 != 0)
          {
            ILogger.instance().logError("Problem parsing wall coordinates.");
          }
  
          surfaces.add(CityGMLBuildingSurface.createFromArrayOfCityGMLWGS84Coordinates(coors, CityGMLBuildingSurfaceType.ROOF));
  
          str = null;
        }
  
        if (roofs.get(j) != null)
           roofs.get(j).dispose();
      }
  
      CityGMLBuilding nb = new CityGMLBuilding(name, 1, surfaces);
  
      buildings.add(nb);
  
      name = null;
      if (b != null)
         b.dispose();
  
    }
  
    ILogger.instance().logInfo("CityGMLParser parse finished: %d buildings.", buildings.size());
  
    return buildings;
  }

  public static java.util.ArrayList<CityGMLBuilding> parseLOD2Buildings2(String cityGMLString)
  {
  
    ILogger.instance().logInfo("CityGMLParser starting parse");
  
    java.util.ArrayList<CityGMLBuilding> buildings = new java.util.ArrayList<CityGMLBuilding>();
  
  
    int pos = 0;
    final int length = cityGMLString.length();
    while (pos < length)
    {
      IStringUtils.StringExtractionResult beginning = IStringUtils.extractSubStringBetween(cityGMLString, "bldg:Building gml:id=\"", "\"", pos);
      String name = beginning._string;
      if (beginning._endingPos == String.npos)
      {
        break;
      }
      pos = beginning._endingPos + 1;
  
  
  
      int endPos = cityGMLString.indexOf("</bldg:Building>", pos);
  
      //Reading surfaces
      java.util.ArrayList<CityGMLBuildingSurface> surfaces = new java.util.ArrayList<CityGMLBuildingSurface>();
      while (true)
      {
        IStringUtils.StringExtractionResult points = IStringUtils.extractSubStringBetween(cityGMLString, "<gml:posList>", "</gml:posList>", pos);
  
        if (points._endingPos == String.npos || points._endingPos >= endPos)
        {
          break;
        }
  
        CityGMLBuildingSurfaceType type = CityGMLBuildingSurfaceType.WALL;
        int groundPos = cityGMLString.indexOf("bldg:GroundSurface", pos);
        if (groundPos < points._endingPos)
        {
          type = CityGMLBuildingSurfaceType.GROUND;
        }
        else
        {
          int roofPos = cityGMLString.indexOf("bldg:RoofSurface", pos);
          if (roofPos < points._endingPos)
          {
            type = CityGMLBuildingSurfaceType.ROOF;
          }
        }
  
        pos = points._endingPos +1;
  
        pos = cityGMLString.indexOf("</bldg:boundedBy>", pos);
  
        java.util.ArrayList<Double> coors = IStringUtils.instance().parseDoubles(points._string, " ");
        surfaces.add(CityGMLBuildingSurface.createFromArrayOfCityGMLWGS84Coordinates(coors, type));
  
      }
  
      CityGMLBuilding nb = new CityGMLBuilding(name, 1, surfaces);
      buildings.add(nb);
  
    }
    ILogger.instance().logInfo("CityGMLParser parse finished: %d buildings.", buildings.size());
  
    return buildings;
  }

  public static void parseFromURL(URL url, CityGMLListener listener, boolean deleteListener)
  {
  
    if (_downloader == null)
    {
      throw new RuntimeException("CityGMLParser not initialized");
    }
  
    _downloader.requestBuffer(url, DownloadPriority.HIGHEST, TimeInterval.fromHours(1), true, new CityGMLParser_BufferDownloadListener(_threadUtils, listener, deleteListener), true);
  }

}