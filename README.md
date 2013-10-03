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
the simbolization library

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

####Vectorial Support.
The vectorial stuff could be done with differents approach depending the data and the capabilities that
developer wants for his application, layers could be rasterized and cached offline when are very big or complex, in 
other cases the vectorial 

+ geoJSON -> bson
+ Any other format throught OGR-GDAL conversion

####Symbology
+ 2D Symbols
+ CartoCSS support
+ 3D Symbols
+ Temporal series support

####Point Clouds
+ Any format through preprocess

####3D Objects
+ Scene JS (There is a Blender plugin to export to this format optimized) -> bson

####Camera animation

####Very simple API 
Using the Builder Pattern we allow to programmers the fast development of apps.


Use Cases
=========

Videos
======

Deployed Apps
=============

Demos
=====

Architecture
============ 

Roadpmap
========
##Near (weeks)

+ GPS Support
+ MBTiles


##After (months)

+ WebGL Cache
+ 

License
=======
G3M is released under a 2 [clauses BSD license](https://github.com/glob3mobile/g3m/blob/purgatory/LICENSE.txt) Except for the Blender plugin exporter which is available under the GPL 2.0 License.:



