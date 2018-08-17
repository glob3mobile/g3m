package org.glob3.mobile.generated;//
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





//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class CompositeElevationData;

public class CompositeElevationDataProvider extends ElevationDataProvider
{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final G3MContext _context;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public G3MContext _context = new internal();
//#endif

  private java.util.ArrayList<ElevationDataProvider> _providers = new java.util.ArrayList<ElevationDataProvider>();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<ElevationDataProvider*> getProviders(const Sector& s) const
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
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (sectorsI[j]->touchesWith(s))
		if (sectorsI.get(j).touchesWith(new Sector(s))) //This provider contains the sector
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
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _request->onData(sector, extent, elevationData);
		_request.onData(new Sector(sector), new Vector2I(extent), elevationData);
	  }
	}

	public final void onError(Sector sector, Vector2I extent)
	{
	  if (_request != null)
	  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _request->onError(sector, extent);
		_request.onError(new Sector(sector), new Vector2I(extent));
	  }
	}

	public final void onCancel(Sector sector, Vector2I extent)
	{
	  if (_request != null)
	  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _request->onCancel(sector, extent);
		_request.onCancel(new Sector(sector), new Vector2I(extent));
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	private final Vector2I _resolution = new Vector2I();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	public final Vector2I _resolution = new internal();
//#endif
	private final Sector _sector = new Sector();


	public java.util.ArrayList<ElevationDataProvider> _providers = new java.util.ArrayList<ElevationDataProvider>();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: ElevationDataProvider* popBestProvider(java.util.ArrayList<ElevationDataProvider*>& ps, const Vector2I& extent) const
	public final ElevationDataProvider popBestProvider(tangible.RefObject<java.util.ArrayList<ElevationDataProvider>> ps, Vector2I extent)
	{
    
	  double bestRes = extent.squaredLength();
	  double selectedRes = IMathUtils.instance().maxDouble();
	  double selectedResDistance = IMathUtils.instance().maxDouble();
	  final IMathUtils mu = IMathUtils.instance();
    
    
	  ElevationDataProvider provider = null;
    
	  final int psSize = ps.argvalue.size();
	  int selectedIndex = -1;
	  for (int i = 0; i < psSize; i++)
	  {
		ElevationDataProvider each = ps.argvalue.get(i);
    
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'erase' method in Java:
		ps.argvalue.erase(ps.argvalue.iterator() + selectedIndex);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
		ps.argvalue.remove(selectedIndex);
//#endif
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
		_compData = null;
		_currentStep = null;
	}

	public void dispose()
	{

	}

	public final boolean launchNewStep()
	{
	  tangible.RefObject<java.util.ArrayList<ElevationDataProvider>> tempRef__providers = new tangible.RefObject<java.util.ArrayList<ElevationDataProvider>>(_providers);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _currentProvider = popBestProvider(_providers, _resolution);
	  _currentProvider = popBestProvider(tempRef__providers, new Vector2I(_resolution));
	  _providers = tempRef__providers.argvalue;
	  if (_currentProvider != null)
	  {
		_currentStep = new CompositeElevationDataProvider_RequestStepListener(this);
    
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _currentID = _currentProvider->requestElevationData(_sector, _resolution, _currentStep, true);
		_currentID = _currentProvider.requestElevationData(new Sector(_sector), new Vector2I(_resolution), _currentStep, true);
    
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
    
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _listener->onCancel(_sector, _resolution);
	  _listener.onCancel(new Sector(_sector), new Vector2I(_resolution));
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void respondToListener() const
	public final void respondToListener()
	{
    
	  if (_compData == null)
	  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _listener->onError(_sector, _resolution);
		_listener.onError(new Sector(_sector), new Vector2I(_resolution));
		if (_autodelete)
		{
		  if (_listener != null)
			  _listener.dispose();
		}
	  }
	  else
	  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _listener->onData(_sector, _resolution, _compData);
		_listener.onData(new Sector(_sector), new Vector2I(_resolution), _compData);
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
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	java.util.Iterator<Long, CompositeElevationDataProvider_Request> it;
	for (it = _requests.iterator(); it.hasNext();)
	{
	  final CompositeElevationDataProvider_Request reqI = it.next().getValue();
	  if (reqI == req)
	  {
		_requests.remove(it);
  
		if (req != null)
			req.dispose();
  
		return;
	  }
	}
  
	if (it == _requests.end())
	{
	  ILogger.instance().logError("Deleting nonexisting request in CompositeElevationDataProvider.");
	}
//#endif
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	for (final java.util.Map.Entry<Long, CompositeElevationDataProvider_Request> entry : _requests.entrySet())
	{
	  final CompositeElevationDataProvider_Request reqI = entry.getValue();
	  if (reqI == req)
	  {
		_requests.remove(entry.getKey());
		reqI.dispose();
		return;
	  }
	}
  
	ILogger.instance().logError("Deleting nonexisting request in CompositeElevationDataProvider.");
//#endif
  
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

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  super.dispose();
//#endif

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
  public final void changeFirstEDP(ElevationDataProvider edp)
  {
	  ElevationDataProvider oldEdp = _providers.get(0);
	  _providers.set(0, edp);
	  if (_context != null)
	  {
		  edp.initialize(_context);
	  }
  
	  edp.setChangedListener(_changedListener);
	  onChanged();
  
	  if (oldEdp != null)
		  oldEdp.dispose();
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	java.util.Iterator<Long, CompositeElevationDataProvider_Request> it = _requests.find(requestId);
	if (it.hasNext())
	{
	  CompositeElevationDataProvider_Request req = it.next().getValue();
	  req.cancel();
	  _requests.remove(requestId);
	  if (req != null)
		  req.dispose();
	}
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	final CompositeElevationDataProvider_Request req = _requests.remove(requestId);
	if (req != null)
	{
	  req.cancel();
	  req.dispose();
	}
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<const Sector*> getSectors() const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Vector2I getMinResolution() const
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
