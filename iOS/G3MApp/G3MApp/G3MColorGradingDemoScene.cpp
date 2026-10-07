//
//  G3MColorGradingDemoScene.cpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 10/7/26.
//

#include "G3MColorGradingDemoScene.hpp"

#include <G3M/Angle.hpp>
#include <G3M/Color.hpp>
#include <G3M/ColorMatrix.hpp>
#include <G3M/ILogger.hpp>
#include <G3M/Layer.hpp>
#include <G3M/LayerBuilder.hpp>
#include <G3M/LayerSet.hpp>
#include <G3M/Matrix44D.hpp>
#include <G3M/PlanetRenderer.hpp>

#include "G3MDemoModel.hpp"
#include "G3MRasterLayersDemoScene.hpp"


G3MColorGradingDemoScene::G3MColorGradingDemoScene(G3MDemoModel* model) :
G3MDemoScene(model, "Color Grading", "Layer", "<select layer>", 0)
{
  addOption("Sentinel-2 cloudless");
  addOption("ESRI World Imagery");
  addOption("Open Street Map");
  addOption("OpenTopoMap");

  _effectGroupIndex = addOptionGroup("Effect", "<select effect>", 0);
  addOption(_effectGroupIndex, "None");
  addOption(_effectGroupIndex, "Grayscale");
  addOption(_effectGroupIndex, "Desaturated");
  addOption(_effectGroupIndex, "Saturated");
  addOption(_effectGroupIndex, "Sepia");
  addOption(_effectGroupIndex, "Brand duotone");
  addOption(_effectGroupIndex, "Dimmed");
  addOption(_effectGroupIndex, "Inverted");
  addOption(_effectGroupIndex, "Custom");
}

void G3MColorGradingDemoScene::rawActivate(const G3MContext* context) {
  LayerSet* layerSet = getModel()->getLayerSet();

  Layer* sentinel2 = getModel()->createRasterLayer();
  sentinel2->setTitle("Sentinel-2 cloudless");
  sentinel2->setEnable(false);
  layerSet->addLayer(sentinel2);

  layerSet->addLayer( G3MRasterLayersDemoScene::createMercatorLayer("ESRI World Imagery",
                                                                    "https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}",
                                                                    18,
                                                                    false,
                                                                    "Esri, Maxar, Earthstar Geographics, and the GIS User Community") );

  Layer* osm = LayerBuilder::createOSMLayer();
  osm->setTitle("Open Street Map");
  osm->setEnable(false);
  layerSet->addLayer(osm);

  layerSet->addLayer( G3MRasterLayersDemoScene::createMercatorLayer("OpenTopoMap",
                                                                    "https://tile.opentopomap.org/{z}/{x}/{y}.png",
                                                                    17,
                                                                    false,
                                                                    "Map data: OpenStreetMap contributors, SRTM. Map style: OpenTopoMap (CC-BY-SA)") );
}

void G3MColorGradingDemoScene::rawSelectOption(const std::string& option,
                                               int optionIndex) {
  LayerSet* layerSet = getModel()->getLayerSet();
  layerSet->disableAllLayers();
  layerSet->getLayerByTitle(option)->setEnable(true);
}

void G3MColorGradingDemoScene::rawSelectGroupOption(size_t groupIndex,
                                                    const std::string& option,
                                                    int optionIndex) {
  if (groupIndex == _effectGroupIndex) {
    selectEffect(option);
  }
  else {
    rawSelectOption(option, optionIndex);
  }
}

void G3MColorGradingDemoScene::selectEffect(const std::string& effect) {
  G3MDemoModel*   model          = getModel();
  PlanetRenderer* planetRenderer = model->getPlanetRenderer();
  if (effect == "Custom") {
    model->showColorGradingPanel(this);
    return;
  }

  model->hideColorGradingPanel();
  if (effect == "None") {
    planetRenderer->removeColorMatrix();
    return;
  }

  Matrix44D* colorMatrix = createEffectMatrix(effect);
  if (colorMatrix == NULL) {
    ILogger::instance()->logError("effect \"%s\" not supported", effect.c_str());
    return;
  }
  planetRenderer->setColorMatrix(colorMatrix);
  colorMatrix->_release();
}

// The values are examples to be tuned with the sliders
Matrix44D* G3MColorGradingDemoScene::createEffectMatrix(const std::string& effect) {
  if (effect == "Grayscale") {
    return ColorMatrix::createSaturation(0);
  }
  if (effect == "Desaturated") {
    return ColorMatrix::createSaturation(0.5);
  }
  if (effect == "Saturated") {
    return ColorMatrix::createSaturation(1.5);
  }
  if (effect == "Sepia") {
    Matrix44D* grayscale = ColorMatrix::createSaturation(0);
    Matrix44D* tint      = ColorMatrix::createTint(Color::fromRGBA255(255, 222, 173));
    Matrix44D* sepia     = ColorMatrix::createSequence(grayscale, tint);
    grayscale->_release();
    tint->_release();
    return sepia;
  }
  if (effect == "Brand duotone") {
    return ColorMatrix::createDuotone(Color::fromRGBA255( 10,  30,  70),   // navy
                                         Color::fromRGBA255( 64, 208, 224));  // cyan
  }
  if (effect == "Dimmed") {
    return ColorMatrix::createBrightness(0.6);
  }
  if (effect == "Inverted") {
    return ColorMatrix::createInversion();
  }
  return NULL;
}

// Fixed order, so each slider keeps its meaning whatever the others do
Matrix44D* G3MColorGradingDemoScene::createCustomMatrix(double saturation,
                                                        const Angle& hue,
                                                        double contrast,
                                                        double brightness,
                                                        const Color& tint,
                                                        double tintIntensity) {
  const Color intenseTint = Color::fromRGBA((float) (1 - tintIntensity + tintIntensity * tint._red),
                                            (float) (1 - tintIntensity + tintIntensity * tint._green),
                                            (float) (1 - tintIntensity + tintIntensity * tint._blue),
                                            1);
  Matrix44D* steps[] = {
    ColorMatrix::createSaturation(saturation),
    ColorMatrix::createHueRotation(hue),
    ColorMatrix::createContrast(contrast),
    ColorMatrix::createBrightness(brightness),
    ColorMatrix::createTint(intenseTint)
  };
  const int stepsCount = sizeof(steps) / sizeof(steps[0]);

  Matrix44D* result = steps[0];
  for (int i = 1; i < stepsCount; i++) {
    Matrix44D* sequence = ColorMatrix::createSequence(result, steps[i]);
    result->_release();
    steps[i]->_release();
    result = sequence;
  }
  return result;
}

void G3MColorGradingDemoScene::setCustomGrade(double saturation,
                                              const Angle& hue,
                                              double contrast,
                                              double brightness,
                                              const Color& tint,
                                              double tintIntensity) {
  Matrix44D* colorMatrix = createCustomMatrix(saturation, hue, contrast, brightness, tint, tintIntensity);
  getModel()->getPlanetRenderer()->setColorMatrix(colorMatrix);
  colorMatrix->_release();
}

void G3MColorGradingDemoScene::logCustomGrade(double saturation,
                                              const Angle& hue,
                                              double contrast,
                                              double brightness,
                                              const Color& tint,
                                              double tintIntensity) const {
  Matrix44D* m = createCustomMatrix(saturation, hue, contrast, brightness, tint, tintIntensity);
  ILogger::instance()->logInfo("Custom color grade: saturation=%.3f hue=%.1f contrast=%.3f brightness=%.3f tint=(%d, %d, %d) tintIntensity=%.3f\n"
                               "  | %.4f %.4f %.4f | + %.4f\n"
                               "  | %.4f %.4f %.4f | + %.4f\n"
                               "  | %.4f %.4f %.4f | + %.4f",
                               saturation, hue._degrees, contrast, brightness,
                               (int) (tint._red * 255 + 0.5), (int) (tint._green * 255 + 0.5), (int) (tint._blue * 255 + 0.5),
                               tintIntensity,
                               m->_m00, m->_m01, m->_m02, m->_m03,
                               m->_m10, m->_m11, m->_m12, m->_m13,
                               m->_m20, m->_m21, m->_m22, m->_m23);
  m->_release();
}
