g3m
===

##The multiplatform advanced visualization framework

###G3M is a **framework** developed and designed to: 

 + Develop **mobile maps** apps in 2D, 2,5D and 3D
 + Work with **real time** data
 + Integrate **any kind of data** (format,size)
 + Be integrated on any legacy system
 + **High performance** mobile native development
 + Multi Touch screens
 + Face the problem of the **mobile performance** as an **integrated** problem between server & client

Capabilities
============

####Works on iOS, Android devices and HTML5 environments
![Screenshot Android] (https://dl.dropboxusercontent.com/u/20446978/wiki-github/android.png)
![Screenshot iOS] (https://dl.dropboxusercontent.com/u/20446978/wiki-github/ios.png)
![Screenshot webGL] (https://dl.dropboxusercontent.com/u/20446978/wiki-github/webgl.png)
####Written from the scratch, no legacy dependencies.
G3M is a library thought to develop mobile apps from the scratch, since first line of code all the development goes towards 
a productive environment for multiplatform mobile apps development.
####Mutitouch support
We support all possible events on mobile devices. The advantages of multi-touch navigation are exploited to visualize map.
![Screenshot gestures] (https://dl.dropboxusercontent.com/u/20446978/wiki-github/gesture.png)
####2D-3D
G3m supports a flat world mode and a complete world , this is customizable by developer

####Scenario maps
You can create a scenario map, you only have to pass a bounding box (a Sector in g3m terminology) and you hill have a part of
the world for your app.
![Screenshot webGL] (https://dl.dropboxusercontent.com/u/20446978/wiki-github/scenario.png)


####Real Time Support
We have developed a real-time server called tubes using the [Netty library] (http://netty.io/) that allow full duplex conexion 
between all devices connected to the server.

+ For example with this module you can face the realtime layer editing, any change on a individual mobile device is broadcasted
to the server and to the other devices connected

![Screenshot webGL] (https://dl.dropboxusercontent.com/u/20446978/wiki-github/broadcast.png)

+ Another example is push notification from server to devices

![Screenshot webGL] (https://dl.dropboxusercontent.com/u/20446978/wiki-github/push.png)


####Works offline

All things that you can see on the screen could be cached to be used offline.  G
3M has a cache implemented with
SQLite. We use the  [decorator pattern] (http://en.wikipedia.org/wiki/Decorator_pattern) , the native downloader does not cache, this function is done by a decorator
"CachedDownloader".

####Bitmap generation multiplatform API 
This library is used for tasks like labeling, rasterization, HUD. This development is used for other libraries like 
the simbolization library or the vectorial rendering on the fly.
On the next screenshot there are some labels rendered by this API

![Rasterizing labels](https://dl.dropboxusercontent.com/u/20446978/wiki-github/bitmaps.PNG)

####Projection
The g3m widget supports the next projections:
 + [EPSG:4326] (http://spatialreference.org/ref/epsg/4326/)
 + [EPSG:3857] (http://wiki.openstreetmap.org/wiki/EPSG:3857)
 
Other projections can be accesed by gdal with preprocess transformation on server.
 
####Support terrain models.
+ Bil
+ Any other format throught OGR-GDAL conversion

####Raster Support
+ WMS
+ MapBox
+ CartoDB
+ Mapquest
+ Bing
+ ArcGIS Server
+ Any tiled WebMercator format

MapBox, Mapquest and CartoDB layers:

![Rasterizing labels](https://dl.dropboxusercontent.com/u/20446978/wiki-github/rasterlayers.PNG)

ArcGIS Server - ArcGIS Online layers

![Rasterizing labels](https://dl.dropboxusercontent.com/u/20446978/wiki-github/esri.PNG)

####Vectorial Support.
The vectorial stuff could be done with differents approach depending the data and the capabilities that
developer wants for his application, layers could be rasterized and cached offline when are very big or complex, in 
other cases the vectorial coulb be rendered on the fly.
In teh repositry are the libraries to transform the data to optimized formats 
+ geoJSON -> bson
+ Any other format throught OGR-GDAL conversion (shp, kml , ogr)

Vectorial file rendered on the fly:

![Rasterizing labels](https://dl.dropboxusercontent.com/u/20446978/wiki-github/rasterizing.PNG)



####Symbology

G3M contains all elements needed for a complete symbolization of any kind of information. In order to access the symbolizers
from web interfaces has been developed a CartoCSS parser.
This library also have support for 3d animation of temporal series 

+ 2D Symbols
+ CartoCSS support
+ 3D Symbols
+ Temporal series support

![Rasterizing labels](https://dl.dropboxusercontent.com/u/20446978/wiki-github/shapes.PNG)


####Point Clouds
Simple rendering of point clouds, next step will be online streaming rendering of huge clouds. Currently 
the amount of points is limited by devices capabilities

+ Any format through preprocess

![Rasterizing labels](https://dl.dropboxusercontent.com/u/20446978/wiki-github/pointcloud.png)

####3D Objects
+ Scene JS (There is a Blender plugin to export to this format optimized) -> bson

####3D Info
+ NetCDF, Scientific Data. Preprocess on server.

![Rasterizing labels](https://dl.dropboxusercontent.com/u/20446978/wiki-github/netcdf.png)

####Camera animation

Manage of camera animation with different options to change the point of view over the map depending 
the app capabilities.

####Very simple API 

Using the Builder Pattern we allow to programmers the fast development of apps.

With 2 lines of code you have a 3d map working on a android device (In html5 and iOS is exactly the same API)

<pre>
 <code>
  final G3MBuilder_Android builder = new G3MBuilder_Android(this);
 _g3mWidget = builder.createWidget();
 </code>
</pre> 

But you can develop more complex things only parameterizing the builder

<pre>
  <code>
  builder.setPlanet(Planet.createSphericalEarth());

      //Sector definition, a sector is a 3d bounding box
      final Sector demSector = new Sector(lower, upper);

      //Defining the terraing provider
      final ElevationDataProvider dem = new SingleBillElevationDataProvider(new URL("file:///dem4326.bil", false), demSector,
               new Vector2I(2083, 2001), DELTA_HEIGHT);
      
      //We add the terrain and the vertical exaggeration of that
      builder.getPlanetRendererBuilder().setElevationDataProvider(dem);
      builder.getPlanetRendererBuilder().setVerticalExaggeration(_VerticalExaggeration);
      
      //The initilization task are done the firs time you create the app, tipically load data
      builder.setInitializationTask(loadDataInitializationTask(builder.getPlanet()));
      
      //We add a mesh renderer to show a point cloud (loaded in the initialization task)
      builder.addRenderer(_meshRenderer);

      //Adding simbology to a vectorial renderer to paint a shapefile converted to geojson
      _vectorialRenderer = builder.createGEORenderer(Symbology.Symbolizer);
      //Making invisible the layer
      _vectorialRenderer.setEnable(false);

      //With this option you are defining a scene for your map
      builder.setShownSector(demSector);

      // With this code you define the layers you want in your map
      _layerSet = MiningLayerBuilder.createMiningLayerSet();
      builder.getPlanetRendererBuilder().setLayerSet(_layerSet);
      
      //Creating the widget through the builder parameterization
      _g3mWidget = builder.createWidget();
      
      //Animated camera to a position
      _g3mWidget.setAnimatedCameraPosition(new Geodetic3D(demSector.getCenter(), 1000), TimeInterval.fromSeconds(5));
     
       //Add my map widget to the android interface
      _placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
      _placeHolder.addView(_g3mWidget);
  </code>
</pre>


Use Cases Deployed Apps and Demos
=================================

+ [Mapboo] (http://www.mapboo.com) is a platform for automatic deploying of mobile map apps. You have a dashboard where you 
can define your differents applicatiosn with all the maps you want. You can publish you mobile map app simply with a click
and mapboo publish to different stores. Try it!
+ The G3M Demo application: This is a demo of the capabilities of the API, is based in the master branch and normally is not 
the latest version of the API but is a good getting started to test G3M.
<table>
  <tr>
    <td><a href="https://itunes.apple.com/es/app/g3m/id601442573?mt=8#" ><img src="https://dl.dropboxusercontent.com/u/20446978/wiki-github/appstore.png"></a></td>
    <td> <a href="https://play.google.com/store/apps/details?id=org.glob3.mobile.demo"><img src="https://dl.dropboxusercontent.com/u/20446978/wiki-github/GooglePlay.png"></a></td>
    <td><a href="http://www.amazon.com/IGO-SOFTWARE-glob3-mobile/dp/B00AHK2ACE"> <img src="https://dl.dropboxusercontent.com/u/20446978/wiki-github/amazon.png"></a></td>
  </tr>
</table>

+ [Open Weather Map App](http://owmgapp.glob3mobile.com). Proof of concept of app developed to consume open data from an interesting API.
Open Weather Map (http://www.openweathermap.org) has realtime weather data that is changing all the time, in this app the cache is only used
for the base layer, all the other layers are changing dinamically when ther servers change. G3M allows the cache managemente exactly developer
wants.
+ Another more


Videos
======




Architecture
============ 

Roadpmap
========
##Near (weeks)

+ GPS Support
+ MBTiles
+ CartoCSS extension for 3d symbolization


##After (months)

+ WebGL Cache
+ Point Cloud Streaming

License
=======
G3M is released under a 2 [clauses BSD license](https://github.com/glob3mobile/g3m/blob/purgatory/LICENSE.txt) Except for the Blender plugin exporter which is available under the GPL 2.0 License.:



