//
//  G3MNonOverlappingMarksDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 1/30/15.
//  Copyright (c) 2015 Igo Software SL. All rights reserved.
//

#include "G3MNonOverlappingMarksDemoScene.hpp"

#include <G3MiOSSDK/URLTemplateLayer.hpp>
#include <G3MiOSSDK/LayerSet.hpp>
#include <G3MiOSSDK/NonOverlappingMarksRenderer.hpp>
#include <G3MiOSSDK/DownloaderImageBuilder.hpp>
#include <G3MiOSSDK/G3MWidget.hpp>
#include <G3MiOSSDK/LabelImageBuilder.hpp>
#include <G3MiOSSDK/ColumnLayoutImageBuilder.hpp>
#include <G3MiOSSDK/Context.hpp>
#include <G3MiOSSDK/IDownloader.hpp>
#include <G3MiOSSDK/IBufferDownloadListener.hpp>
#include <G3MiOSSDK/IJSONParser.hpp>
#include <G3MiOSSDK/JSONArray.hpp>
#include <G3MiOSSDK/JSONObject.hpp>

#include "G3MDemoModel.hpp"


class G3MNonOverlappingMarksDemoScene_VisibilityListener : public NonOverlappingMarksVisibilityListener {
public:
  void onVisibilityChange(const std::vector<NonOverlappingMark*>& visible) {

  }
};

class G3MNonOverlappingMarksDemoScene_NonOverlappingMarkTouchListener : public NonOverlappingMarkTouchListener {
public:
  bool touchedMark(const NonOverlappingMark* mark,
                   const Vector2F& touchedPixel) {
    const std::string url = mark->getID();

    NSString* urlObjC = [NSString stringWithCString: url.c_str()
                                           encoding: NSUTF8StringEncoding];

    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:urlObjC]];

    return true;
  }
};


class JSONBufferDownloadListener : public IBufferDownloadListener {
private:
  NonOverlappingMarksRenderer* _nonOverlappingMarksRenderer;
public:
  JSONBufferDownloadListener(NonOverlappingMarksRenderer* nonOverlappingMarksRenderer) :
  _nonOverlappingMarksRenderer(nonOverlappingMarksRenderer)
  {
  }

  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    const JSONBaseObject* jsonBaseObject = IJSONParser::instance()->parse(buffer);

    const JSONArray* articles = jsonBaseObject->asArray();
    if (articles) {
      for (int i = 0; i < articles->size(); i++) {
        const JSONObject* article = articles->getAsObject(i);

        const std::string title     = article->getAsString("title", "");
        const double      lat       = article->getAsNumber("lat", 0.0);
        const double      lon       = article->getAsNumber("lon", 0.0);
        const std::string urlS      = article->getAsString("url", "");
        const std::string thumbnail = article->getAsString("thumbnail", "");

        LabelImageBuilder* titleBuilder = new LabelImageBuilder(title,
                                                                GFont::sansSerif(14),
                                                                4,
                                                                Color::black(),
                                                                Color::transparent(),
                                                                0,0,0,
                                                                Color::white(),
                                                                4);

        NonOverlappingMark* mark;
        if (thumbnail == "") {
          mark = new NonOverlappingMark(titleBuilder,
                                        new DownloaderImageBuilder(URL("file:///anchorWidget.png")),
                                        Geodetic3D::fromDegrees(lat, lon, 0));
        }
        else {
          ColumnLayoutImageBuilder* columnBuilder = new ColumnLayoutImageBuilder(new DownloaderImageBuilder(URL(thumbnail)),
                                                                                 titleBuilder,
                                                                                 0,                     // margin
                                                                                 0,                     // borderWidth,
                                                                                 Color::transparent(),  // borderColor
                                                                                 0,                     // padding
                                                                                 Color::transparent(),  // backgroundColor
                                                                                 0,                     // cornerRadius
                                                                                 2                      // childrenSeparation
                                                                                 );

          mark = new NonOverlappingMark(columnBuilder,
                                        new DownloaderImageBuilder(URL("file:///anchorWidget.png")),
                                        Geodetic3D::fromDegrees(lat, lon, 0));
        }

        mark->setID(urlS);

        _nonOverlappingMarksRenderer->addMark(mark);
      }
    }

    delete jsonBaseObject;
    delete buffer;
  }

  void onError(const URL& url) {
  }

  void onCancel(const URL& url) {
  }

  void onCanceledDownload(const URL& url,
                          IByteBuffer* buffer,
                          bool expired) {

  }

};


void G3MNonOverlappingMarksDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();

//#warning Testing infos
  std::vector<const Info*>* layerInfo = new std::vector<const Info*>();
//  layerInfo->push_back( new Info("(C) Stamen") );
  URLTemplateLayer* layer = URLTemplateLayer::newMercator(//"http://c.tile.stamen.com/toner/{level}/{x}/{y}.png",
                                                          "http://[abcd].tile.stamen.com/watercolor/{level}/{x}/{y}.png",
                                                          Sector::fullSphere(),
                                                          true,
                                                          1,
                                                          18,
                                                          TimeInterval::fromDays(30),
                                                          true,
                                                          1, // transparency
                                                          NULL, // condition
                                                          layerInfo);
  model->getLayerSet()->addLayer(layer);

  NonOverlappingMarksRenderer* renderer = model->getNonOverlappingMarksRenderer();

  renderer->addVisibilityListener(new G3MNonOverlappingMarksDemoScene_VisibilityListener());
  renderer->setTouchListener(new G3MNonOverlappingMarksDemoScene_NonOverlappingMarkTouchListener());

  context->getDownloader()->requestBuffer(URL("file:///BuenosAires-Wikipedia.json"),
                                          100000,
                                          TimeInterval::zero(),
                                          false,
                                          new JSONBufferDownloadListener(renderer),
                                          true);

  // Buenos Aires, there we go!
  g3mWidget->setAnimatedCameraPosition( Geodetic3D::fromDegrees(-34.611154532065633305,
                                                                -58.447065610887868559,
                                                                37000) );
}
