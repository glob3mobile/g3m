package org.glob3.mobile.generated; 
public class SubviewEDTask extends GAsyncTask
{

  public ElevationData _ed;
  public ElevationData _subview;
  public final Sector _sector ;
  public final Vector2I _extent;
  public IElevationDataListener _listener;
  public boolean _autodelete;

  public SubviewEDTask(ElevationData ed, Sector sector, Vector2I extent, IElevationDataListener listener, boolean autodelete)
  {
     _ed = ed;
     _sector = new Sector(sector);
     _extent = new Vector2I(extent);
     _subview = null;
     _listener = listener;
     _autodelete = autodelete;

  }

  public void runInBackground(G3MContext context)
  {

    _subview = new InterpolatedSubviewElevationData(_ed, _sector, _extent);

  }

  public void onPostExecute(G3MContext context)
  {

    _listener.onData(_sector, _extent, _subview);

    if (_autodelete)
    {
      if (_listener != null)
         _listener.dispose();
    }

  }

}