

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

public class CompositeElevationDataProvider
         extends
            ElevationDataProvider {
   private final G3MContext                                 _context;
   private final java.util.ArrayList<ElevationDataProvider> _providers = new java.util.ArrayList<ElevationDataProvider>();


   private java.util.ArrayList<ElevationDataProvider> getProviders(final Sector s) {
      final int size = _providers.size();
      final java.util.ArrayList<ElevationDataProvider> providers = new java.util.ArrayList<ElevationDataProvider>();

      for (int i = 0; i < size; i++) {

         final ElevationDataProvider edp = _providers.get(i);

         final java.util.ArrayList<Sector> sectorsI = edp.getSectors();
         final int sizeI = sectorsI.size();
         for (int j = 0; j < sizeI; j++) {
            if (sectorsI.get(j).touchesWith(s)) //This provider contains the sector
            {
               providers.add(edp);
            }
         }
      }
      return providers;
   }

   private long _currentID;


   private static class CompositeElevationDataProvider_Request
            implements
               IElevationDataListener {

      private ElevationDataProvider                     _currentRequestEDP;
      private long                                      _currentRequestID;
      private final CompositeElevationDataProvider      _compProvider;

      private boolean                                   _hasBeenCanceled;


      public CompositeElevationData                     _compData;
      public IElevationDataListener                     _listener;
      public final boolean                              _autodelete;
      public final Vector2I                             _resolution = new Vector2I();
      public final Sector                               _sector;

      public java.util.ArrayList<ElevationDataProvider> _providers  = new java.util.ArrayList<ElevationDataProvider>();


      public final ElevationDataProvider popBestProvider(final java.util.ArrayList<ElevationDataProvider> ps,
                                                         final Vector2I resolution) {
         java.util.Iterator<ElevationDataProvider> edp = ps.end();
         final double bestRes = resolution.squaredLength();
         double selectedRes = 99999999999;
         double selectedResDistance = 99999999999999999;
         final IMathUtils mu = IMathUtils.instance();
         for (final java.util.Iterator<ElevationDataProvider> it = ps.iterator(); it.hasNext();) {
            final double res = (it.next()).getMinResolution().squaredLength();
            final double newResDistance = mu.abs(bestRes - res);

            if ((newResDistance < selectedResDistance) || ((newResDistance == selectedResDistance) && (res < selectedRes))) //or equal and higher resolution - Closer Resolution
            {
               selectedResDistance = newResDistance;
               selectedRes = res;
               edp = it;
            }
         }

         ElevationDataProvider provider = null;
         if (edp.hasNext()) {
            provider = edp.next();
            //C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'erase' method in Java:
            ps.erase(edp);
         }
         return provider;
      }


      public CompositeElevationDataProvider_Request(final CompositeElevationDataProvider provider,
                                                    final Sector sector,
                                                    final Vector2I resolution,
                                                    final IElevationDataListener listener,
                                                    final boolean autodelete) {
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


      public final boolean launchNewRequest() {
         _currentRequestEDP = popBestProvider(_providers, _resolution);
         if (_currentRequestEDP != null) {
            _currentRequestID = _currentRequestEDP.requestElevationData(_sector, _resolution, this, false);
            return true;
         }
         else {
            _currentRequestID = -1; //Waiting for no request
            return false;
         }
      }


      @Override
      public final void onData(final Sector sector,
                               final Vector2I resolution,
                               final ElevationData elevationData) {
         _currentRequestID = -1; //Waiting for no request
         if (_hasBeenCanceled) {
            if (elevationData != null) {
               elevationData.dispose();
            }
            if (_compData != null) {
               _compData.dispose();
            }
            this = null;
         }
         else {

            if (_compData == null) {
               _compData = new CompositeElevationData(elevationData);
            }
            else {
               _compData.addElevationData(elevationData);
            }

            if (!_compData.hasNoData()) {
               _compProvider.requestFinished(this); //If this data is enough we respond
            }
            else {
               if (!launchNewRequest()) //If there are no more providers we respond
               {
                  _compProvider.requestFinished(this);
               }
            }
         }
      }


      public final void cancel() {
         if (_currentRequestEDP != null) {
            _currentRequestEDP.cancelRequest(_currentRequestID);
         }
         _hasBeenCanceled = true;

         if (_currentRequestID == -1) {
            this = null;
         }
      }


      @Override
      public final void onError(final Sector sector,
                                final Vector2I resolution) {
         _currentRequestID = -1; //Waiting for no request
         if (_hasBeenCanceled) {
            this = null;
         }
         else {
            if (!launchNewRequest()) {
               //If there are no more providers we respond
               _compProvider.requestFinished(this);
            }
         }
      }


      @Override
      public void dispose() {
         // TODO Auto-generated method stub

      }

   }


   private final java.util.HashMap<Long, CompositeElevationDataProvider_Request> _requests = new java.util.HashMap<Long, CompositeElevationDataProvider_Request>();


   private void requestFinished(final CompositeElevationDataProvider_Request req) {

      final CompositeElevationData data = req._compData;
      final IElevationDataListener listener = req._listener;
      final boolean autodelete = req._autodelete;
      final Vector2I resolution = req._resolution;
      final Sector sector = req._sector;

      if (data == null) {
         listener.onError(sector, resolution);
         if (autodelete) {
            if (listener != null) {
               listener.dispose();
            }
            req._listener = null;
         }
      }
      else {
         listener.onData(sector, resolution, data);
         if (autodelete) {
            if (listener != null) {
               listener.dispose();
            }
            req._listener = null;
         }
      }
      java.util.Iterator<Long, CompositeElevationDataProvider_Request> it;
      for (it = _requests.iterator(); it.hasNext();) {
         final CompositeElevationDataProvider_Request reqI = it.next().getValue();
         if (reqI == req) {
            _requests.remove(it);
            break;
         }
      }

      if (it == _requests.end()) {
         ILogger.instance().logError("Deleting nonexisting request in CompositeElevationDataProvider.");
      }
   }


   public CompositeElevationDataProvider() {
      _context = null;
      _currentID = 0;
   }


   @Override
   public void dispose() {
      final int size = _providers.size();
      for (int i = 0; i < size; i++) {
         if (_providers.get(i) != null) {
            _providers.get(i).dispose();
         }
      }
   }


   public final void addElevationDataProvider(final ElevationDataProvider edp) {
      _providers.add(edp);
      if (_context != null) {
         edp.initialize(_context);
      }
   }


   @Override
   public final boolean isReadyToRender(final G3MRenderContext rc) {
      final int size = _providers.size();
      for (int i = 0; i < size; i++) {
         if (!_providers.get(i).isReadyToRender(rc)) {
            return false;
         }
      }
      return true;
   }


   @Override
   public final void initialize(final G3MContext context) {
      _context = context;
      final int size = _providers.size();
      for (int i = 0; i < size; i++) {
         _providers.get(i).initialize(context);
      }
   }


   @Override
   public final long requestElevationData(final Sector sector,
                                          final Vector2I resolution,
                                          final IElevationDataListener listener,
                                          final boolean autodeleteListener) {

      final CompositeElevationDataProvider_Request req = new CompositeElevationDataProvider_Request(this, sector, resolution,
               listener, autodeleteListener);
      _currentID++;
      _requests.put(_currentID, req);

      req.launchNewRequest();

      return _currentID;
   }


   @Override
   public final void cancelRequest(final long requestId) {
      java.util.Iterator<Long, CompositeElevationDataProvider_Request> it = _requests.indexOf(requestId);
      if (it.hasNext()) {
         final CompositeElevationDataProvider_Request req = it.next().getValue();
         req.cancel();
         _requests.remove(requestId);
         if (req != null) {
            req.dispose();
         }
      }
   }


   @Override
   public final java.util.ArrayList<Sector> getSectors() {
      final java.util.ArrayList<Sector> sectors = new java.util.ArrayList<Sector>();
      final int size = _providers.size();
      for (int i = 0; i < size; i++) {
         final java.util.ArrayList<Sector> sectorsI = _providers.get(i).getSectors();
         final int sizeI = sectorsI.size();
         for (int j = 0; j < sizeI; j++) {
            sectors.add(sectorsI.get(j));
         }
      }
      return sectors;
   }


   @Override
   public final Vector2I getMinResolution() {

      final int size = _providers.size();
      double minD = 9999999999;
      int x = -1;
      int y = -1;

      for (int i = 0; i < size; i++) {

         final Vector2I res = _providers.get(i).getMinResolution();
         final double d = res.squaredLength();

         if (minD > d) {
            minD = d;
            x = res._x;
            y = res._y;
         }
      }
      return new Vector2I(x, y);
   }


   @Override
   public final ElevationData createSubviewOfElevationData(final ElevationData elevationData,
                                                           final Sector sector,
                                                           final Vector2I resolution) {
      return new SubviewElevationData(elevationData, false, sector, resolution, false);
   }

}
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#pragma mark Request