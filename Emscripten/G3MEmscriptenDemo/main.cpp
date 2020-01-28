
//#include <stdio.h>

//#include <emscripten/html5.h>

#include "G3MBuilder_Emscripten.hpp"
#include "G3MWidget_Emscripten.hpp"

#include "G3M/LayerSet.hpp"
#include "G3M/LayerTilesRenderParameters.hpp"
#include "G3M/URLTemplateLayer.hpp"
#include "G3M/PlanetRendererBuilder.hpp"


int main() {
  emscripten_console_log("Step 1");
  G3MBuilder_Emscripten builder;

  builder.setAtmosphere(true);

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
  G3MWidget_Emscripten* widget = builder.createWidget();

  emscripten_console_log("Step 3");

  widget->startWidget("g3mWidgetHolder");

  emscripten_console_log("WOOOOOOOWWWW!!!");
}

