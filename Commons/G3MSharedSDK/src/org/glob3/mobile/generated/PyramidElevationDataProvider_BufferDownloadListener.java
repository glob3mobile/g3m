package org.glob3.mobile.generated; 
public class PyramidElevationDataProvider_BufferDownloadListener extends IBufferDownloadListener
{

    private final Sector _sector;
    private int _width;
    private int _height;
    private IElevationDataListener _listener;
    private boolean _autodeleteListener;
    private double _deltaHeight;

    private Vector2I getResolution(JSONObject data){
        return new Vector2I((int) data.getAsNumber("width",0),(int) data.getAsNumber("height",0));
    }

    private ShortBufferElevationData getElevationData(Sector sector, Vector2I extent, JSONObject data, double deltaHeight)
    {
        final short minValue = IMathUtils.instance().minInt16();
        final int size = extent._x * extent._y;
        final JSONArray dataArray = data.getAsArray("data");
        short[] shortBuffer = new short[size];
        for (int i = 0; i < size; i++)
        {
            short height = (short) dataArray.getAsNumber(i, minValue);

            if (height == 15000) //Our own NODATA, since -9999 is a valid height.
            {
                height = ShortBufferElevationData.NO_DATA_VALUE;
            }
            else if (height == minValue)
            {
                height = ShortBufferElevationData.NO_DATA_VALUE;
            }

            shortBuffer[i] = height;
        }

        short max = (short) data.getAsNumber("max", IMathUtils.instance().minInt16());
        short min = (short) data.getAsNumber("min", IMathUtils.instance().maxInt16());
        short children = (short) data.getAsNumber("withChildren", 0);
        short similarity = (short) data.getAsNumber("similarity", 0);

        return new ShortBufferElevationData(sector, extent, sector, extent, shortBuffer, size, deltaHeight,max,min,children,similarity);
    }

    public PyramidElevationDataProvider_BufferDownloadListener(Sector sector, Vector2I extent, IElevationDataListener listener, boolean autodeleteListener, double deltaHeight)
    {
       _sector = new Sector(sector);
       _width = extent._x;
       _height = extent._y;
       _listener = listener;
       _autodeleteListener = autodeleteListener;
       _deltaHeight = deltaHeight;

    }

    public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
    {

        ShortBufferElevationData elevationData;

        String contents = buffer.getAsString();
        final JSONObject jsonContent = IJSONParser.instance().parse(contents).asObject();
        final Vector2I resolution = getResolution(jsonContent);
        elevationData = getElevationData(_sector, resolution, jsonContent, _deltaHeight);

        if (buffer != null)
           if (buffer != null)
              buffer.dispose();

        if (elevationData == null)
        {
            _listener.onError(_sector, resolution);
        }
        else
        {
            _listener.onData(_sector, resolution, elevationData);
            //elevationData->_release();
        }


        if (_autodeleteListener)
        {
            if (_listener != null)
               if (_listener != null)
                  _listener.dispose();
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
               if (_listener != null)
                  _listener.dispose();
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
                   if (_listener != null)
                      _listener.dispose();
                _listener = null;
            }
        }
    }


    public final void onCanceledDownload(URL url, IByteBuffer data, boolean expired)
    {
        if (_autodeleteListener)
        {
            if (_listener != null)
               if (_listener != null)
                  _listener.dispose();
            _listener = null;
        }
    }


}