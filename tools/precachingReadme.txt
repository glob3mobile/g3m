Precaching

Precaching fill database "g3m.cache" with tiles of specified layers in indicated sector and levels:

//0. Initialize builders and basic params
      final G3MBuilder_Android builder = new G3MBuilder_Android(getApplicationContext());
      builder.setLogFPS(true);

      final Planet planet = Planet.createSphericalEarth();
      builder.setPlanet(planet);

      final PlanetRendererBuilder planetRendererBuilder = builder.getPlanetRendererBuilder();

//1. Fill LayerSet with the layers you want to cache
      
	final LayerSet layerSet = new LayerSet();

	layerSet.addLayer(...);

      	planetRendererBuilder.setLayerSet(layerSet);
      	planetRendererBuilder.setRenderDebug(false);

//2. Create Widget:
//Always after setting params
      	_widgetAndroid = builder.createWidget();

//3. Create TileVisitor:
      	final TileVisitorCache_Android tvc = new TileVisitorCache_Android(_widgetAndroid.getG3MContext());
      
// 3.1 Recommended
// Sector specified cached at the indicated levels    
  
	_widgetAndroid.getG3MWidget().getPlanetRenderer().acceptTileVisitor(tvc, Sector.fullSphere(), 0, 2);




// 3.2 Specificied sector and levels
// Sector specified cached at the indicated levels

	_widgetAndroid.getG3MWidget().getPlanetRenderer().acceptTileVisitor(
               tvc,
               new Sector(new Geodetic2D(Angle.fromDegrees(39.31), Angle.fromDegrees(-6.72)), new Geodetic2D(
                        Angle.fromDegrees(39.38), Angle.fromDegrees(-6.64))), 2, 14);

      	_widgetAndroid.getG3MContext().getLogger().logInfo("Precaching has been completed");