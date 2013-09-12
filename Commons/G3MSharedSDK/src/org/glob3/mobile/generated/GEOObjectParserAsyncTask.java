package org.glob3.mobile.generated; 
//class GEOObjectAdderTask : public GTask {
//private:
//  GEOObject*     _geoObject;
//  GEORenderer*   _geoRenderer;
//  GEOSymbolizer* _symbolizer;
//
//public:
//  GEOObjectAdderTask(GEOObject* geoObject,
//                     GEORenderer* geoRenderer,
//                     GEOSymbolizer* symbolizer) :
//  _geoObject(geoObject),
//  _geoRenderer(geoRenderer),
//  _symbolizer(symbolizer)
//  {
//
//  }
//
//  void run(const G3MContext* context) {
//    _geoRenderer->addGEOObject(_geoObject, _symbolizer);
//  }
//};
//
//class GEOObjectParserTask : public GTask {
//private:
//  const URL      _url;
//  IByteBuffer*   _buffer;
//  GEORenderer*   _geoRenderer;
//  GEOSymbolizer* _symbolizer;
//
//public:
//  GEOObjectParserTask(const URL& url,
//                      IByteBuffer* buffer,
//                      GEORenderer* geoRenderer,
//                      GEOSymbolizer* symbolizer) :
//  _url(url),
//  _buffer(buffer),
//  _geoRenderer(geoRenderer),
//  _symbolizer(symbolizer)
//  {
//  }
//
//  void run(const G3MContext* context) {
//    GEOObject* geoObject = GEOJSONParser::parse(_buffer);
//
//    if (geoObject == NULL) {
//      ILogger::instance()->logError("Error parsing GEOJSON from \"%s\"", _url.getPath().c_str());
//    }
//    else {
//      context->getThreadUtils()->invokeInRendererThread(new GEOObjectAdderTask(geoObject,
//                                                                               _geoRenderer,
//                                                                               _symbolizer),
//                                                        true);
//    }
//
//    delete _buffer;
//    _buffer = NULL;
//  }
//};


public class GEOObjectParserAsyncTask extends GAsyncTask
{
  public final URL _url;

  private IByteBuffer _buffer;
  private GEORenderer _geoRenderer;
  private GEOSymbolizer _symbolizer;

  private GEOObject _geoObject;

  public GEOObjectParserAsyncTask(URL url, IByteBuffer buffer, GEORenderer geoRenderer, GEOSymbolizer symbolizer)
  {
     _url = url;
     _buffer = buffer;
     _geoRenderer = geoRenderer;
     _symbolizer = symbolizer;
     _geoObject = null;
  }

  public void dispose()
  {
    if (_buffer != null)
       _buffer.dispose();
    if (_geoObject != null)
       _geoObject.dispose();
  }

  public final void runInBackground(G3MContext context)
  {
    _geoObject = GEOJSONParser.parse(_buffer);

    if (_buffer != null)
       _buffer.dispose();
    _buffer = null;
  }

  public final void onPostExecute(G3MContext context)
  {
    if (_geoObject == null)
    {
      ILogger.instance().logError("Error parsing GEOJSON from \"%s\"", _url.getPath());
    }
    else
    {
      _geoRenderer.addGEOObject(_geoObject, _symbolizer);
      _geoObject = null;
    }
  }
}