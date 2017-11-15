package org.glob3.mobile.generated; 
public class BuildingDataBDL extends IBufferDownloadListener
{
  private java.util.ArrayList<CityGMLBuilding> _buildings = new java.util.ArrayList<CityGMLBuilding>();

  private static class DataParsingTask extends GAsyncTask
  {
    private java.util.ArrayList<CityGMLBuilding> _buildings = new java.util.ArrayList<CityGMLBuilding>();
    private final String _s;

    public DataParsingTask(String s, java.util.ArrayList<CityGMLBuilding> buildings)
    {
       _s = s;
       _buildings = buildings;
    }

    public void runInBackground(G3MContext context)
    {
      BuildingDataParser.includeDataInBuildingSet(_s, _buildings);
    }

    public void onPostExecute(G3MContext context)
    {
    }
  }

  private final G3MContext _context;

  public BuildingDataBDL(java.util.ArrayList<CityGMLBuilding> buildings, G3MContext context)
  {
     _buildings = buildings;
     _context = context;
  }

  public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
  {

    String s = buffer.getAsString();
    if (buffer != null)
       buffer.dispose();

    _context.getThreadUtils().invokeAsyncTask(new DataParsingTask(s, _buildings), true);
  }

  public final void onError(URL url)
  {
    ILogger.instance().logError("Error downloading \"%s\"", url._path);
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