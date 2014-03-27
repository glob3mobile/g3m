===========
1. Poly2Tri
===========

Package for triangulating complex polygons (with holes) usign
sweep line algorithm.

This is Java implementation of C++ Poly2Tri made by Wu Liang
See http://www.mema.ucl.ac.be/~wu/Poly2Tri/poly2tri.html



===============
2. Requirements
===============

It was created for Sun Java 1.5.0 and newer, not because of templates but becouse of PriorityQueue (see poly2Tri.Polygon),
if you provide your own implementation of priority queue it should run on older version (1.4.x, may be even older)
of Java too.



===============
3. Main Classes
===============

poly2Tri.Triangulation               -> main class with method triangulate(), using poly2Tri.Polygon class

poly2Tri.Polygon                     -> class with method triangulate() ... poly2Tri.Triangulation creates this polygon, calls triangulate() and retuns results

poly2Tri.testPoly2Tri.Poly2TriTest   -> method for testing the tringulation, using swing to visualize the results



==============
4. Compilation
==============

Example for WinXP system using Sun jdk-1.5.0_06 with path set to the java\bin directory (where javac.exe, java.exe are).

1) copy poly2Tri into some directory e.g. c:\my_dir
2) compile the package ... best to do that is to complie poly2Tri.testPoly2Tri.Poly2TriTest as it's using the triangulation
   command: javac c:\my_dir\poly2Tri\testPoly2Tri\Poly2TriTest.java -classpath c:\my_dir



============================
5. Testing the triangulation
============================

Continuing the steps from 4.

3) from c:\my_dir 
   command: java poly2Tri.testPoly2Tri.Poly2TriTest
   will run the test of triangulation, this should create several Swing JFrames with triangulated polygons

This will also output time in ms needed for triangulation of each polygon.



==========
6. Results
==========

Notebook Celeron M 1.4 GHz, 1 GB Ram, WinXP, Sun Java 1.5.0_06:

1st, time: 16 miliseconds
2nd, time: 0 miliseconds
3rd, time: 0 miliseconds
boxc100.bdm, time: 16 miliseconds
circle1.bdm, time: 0 miliseconds
circle2.bdm, time: 16 miliseconds
circle3.bdm, time: 32 miliseconds
crazybox1.bdm, time: 78 miliseconds
crazybox2.bdm, time: 219 miliseconds
guitar.bdm, time: 15 miliseconds
sample1.bdm, time: 0 miliseconds
sample2.bdm, time: 0 miliseconds
sample3.bdm, time: 0 miliseconds
superior.bdm, time: 63 miliseconds