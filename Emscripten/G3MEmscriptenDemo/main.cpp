
//#include <stdio.h>

//#include <emscripten/html5.h>

#include "G3MBuilder_Emscripten.hpp"
#include "G3MWidget_Emscripten.hpp"

#include "LayerSet.hpp"
#include "LayerTilesRenderParameters.hpp"
#include "URLTemplateLayer.hpp"
#include "PlanetRendererBuilder.hpp"
//#include "HUDRenderer.hpp"

//   EMStorage::initialize();
//   IStringBuilder::setInstance( new StringBuilder_Emscripten(IStringBuilder::DEFAULT_FLOAT_PRECISION) );
//   IMathUtils::setInstance( new MathUtils_Emscripten() );
//   IStringUtils::setInstance( new StringUtils_Emscripten() );


int main() {
  emscripten_console_log("Step 1");
  G3MBuilder_Emscripten builder;

  builder.setAtmosphere(true);
  
  // HUDRenderer* hudRenderer = new HUDRenderer();
  // builder.setHUDRenderer(hudRenderer);


  PlanetRendererBuilder* planetRendererBuilder = builder.getPlanetRendererBuilder();

  LayerSet* layerSet = new LayerSet();

  LayerTilesRenderParameters* parameters = LayerTilesRenderParameters::createDefaultWGS84(Sector::FULL_SPHERE,
											  1,  // topSectorSplitsByLatitude
											  2,  // topSectorSplitsByLongitude
											  1,  // firstLevel
											  13  // maxLevel
											  );

  layerSet->addLayer( new URLTemplateLayer("http://brownietech.ddns.net/maps/s2cloudless/{z}/{y}/{x}.jpg",
					   Sector::FULL_SPHERE,
					   false,
					   TimeInterval::fromDays(30),
					   true, // readExpired
					   NULL, // condition
					   parameters) );
  planetRendererBuilder->setLayerSet(layerSet);

  planetRendererBuilder->setIncrementalTileQuality(true);

      
  emscripten_console_log("Step 2");
  G3MWidget_Emscripten* widget = builder.createWidget("g3mWidgetHolder");

  emscripten_console_log("Step 3");

  emscripten_console_log("WOOOOOOOWWWW!!!");

}

