package org.glob3.mobile.generated; 
//
//  CompositeElevationDataProvider.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/04/13.
//
//

//
//  CompositeElevationDataProvider.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/04/13.
//
//





//class CompositeElevationData;

public class CompositeElevationDataProvider extends ElevationDataProvider
{
  private G3MContext _context;

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

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following type could not be found.
//  class CompositeElevationDataProvider_Request;

  private static class CompositeElevationDataProvider_RequestStepListener implements IElevationDataListener
  {


    public CompositeElevationDataProvider_Request _request;

    public CompositeElevationDataProvider_RequestStepListener(CompositeElevationDataProvider_Request request)
    {
       _request = request;
    }

    public final void onData(Sector sector, Vector2I extent, ElevationData elevationData)
    {
      if (_request != null)
      {
        _request.onData(sector, extent, elevationData);
      }
    }

    public final void onError(Sector sector, Vector2I extent)
    {
      if (_request != null)
      {
        _request.onError(sector, extent);
      }
    }

    public final void onCancel(Sector sector, Vector2I extent)
    {
      if (_request != null)
      {
        _request.onCancel(sector, extent);
      }
    }

    public void dispose()
    {
    }

  }



  private static class CompositeElevationDataProvider_Request
  {

    private final CompositeElevationDataProvider _compProvider;

    private ElevationDataProvider _currentProvider;
    private CompositeElevationDataProvider_RequestStepListener _currentStep;
    private long _currentID;

    private ElevationData _compData;
    private IElevationDataListener _listener;
    private final boolean _autodelete;
    private final Vector2I _resolution;
    private final Sector _sector ;


    public java.util.ArrayList<ElevationDataProvider> _providers = new java.util.ArrayList<ElevationDataProvider>();

    public final ElevationDataProvider popBestProvider(java.util.ArrayList<ElevationDataProvider> ps, Vector2I extent)
    {
    
      double bestRes = extent.squaredLength();
      double selectedRes = IMathUtils.instance().maxDouble();
      double selectedResDistance = IMathUtils.instance().maxDouble();
      final IMathUtils mu = IMathUtils.instance();
    
    
      ElevationDataProvider provider = null;
    
      final int psSize = ps.size();
      int selectedIndex = -1;
      for (int i = 0; i < psSize; i++)
      {
        ElevationDataProvider each = ps.get(i);
    
        final double res = each.getMinResolution().squaredLength();
        final double newResDistance = mu.abs(bestRes - res);
    
        if (newResDistance < selectedResDistance || (newResDistance == selectedResDistance && res < selectedRes)) //or equal and higher resolution - Closer Resolution
        {
          selectedResDistance = newResDistance;
          selectedRes = res;
          selectedIndex = i;
          provider = each;
        }
      }
    
      if (provider != null)
      {
        ps.remove(selectedIndex);
      }
    
      return provider;
    }

    public CompositeElevationDataProvider_Request(CompositeElevationDataProvider provider, Sector sector, Vector2I resolution, IElevationDataListener listener, boolean autodelete)
    {
       _providers = provider.getProviders(sector);
       _sector = new Sector(sector);
       _resolution = resolution;
       _listener = listener;
       _autodelete = autodelete;
       _compProvider = provider;
       _compData = null;
       _currentStep = null;
    }

    public void dispose()
    {

    }

    public final boolean launchNewStep()
    {
      _currentProvider = popBestProvider(_providers, _resolution);
      if (_currentProvider != null)
      {
        _currentStep = new CompositeElevationDataProvider_RequestStepListener(this);
    
        _currentID = _currentProvider.requestElevationData(_sector, _resolution, _currentStep, true);
    
        return true;
      }
    
      _currentStep = null; //Waiting for no request
      return false;
    }

    public final void onData(Sector sector, Vector2I extent, ElevationData elevationData)
    {
      _currentStep = null;
      if (_compData == null)
      {
        _compData = new CompositeElevationData(elevationData);
      }
      else
      {
        ((CompositeElevationData)_compData).addElevationData(elevationData);
      }
    
      if (!_compData.hasNoData())
      {
        _compProvider.requestFinished(this); //If this data is enough we respond
      }
      else
      {
        if (!launchNewStep()) //If there are no more providers we respond
        {
          _compProvider.requestFinished(this);
        }
      }
    }

    public final void cancel()
    {
    
      if (_currentStep != null)
      {
        _currentStep._request = null;
        _currentProvider.cancelRequest(_currentID);
        _currentStep = null;
      }
    
      _listener.onCancel(_sector, _resolution);
      if (_autodelete)
      {
        if (_listener != null)
           _listener.dispose();
      }
    }

    public final void onError(Sector sector, Vector2I extent)
    {
      _currentStep = null;
      if (!launchNewStep())
      {
        //If there are no more providers we respond
        _compProvider.requestFinished(this);
      }
    }

    public final void onCancel(Sector sector, Vector2I extent)
    {
      _currentStep = null;
    }

    public final void respondToListener()
    {
    
      if (_compData == null)
      {
        _listener.onError(_sector, _resolution);
        if (_autodelete)
        {
          if (_listener != null)
             _listener.dispose();
        }
      }
      else
      {
        _listener.onData(_sector, _resolution, _compData);
        if (_autodelete)
        {
          if (_listener != null)
             _listener.dispose();
        }
      }
    }

  }


  private java.util.HashMap<Long, CompositeElevationDataProvider_Request> _requests = new java.util.HashMap<Long, CompositeElevationDataProvider_Request>();

  private void requestFinished(CompositeElevationDataProvider_Request req)
  {
  
    req.respondToListener();
  
  
    for (final java.util.Map.Entry<Long, CompositeElevationDataProvider_Request> entry : _requests.entrySet()) {
      final CompositeElevationDataProvider_Request reqI = entry.getValue();
      if (reqI == req) {
        _requests.remove(entry.getKey());
        reqI.dispose();
        return;
      }
    }
  
    ILogger.instance().logError("Deleting nonexisting request in CompositeElevationDataProvider.");
  
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

  super.dispose();

  }

  public final void addElevationDataProvider(ElevationDataProvider edp)
  {
    _providers.add(edp);
    if (_context != null)
    {
      edp.initialize(_context);
    }
  
    edp.setChangedListener(_changedListener); //Setting Changed Listener on child
  
    onChanged();
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

  public final long requestElevationData(Sector sector, Vector2I extent, IElevationDataListener listener, boolean autodeleteListener)
  {
  
    CompositeElevationDataProvider_Request req = new CompositeElevationDataProvider_Request(this, sector, extent, listener, autodeleteListener);
    _currentID++;
    _requests.put(_currentID, req);
  
    req.launchNewStep();
  
    return _currentID;
  }

  public final void cancelRequest(long requestId)
  {
    final CompositeElevationDataProvider_Request req = _requests.remove(requestId);
    if (req != null) {
      req.cancel();
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
    final int size = _providers.size();
    double minD = IMathUtils.instance().maxDouble();
    int x = -1;
    int y = -1;
  
    for (int i = 0; i < size; i++)
    {
      final Vector2I res = _providers.get(i).getMinResolution();
      final double d = res.squaredLength();
  
      if (minD > d)
      {
        minD = d;
        x = res._x;
        y = res._y;
      }
    }
    return new Vector2I(x,y);
  }

}
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#pragma mark Request

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#pragma mark RequestStep
