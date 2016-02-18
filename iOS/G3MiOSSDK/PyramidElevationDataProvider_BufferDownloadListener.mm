//
//  PyramidElevationDataProvider_BufferDownloadListener.m
//  G3MiOSSDK
//
//  Created by Sebastian Ortega Trujillo on 18/2/16.
//
//

#import <Foundation/Foundation.h>
#import "PyramidElevationDataProvider_BufferDownloadListener.hpp"

PyramidElevationDataProvider_BufferDownloadListener::PyramidElevationDataProvider_BufferDownloadListener(const Sector& sector,
                                                                               const Vector2I& extent,
                                                                               bool variableSized,
                                                                               IElevationDataListener *listener,
                                                                               bool autodeleteListener,
                                                                               double deltaHeight) : _sector(sector){
    //_sector = Sector(sector);
    _width = extent._x;
    _height = extent._y;
    _listener = listener;
    _autodeleteListener = autodeleteListener;
    _deltaHeight = deltaHeight;
    _variableSized = variableSized;
}

void PyramidElevationDataProvider_BufferDownloadListener::onDownload(const URL& url,IByteBuffer* buffer,bool expired){
    
    Vector2I *resolution = NULL;
    ShortBufferElevationData *elevationData;
    if (!_variableSized) {
#warning Chano_at_work: Si bien la resolución está fijada a 8 porque la pirámide que sirvo la tiene, debo encontrar una forma de no fijarla por hardcoding.
        resolution = new Vector2I(8, 8);
        elevationData = BilParser::parseBil16MaxMin(_sector, *resolution, buffer, _deltaHeight);
    }
    else {
        elevationData = BilParser::parseBil16Redim(_sector, buffer, _deltaHeight);
        resolution = new Vector2I(elevationData->getExtent());
    }
    
    if (buffer != NULL) delete buffer;
    
    if (elevationData == NULL)
    {
        _listener->onError(_sector, *resolution);
    }
    else
    {
        _listener->onData(_sector, *resolution, elevationData);
        //elevationData->_release();
    }
    
    
    if (_autodeleteListener)
    {
        if (_listener != NULL) delete _listener;
        _listener = NULL;
    }
}
void PyramidElevationDataProvider_BufferDownloadListener::onError(const URL& url){
    const Vector2I resolution = Vector2I(_width, _height);
    
    _listener->onError(_sector, resolution);
    if (_autodeleteListener)
    {
        if (_listener != NULL) delete _listener;
        _listener = NULL;
    }
}
void PyramidElevationDataProvider_BufferDownloadListener::onCancel(const URL& url){
    if (_listener != NULL)
    {
        const Vector2I resolution = Vector2I(_width, _height);
        _listener->onCancel(_sector, resolution);
        if (_autodeleteListener)
        {
            if (_listener != NULL) delete _listener;
            _listener = NULL;
        }
    }
}

void PyramidElevationDataProvider_BufferDownloadListener::onCanceledDownload(const URL& url,
                                                                IByteBuffer* data,
                                                                bool expired){
    if (_autodeleteListener)
    {
        if (_listener != NULL) delete _listener;
        _listener = NULL;
    }
}
