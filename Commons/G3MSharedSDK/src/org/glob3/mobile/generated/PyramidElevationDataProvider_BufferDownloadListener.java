package org.glob3.mobile.generated; 
public class PyramidElevationDataProvider_BufferDownloadListener extends IBufferDownloadListener
{

    private final Sector _sector;
    private int _width;
    private int _height;
    private IElevationDataListener _listener;
    private boolean _autodeleteListener;
    private double _deltaHeight;
    private int _noDataValue;

    public PyramidElevationDataProvider_BufferDownloadListener(Sector sector, Vector2I extent, IElevationDataListener listener, boolean autodeleteListener, int noDataValue, double deltaHeight)
    {
       _sector = sector;
       _width = extent._x;
       _height = extent._y;
       _listener = listener;
       _autodeleteListener = autodeleteListener;
       _deltaHeight = deltaHeight;
       _noDataValue = noDataValue;

    }

    public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
    {


        final Vector2I resolution = JSONDemParser.getResolution(buffer);
        ShortBufferElevationData elevationData = JSONDemParser.parseJSONDemElevationData(_sector, resolution, buffer, (short) _noDataValue, _deltaHeight);

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

    public void dispose()
    {
        if (_sector != null)
           _sector.dispose();

        super.dispose();
    }

}