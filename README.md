g3m
===

###The multiplatform advanced visualization framework
###G3M is a **framework** developed and designed to: 

 + Develop **mobile maps** apps in 2D, 2,5D and 3D
 + Work with **real time** data
 + Integrate **any kind of data** (format,size)
 + Be integrated on any legacy system
 + **High performance** mobile native development
 + Multi Touch screens
 + Face the problem of the **mobile performance** as an **integrated** problem between server & client
 + Online - Offline, cache management
 + Real time, push management


###Augmented reality and google glasses
[Augmented reality on google glass](http://glasses.mobilemaptools.com)

<a href="https://www.youtube.com/watch?feature=player_detailpage&v=6K2UF8O6JPg" frameborder="0" allowfullscreen>AR Google Glass</a>


Videos
======

+ <a href="http://www.youtube.com/watch?feature=player_detailpage&v=kEep7i0ZurE" frameborder="0" allowfullscreen>Mapboo</a>
+ <a href="http://www.youtube.com/embed/2iwYc0mvG84?feature=player_detailpage" frameborder="0" allowfullscreen>Glob3 Desktop</a>
+ <a href="http://www.youtube.com/embed/YXmj_uc2d68?feature=player_detailpage" frameborder="0" allowfullscreen>G3M on Mozilla OS</a>
+ <a href="http://www.youtube.com/embed/Nk_yc81sU44?feature=player_detailpage" frameborder="0" allowfullscreen>Glob3 Mobile</a>
+ <a href="http://www.youtube.com/embed/bbMo2iH8Tfs?feature=player_detailpage" frameborder="0" allowfullscreen>Streaming point clouds Desktop devices</a>


Architecture
============ 

The G3M architecture has been created to have a Multiplatform API creating the fewest possible amount of source code.
We have developed a core in C++ that is translated to Java (to work on Android devices) and later is translated to GWT (HTML5 - Javascript)
In every platform there are some libraries that must be developed in the diferent prgramming languages, this classes are declared as abstract and must be developed individually for every platform.
The result is that we have a native API on IOS, Android and webGL and we can  easily add new platforms when it was needed. 

![Rasterizing labels](https://dl.dropboxusercontent.com/u/20446978/wiki-github/arch.png)

Slides
======

+ <a href="http://prezi.com/3ziicw6amq1k/?utm_campaign=share&utm_medium=copy" >Notthingam FOSS4G 2013</a>
+ <a href="https://docs.google.com/presentation/d/1jVjls2m6yFb3j2_-VRWRonGIFk7TrXr3rjEgurBFJcE/edit?usp=sharing" >G3M Capabilities</a>


Roadpmap
========

Our roadmap is changing continuously, and we are open to change the order of new development according the requests of our users.

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


Information
===========
This project has beed developed with the help of Ministerio de Industria Energía y Turismo.
![logos](http://glob3mobile.com/wp-content/uploads/2016/05/avanza2.gif)
![logos](http://glob3mobile.com/wp-content/uploads/2016/05/logomityc.png)
![logos](http://glob3mobile.com/wp-content/uploads/2016/05/ue_feder.jpg)

