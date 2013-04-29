package org.glob3.mobile.generated; 
//
//  CompositeElevationDataProvider.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/04/13.
//
//

//
//  CompositeElevationDataProvider.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/04/13.
//
//





//class CompositeElevationData;

public class CompositeElevationDataProvider extends ElevationDataProvider
{
  private final G3MContext _context;
  private java.util.ArrayList<ElevationDataProvider> _providers = new java.util.ArrayList<ElevationDataProvider>();

  private java.util.ArrayList<ElevationDataProvider> getProviders(Sector s)
  {
    int size = _providers.size();
    java.util.ArrayList<ElevationDataProvider> providers = new java.util.ArrayList<ElevationDataProvider>();
  
    for (int i = 0; i < size; i++)
    {
  
      ElevationDataProvider edp = _providers.get(i);
  
      final java.util.ArrayList<Sector> sectorsI = edp.getSectors();
      int sizeI = sectorsI.size();
      for (int j = 0; j < sizeI; j++)
      {
        if (sectorsI.get(j).touchesWith(s)) //This provider contains the sector
        {
          providers.add(edp);
        }
      }
    }
    return providers;
  }
  private long _currentID;



  private static class CompositeElevationDataProvider_Request implements IElevationDataListener
  {

    private ElevationDataProvider _currentRequestEDP;
    private long _currentRequestID;
    private final CompositeElevationDataProvider _compProvider;

    private boolean _hasBeenCanceled;


    public CompositeElevationData _compData;
    public IElevationDataListener _listener;
    public final boolean _autodelete;
    public final Vector2I _resolution = new Vector2I();
    public final Sector _sector;

    public java.util.ArrayList<ElevationDataProvider> _providers = new java.util.ArrayList<ElevationDataProvider>();

    public final ElevationDataProvider popBestProvider(java.util.ArrayList<ElevationDataProvider> ps, Vector2I resolution)
    {
      java.util.Iterator<ElevationDataProvider> edp = ps.end();
      double bestRes = resolution.squaredLength();
      double selectedRes = 99999999999;
      double selectedResDistance = 99999999999999999;
      IMathUtils mu = IMathUtils.instance();
      for (java.util.Iterator<ElevationDataProvider> it = ps.iterator() ; it.hasNext();)
      {
        double res = (it.next()).getMinResolution().squaredLength();
        double newResDistance = mu.abs(bestRes - res);
    
        if (newResDistance < selectedResDistance || (newResDistance == selectedResDistance && res < selectedRes)) //or equal and higher resolution - Closer Resolution
        {
          selectedResDistance = newResDistance;
          selectedRes = res;
          edp = it;
        }
      }
    
      ElevationDataProvider provider = null;
      if (edp.hasNext())
      {
        provider = edp.next();
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'erase' method in Java:
        ps.erase(edp);
      }
      return provider;
    }

    public CompositeElevationDataProvider_Request(CompositeElevationDataProvider provider, Sector sector, Vector2I resolution, IElevationDataListener listener, boolean autodelete)
    {
       _providers = provider.getProviders(sector);
       _sector = new Sector(sector);
       _resolution = new Vector2I(resolution);
       _listener = listener;
       _autodelete = autodelete;
       _compProvider = provider;
       _currentRequestEDP = null;
       _compData = null;
       _hasBeenCanceled = false;
    }

    public final boolean launchNewRequest()
    {
      _currentRequestEDP = popBestProvider(_providers, _resolution);
      if (_currentRequestEDP != null)
      {
        _currentRequestID = _currentRequestEDP.requestElevationData(_sector, _resolution, this, false);
        return true;
      }
      else
      {
        _currentRequestID = -1; //Waiting for no request
        return false;
      }
    }

    public final void onData(Sector sector, Vector2I resolution, ElevationData elevationData)
    {
      _currentRequestID = -1; //Waiting for no request
      if (_hasBeenCanceled)
      {
        if (elevationData != null)
           elevationData.dispose();
        if (_compData != null)
           _compData.dispose();
        this = null;
      }
      else
      {
    
        if (_compData == null)
        {
          _compData = new CompositeElevationData(elevationData);
        }
        else
        {
          _compData.addElevationData(elevationData);
        }
    
        if (!_compData.hasNoData())
        {
          _compProvider.requestFinished(this); //If this data is enough we respond
        }
        else
        {
          if (!launchNewRequest()) //If there are no more providers we respond
          {
            _compProvider.requestFinished(this);
          }
        }
      }
    }

    public final void cancel()
    {
      if (_currentRequestEDP != null)
      {
        _currentRequestEDP.cancelRequest(_currentRequestID);
      }
      _hasBeenCanceled = true;
    
      if (_currentRequestID == -1)
      {
        this = null;
      }
    }

    public final void onError(Sector sector, Vector2I resolution)
    {
      _currentRequestID = -1; //Waiting for no request
      if (_hasBeenCanceled)
      {
        this = null;
      }
      else
      {
        if (!launchNewRequest())
        {
          //If there are no more providers we respond
          _compProvider.requestFinished(this);
        }
      }
    }

  }


  private java.util.HashMap<Long, CompositeElevationDataProvider_Request> _requests = new java.util.HashMap<Long, CompositeElevationDataProvider_Request>();

  private void requestFinished(CompositeElevationDataProvider_Request req)
  {
  
    CompositeElevationData data = req._compData;
    IElevationDataListener listener = req._listener;
    final boolean autodelete = req._autodelete;
    final Vector2I resolution = req._resolution;
    final Sector sector = req._sector;
  
    if (data == null)
    {
      listener.onError(sector, resolution);
      if (autodelete)
      {
        if (listener != null)
           listener.dispose();
        req._listener = null;
      }
    }
    else
    {
      listener.onData(sector, resolution, data);
      if (autodelete)
      {
        if (listener != null)
           listener.dispose();
        req._listener = null;
      }
    }
    java.util.Iterator<Long, CompositeElevationDataProvider_Request> it;
    for (it = _requests.iterator(); it.hasNext();)
    {
      final CompositeElevationDataProvider_Request reqI = it.next().getValue();
      if (reqI == req)
      {
        _requests.remove(it);
        break;
      }
    }
  
    if (it == _requests.end())
    {
      ILogger.instance().logError("Deleting nonexisting request in CompositeElevationDataProvider.");
    }
  }

  public CompositeElevationDataProvider()
  {
     _context = null;
     _currentID = 0;
  }

  public void dispose()
  {
    int size = _providers.size();
    for (int i = 0; i < size; i++)
    {
      if (_providers.get(i) != null)
         _providers.get(i).dispose();
    }
  }

  public final void addElevationDataProvider(ElevationDataProvider edp)
  {
    _providers.add(edp);
    if (_context != null)
    {
      edp.initialize(_context);
    }
  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    int size = _providers.size();
    for (int i = 0; i < size; i++)
    {
      if (!_providers.get(i).isReadyToRender(rc))
      {
        return false;
      }
    }
    return true;
  }

  public final void initialize(G3MContext context)
  {
    _context = context;
    int size = _providers.size();
    for (int i = 0; i < size; i++)
    {
      _providers.get(i).initialize(context);
    }
  }

  public final long requestElevationData(Sector sector, Vector2I resolution, IElevationDataListener listener, boolean autodeleteListener)
  {
  
    CompositeElevationDataProvider_Request req = new CompositeElevationDataProvider_Request(this, sector, resolution, listener, autodeleteListener);
    _currentID++;
    _requests.put(_currentID, req);
  
    req.launchNewRequest();
  
    return _currentID;
  }

  public final void cancelRequest(long requestId)
  {
    java.util.Iterator<Long, CompositeElevationDataProvider_Request> it = _requests.indexOf(requestId);
    if (it.hasNext())
    {
      CompositeElevationDataProvider_Request req = it.next().getValue();
      req.cancel();
      _requests.remove(requestId);
      if (req != null)
         req.dispose();
    }
  }

  public final java.util.ArrayList<Sector> getSectors()
  {
    final java.util.ArrayList<Sector> sectors = new java.util.ArrayList<Sector>();
    int size = _providers.size();
    for (int i = 0; i < size; i++)
    {
      final java.util.ArrayList<Sector> sectorsI = _providers.get(i).getSectors();
      int sizeI = sectorsI.size();
      for (int j = 0; j < sizeI; j++)
      {
        sectors.add(sectorsI.get(j));
      }
    }
    return sectors;
  }

  public final Vector2I getMinResolution()
  {
  
    int size = _providers.size();
    double minD = 9999999999;
    int x = -1;
    int y = -1;
  
    for (int i = 0; i < size; i++)
    {
  
      Vector2I res = _providers.get(i).getMinResolution();
      double d = res.squaredLength();
  
      if (minD > d)
      {
        minD = d;
        x = res._x;
        y = res._y;
      }
    }
    return new Vector2I(x,y);
  }

  public final ElevationData createSubviewOfElevationData(ElevationData elevationData, Sector sector, Vector2I resolution)
  {
    return new SubviewElevationData(elevationData, false, sector, resolution, false);
  }

}
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#pragma mark Request