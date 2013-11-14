//
//  Quadric.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 29/09/13.
//
//

#include "Quadric.hpp"


// Algorithm from http://serdis.dis.ulpgc.es/~atrujill/glob3m/IGO/Cuadricas.pdf


std::vector<double> Quadric::intersectionsDistances(const Vector3D& origin,
                                                    const Vector3D& direction) const {
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
  
  double c = A*x0*x0 + B*y0*y0 + C*z0*z0 + K + 2*(D*x0*y0 + F*x0*z0 + G*x0 + E*y0*z0 + H*y0  + J*z0);
  double b = 2 * (G*u + H*v + J*w + A*u*x0 + B*v*y0 + C*w*z0 + F*u*z0 + F*w*x0 + D*u*y0 + D*v*x0 + E*v*z0 + E*w*y0);
  double a = A*u*u + 2*D*u*v + 2*F*u*w + B*v*v + 2*E*v*w + C*w*w;
  
  std::vector<double> distances;
  const IMathUtils* mu = IMathUtils::instance();
  if (a != 0) {
    double root = b*b - 4*a*c;
    if (root >= 0) {
      double r = mu->sqrt(root);
      double t1 = (-b-r) / 2 / a;
      double t2 = (-b+r) / 2 / a;
      if (t1 > t2) {
        double temp = t1;
        t1 = t2;
        t2 = temp;
      }
      if (t1 > 0)
        distances.push_back(t1);
      if (t2 > 0)
        distances.push_back(t2);
    }
  } else {
    if (b != 0) {
      double t = -c / b;
      distances.push_back(t);
    }
  }
  
  return distances;
}
