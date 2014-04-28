//
//  GEOVectorialLODTileRasterizer.cpp
//  G3MiOSSDK
//
//  Created by fpulido on 17/03/14.
//
//

#include "GEOVectorialLODTileRasterizer.hpp"

#include "GEORasterSymbol.hpp"
#include "Tile.hpp"
#include "IImageListener.hpp"
#include "IImage.hpp"
#include "ICanvas.hpp"
#include "Color.hpp"
#include "GFont.hpp"
#include "IStringBuilder.hpp"
#include "GEORasterProjection.hpp"
#include "IBufferDownloadListener.hpp"
#include "GEORenderer.hpp"
#include "GEORasterSymbolizer.hpp"
#include "GEOJSONParser.hpp"
#include "IThreadUtils.hpp"
#include "GEOFeatureCollection.hpp"
#include "GEOFeature.hpp"
#include "GEOGeometry2D.hpp"
#include "GEOVectorialLODRasterSymbolizer.hpp"

class GEOVectorialLODBufferDownloadListener;


const URL* TEST_URL= new URL("http://192.168.1.26/vectorial/ne_10m_admin_0_countries_6-LEVELS_WGS84", false);


URL* GEOVectorialLODTileRasterizer::builURLForVectorialTile(const URL* baseUrl, const Tile* tile) const {
    
    const IStringUtils* iu = IStringUtils::instance();
    IStringBuilder* isb = IStringBuilder::newStringBuilder();
    
    std::string level = iu->toString(tile->_level/3); // for 18 levels of tiles and only 6 of vectorial data
    std::string row = iu->toString(tile->_row);
    std::string column = iu->toString(tile->_column);
    
    isb->addString(baseUrl->getPath());
    isb->addString("/");
    isb->addString(level);
    isb->addString("/");
    isb->addString(row);
    isb->addString("/");
    isb->addString(level);
    isb->addString("_");
    isb->addString(row);
    isb->addString("-");
    isb->addString(column);
    isb->addString(".json");

    const std::string tileUrl = isb->getString();
    ILogger::instance()->logInfo(tileUrl);
    
    return new URL(tileUrl,false);
}

void GEOVectorialLODTileRasterizer::initialize(const G3MContext* context) {
    _downloader = context->getDownloader();
    _threadUtils = context->getThreadUtils(); //always init for background rasterize
}

//void GEOVectorialLODTileRasterizer::clear() {
//    //TODO: ??
//    notifyChanges();
//}


void GEOVectorialLODTileRasterizer::rawRasterize(const IImage* image,
                                     const TileRasterizerContext& trc,
                                     IImageListener* listener,
                                     bool autodelete) const {
    
    const URL* tileUrl = builURLForVectorialTile(TEST_URL, trc._tile);
    
//    const GEOVectorialLODRasterSymbolizer* rasterSymbolizer = new GEOVectorialLODRasterSymbolizer();
//    const GEOVectorialLODBufferDownloadListener* l = new GEOVectorialLODBufferDownloadListener(rasterSymbolizer, _threadUtils, false);
//    
//    
//    _downloader->requestBuffer(tileUrl, 1000000, TimeInterval::fromDays(30), l, true);
    
    
}

TileRasterizer_AsyncTask* GEOVectorialLODTileRasterizer::getRawRasterizeTask(const IImage* image,
                                                                       const TileRasterizerContext& trc,
                                                                       IImageListener* listener,
                                                                       bool autodelete) const {
    
    return NULL;
}


//---------------

class GEOVectorialLODParser_AsyncTask : public GAsyncTask {
private:
#ifdef C_CODE
    const URL          _url;
#endif
#ifdef JAVA_CODE
    public final URL _url;
#endif
    
    IByteBuffer*   _buffer;
    GEORasterSymbolizer* _symbolizer;
    
    const bool _isBSON;
    
    GEOObject* _geoObject;
    
public:
    GEOVectorialLODParser_AsyncTask(const URL& url,
                                   IByteBuffer* buffer,
                                   GEORasterSymbolizer* symbolizer,
                                   bool isBSON) :
    _url(url),
    _buffer(buffer),
    _symbolizer(symbolizer),
    _isBSON(isBSON),
    _geoObject(NULL)
    {
    }
    
    ~GEOVectorialLODParser_AsyncTask() {
        delete _buffer;
        //    delete _geoObject;
    }
    
    void runInBackground(const G3MContext* context) {
        
        if (_isBSON) {
            _geoObject = GEOJSONParser::parseBSON(_buffer);
        }
        else {
            _geoObject = GEOJSONParser::parseJSON(_buffer);
        }
        
        GEOFeatureCollection* fc = (GEOFeatureCollection*) _geoObject;
        
        
//        for (int i = 0; i < fc->size(); i++) {
//            GEOGeometry2D* g = fc->get(i)->getGeometry();
//            //fc->get(i)->getGeometry()->createSymbols(_symbolizer);
//            
//            GEORasterSymbol* symbol = _symbolizer->createSymbols(g);
//            symbol->rasterize(<#ICanvas *canvas#>, <#const GEORasterProjection *projection#>, <#int tileLevel#>);
//        }
        
        delete _buffer;
        _buffer = NULL;
    }
    
    void onPostExecute(const G3MContext* context) {
        if (_geoObject == NULL) {
            ILogger::instance()->logError("Error parsing GEOJSON from \"%s\"", _url.getPath().c_str());
        }
        else {
            
            _geoObject = NULL;
        }
    }
};



class GEOVectorialLODBufferDownloadListener : public IBufferDownloadListener {
private:

    GEORasterSymbolizer*      _symbolizer;
    const IThreadUtils* _threadUtils;
    const bool          _isBSON;
    
public:
    GEOVectorialLODBufferDownloadListener(GEORasterSymbolizer* symbolizer,
                                                const IThreadUtils* threadUtils,
                                                bool isBSON) :
    _symbolizer(symbolizer),
    _threadUtils(threadUtils),
    _isBSON(isBSON)
    {
    }
    
    void onDownload(const URL& url,
                    IByteBuffer* buffer,
                    bool expired) {
        ILogger::instance()->logInfo("Downloaded GEOObject buffer from \"%s\" (%db)",
                                     url.getPath().c_str(),
                                     buffer->size());
        
        _threadUtils->invokeAsyncTask(new GEOVectorialLODParser_AsyncTask(url,
                                                                               buffer,
                                                                               _symbolizer,
                                                                               _isBSON),
                                      true);
    }
    
    void onError(const URL& url) {
        ILogger::instance()->logError("Error downloading \"%s\"", url.getPath().c_str());
    }
    
    void onCancel(const URL& url) {
        ILogger::instance()->logInfo("Canceled download of \"%s\"", url.getPath().c_str());
    }
    
    void onCanceledDownload(const URL& url,
                            IByteBuffer* buffer,
                            bool expired) {
        // do nothing
    }
};



