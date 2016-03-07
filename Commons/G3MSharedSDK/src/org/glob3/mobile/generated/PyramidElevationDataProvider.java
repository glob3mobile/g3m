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
    private final Sector _sector ;
    private double _deltaHeight;
    private final String _layer;

    private static class PyramidComposition
    {
        public double _upperLat;
        public double _upperLon;
        public double _lowerLat;
        public double _lowerLon;
        public int _pyramidLevel;

        public PyramidComposition(double lowerLat, double lowerLon, double upperLat, double upperLon, int pyramidLevel)
        {
            _lowerLat = lowerLat;
            _lowerLon = lowerLon;
            _upperLat = upperLat;
            _upperLon = upperLon;
            _pyramidLevel = pyramidLevel;
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

            final String str = buffer.getAsString();

            IJSONParser parser = IJSONParser.instance();
            final JSONArray array = parser.parse(str).asObject().getAsArray("sectors");
            if (array == null)
            {
                throw new RuntimeException("Problem parsing at PyramidElevationDataProvider::MetadataListener::onDownload().");
            }

            for (int i = 0; i<array.size(); i++)
            {
                _itself.add(new PyramidComposition(getLowerLat(array, i),getLowerLon(array, i),getUpperLat(array, i),getUpperLon(array, i),getLevel(array, i)));
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

        private double getUpperLat(JSONArray array, int index)
        {
            JSONFloat doble = (JSONFloat) array.getAsObject(index).getAsObject("sector").getAsObject("upper").getAsNumber("lat");
            return doble.value();
        }

        private double getLowerLat(JSONArray array, int index)
        {
            JSONFloat doble = (JSONFloat) array.getAsObject(index).getAsObject("sector").getAsObject("lower").getAsNumber("lat");
            return doble.value();
        }

        private double getUpperLon(JSONArray array, int index)
        {
            JSONFloat doble = (JSONFloat) array.getAsObject(index).getAsObject("sector").getAsObject("upper").getAsNumber("lon");
            return doble.value();
        }

        private double getLowerLon(JSONArray array, int index)
        {
            JSONFloat doble = (JSONFloat)array.getAsObject(index).getAsObject("sector").getAsObject("lower").getAsNumber("lon");
            return doble.value();
        }

        private int getLevel(JSONArray array, int index)
        {
            JSONInteger integer = (JSONInteger) array.getAsObject(index).getAsNumber("pyrLevel");
            return integer.intValue();
        }

    }

    private java.util.ArrayList<PyramidComposition> _pyrComposition;

    private boolean aboveLevel(Sector sector, int level)
    {
      int maxLevel = 0;
      for (int i = 0; i< _pyrComposition.size(); i++)
        if (sector.touchesWith(_pyrComposition.get(i).getSector()))
          maxLevel = IMathUtils.instance().max(maxLevel, _pyrComposition.get(i)._pyramidLevel);
    
      if (level > maxLevel)
         return true;
      else
      {
        if (!sector.touchesWith(_sector))
           return true;
        return false;
      }
    }

    public PyramidElevationDataProvider(String layer, Sector sector)
    {
       this(layer, sector, 0);
    }
    public PyramidElevationDataProvider(String layer, Sector sector, double deltaHeight)
    {
       _sector = new Sector(sector);
       _layer = layer;
      _pyrComposition = new java.util.ArrayList<PyramidComposition>();
      _deltaHeight = deltaHeight;
    }

    public void dispose()
    {
      _pyrComposition.clear();
      _pyrComposition = null;
      _pyrComposition = null;
    
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
      getMetadata();
    }
    public final long requestElevationData(Sector sector, Vector2I extent, IElevationDataListener listener, boolean autodeleteListener)
    {
      //This requester is not necessary, but we are forced to implement it, so -1.
      return -1;
    }
    public final long requestElevationData(Sector sector, int level, int row, int column, Vector2I extent, IElevationDataListener listener, boolean autodeleteListener)
    {
    
      if ((_downloader == null) || (aboveLevel(sector, level)))
      {
        return -1;
      }
    
      String path = requestStringPath(_layer, level, row, column);
    
      return _downloader.requestBuffer(new URL(path,false), DownloadPriority.HIGHEST - level, TimeInterval.fromDays(30), true, new PyramidElevationDataProvider_BufferDownloadListener(sector, extent, listener, autodeleteListener, _deltaHeight), true);
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
      //    int WORKING_JM;
      return Vector2I.zero();
    }

}