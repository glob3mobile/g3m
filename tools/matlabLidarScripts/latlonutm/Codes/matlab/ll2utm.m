function [x,y,f]=ll2utm(varargin)
%LL2UTM Lat/Lon to UTM coordinates precise conversion.
%	[X,Y]=LL2UTM2(LAT,LON) or LL2UTM([LAT,LON]) converts coordinates 
%	LAT,LON (in degrees) to UTM X and Y (in meters). Default datum is WGS84.
%
%	LAT and LON can be scalars, vectors or matrix. Outputs X and Y will
%	have the same size as inputs.
%
%	LL2UTM(...,DATUM) uses specific DATUM for conversion. DATUM can be one
%	of the following char strings:
%		'wgs84': World Geodetic System 1984 (default)
%		'nad27': North American Datum 1927
%		'clk66': Clarke 1866
%		'nad83': North American Datum 1983
%		'grs80': Geodetic Reference System 1980
%		'int24': International 1924 / Hayford 1909
%	or DATUM can be a 2-element vector [A,F] where A is semimajor axis (in
%	meters)	and F is flattening of the user-defined ellipsoid.
%
%	LL2UTM(...,ZONE) forces the UTM ZONE (scalar integer or same size as
%   LAT and LON) instead of automatic set.
%
%	[X,Y,ZONE]=LL2UTM(...) returns also the computed UTM ZONE (negative
%	value for southern hemisphere points).
%
%
%	XY=LL2UTM(...) or without any output argument returns a 2-column 
%	matrix [X,Y].
%
%	Note:
%		- LL2UTM does not perform cross-datum conversion.
%		- precision is near a millimeter.
%
%
%	Reference:
%		I.G.N., Projection cartographique Mercator Transverse: Algorithmes,
%		   Notes Techniques NT/G 76, janvier 1995.
%
%	Acknowledgments: Mathieu, Frederic Christen.
%
%
%	Author: Francois Beauducel, <beauducel@ipgp.fr>
%	Created: 2003-12-02
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

% constants
D0 = 180/pi;	% conversion rad to deg
K0 = 0.9996;	% UTM scale factor
X0 = 500000;	% UTM false East (m)

% defaults
datum = 'wgs84';
zone = [];

if nargin < 1
	error('Not enough input arguments.')
end

if nargin > 1 && isnumeric(varargin{1}) && isnumeric(varargin{2}) && all(size(varargin{1})==size(varargin{2}))
	lat = varargin{1};
	lon = varargin{2};
	v = 2;
elseif isnumeric(varargin{1}) && size(varargin{1},2) == 2
	lat = varargin{1}(:,1);
	lon = varargin{1}(:,2);
	v = 1;
else
	error('Single input argument must be a 2-column matrix [LAT,LON].')
end

if all([numel(lat),numel(lon)] > 1) && any(size(lat) ~= size(lon))
	error('LAT and LON must be the same size or scalars.')
end

for n = (v+1):nargin
	% LL2UTM(...,DATUM)
	if ischar(varargin{n}) || (isnumeric(varargin{n}) && numel(varargin{n})==2)
		datum = varargin{n};
	% LL2UTM(...,ZONE)
	elseif isnumeric(varargin{n}) && (isscalar(varargin{n}) || all(size(varargin{n})==size(lat)))
			zone = round(varargin{n});
	else
		error('Unknown argument #%d. See documentation.',n)
	end
end

if ischar(datum)
	% LL2UTM(...,DATUM) with DATUM as char
	if ~any(strcmpi(datum,datums(:,1)))
		error('Unkown DATUM name "%s"',datum);
	end
	k = find(strcmpi(datum,datums(:,1)));
	A1 = datums{k,2};
	F1 = datums{k,3};	
else
	% LL2UTM(...,DATUM) with DATUM as [A,F] user-defined
	A1 = datum(1);
	F1 = datum(2);
end

p1 = lat/D0;			% Phi = Latitude (rad)
l1 = lon/D0;			% Lambda = Longitude (rad)

% UTM zone automatic setting
if isempty(zone)
	F0 = round((l1*D0 + 183)/6);
else
	F0 = zone;
end

B1 = A1*(1 - 1/F1);
E1 = sqrt((A1*A1 - B1*B1)/(A1*A1));
P0 = 0/D0;
L0 = (6*F0 - 183)/D0;	% UTM origin longitude (rad)
Y0 = 1e7*(p1 < 0);		% UTM false northern (m)
N = K0*A1;

C = coef(E1,0);
B = C(1)*P0 + C(2)*sin(2*P0) + C(3)*sin(4*P0) + C(4)*sin(6*P0) + C(5)*sin(8*P0);
YS = Y0 - N*B;

C = coef(E1,2);
L = log(tan(pi/4 + p1/2).*(((1 - E1*sin(p1))./(1 + E1*sin(p1))).^(E1/2)));
z = complex(atan(sinh(L)./cos(l1 - L0)),log(tan(pi/4 + asin(sin(l1 - L0)./cosh(L))/2)));
Z = N.*C(1).*z + N.*(C(2)*sin(2*z) + C(3)*sin(4*z) + C(4)*sin(6*z) + C(5)*sin(8*z));
xs = imag(Z) + X0;
ys = real(Z) + YS;

% outputs zone if needed: scalar value if unique, or vector/matrix of the
% same size as x/y in case of crossed zones
if nargout > 2
   	f = F0.*sign(lat);
	fu = unique(f);
	if isscalar(fu)
		f = fu;
	end
end

if nargout < 2
	x = [xs(:),ys(:)];
else
	x = xs;
	y = ys;
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
