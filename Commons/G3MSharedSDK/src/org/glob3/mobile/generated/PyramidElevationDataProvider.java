package org.glob3.mobile.generated; 
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
    private boolean _isMercator;
    private final String _layer;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following type could not be found.
//    class MetadataListener;

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

        public final String description()
        {
            //TODO: implement this better.
            return "";
            //return _sector.description() + ", pyramidLevel: "+_pyramidLevel;
        }

        public final Sector getSector()
        {
            return Sector.fromDegrees(_lowerLat, _lowerLon, _upperLat, _upperLon);
        }
    }

    private java.util.ArrayList<PyramidComposition> _pyrComposition;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//    boolean aboveLevel(Sector sector, int level);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//    PyramidElevationDataProvider(String layer, Sector sector, boolean isMercator, double deltaHeight);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//    public void dispose()

    public final boolean isReadyToRender (G3MRenderContext rc)
    {
       return true;
    }
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//    void getMetadata();

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//    void initialize(G3MContext context);
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//    long requestElevationData(Sector sector, Vector2I extent, IElevationDataListener listener, boolean autodeleteListener);
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//    long requestElevationData(Sector sector, int level, int row, int column, Vector2I extent, IElevationDataListener listener, boolean autodeleteListener);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//    String requestStringPath(Sector sector, Vector2I extent);
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//    String requestStringPath(String layer, int level, int row, int column);
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//    String requestMetadataPath();

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//    void cancelRequest(long requestId);
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//    java.util.ArrayList<Sector> getSectors();
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//    Vector2I getMinResolution();

}