package org.glob3.mobile.generated;import java.util.*;

public class GEORenderer_GEOObjectParserAsyncTask extends GAsyncTask
{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final URL _url = new URL();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final URL _url = new public();
//#endif

  private IByteBuffer _buffer;
  private GEORenderer _geoRenderer;
  private GEOSymbolizer _symbolizer;

  private final boolean _isBSON;

  private GEOObject _geoObject;

  public GEORenderer_GEOObjectParserAsyncTask(URL url, IByteBuffer buffer, GEORenderer geoRenderer, GEOSymbolizer symbolizer, boolean isBSON)
  {
	  _url = new URL(url);
	  _buffer = buffer;
	  _geoRenderer = geoRenderer;
	  _symbolizer = symbolizer;
	  _isBSON = isBSON;
	  _geoObject = null;
  }

  public void dispose()
  {
	if (_buffer != null)
		_buffer.dispose();
//    delete _geoObject;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  public final void runInBackground(G3MContext context)
  {
//    ILogger::instance()->logInfo("Parsing GEOObject buffer from \"%s\" (%db)",
//                                 _url._path.c_str(),
//                                 _buffer->size());

	if (_isBSON)
	{
	  _geoObject = GEOJSONParser.parseBSON(_buffer);
	}
	else
	{
	  _geoObject = GEOJSONParser.parseJSON(_buffer);
	}

	if (_buffer != null)
		_buffer.dispose();
	_buffer = null;
  }

  public final void onPostExecute(G3MContext context)
  {
	if (_geoObject == null)
	{
	  ILogger.instance().logError("Error parsing GEOJSON from \"%s\"", _url._path.c_str());
	}
	else
	{
//      ILogger::instance()->logInfo("Adding GEOObject to _geoRenderer");
	  _geoRenderer.addGEOObject(_geoObject, _symbolizer);
	  _geoObject = null;
	}
  }
}
