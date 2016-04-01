package org.glob3.mobile.generated; 
public class G3MCityGMLDemoScene_BufferDownloadListener extends IBufferDownloadListener
{
  private final Planet _planet;
  private MeshRenderer _meshRenderer;
  private MarksRenderer _marksRenderer;
  public G3MCityGMLDemoScene_BufferDownloadListener(Planet planet, MeshRenderer meshRenderer, MarksRenderer marksRenderer)
  {
     _planet = planet;
     _meshRenderer = meshRenderer;
     _marksRenderer = marksRenderer;
  }

  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {

    String s = buffer.getAsString();
    if (buffer != null)
       buffer.dispose();

    IXMLNode xml = IFactory.instance().createXMLNodeFromXML(s);

    java.util.ArrayList<CityGMLBuilding> buildings = CityGMLParser.parseLOD2Buildings2(xml);

    //Adding marks
    for (int i = 0; i < buildings.size(); i++)
    {
      _marksRenderer.addMark(buildings.get(i).createMark(false));
    }

    //Checking walls visibility
    int n = CityGMLBuilding.checkWallsVisibility(buildings);
    ILogger.instance().logInfo("Removed %d invisible walls from the model.", n);

    //Creating mesh model
    Mesh mesh = CityGMLBuilding.createSingleIndexedMeshWithColorPerVertexForBuildings(buildings, _planet, false, false);

    _meshRenderer.addMesh(mesh);

    if (xml != null)
       xml.dispose();

    for (int i = 0; i < buildings.size(); i++)
    {
      if (buildings.get(i) != null)
         buildings.get(i).dispose();
    }
  }

  public final void onError(URL url)
  {
    ILogger.instance().logError("Error downloading \"%s\"", url.getPath());
  }

  public final void onCancel(URL url)
  {
    // do nothing
  }

  public final void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired)
  {
    // do nothing
  }

}