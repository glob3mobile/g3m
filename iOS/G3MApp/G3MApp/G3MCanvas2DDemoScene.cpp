//
//  G3MCanvas2DDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 2/12/15.
//

#include "G3MCanvas2DDemoScene.hpp"

#include <G3M/G3MWidget.hpp>
#include <G3M/OSMLayer.hpp>
#include <G3M/LayerSet.hpp>
#include <G3M/IFactory.hpp>
#include <G3M/ICanvas.hpp>
#include <G3M/QuadShape.hpp>
#include <G3M/ShapesRenderer.hpp>
#include <G3M/GFont.hpp>
#include <G3M/IDownloader.hpp>
#include <G3M/IImageDownloadListener.hpp>
#include <G3M/Geodetic3D.hpp>
#include <G3M/IImage.hpp>
#include <G3M/Color.hpp>
#include <G3M/G3MContext.hpp>

#include "G3MDemoModel.hpp"


class G3MCanvas2DDemoScene_ImageListener : public IImageListener {
private:
  ShapesRenderer* _shapesRenderer;

public:
  G3MCanvas2DDemoScene_ImageListener(ShapesRenderer* shapesRenderer) :
  _shapesRenderer(shapesRenderer)
  {
  }

  void imageCreated(const IImage* image) {
    QuadShape* quad = new QuadShape(new Geodetic3D(Angle::fromDegrees(-34.615047738942699596),
                                                   Angle::fromDegrees(-58.4447233540403559),
                                                   1000),
                                    ABSOLUTE,
                                    image,
                                    image->getWidth()  * 15.0f,
                                    image->getHeight() * 15.0f,
                                    true);

    _shapesRenderer->addShape(quad);
  }
};


class G3MCanvas2DDemoScene_ImageDownloadListener : public IImageDownloadListener {
private:
  const IFactory* _factory;
  ShapesRenderer* _shapesRenderer;

public:
  G3MCanvas2DDemoScene_ImageDownloadListener(const IFactory* factory,
                                             ShapesRenderer* shapesRenderer) :
  _factory(factory),
  _shapesRenderer(shapesRenderer)
  {

  }

  void onDownload(const URL& url,
                  IImage* image,
                  bool expired) {

    ICanvas* canvas = _factory->createCanvas(false);
    const int width  = 1024;
    const int height = 1024;
    canvas->initialize(width, height);

    canvas->setFillColor(Color::fromRGBA(1, 1, 0, 0.5));
    canvas->fillRectangle(0, 0, width, height);
    canvas->setLineWidth(4);
    canvas->setLineColor(Color::BLACK);
    canvas->strokeRectangle(0, 0, width, height);

    const int steps = 8;
    const float leftStep = (float) width / steps;
    const float topStep  = (float) height / steps;

    canvas->setLineWidth(1);
    canvas->setFillColor(Color::fromRGBA(0, 0, 0, 0.75));
    for (int i = 1; i < steps; i++) {
      canvas->fillRectangle(0, topStep*i, width, 1);
      canvas->fillRectangle(leftStep*i, 0, 1, height);
    }

    canvas->setFont(GFont::monospaced());
    canvas->setFillColor(Color::BLACK);
//    canvas->fillText("0,0", 0, 0);
//    canvas->fillText("w,h", width, height);

    for (int i = 0; i < steps; i++) {
      canvas->fillText("Hellow World", leftStep*i, topStep*i);
    }

    canvas->drawImage(image, width / 8,     height / 8);       // ok
    canvas->drawImage(image, width / 8 * 3, height / 8, 0.5);  // ok

    canvas->drawImage(image, width / 8,     height / 8 * 3, image->getWidth()*2, image->getHeight()*2); // ok
    canvas->drawImage(image, width / 8 * 3, height / 8 * 3, image->getWidth()*2, image->getHeight()*2, 0.5); //ok

    // ok
    canvas->drawImage(image,
                      0, 0, image->getWidth(), image->getHeight(),
                      width / 8 * 5, height / 8 * 5, image->getWidth()*2, image->getHeight()*2);
    // ok
    canvas->drawImage(image,
                      0, 0, image->getWidth(), image->getHeight(),
                      width / 8 * 7, height / 8 * 7, image->getWidth()*2, image->getHeight()*2,
                      0.5);

    canvas->createImage(new G3MCanvas2DDemoScene_ImageListener(_shapesRenderer),
                        true);
    
    delete canvas;

    delete image;
  }

  void onError(const URL& url) {

  }

  void onCancel(const URL& url) {
    // do nothing
  }

  void onCanceledDownload(const URL& url,
                          IImage* image,
                          bool expired) {
    // do nothing
  }

};


void G3MCanvas2DDemoScene::rawActivate(const G3MContext* context) {
  G3MDemoModel* model     = getModel();
  G3MWidget*    g3mWidget = model->getG3MWidget();

  OSMLayer* layer = new OSMLayer(TimeInterval::fromDays(30));
  model->getLayerSet()->addLayer(layer);


  context->getDownloader()->requestImage(//URL("file:///colorgrid.jpg"),
                                         URL("file:///g3m-mark.png"),
                                         1, // priority,
                                         TimeInterval::zero(),
                                         false,
                                         new G3MCanvas2DDemoScene_ImageDownloadListener(context->getFactory(),
                                                                                        model->getShapesRenderer()),
                                         true);

  g3mWidget->setAnimatedCameraPosition( Geodetic3D::fromDegrees(-34.615047738942699596, -58.4447233540403559, 35000) );
  
}
