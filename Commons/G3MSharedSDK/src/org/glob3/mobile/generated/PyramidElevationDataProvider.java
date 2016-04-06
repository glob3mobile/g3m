package org.glob3.mobile.generated; 
//
//  PyramidElevationDataProvider.cpp
//  G3MiOSSDK
//
//  Created by Sebastian Ortega Trujillo on 4/3/16.
//
//

//
//  PyramidElevationDataProvider.hpp
//  G3MiOSSDK
//
//  Created by Sebastian Ortega Trujillo on 4/3/16.
//
//



public class PyramidElevationDataProvider extends ElevationDataProvider
{
    private IDownloader _downloader;
    private final IThreadUtils _threadUtils;
    private final Sector _sector ;
    private double _deltaHeight;
    private final String _layer;
    private MutableVector2I _minRes = new MutableVector2I();

    private static class PyramidComposition
    {
        public double _upperLat;
        public double _upperLon;
        public double _lowerLat;
        public double _lowerLon;
        public int _pyramidLevel;

        public PyramidComposition(double lowerLat, double lowerLon, double upperLat, double upperLon, double pyramidLevel)
        {
            _lowerLat = lowerLat;
            _lowerLon = lowerLon;
            _upperLat = upperLat;
            _upperLon = upperLon;
            _pyramidLevel = (int) pyramidLevel;
        }

        public final Sector getSector()
        {
            return Sector.fromDegrees(_lowerLat, _lowerLon, _upperLat, _upperLon);
        }
    }

    private static class MetadataListener extends IBufferDownloadListener
    {
        public MetadataListener(java.util.ArrayList<PyramidComposition> itself)
        {
           _itself = itself;
        }

        public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
        {

            java.util.ArrayList<Double> array = JSONDemParser.parseDemMetadata(buffer);


            if (array.size() == 0)
            {
                throw new RuntimeException("Problem parsing at PyramidElevationDataProvider::MetadataListener::onDownload().");
            }

            for (int i = 1; i<array.get(0); i+=5)
            {
                _itself.add(new PyramidComposition(array.get(i),array.get(i+1),array.get(i+2),array.get(i+3),array.get(i+4)));
            }
        }
        public final void onError(URL url)
        {
        }
        public final void onCancel(URL url)
        {
        }

        public final void onCanceledDownload(URL url, IByteBuffer data, boolean expired)
        {
        }

        private java.util.ArrayList<PyramidComposition> _itself;
    }

    private java.util.ArrayList<PyramidComposition> _pyrComposition;
    private int _noDataValue;

    private boolean aboveLevel(Sector sector, int level)
    {
      int maxLevel = 0;
      for (int i = 0; i< _pyrComposition.size(); i++)
      {
        if (sector.touchesWith(_pyrComposition.get(i).getSector()))
        {
          maxLevel = IMathUtils.instance().max(maxLevel, _pyrComposition.get(i)._pyramidLevel);
        }
      }
    
      return ((level > maxLevel) || (!sector.touchesWith(_sector)));
    }

    public PyramidElevationDataProvider(String layer, Sector sector, int noDataValue)
    {
       this(layer, sector, noDataValue, 0);
    }
    public PyramidElevationDataProvider(String layer, Sector sector)
    {
       this(layer, sector, 15000, 0);
    }
    public PyramidElevationDataProvider(String layer, Sector sector, int noDataValue, double deltaHeight)
    {
       _sector = new Sector(sector);
       _layer = layer;
       _noDataValue = noDataValue;
      _pyrComposition = new java.util.ArrayList<PyramidComposition>();
      _deltaHeight = deltaHeight;
      _minRes = new MutableVector2I(256, 256);
    }

    public void dispose()
    {
      _pyrComposition.clear();
      _pyrComposition = null;
      _pyrComposition = null;
    
        super.dispose();
    
    }

    public final boolean isReadyToRender (G3MRenderContext rc)
    {
       return true;
    }
    public final void getMetadata()
    {
    
      _downloader.requestBuffer(new URL(requestMetadataPath(),false), DownloadPriority.HIGHER, TimeInterval.fromDays(30), true, new MetadataListener(_pyrComposition), true);
    }

    public final void initialize(G3MContext context)
    {
      _downloader = context.getDownloader();
      _threadUtils = context.getThreadUtils();
      getMetadata();
    }

    public final long requestElevationData(Sector sector, Vector2I extent, int level, int row, int column, IElevationDataListener listener, boolean autodeleteListener)
    {
        Sector sectorCopy = new Sector(sector);
    
        if ((_downloader == null) || (aboveLevel(sectorCopy, level)))
        {
            if (sectorCopy != null)
               sectorCopy.dispose();
            return -1;
        }
    
    
        String path = requestStringPath(_layer, level, row, column);
    
        return _downloader.requestBuffer(new URL(path,false), DownloadPriority.HIGHEST - level, TimeInterval.fromDays(30), true, new PyramidElevationDataProvider_BufferDownloadListener(sectorCopy, extent, listener, autodeleteListener,_noDataValue, _deltaHeight, _minRes, _threadUtils), true);
    
    }

    public final String requestStringPath(String layer, int level, int row, int column)
    {
    
        IStringBuilder istr = IStringBuilder.newStringBuilder();
        istr.addString(_layer);
        istr.addInt(level);
        istr.addString("/");
        istr.addInt(column);
        istr.addString("/");
        istr.addInt(row);
        istr.addString(".json");
        String res = istr.getString();
        if (istr != null)
           istr.dispose();
        return res;
    }
    public final String requestMetadataPath()
    {
      return _layer + "meta.json";
    }

    public final void cancelRequest(long requestId)
    {
      _downloader.cancelRequest(requestId);
    }
    public final java.util.ArrayList<Sector> getSectors()
    {
      final java.util.ArrayList<Sector> sectors = new java.util.ArrayList<Sector>();
      sectors.add(_sector);
      return sectors;
    }

    public final Vector2I getMinResolution()
    {
        return _minRes.asVector2I();
    }

}