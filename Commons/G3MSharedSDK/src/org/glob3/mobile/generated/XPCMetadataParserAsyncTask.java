package org.glob3.mobile.generated;
public class XPCMetadataParserAsyncTask extends GAsyncTask
{
  private XPCPointCloud _pointCloud;
  private IByteBuffer _buffer;

  private XPCMetadata _metadata;

  public XPCMetadataParserAsyncTask(XPCPointCloud pointCloud, IByteBuffer buffer)
  {
     _pointCloud = pointCloud;
     _buffer = buffer;
     _metadata = null;
    _pointCloud._retain();
  }

  public void dispose()
  {
    _pointCloud._release();

    if (_buffer != null)
       _buffer.dispose();

    if (_metadata != null)
       _metadata.dispose();
  }

  public final void runInBackground(G3MContext context)
  {
    final JSONBaseObject jsonBaseObject = IJSONParser.instance().parse(_buffer);

    if (_buffer != null)
       _buffer.dispose();
    _buffer = null;

    if (jsonBaseObject != null)
    {
      final JSONObject jsonObject = jsonBaseObject.asObject();
      if (jsonObject != null)
      {
        _metadata = XPCMetadata.fromJSON(jsonObject);
      }
    }
  }

  public final void onPostExecute(G3MContext context)
  {
    if (_metadata != null)
    {
      _pointCloud.parsedMetadata(_metadata);
      _metadata = null; // moves ownership to pointCloud
    }
    else
    {
      _pointCloud.errorParsingMetadata();
    }
  }

}