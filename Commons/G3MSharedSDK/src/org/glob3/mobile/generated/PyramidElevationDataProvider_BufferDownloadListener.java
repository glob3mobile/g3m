package org.glob3.mobile.generated; 
public class PyramidElevationDataProvider_BufferDownloadListener extends IBufferDownloadListener
{

    private final Sector _sector;
    private int _width;
    private int _height;
    private MutableVector2I _minRes;
    private IElevationDataListener _listener;
    private boolean _autodeleteListener;
    private double _deltaHeight;
    private int _noDataValue;

    public PyramidElevationDataProvider_BufferDownloadListener(Sector sector, Vector2I extent, IElevationDataListener listener, boolean autodeleteListener, int noDataValue, double deltaHeight, MutableVector2I minRes)
    {
       _sector = sector;
       _width = extent._x;
       _height = extent._y;
       _listener = listener;
       _minRes = new MutableVector2I(minRes);
       _autodeleteListener = autodeleteListener;
       _deltaHeight = deltaHeight;
       _noDataValue = noDataValue;

    }

    public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
    {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Bil parser code commented to be applied when necessary.
        JSONDemParser parser = new JSONDemParser(buffer.getAsString());
        final Vector2I resolution = parser.getResolution();
        ShortBufferElevationData elevationData = parser.parseJSONDemElevationData(_sector, resolution, buffer, (short) _noDataValue, _deltaHeight);

       /* ShortBufferElevationData *elevationData = BilParser::parseBil16Redim(*_sector, buffer, (short) _noDataValue);
        const Vector2I *resolution = new Vector2I(elevationData->getExtent()); */

        if (buffer != null)
        {
            if (buffer != null)
               buffer.dispose();
        }

        if (elevationData == null)
        {
            _listener.onError(_sector, resolution);
        }
        else
        {
            _listener.onData(_sector, resolution, elevationData);
            if ((_minRes.x() * _minRes.y()) > (resolution._x * resolution._y))
            {
                _minRes = resolution.asMutableVector2I();
            }
        }


        if (_autodeleteListener)
        {
            if (_listener != null)
            {
                if (_listener != null)
                   _listener.dispose();
            }
            _listener = null;
        }
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning Discard parser deletion when bil needed.
        if (parser != null)
           parser.dispose();
        if (_sector != null)
           _sector.dispose();

    }

    public final void onError(URL url)
    {
        final Vector2I resolution = new Vector2I(_width, _height);

        _listener.onError(_sector, resolution);
        if (_autodeleteListener)
        {
            if (_listener != null)
            {
              if (_listener != null)
                 _listener.dispose();
            }
            _listener = null;
        }
    }

    public final void onCancel(URL url)
    {
        if (_listener != null)
        {
            final Vector2I resolution = new Vector2I(_width, _height);
            _listener.onCancel(_sector, resolution);
            if (_autodeleteListener)
            {
                if (_listener != null)
                {
                  if (_listener != null)
                     _listener.dispose();
                }
                _listener = null;
            }
        }
    }


    public final void onCanceledDownload(URL url, IByteBuffer data, boolean expired)
    {
        if (_autodeleteListener)
        {
            if (_listener != null)
            {
              if (_listener != null)
                 _listener.dispose();
            }
            _listener = null;
        }
    }

}