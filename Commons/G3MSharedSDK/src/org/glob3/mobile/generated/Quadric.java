package org.glob3.mobile.generated; 
//
//  Quadric.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 29/09/13.
//
//

//
//  Quadric.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 29/09/13.
//
//




public class Quadric
{

  private MutableMatrix44D Q = new MutableMatrix44D();

  private Quadric(MutableMatrix44D M)
  {
     Q = new MutableMatrix44D(M);
  }


  public static Quadric fromEllipsoid(Ellipsoid ellipsoid)
  {
    // assuming ellipsoid is centered on origin
    final Vector3D R = ellipsoid._oneOverRadiiSquared;
    return new Quadric(new MutableMatrix44D(R._x, 0, 0, 0, 0, R._y, 0, 0, 0, 0, R._z, 0, 0, 0, 0, -1));

  }

  public static Quadric fromPlane(double a, double b, double c, double d)
  {
    return new Quadric(new MutableMatrix44D(0, 0, 0, a/2, 0, 0, 0, b/2, 0, 0, 0, c/2, a/2, b/2, c/2, d));

  }

  public final Quadric transformBy(MutableMatrix44D M)
  {
    MutableMatrix44D I = M.inversed();
    MutableMatrix44D T = I.transposed();
    return new Quadric(T.multiply(Q).multiply(I));
  }


  // Algorithm from http://serdis.dis.ulpgc.es/~atrujill/glob3m/IGO/Cuadricas.pdf
  
  
  public final java.util.ArrayList<Double> intersectionsDistances(Vector3D origin, Vector3D direction)
  {
    double x0 = origin._x;
    double y0 = origin._y;
    double z0 = origin._z;
    double u = direction._x;
    double v = direction._y;
    double w = direction._z;
    double A = Q.get0();
    double D = Q.get1();
    double F = Q.get2();
    double G = Q.get3();
    double B = Q.get5();
    double E = Q.get6();
    double H = Q.get7();
    double C = Q.get10();
    double J = Q.get11();
    double K = Q.get15();
  
    double c = A *x0 *x0 + B *y0 *y0 + C *z0 *z0 + K + 2*(D *x0 *y0 + F *x0 *z0 + G *x0 + E *y0 *z0 + H *y0 + J *z0);
    double b = 2 * (G *u + H *v + J *w + A *u *x0 + B *v *y0 + C *w *z0 + F *u *z0 + F *w *x0 + D *u *y0 + D *v *x0 + E *v *z0 + E *w *y0);
    double a = A *u *u + 2 *D *u *v + 2 *F *u *w + B *v *v + 2 *E *v *w + C *w *w;
  
    java.util.ArrayList<Double> distances = new java.util.ArrayList<Double>();
    final IMathUtils mu = IMathUtils.instance();
    if (a != 0)
    {
      double root = b *b - 4 *a *c;
      if (root >= 0)
      {
        double r = mu.sqrt(root);
        double t1 = (-b-r) / 2 / a;
        double t2 = (-b+r) / 2 / a;
        if (t1 > t2)
        {
          double temp = t1;
          t1 = t2;
          t2 = temp;
        }
        if (t1 > 0)
          distances.add(t1);
        if (t2 > 0)
          distances.add(t2);
      }
    }
    else
    {
      if (b != 0)
      {
        double t = -c / b;
        distances.add(t);
      }
    }
  
    return distances;
  }
}