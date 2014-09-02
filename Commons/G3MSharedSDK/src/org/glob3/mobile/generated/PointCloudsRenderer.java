

package org.glob3.mobile.generated;

//
//  PointCloudsRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/19/14.
//
//

//
//  PointCloudsRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/19/14.
//
//


//class IDownloader;
//class Sector;


public class PointCloudsRenderer
         extends
            DefaultRenderer {

   public abstract static class PointCloudMetadataListener {
      public void dispose() {
      }


      public abstract void onMetadata(long pointsCount,
                                      Sector sector,
                                      double minHeight,
                                      double maxHeight);
   }


   //C++ TO JAVA CONVERTER TODO TASK: The implementation of the following type could not be found.
   //  class PointCloud;

   private static class PointCloudMetadataParserAsyncTask
            extends
               GAsyncTask {
      private final PointCloud _pointCloud;
      private IByteBuffer      _buffer;
      private long             _pointsCount;
      private Sector           _sector;
      private double           _minHeight;
      private double           _maxHeight;


      public PointCloudMetadataParserAsyncTask(final PointCloud pointCloud,
                                               final IByteBuffer buffer) {
         _pointCloud = pointCloud;
         _buffer = buffer;
         _pointsCount = -1;
         _sector = null;
         _minHeight = 0;
         _maxHeight = 0;
      }


      @Override
      public void dispose() {
         if (_sector != null) {
            _sector.dispose();
         }
         if (_buffer != null) {
            _buffer.dispose();
         }
      }


      @Override
      public final void runInBackground(final G3MContext context) {
         final ByteBufferIterator it = new ByteBufferIterator(_buffer);

         _pointsCount = it.nextInt64();

         final double lowerLatitude = it.nextDouble();
         final double lowerLongitude = it.nextDouble();
         final double upperLatitude = it.nextDouble();
         final double upperLongitude = it.nextDouble();

         _sector = new Sector(Geodetic2D.fromRadians(lowerLatitude, lowerLongitude), Geodetic2D.fromRadians(upperLatitude,
                  upperLongitude));

         _minHeight = it.nextDouble();
         _maxHeight = it.nextDouble();

         final int nodesCount = it.nextInt32();
         final java.util.ArrayList<PointCloudNode> nodes = new java.util.ArrayList<PointCloudNode>();

         for (int i = 0; i < nodesCount; i++) {
            final int idLength = it.nextUInt8();
            final byte[] id = new byte[idLength];
            it.nextUInt8(idLength, id);

            final java.util.ArrayList<Integer> levelsCount = new java.util.ArrayList<Integer>();
            final int byteLevelsCount = it.nextUInt8();
            for (int j = 0; j < byteLevelsCount; j++) {
               levelsCount.add(it.nextUInt8());
            }
            final int shortLevelsCount = it.nextUInt8();
            for (int j = 0; j < shortLevelsCount; j++) {
               levelsCount.add(it.nextInt16());
            }
            final int intLevelsCount = it.nextUInt8();
            for (int j = 0; j < intLevelsCount; j++) {
               levelsCount.add(it.nextInt32());
            }

            nodes.add(new PointCloudNode(id, levelsCount));

            //    delete [] id;
            //    delete [] byteLevels;
            //    delete [] shortLevels;
            //    delete [] intLevels;
         }

         if (it.hasNext()) {
            throw new RuntimeException("Logic error");
         }

         if (_buffer != null) {
            _buffer.dispose();
         }
         _buffer = null;
      }


      @Override
      public final void onPostExecute(final G3MContext context) {
         _pointCloud.parsedMetadata(_pointsCount, _sector, _minHeight, _maxHeight);
         _sector = null;
      }

   }


   private static class PointCloudMetadataDownloadListener
            extends
               IBufferDownloadListener {
      private final PointCloud   _pointCloud;
      private final IThreadUtils _threadUtils;


      public PointCloudMetadataDownloadListener(final PointCloud pointCloud,
                                                final IThreadUtils threadUtils) {
         _pointCloud = pointCloud;
         _threadUtils = threadUtils;
      }


      @Override
      public final void onDownload(final URL url,
                                   final IByteBuffer buffer,
                                   final boolean expired) {
         ILogger.instance().logInfo("Downloaded metadata for \"%s\" (bytes=%ld)", _pointCloud.getCloudName(), buffer.size());

         _threadUtils.invokeAsyncTask(new PointCloudMetadataParserAsyncTask(_pointCloud, buffer), true);
      }


      @Override
      public final void onError(final URL url) {
         _pointCloud.errorDownloadingMetadata();
      }


      @Override
      public final void onCancel(final URL url) {
         // do nothing
      }


      @Override
      public final void onCanceledDownload(final URL url,
                                           final IByteBuffer buffer,
                                           final boolean expired) {
         // do nothing
      }

   }


   private static class PointCloudNode {
      private final byte[]                       _id;
      private final java.util.ArrayList<Integer> _levelsCount;


      public PointCloudNode(final byte[] id,
                            final java.util.ArrayList<Integer> levelsCount) {
         _id = id;
         _levelsCount = levelsCount;
      }


      public void dispose() {
      }
   }


   private static class PointCloud {
      private final URL                  _serverURL;
      private final String               _cloudName;

      private final long                 _downloadPriority;
      private final TimeInterval         _timeToCache;
      private final boolean              _readExpired;

      private PointCloudMetadataListener _metadataListener;
      private final boolean              _deleteListener;

      private boolean                    _downloadingMetadata;
      private boolean                    _errorDownloadingMetadata;
      private boolean                    _errorParsingMetadata;

      private long                       _pointsCount;
      private Sector                     _sector;
      private double                     _minHeight;
      private double                     _maxHeight;


      public PointCloud(final URL serverURL,
                        final String cloudName,
                        final long downloadPriority,
                        final TimeInterval timeToCache,
                        final boolean readExpired,
                        final PointCloudMetadataListener metadataListener,
                        final boolean deleteListener) {
         _serverURL = serverURL;
         _cloudName = cloudName;
         _downloadPriority = downloadPriority;
         _timeToCache = timeToCache;
         _readExpired = readExpired;
         _metadataListener = metadataListener;
         _deleteListener = deleteListener;
         _downloadingMetadata = false;
         _errorDownloadingMetadata = false;
         _errorParsingMetadata = false;
         _pointsCount = -1;
         _sector = null;
         _minHeight = 0;
         _maxHeight = 0;
      }


      public void dispose() {

         if (_sector != null) {
            _sector.dispose();
         }
      }


      public final String getCloudName() {
         return _cloudName;
      }


      public final void initialize(final G3MContext context) {
         _downloadingMetadata = true;
         _errorDownloadingMetadata = false;
         _errorParsingMetadata = false;

         final String planetType = context.getPlanet().getType();

         final URL metadataURL = new URL(_serverURL, _cloudName + "?planet=" + planetType + "&format=binary");

         ILogger.instance().logInfo("Downloading metadata for \"%s\"", _cloudName);

         context.getDownloader().requestBuffer(metadataURL, _downloadPriority, _timeToCache, _readExpired,
                  new PointCloudsRenderer.PointCloudMetadataDownloadListener(this, context.getThreadUtils()), true);
      }


      //void PointCloudsRenderer::PointCloud::downloadedMetadata(IByteBuffer* buffer) {
      //  ILogger::instance()->logInfo("Downloaded metadata for \"%s\" (bytes=%ld)", _cloudName.c_str(), buffer->size());
      //
      //  _threadUtils->invokeAsyncTask(new PointCloudMetadataParserAsyncTask(this, buffer),
      //                                true);
      //
      //  //  _downloadingMetadata = false;
      //  //
      //  //
      //  //#warning DGD at work!
      //  ////  _errorParsingMetadata = true;
      //  //
      //  //  delete buffer;
      //}

      public final RenderState getRenderState(final G3MRenderContext rc) {
         if (_downloadingMetadata) {
            return RenderState.busy();
         }

         if (_errorDownloadingMetadata) {
            return RenderState.error("Error downloading metadata of \"" + _cloudName + "\" from \"" + _serverURL.getPath() + "\"");
         }

         if (_errorParsingMetadata) {
            return RenderState.error("Error parsing metadata of \"" + _cloudName + "\" from \"" + _serverURL.getPath() + "\"");
         }

         return RenderState.ready();
      }


      public final void errorDownloadingMetadata() {
         _downloadingMetadata = false;
         _errorDownloadingMetadata = true;
      }


      //    void downloadedMetadata(IByteBuffer* buffer);

      public final void parsedMetadata(final long pointsCount,
                                       final Sector sector,
                                       final double minHeight,
                                       final double maxHeight) {
         _pointsCount = pointsCount;
         _sector = sector;
         _minHeight = minHeight;
         _maxHeight = maxHeight;

         _downloadingMetadata = false;

         ILogger.instance().logInfo("Parsed metadata for \"%s\"", _cloudName);

         if (_metadataListener != null) {
            _metadataListener.onMetadata(pointsCount, sector, minHeight, maxHeight);
            if (_deleteListener) {
               if (_metadataListener != null) {
                  _metadataListener.dispose();
               }
            }
            _metadataListener = null;
         }

      }


      public final void render(final G3MRenderContext rc,
                               final GLState glState) {


      }

   }


   private final java.util.ArrayList<PointCloud> _clouds = new java.util.ArrayList<PointCloud>();
   private int                                   _cloudsSize;
   private final java.util.ArrayList<String>     _errors = new java.util.ArrayList<String>();


   @Override
   protected final void onChangedContext() {
      for (int i = 0; i < _cloudsSize; i++) {
         final PointCloud cloud = _clouds.get(i);
         cloud.initialize(_context);
      }
   }


   public PointCloudsRenderer() {
      _cloudsSize = 0;
   }


   @Override
   public void dispose() {
      for (int i = 0; i < _cloudsSize; i++) {
         final PointCloud cloud = _clouds.get(i);
         if (cloud != null) {
            cloud.dispose();
         }
      }
      super.dispose();
   }


   @Override
   public final RenderState getRenderState(final G3MRenderContext rc) {
      _errors.clear();
      boolean busyFlag = false;
      boolean errorFlag = false;

      for (int i = 0; i < _cloudsSize; i++) {
         final PointCloud cloud = _clouds.get(i);
         final RenderState childRenderState = cloud.getRenderState(rc);

         final RenderState_Type childRenderStateType = childRenderState._type;

         if (childRenderStateType == RenderState_Type.RENDER_ERROR) {
            errorFlag = true;

            final java.util.ArrayList<String> childErrors = childRenderState.getErrors();
            _errors.addAll(childErrors);
         }
         else if (childRenderStateType == RenderState_Type.RENDER_BUSY) {
            busyFlag = true;
         }
      }

      if (errorFlag) {
         return RenderState.error(_errors);
      }
      else if (busyFlag) {
         return RenderState.busy();
      }
      else {
         return RenderState.ready();
      }
   }


   @Override
   public final void render(final G3MRenderContext rc,
                            final GLState glState) {
      for (int i = 0; i < _cloudsSize; i++) {
         final PointCloud cloud = _clouds.get(i);
         cloud.render(rc, glState);
      }
   }


   @Override
   public final void onResizeViewportEvent(final G3MEventContext ec,
                                           final int width,
                                           final int height) {

   }


   public final void addPointCloud(final URL serverURL,
                                   final String cloudName,
                                   final PointCloudMetadataListener metadataListener,
                                   final boolean deleteListener) {
      addPointCloud(serverURL, cloudName, DownloadPriority.MEDIUM, TimeInterval.fromDays(30), true, metadataListener,
               deleteListener);
   }


   public final void addPointCloud(final URL serverURL,
                                   final String cloudName,
                                   final long downloadPriority,
                                   final TimeInterval timeToCache,
                                   final boolean readExpired,
                                   final PointCloudMetadataListener metadataListener,
                                   final boolean deleteListener) {
      final PointCloud pointCloud = new PointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired,
               metadataListener, deleteListener);
      if (_context != null) {
         pointCloud.initialize(_context);
      }
      _clouds.add(pointCloud);
      _cloudsSize = _clouds.size();
   }


   public final void removeAllPointClouds() {
      for (int i = 0; i < _cloudsSize; i++) {
         final PointCloud cloud = _clouds.get(i);
         if (cloud != null) {
            cloud.dispose();
         }
      }
      _clouds.clear();
      _cloudsSize = _clouds.size();
   }

}
