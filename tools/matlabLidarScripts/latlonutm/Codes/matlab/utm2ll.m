function [lat,lon]=utm2ll(x,y,f,datum,varargin)
%UTM2LL UTM to Lat/Lon coordinates precise conversion.
%	[LAT,LON]=UTM2LL(X,Y,ZONE) converts UTM coordinates X,Y (in meters)
%	defined in the UTM ZONE (integer) to latitude LAT and longitude LON 
%	(in degrees). Default datum is WGS84.
%
%	X, Y and F can be scalars, vectors or matrix. Outputs LAT and LON will
%	have the same size as inputs.
%
%	For southern hemisphere points, use negative zone -ZONE.
%
%	UTM2LL(X,Y,ZONE,DATUM) uses specific DATUM for conversion. DATUM can be
%	a string in the following list:
%		'wgs84': World Geodetic System 1984 (default)
%		'nad27': North American Datum 1927
%		'clk66': Clarke 1866
%		'nad83': North American Datum 1983
%		'grs80': Geodetic Reference System 1980
%		'int24': International 1924 / Hayford 1909
%	or DATUM can be a 2-element vector [A,F] where A is semimajor axis (in
%	meters)	and F is flattening of the user-defined ellipsoid.
%
%	Notice:
%		- UTM2LL does not perform cross-datum conversion.
%		- precision is near a millimeter.
%
%
%	Reference:
%		I.G.N., Projection cartographique Mercator Transverse: Algorithmes,
%		   Notes Techniques NT/G 76, janvier 1995.
%
%	Author: Francois Beauducel, <beauducel@ipgp.fr>
%	Created: 2001-08-23
%	Updated: 2015-01-29


%	Copyright (c) 2001-2015, François Beauducel, covered by BSD License.
%	All rights reserved.
%
%	Redistribution and use in source and binary forms, with or without 
%	modification, are permitted provided that the following conditions are 
%	met:
%
%	   * Redistributions of source code must retain the above copyright 
%	     notice, this list of conditions and the following disclaimer.
%	   * Redistributions in binary form must reproduce the above copyright 
%	     notice, this list of conditions and the following disclaimer in 
%	     the documentation and/or other materials provided with the distribution
%	                           
%	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
%	AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
%	IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
%	ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
%	LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
%	CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
%	SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
%	INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
%	CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
%	ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
%	POSSIBILITY OF SUCH DAMAGE.

% Available datums
datums = [ ...
	{ 'wgs84', 6378137.0, 298.257223563 };
	{ 'nad83', 6378137.0, 298.257222101 };
	{ 'grs80', 6378137.0, 298.257222101 };
	{ 'nad27', 6378206.4, 294.978698214 };
	{ 'int24', 6378388.0, 297.000000000 };
	{ 'clk66', 6378206.4, 294.978698214 };
];

if nargin < 3
	error('Not enough input arguments.')
end

if all([numel(x),numel(y)] > 1) && any(size(x) ~= size(y))
	error('X and Y must be the same size or scalars.')
end

if ~isnumeric(f) || any(f ~= round(f)) || (~isscalar(f) && any(size(f) ~= size(x)))
	error('ZONE must be integer value, scalar or same size as X and/or Y.')
end

if nargin < 4
	datum = 'wgs84';
end

if ischar(datum)
	if ~any(strcmpi(datum,datums(:,1)))
		error('Unkown DATUM name "%s"',datum);
	end
	k = find(strcmpi(datum,datums(:,1)));
	A1 = datums{k,2};
	F1 = datums{k,3};	
else
	if numel(datum) ~= 2
		error('User defined DATUM must be a vector [A,F].');
	end
	A1 = datum(1);
	F1 = datum(2);
end

% constants
D0 = 180/pi;	% conversion rad to deg
maxiter = 100;	% maximum iteration for latitude computation
eps = 1e-11;	% minimum residue for latitude computation

K0 = 0.9996;					% UTM scale factor
X0 = 500000;					% UTM false East (m)
Y0 = 1e7*(f < 0);				% UTM false North (m)
P0 = 0;						% UTM origin latitude (rad)
L0 = (6*abs(f) - 183)/D0;			% UTM origin longitude (rad)
E1 = sqrt((A1^2 - (A1*(1 - 1/F1))^2)/A1^2);	% ellpsoid excentricity
N = K0*A1;

% computing parameters for Mercator Transverse projection
C = coef(E1,0);
YS = Y0 - N*(C(1)*P0 + C(2)*sin(2*P0) + C(3)*sin(4*P0) + C(4)*sin(6*P0) + C(5)*sin(8*P0));

C = coef(E1,1);
zt = complex((y - YS)/N/C(1),(x - X0)/N/C(1));
z = zt - C(2)*sin(2*zt) - C(3)*sin(4*zt) - C(4)*sin(6*zt) - C(5)*sin(8*zt);
L = real(z);
LS = imag(z);

l = L0 + atan(sinh(LS)./cos(L));
p = asin(sin(L)./cosh(LS));

L = log(tan(pi/4 + p/2));

% calculates latitude from the isometric latitude
p = 2*atan(exp(L)) - pi/2;
p0 = NaN;
n = 0;
while any(isnan(p0) | abs(p - p0) > eps) && n < maxiter
	p0 = p;
	es = E1*sin(p0);
	p = 2*atan(((1 + es)./(1 - es)).^(E1/2).*exp(L)) - pi/2;
	n = n + 1;
end

if nargout < 2
	lat = D0*[p(:),l(:)];
else
	lat = p*D0;
	lon = l*D0;
end


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
function c = coef(e,m)
%COEF Projection coefficients
%	COEF(E,M) returns a vector of 5 coefficients from:
%		E = first ellipsoid excentricity
%		M = 0 for transverse mercator
%		M = 1 for transverse mercator reverse coefficients
%		M = 2 for merdian arc


if nargin < 2
	m = 0;
end

switch m
	case 0
	c0 = [-175/16384, 0,   -5/256, 0,  -3/64, 0, -1/4, 0, 1;
           -105/4096, 0, -45/1024, 0,  -3/32, 0, -3/8, 0, 0;
           525/16384, 0,  45/1024, 0, 15/256, 0,    0, 0, 0;
          -175/12288, 0, -35/3072, 0,      0, 0,    0, 0, 0;
          315/131072, 0,        0, 0,      0, 0,    0, 0, 0];
	  
	case 1
	c0 = [-175/16384, 0,   -5/256, 0,  -3/64, 0, -1/4, 0, 1;
             1/61440, 0,   7/2048, 0,   1/48, 0,  1/8, 0, 0;
          559/368640, 0,   3/1280, 0,  1/768, 0,    0, 0, 0;
          283/430080, 0, 17/30720, 0,      0, 0,    0, 0, 0;
       4397/41287680, 0,        0, 0,      0, 0,    0, 0, 0];

	case 2
	c0 = [-175/16384, 0,   -5/256, 0,  -3/64, 0, -1/4, 0, 1;
         -901/184320, 0,  -9/1024, 0,  -1/96, 0,  1/8, 0, 0;
         -311/737280, 0,  17/5120, 0, 13/768, 0,    0, 0, 0;
          899/430080, 0, 61/15360, 0,      0, 0,    0, 0, 0;
      49561/41287680, 0,        0, 0,      0, 0,    0, 0, 0];
   
end
c = zeros(size(c0,1),1);

for i = 1:size(c0,1)
    c(i) = polyval(c0(i,:),e);
end
