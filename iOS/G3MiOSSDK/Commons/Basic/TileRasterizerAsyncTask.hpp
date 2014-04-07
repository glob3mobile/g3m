//
//  TileRasterizerAsyncTask.h
//  G3MiOSSDK
//
//  Created by fpulido on 29/03/14.
//
//

#ifndef __G3MiOSSDK__TileRasterizerAsyncTask__
#define __G3MiOSSDK__TileRasterizerAsyncTask__

//#include <iostream>
#include <string>
//#include "ILogger.hpp"
//#include "TileTextureBuilder.hpp"
#include "IThreadUtils.hpp"


class IImage;
//class Tile;
class IImageListener;
//class ChangedListener;
class G3MContext;
//class TileRasterizer_AsyncTask;
class TileTextureBuilder;
class TileRasterizerContext;


class TileRasterizer_AsyncTask : public GAsyncTask {
private:
    const TileTextureBuilder* _builder;
    const IImage* _image;
    const TileRasterizerContext* _trc;
    IImageListener* _listener;
    bool _autodelete;
    
public:
    TileRasterizer_AsyncTask(const IImage* image,
                             const TileRasterizerContext* trc,
                             IImageListener* listener,
                             bool autodelete);
    
    ~TileRasterizer_AsyncTask();
    
    virtual void runInBackground(const G3MContext* context) = 0;
    
    virtual void onPostExecute(const G3MContext* context) = 0;
};

#endif /* defined(__G3MiOSSDK__TileRasterizerAsyncTask__) */
