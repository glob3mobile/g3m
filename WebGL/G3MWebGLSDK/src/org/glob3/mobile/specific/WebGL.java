

package org.glob3.mobile.specific;



public final class WebGL {
   //
   //   private final int                    numIndices = 0;
   //   private int[]                        indices;
   //
   //   public static List<JavaScriptObject> programList;
   //
   //
   //   public static native void jsInitGL(JavaScriptObject canvas) /*-{
   //
   //		$wnd.gl = canvas.getContext("experimental-webgl");
   //		$wnd.gl.viewportWidth = canvas.width;
   //		$wnd.gl.viewportHeight = canvas.height;
   //		if (!$wnd.gl) {
   //			alert("Could not initialise WebGL");
   //		}
   //
   //		//Setting clear color and depth test enabling
   //		$wnd.gl.clearColor(0.6, 0.6, 0.6, 1.0);
   //		$wnd.gl.enable($wnd.gl.DEPTH_TEST);
   //		$wnd.gl.enable($wnd.gl.CULL_FACE);
   //   }-*/;
   //
   //
   //   public static native void jsLoadWebGlUtils() /*-{
   //		$wnd.WebGLUtils = function() {
   //			var makeFailHTML = function(msg) {
   //				return ''
   //						+ '<table style="background-color: #8CE; width: 100%; height: 100%;"><tr>'
   //						+ '<td align="center">'
   //						+ '<div style="display: table-cell; vertical-align: middle;">'
   //						+ '<div style="">' + msg + '</div>' + '</div>'
   //						+ '</td></tr></table>';
   //			};
   //
   //			var GET_A_WEBGL_BROWSER = ''
   //					+ 'This page requires a browser that supports WebGL.<br/>'
   //					+ '<a href="http://get.webgl.org">Click here to upgrade your browser.</a>';
   //
   //			var OTHER_PROBLEM = ''
   //					+ "It doesn't appear your computer can support WebGL.<br/>"
   //					+ '<a href="http://get.webgl.org/troubleshooting/">Click here for more information.</a>';
   //
   //			var setupWebGL = function(canvas, opt_attribs, opt_onError) {
   //				function handleCreationError(msg) {
   //					var container = canvas.parentNode;
   //					if (container) {
   //						var str = window.WebGLRenderingContext ? OTHER_PROBLEM
   //								: GET_A_WEBGL_BROWSER;
   //						if (msg) {
   //							str += "<br/><br/>Status: " + msg;
   //						}
   //						container.innerHTML = makeFailHTML(str);
   //					}
   //				}
   //				;
   //
   //				opt_onError = opt_onError || handleCreationError;
   //
   //				if (canvas.addEventListener) {
   //					canvas.addEventListener("webglcontextcreationerror",
   //							function(event) {
   //								opt_onError(event.statusMessage);
   //							}, false);
   //				}
   //
   //				var context = create3DContext(canvas, opt_attribs);
   //
   //				if (!context) {
   //					if (!window.WebGLRenderingContext) {
   //						opt_onError("");
   //					}
   //				}
   //				return context;
   //			};
   //
   //			var create3DContext = function(canvas, opt_attribs) {
   //				var names = [ "webgl", "experimental-webgl", "webkit-3d",
   //						"moz-webgl" ];
   //				var context = null;
   //				for ( var ii = 0; ii < names.length; ++ii) {
   //					try {
   //						context = canvas.getContext(names[ii], opt_attribs);
   //					} catch (e) {
   //					}
   //					if (context) {
   //						break;
   //					}
   //				}
   //				return context;
   //			}
   //
   //			return {
   //				create3DContext : create3DContext,
   //				setupWebGL : setupWebGL
   //			};
   //		}();
   //
   //		$wnd.requestAnimFrame = (function() {
   //			return $wnd.requestAnimationFrame
   //					|| $wnd.webkitRequestAnimationFrame
   //					|| $wnd.mozRequestAnimationFrame
   //					|| $wnd.oRequestAnimationFrame
   //					|| $wnd.msRequestAnimationFrame
   //					|| function(callback, element) {
   //						$wnd.setTimeout(callback, 1000 / 60);
   //					};
   //		})();
   //   }-*/;
   //
   //
   //   // Enables matrix manipulation functions
   //   public static native void jsLoadGlMatrix() /*-{
   //		var glMatrixArrayType = typeof Float32Array != "undefined" ? Float32Array
   //				: typeof WebGLFloatArray != "undefined" ? WebGLFloatArray
   //						: Array;
   //		$wnd.vec3 = {};
   //		$wnd.vec3.create = function(a) {
   //			var b = new glMatrixArrayType(3);
   //			if (a) {
   //				b[0] = a[0];
   //				b[1] = a[1];
   //				b[2] = a[2]
   //			}
   //			return b
   //		};
   //		$wnd.vec3.set = function(a, b) {
   //			b[0] = a[0];
   //			b[1] = a[1];
   //			b[2] = a[2];
   //			return b
   //		};
   //		$wnd.vec3.add = function(a, b, c) {
   //			if (!c || a == c) {
   //				a[0] += b[0];
   //				a[1] += b[1];
   //				a[2] += b[2];
   //				return a
   //			}
   //			c[0] = a[0] + b[0];
   //			c[1] = a[1] + b[1];
   //			c[2] = a[2] + b[2];
   //			return c
   //		};
   //		$wnd.vec3.subtract = function(a, b, c) {
   //			if (!c || a == c) {
   //				a[0] -= b[0];
   //				a[1] -= b[1];
   //				a[2] -= b[2];
   //				return a
   //			}
   //			c[0] = a[0] - b[0];
   //			c[1] = a[1] - b[1];
   //			c[2] = a[2] - b[2];
   //			return c
   //		};
   //		$wnd.vec3.negate = function(a, b) {
   //			b || (b = a);
   //			b[0] = -a[0];
   //			b[1] = -a[1];
   //			b[2] = -a[2];
   //			return b
   //		};
   //		$wnd.vec3.scale = function(a, b, c) {
   //			if (!c || a == c) {
   //				a[0] *= b;
   //				a[1] *= b;
   //				a[2] *= b;
   //				return a
   //			}
   //			c[0] = a[0] * b;
   //			c[1] = a[1] * b;
   //			c[2] = a[2] * b;
   //			return c
   //		};
   //		$wnd.vec3.normalize = function(a, b) {
   //			b || (b = a);
   //			var c = a[0], d = a[1], e = a[2], g = Math.sqrt(c * c + d * d + e
   //					* e);
   //			if (g) {
   //				if (g == 1) {
   //					b[0] = c;
   //					b[1] = d;
   //					b[2] = e;
   //					return b
   //				}
   //			} else {
   //				b[0] = 0;
   //				b[1] = 0;
   //				b[2] = 0;
   //				return b
   //			}
   //			g = 1 / g;
   //			b[0] = c * g;
   //			b[1] = d * g;
   //			b[2] = e * g;
   //			return b
   //		};
   //		$wnd.vec3.cross = function(a, b, c) {
   //			c || (c = a);
   //			var d = a[0], e = a[1];
   //			a = a[2];
   //			var g = b[0], f = b[1];
   //			b = b[2];
   //			c[0] = e * b - a * f;
   //			c[1] = a * g - d * b;
   //			c[2] = d * f - e * g;
   //			return c
   //		};
   //		$wnd.vec3.length = function(a) {
   //			var b = a[0], c = a[1];
   //			a = a[2];
   //			return Math.sqrt(b * b + c * c + a * a)
   //		};
   //		$wnd.vec3.dot = function(a, b) {
   //			return a[0] * b[0] + a[1] * b[1] + a[2] * b[2]
   //		};
   //		$wnd.vec3.direction = function(a, b, c) {
   //			c || (c = a);
   //			var d = a[0] - b[0], e = a[1] - b[1];
   //			a = a[2] - b[2];
   //			b = Math.sqrt(d * d + e * e + a * a);
   //			if (!b) {
   //				c[0] = 0;
   //				c[1] = 0;
   //				c[2] = 0;
   //				return c
   //			}
   //			b = 1 / b;
   //			c[0] = d * b;
   //			c[1] = e * b;
   //			c[2] = a * b;
   //			return c
   //		};
   //		$wnd.vec3.lerp = function(a, b, c, d) {
   //			d || (d = a);
   //			d[0] = a[0] + c * (b[0] - a[0]);
   //			d[1] = a[1] + c * (b[1] - a[1]);
   //			d[2] = a[2] + c * (b[2] - a[2]);
   //			return d
   //		};
   //		$wnd.vec3.str = function(a) {
   //			return "[" + a[0] + ", " + a[1] + ", " + a[2] + "]"
   //		};
   //		$wnd.mat3 = {};
   //		$wnd.mat3.create = function(a) {
   //			var b = new glMatrixArrayType(9);
   //			if (a) {
   //				b[0] = a[0];
   //				b[1] = a[1];
   //				b[2] = a[2];
   //				b[3] = a[3];
   //				b[4] = a[4];
   //				b[5] = a[5];
   //				b[6] = a[6];
   //				b[7] = a[7];
   //				b[8] = a[8];
   //				b[9] = a[9]
   //			}
   //			return b
   //		};
   //		$wnd.mat3.set = function(a, b) {
   //			b[0] = a[0];
   //			b[1] = a[1];
   //			b[2] = a[2];
   //			b[3] = a[3];
   //			b[4] = a[4];
   //			b[5] = a[5];
   //			b[6] = a[6];
   //			b[7] = a[7];
   //			b[8] = a[8];
   //			return b
   //		};
   //		$wnd.mat3.identity = function(a) {
   //			a[0] = 1;
   //			a[1] = 0;
   //			a[2] = 0;
   //			a[3] = 0;
   //			a[4] = 1;
   //			a[5] = 0;
   //			a[6] = 0;
   //			a[7] = 0;
   //			a[8] = 1;
   //			return a
   //		};
   //		$wnd.mat3.transpose = function(a, b) {
   //			if (!b || a == b) {
   //				var c = a[1], d = a[2], e = a[5];
   //				a[1] = a[3];
   //				a[2] = a[6];
   //				a[3] = c;
   //				a[5] = a[7];
   //				a[6] = d;
   //				a[7] = e;
   //				return a
   //			}
   //			b[0] = a[0];
   //			b[1] = a[3];
   //			b[2] = a[6];
   //			b[3] = a[1];
   //			b[4] = a[4];
   //			b[5] = a[7];
   //			b[6] = a[2];
   //			b[7] = a[5];
   //			b[8] = a[8];
   //			return b
   //		};
   //		$wnd.mat3.toMat4 = function(a, b) {
   //			b || (b = mat4.create());
   //			b[0] = a[0];
   //			b[1] = a[1];
   //			b[2] = a[2];
   //			b[3] = 0;
   //			b[4] = a[3];
   //			b[5] = a[4];
   //			b[6] = a[5];
   //			b[7] = 0;
   //			b[8] = a[6];
   //			b[9] = a[7];
   //			b[10] = a[8];
   //			b[11] = 0;
   //			b[12] = 0;
   //			b[13] = 0;
   //			b[14] = 0;
   //			b[15] = 1;
   //			return b
   //		};
   //		$wnd.mat3.str = function(a) {
   //			return "[" + a[0] + ", " + a[1] + ", " + a[2] + ", " + a[3] + ", "
   //					+ a[4] + ", " + a[5] + ", " + a[6] + ", " + a[7] + ", "
   //					+ a[8] + "]"
   //		};
   //		$wnd.mat4 = {};
   //		$wnd.mat4.create = function(a) {
   //			var b = new glMatrixArrayType(16);
   //			if (a) {
   //				b[0] = a[0];
   //				b[1] = a[1];
   //				b[2] = a[2];
   //				b[3] = a[3];
   //				b[4] = a[4];
   //				b[5] = a[5];
   //				b[6] = a[6];
   //				b[7] = a[7];
   //				b[8] = a[8];
   //				b[9] = a[9];
   //				b[10] = a[10];
   //				b[11] = a[11];
   //				b[12] = a[12];
   //				b[13] = a[13];
   //				b[14] = a[14];
   //				b[15] = a[15]
   //			}
   //			return b
   //		};
   //		$wnd.mat4.set = function(a, b) {
   //			b[0] = a[0];
   //			b[1] = a[1];
   //			b[2] = a[2];
   //			b[3] = a[3];
   //			b[4] = a[4];
   //			b[5] = a[5];
   //			b[6] = a[6];
   //			b[7] = a[7];
   //			b[8] = a[8];
   //			b[9] = a[9];
   //			b[10] = a[10];
   //			b[11] = a[11];
   //			b[12] = a[12];
   //			b[13] = a[13];
   //			b[14] = a[14];
   //			b[15] = a[15];
   //			return b
   //		};
   //		$wnd.mat4.identity = function(a) {
   //			a[0] = 1;
   //			a[1] = 0;
   //			a[2] = 0;
   //			a[3] = 0;
   //			a[4] = 0;
   //			a[5] = 1;
   //			a[6] = 0;
   //			a[7] = 0;
   //			a[8] = 0;
   //			a[9] = 0;
   //			a[10] = 1;
   //			a[11] = 0;
   //			a[12] = 0;
   //			a[13] = 0;
   //			a[14] = 0;
   //			a[15] = 1;
   //			return a
   //		};
   //		$wnd.mat4.transpose = function(a, b) {
   //			if (!b || a == b) {
   //				var c = a[1], d = a[2], e = a[3], g = a[6], f = a[7], h = a[11];
   //				a[1] = a[4];
   //				a[2] = a[8];
   //				a[3] = a[12];
   //				a[4] = c;
   //				a[6] = a[9];
   //				a[7] = a[13];
   //				a[8] = d;
   //				a[9] = g;
   //				a[11] = a[14];
   //				a[12] = e;
   //				a[13] = f;
   //				a[14] = h;
   //				return a
   //			}
   //			b[0] = a[0];
   //			b[1] = a[4];
   //			b[2] = a[8];
   //			b[3] = a[12];
   //			b[4] = a[1];
   //			b[5] = a[5];
   //			b[6] = a[9];
   //			b[7] = a[13];
   //			b[8] = a[2];
   //			b[9] = a[6];
   //			b[10] = a[10];
   //			b[11] = a[14];
   //			b[12] = a[3];
   //			b[13] = a[7];
   //			b[14] = a[11];
   //			b[15] = a[15];
   //			return b
   //		};
   //		$wnd.mat4.determinant = function(a) {
   //			var b = a[0], c = a[1], d = a[2], e = a[3], g = a[4], f = a[5], h = a[6], i = a[7], j = a[8], k = a[9], l = a[10], o = a[11], m = a[12], n = a[13], p = a[14];
   //			a = a[15];
   //			return m * k * h * e - j * n * h * e - m * f * l * e + g * n * l
   //					* e + j * f * p * e - g * k * p * e - m * k * d * i + j * n
   //					* d * i + m * c * l * i - b * n * l * i - j * c * p * i + b
   //					* k * p * i + m * f * d * o - g * n * d * o - m * c * h * o
   //					+ b * n * h * o + g * c * p * o - b * f * p * o - j * f * d
   //					* a + g * k * d * a + j * c * h * a - b * k * h * a - g * c
   //					* l * a + b * f * l * a
   //		};
   //		$wnd.mat4.inverse = function(a, b) {
   //			b || (b = a);
   //			var c = a[0], d = a[1], e = a[2], g = a[3], f = a[4], h = a[5], i = a[6], j = a[7], k = a[8], l = a[9], o = a[10], m = a[11], n = a[12], p = a[13], r = a[14], s = a[15], A = c
   //					* h - d * f, B = c * i - e * f, t = c * j - g * f, u = d
   //					* i - e * h, v = d * j - g * h, w = e * j - g * i, x = k
   //					* p - l * n, y = k * r - o * n, z = k * s - m * n, C = l
   //					* r - o * p, D = l * s - m * p, E = o * s - m * r, q = 1 / (A
   //					* E - B * D + t * C + u * z - v * y + w * x);
   //			b[0] = (h * E - i * D + j * C) * q;
   //			b[1] = (-d * E + e * D - g * C) * q;
   //			b[2] = (p * w - r * v + s * u) * q;
   //			b[3] = (-l * w + o * v - m * u) * q;
   //			b[4] = (-f * E + i * z - j * y) * q;
   //			b[5] = (c * E - e * z + g * y) * q;
   //			b[6] = (-n * w + r * t - s * B) * q;
   //			b[7] = (k * w - o * t + m * B) * q;
   //			b[8] = (f * D - h * z + j * x) * q;
   //			b[9] = (-c * D + d * z - g * x) * q;
   //			b[10] = (n * v - p * t + s * A) * q;
   //			b[11] = (-k * v + l * t - m * A) * q;
   //			b[12] = (-f * C + h * y - i * x) * q;
   //			b[13] = (c * C - d * y + e * x) * q;
   //			b[14] = (-n * u + p * B - r * A) * q;
   //			b[15] = (k * u - l * B + o * A) * q;
   //			return b
   //		};
   //		$wnd.mat4.toRotationMat = function(a, b) {
   //			b || (b = mat4.create());
   //			b[0] = a[0];
   //			b[1] = a[1];
   //			b[2] = a[2];
   //			b[3] = a[3];
   //			b[4] = a[4];
   //			b[5] = a[5];
   //			b[6] = a[6];
   //			b[7] = a[7];
   //			b[8] = a[8];
   //			b[9] = a[9];
   //			b[10] = a[10];
   //			b[11] = a[11];
   //			b[12] = 0;
   //			b[13] = 0;
   //			b[14] = 0;
   //			b[15] = 1;
   //			return b
   //		};
   //		$wnd.mat4.toMat3 = function(a, b) {
   //			b || (b = mat3.create());
   //			b[0] = a[0];
   //			b[1] = a[1];
   //			b[2] = a[2];
   //			b[3] = a[4];
   //			b[4] = a[5];
   //			b[5] = a[6];
   //			b[6] = a[8];
   //			b[7] = a[9];
   //			b[8] = a[10];
   //			return b
   //		};
   //		$wnd.mat4.toInverseMat3 = function(a, b) {
   //			var c = a[0], d = a[1], e = a[2], g = a[4], f = a[5], h = a[6], i = a[8], j = a[9], k = a[10], l = k
   //					* f - h * j, o = -k * g + h * i, m = j * g - f * i, n = c
   //					* l + d * o + e * m;
   //			if (!n)
   //				return null;
   //			n = 1 / n;
   //			b || (b = mat3.create());
   //			b[0] = l * n;
   //			b[1] = (-k * d + e * j) * n;
   //			b[2] = (h * d - e * f) * n;
   //			b[3] = o * n;
   //			b[4] = (k * c - e * i) * n;
   //			b[5] = (-h * c + e * g) * n;
   //			b[6] = m * n;
   //			b[7] = (-j * c + d * i) * n;
   //			b[8] = (f * c - d * g) * n;
   //			return b
   //		};
   //		$wnd.mat4.multiply = function(a, b, c) {
   //			c || (c = a);
   //			var d = a[0], e = a[1], g = a[2], f = a[3], h = a[4], i = a[5], j = a[6], k = a[7], l = a[8], o = a[9], m = a[10], n = a[11], p = a[12], r = a[13], s = a[14];
   //			a = a[15];
   //			var A = b[0], B = b[1], t = b[2], u = b[3], v = b[4], w = b[5], x = b[6], y = b[7], z = b[8], C = b[9], D = b[10], E = b[11], q = b[12], F = b[13], G = b[14];
   //			b = b[15];
   //			c[0] = A * d + B * h + t * l + u * p;
   //			c[1] = A * e + B * i + t * o + u * r;
   //			c[2] = A * g + B * j + t * m + u * s;
   //			c[3] = A * f + B * k + t * n + u * a;
   //			c[4] = v * d + w * h + x * l + y * p;
   //			c[5] = v * e + w * i + x * o + y * r;
   //			c[6] = v * g + w * j + x * m + y * s;
   //			c[7] = v * f + w * k + x * n + y * a;
   //			c[8] = z * d + C * h + D * l + E * p;
   //			c[9] = z * e + C * i + D * o + E * r;
   //			c[10] = z * g + C * j + D * m + E * s;
   //			c[11] = z * f + C * k + D * n + E * a;
   //			c[12] = q * d + F * h + G * l + b * p;
   //			c[13] = q * e + F * i + G * o + b * r;
   //			c[14] = q * g + F * j + G * m + b * s;
   //			c[15] = q * f + F * k + G * n + b * a;
   //			return c
   //		};
   //		$wnd.mat4.multiplyVec3 = function(a, b, c) {
   //			c || (c = b);
   //			var d = b[0], e = b[1];
   //			b = b[2];
   //			c[0] = a[0] * d + a[4] * e + a[8] * b + a[12];
   //			c[1] = a[1] * d + a[5] * e + a[9] * b + a[13];
   //			c[2] = a[2] * d + a[6] * e + a[10] * b + a[14];
   //			return c
   //		};
   //		$wnd.mat4.multiplyVec4 = function(a, b, c) {
   //			c || (c = b);
   //			var d = b[0], e = b[1], g = b[2];
   //			b = b[3];
   //			c[0] = a[0] * d + a[4] * e + a[8] * g + a[12] * b;
   //			c[1] = a[1] * d + a[5] * e + a[9] * g + a[13] * b;
   //			c[2] = a[2] * d + a[6] * e + a[10] * g + a[14] * b;
   //			c[3] = a[3] * d + a[7] * e + a[11] * g + a[15] * b;
   //			return c
   //		};
   //		$wnd.mat4.translate = function(a, b, c) {
   //			var d = b[0], e = b[1];
   //			b = b[2];
   //			if (!c || a == c) {
   //				a[12] = a[0] * d + a[4] * e + a[8] * b + a[12];
   //				a[13] = a[1] * d + a[5] * e + a[9] * b + a[13];
   //				a[14] = a[2] * d + a[6] * e + a[10] * b + a[14];
   //				a[15] = a[3] * d + a[7] * e + a[11] * b + a[15];
   //				return a
   //			}
   //			var g = a[0], f = a[1], h = a[2], i = a[3], j = a[4], k = a[5], l = a[6], o = a[7], m = a[8], n = a[9], p = a[10], r = a[11];
   //			c[0] = g;
   //			c[1] = f;
   //			c[2] = h;
   //			c[3] = i;
   //			c[4] = j;
   //			c[5] = k;
   //			c[6] = l;
   //			c[7] = o;
   //			c[8] = m;
   //			c[9] = n;
   //			c[10] = p;
   //			c[11] = r;
   //			c[12] = g * d + j * e + m * b + a[12];
   //			c[13] = f * d + k * e + n * b + a[13];
   //			c[14] = h * d + l * e + p * b + a[14];
   //			c[15] = i * d + o * e + r * b + a[15];
   //			return c
   //		};
   //		$wnd.mat4.scale = function(a, b, c) {
   //			var d = b[0], e = b[1];
   //			b = b[2];
   //			if (!c || a == c) {
   //				a[0] *= d;
   //				a[1] *= d;
   //				a[2] *= d;
   //				a[3] *= d;
   //				a[4] *= e;
   //				a[5] *= e;
   //				a[6] *= e;
   //				a[7] *= e;
   //				a[8] *= b;
   //				a[9] *= b;
   //				a[10] *= b;
   //				a[11] *= b;
   //				return a
   //			}
   //			c[0] = a[0] * d;
   //			c[1] = a[1] * d;
   //			c[2] = a[2] * d;
   //			c[3] = a[3] * d;
   //			c[4] = a[4] * e;
   //			c[5] = a[5] * e;
   //			c[6] = a[6] * e;
   //			c[7] = a[7] * e;
   //			c[8] = a[8] * b;
   //			c[9] = a[9] * b;
   //			c[10] = a[10] * b;
   //			c[11] = a[11] * b;
   //			c[12] = a[12];
   //			c[13] = a[13];
   //			c[14] = a[14];
   //			c[15] = a[15];
   //			return c
   //		};
   //		$wnd.mat4.rotate = function(a, b, c, d) {
   //			var e = c[0], g = c[1];
   //			c = c[2];
   //			var f = Math.sqrt(e * e + g * g + c * c);
   //			if (!f)
   //				return null;
   //			if (f != 1) {
   //				f = 1 / f;
   //				e *= f;
   //				g *= f;
   //				c *= f
   //			}
   //			var h = Math.sin(b), i = Math.cos(b), j = 1 - i;
   //			b = a[0];
   //			f = a[1];
   //			var k = a[2], l = a[3], o = a[4], m = a[5], n = a[6], p = a[7], r = a[8], s = a[9], A = a[10], B = a[11], t = e
   //					* e * j + i, u = g * e * j + c * h, v = c * e * j - g * h, w = e
   //					* g * j - c * h, x = g * g * j + i, y = c * g * j + e * h, z = e
   //					* c * j + g * h;
   //			e = g * c * j - e * h;
   //			g = c * c * j + i;
   //			if (d) {
   //				if (a != d) {
   //					d[12] = a[12];
   //					d[13] = a[13];
   //					d[14] = a[14];
   //					d[15] = a[15]
   //				}
   //			} else
   //				d = a;
   //			d[0] = b * t + o * u + r * v;
   //			d[1] = f * t + m * u + s * v;
   //			d[2] = k * t + n * u + A * v;
   //			d[3] = l * t + p * u + B * v;
   //			d[4] = b * w + o * x + r * y;
   //			d[5] = f * w + m * x + s * y;
   //			d[6] = k * w + n * x + A * y;
   //			d[7] = l * w + p * x + B * y;
   //			d[8] = b * z + o * e + r * g;
   //			d[9] = f * z + m * e + s * g;
   //			d[10] = k * z + n * e + A * g;
   //			d[11] = l * z + p * e + B * g;
   //			return d
   //		};
   //		$wnd.mat4.rotateX = function(a, b, c) {
   //			var d = Math.sin(b);
   //			b = Math.cos(b);
   //			var e = a[4], g = a[5], f = a[6], h = a[7], i = a[8], j = a[9], k = a[10], l = a[11];
   //			if (c) {
   //				if (a != c) {
   //					c[0] = a[0];
   //					c[1] = a[1];
   //					c[2] = a[2];
   //					c[3] = a[3];
   //					c[12] = a[12];
   //					c[13] = a[13];
   //					c[14] = a[14];
   //					c[15] = a[15]
   //				}
   //			} else
   //				c = a;
   //			c[4] = e * b + i * d;
   //			c[5] = g * b + j * d;
   //			c[6] = f * b + k * d;
   //			c[7] = h * b + l * d;
   //			c[8] = e * -d + i * b;
   //			c[9] = g * -d + j * b;
   //			c[10] = f * -d + k * b;
   //			c[11] = h * -d + l * b;
   //			return c
   //		};
   //		$wnd.mat4.rotateY = function(a, b, c) {
   //			var d = Math.sin(b);
   //			b = Math.cos(b);
   //			var e = a[0], g = a[1], f = a[2], h = a[3], i = a[8], j = a[9], k = a[10], l = a[11];
   //			if (c) {
   //				if (a != c) {
   //					c[4] = a[4];
   //					c[5] = a[5];
   //					c[6] = a[6];
   //					c[7] = a[7];
   //					c[12] = a[12];
   //					c[13] = a[13];
   //					c[14] = a[14];
   //					c[15] = a[15]
   //				}
   //			} else
   //				c = a;
   //			c[0] = e * b + i * -d;
   //			c[1] = g * b + j * -d;
   //			c[2] = f * b + k * -d;
   //			c[3] = h * b + l * -d;
   //			c[8] = e * d + i * b;
   //			c[9] = g * d + j * b;
   //			c[10] = f * d + k * b;
   //			c[11] = h * d + l * b;
   //			return c
   //		};
   //		$wnd.mat4.rotateZ = function(a, b, c) {
   //			var d = Math.sin(b);
   //			b = Math.cos(b);
   //			var e = a[0], g = a[1], f = a[2], h = a[3], i = a[4], j = a[5], k = a[6], l = a[7];
   //			if (c) {
   //				if (a != c) {
   //					c[8] = a[8];
   //					c[9] = a[9];
   //					c[10] = a[10];
   //					c[11] = a[11];
   //					c[12] = a[12];
   //					c[13] = a[13];
   //					c[14] = a[14];
   //					c[15] = a[15]
   //				}
   //			} else
   //				c = a;
   //			c[0] = e * b + i * d;
   //			c[1] = g * b + j * d;
   //			c[2] = f * b + k * d;
   //			c[3] = h * b + l * d;
   //			c[4] = e * -d + i * b;
   //			c[5] = g * -d + j * b;
   //			c[6] = f * -d + k * b;
   //			c[7] = h * -d + l * b;
   //			return c
   //		};
   //		$wnd.mat4.frustum = function(a, b, c, d, e, g, f) {
   //			f || (f = mat4.create());
   //			var h = b - a, i = d - c, j = g - e;
   //			f[0] = e * 2 / h;
   //			f[1] = 0;
   //			f[2] = 0;
   //			f[3] = 0;
   //			f[4] = 0;
   //			f[5] = e * 2 / i;
   //			f[6] = 0;
   //			f[7] = 0;
   //			f[8] = (b + a) / h;
   //			f[9] = (d + c) / i;
   //			f[10] = -(g + e) / j;
   //			f[11] = -1;
   //			f[12] = 0;
   //			f[13] = 0;
   //			f[14] = -(g * e * 2) / j;
   //			f[15] = 0;
   //			return f
   //		};
   //		$wnd.mat4.perspective = function(a, b, c, d, e) {
   //			a = c * Math.tan(a * Math.PI / 360);
   //			b = a * b;
   //			return mat4.frustum(-b, b, -a, a, c, d, e)
   //		};
   //		$wnd.mat4.ortho = function(a, b, c, d, e, g, f) {
   //			f || (f = mat4.create());
   //			var h = b - a, i = d - c, j = g - e;
   //			f[0] = 2 / h;
   //			f[1] = 0;
   //			f[2] = 0;
   //			f[3] = 0;
   //			f[4] = 0;
   //			f[5] = 2 / i;
   //			f[6] = 0;
   //			f[7] = 0;
   //			f[8] = 0;
   //			f[9] = 0;
   //			f[10] = -2 / j;
   //			f[11] = 0;
   //			f[12] = -(a + b) / h;
   //			f[13] = -(d + c) / i;
   //			f[14] = -(g + e) / j;
   //			f[15] = 1;
   //			return f
   //		};
   //		$wnd.mat4.lookAt = function(a, b, c, d) {
   //			d || (d = mat4.create());
   //			var e = a[0], g = a[1];
   //			a = a[2];
   //			var f = c[0], h = c[1], i = c[2];
   //			c = b[1];
   //			var j = b[2];
   //			if (e == b[0] && g == c && a == j)
   //				return mat4.identity(d);
   //			var k, l, o, m;
   //			c = e - b[0];
   //			j = g - b[1];
   //			b = a - b[2];
   //			m = 1 / Math.sqrt(c * c + j * j + b * b);
   //			c *= m;
   //			j *= m;
   //			b *= m;
   //			k = h * b - i * j;
   //			i = i * c - f * b;
   //			f = f * j - h * c;
   //			if (m = Math.sqrt(k * k + i * i + f * f)) {
   //				m = 1 / m;
   //				k *= m;
   //				i *= m;
   //				f *= m
   //			} else
   //				f = i = k = 0;
   //			h = j * f - b * i;
   //			l = b * k - c * f;
   //			o = c * i - j * k;
   //			if (m = Math.sqrt(h * h + l * l + o * o)) {
   //				m = 1 / m;
   //				h *= m;
   //				l *= m;
   //				o *= m
   //			} else
   //				o = l = h = 0;
   //			d[0] = k;
   //			d[1] = h;
   //			d[2] = c;
   //			d[3] = 0;
   //			d[4] = i;
   //			d[5] = l;
   //			d[6] = j;
   //			d[7] = 0;
   //			d[8] = f;
   //			d[9] = o;
   //			d[10] = b;
   //			d[11] = 0;
   //			d[12] = -(k * e + i * g + f * a);
   //			d[13] = -(h * e + l * g + o * a);
   //			d[14] = -(c * e + j * g + b * a);
   //			d[15] = 1;
   //			return d
   //		};
   //		$wnd.mat4.str = function(a) {
   //			return "[" + a[0] + ", " + a[1] + ", " + a[2] + ", " + a[3] + ", "
   //					+ a[4] + ", " + a[5] + ", " + a[6] + ", " + a[7] + ", "
   //					+ a[8] + ", " + a[9] + ", " + a[10] + ", " + a[11] + ", "
   //					+ a[12] + ", " + a[13] + ", " + a[14] + ", " + a[15] + "]"
   //		};
   //		$wnd.quat4 = {};
   //		$wnd.quat4.create = function(a) {
   //			var b = new glMatrixArrayType(4);
   //			if (a) {
   //				b[0] = a[0];
   //				b[1] = a[1];
   //				b[2] = a[2];
   //				b[3] = a[3]
   //			}
   //			return b
   //		};
   //		$wnd.quat4.set = function(a, b) {
   //			b[0] = a[0];
   //			b[1] = a[1];
   //			b[2] = a[2];
   //			b[3] = a[3];
   //			return b
   //		};
   //		$wnd.quat4.calculateW = function(a, b) {
   //			var c = a[0], d = a[1], e = a[2];
   //			if (!b || a == b) {
   //				a[3] = -Math.sqrt(Math.abs(1 - c * c - d * d - e * e));
   //				return a
   //			}
   //			b[0] = c;
   //			b[1] = d;
   //			b[2] = e;
   //			b[3] = -Math.sqrt(Math.abs(1 - c * c - d * d - e * e));
   //			return b
   //		};
   //		$wnd.quat4.inverse = function(a, b) {
   //			if (!b || a == b) {
   //				a[0] *= 1;
   //				a[1] *= 1;
   //				a[2] *= 1;
   //				return a
   //			}
   //			b[0] = -a[0];
   //			b[1] = -a[1];
   //			b[2] = -a[2];
   //			b[3] = a[3];
   //			return b
   //		};
   //		$wnd.quat4.length = function(a) {
   //			var b = a[0], c = a[1], d = a[2];
   //			a = a[3];
   //			return Math.sqrt(b * b + c * c + d * d + a * a)
   //		};
   //		$wnd.quat4.normalize = function(a, b) {
   //			b || (b = a);
   //			var c = a[0], d = a[1], e = a[2], g = a[3], f = Math.sqrt(c * c + d
   //					* d + e * e + g * g);
   //			if (f == 0) {
   //				b[0] = 0;
   //				b[1] = 0;
   //				b[2] = 0;
   //				b[3] = 0;
   //				return b
   //			}
   //			f = 1 / f;
   //			b[0] = c * f;
   //			b[1] = d * f;
   //			b[2] = e * f;
   //			b[3] = g * f;
   //			return b
   //		};
   //		$wnd.quat4.multiply = function(a, b, c) {
   //			c || (c = a);
   //			var d = a[0], e = a[1], g = a[2];
   //			a = a[3];
   //			var f = b[0], h = b[1], i = b[2];
   //			b = b[3];
   //			c[0] = d * b + a * f + e * i - g * h;
   //			c[1] = e * b + a * h + g * f - d * i;
   //			c[2] = g * b + a * i + d * h - e * f;
   //			c[3] = a * b - d * f - e * h - g * i;
   //			return c
   //		};
   //		$wnd.quat4.multiplyVec3 = function(a, b, c) {
   //			c || (c = b);
   //			var d = b[0], e = b[1], g = b[2];
   //			b = a[0];
   //			var f = a[1], h = a[2];
   //			a = a[3];
   //			var i = a * d + f * g - h * e, j = a * e + h * d - b * g, k = a * g
   //					+ b * e - f * d;
   //			d = -b * d - f * e - h * g;
   //			c[0] = i * a + d * -b + j * -h - k * -f;
   //			c[1] = j * a + d * -f + k * -b - i * -h;
   //			c[2] = k * a + d * -h + i * -f - j * -b;
   //			return c
   //		};
   //		$wnd.quat4.toMat3 = function(a, b) {
   //			b || (b = mat3.create());
   //			var c = a[0], d = a[1], e = a[2], g = a[3], f = c + c, h = d + d, i = e
   //					+ e, j = c * f, k = c * h;
   //			c = c * i;
   //			var l = d * h;
   //			d = d * i;
   //			e = e * i;
   //			f = g * f;
   //			h = g * h;
   //			g = g * i;
   //			b[0] = 1 - (l + e);
   //			b[1] = k - g;
   //			b[2] = c + h;
   //			b[3] = k + g;
   //			b[4] = 1 - (j + e);
   //			b[5] = d - f;
   //			b[6] = c - h;
   //			b[7] = d + f;
   //			b[8] = 1 - (j + l);
   //			return b
   //		};
   //		$wnd.quat4.toMat4 = function(a, b) {
   //			b || (b = mat4.create());
   //			var c = a[0], d = a[1], e = a[2], g = a[3], f = c + c, h = d + d, i = e
   //					+ e, j = c * f, k = c * h;
   //			c = c * i;
   //			var l = d * h;
   //			d = d * i;
   //			e = e * i;
   //			f = g * f;
   //			h = g * h;
   //			g = g * i;
   //			b[0] = 1 - (l + e);
   //			b[1] = k - g;
   //			b[2] = c + h;
   //			b[3] = 0;
   //			b[4] = k + g;
   //			b[5] = 1 - (j + e);
   //			b[6] = d - f;
   //			b[7] = 0;
   //			b[8] = c - h;
   //			b[9] = d + f;
   //			b[10] = 1 - (j + l);
   //			b[11] = 0;
   //			b[12] = 0;
   //			b[13] = 0;
   //			b[14] = 0;
   //			b[15] = 1;
   //			return b
   //		};
   //		$wnd.quat4.slerp = function(a, b, c, d) {
   //			d || (d = a);
   //			var e = c;
   //			if (a[0] * b[0] + a[1] * b[1] + a[2] * b[2] + a[3] * b[3] < 0)
   //				e = -1 * c;
   //			d[0] = 1 - c * a[0] + e * b[0];
   //			d[1] = 1 - c * a[1] + e * b[1];
   //			d[2] = 1 - c * a[2] + e * b[2];
   //			d[3] = 1 - c * a[3] + e * b[3];
   //			return d
   //		};
   //		$wnd.quat4.str = function(a) {
   //			return "[" + a[0] + ", " + a[1] + ", " + a[2] + ", " + a[3] + "]"
   //		};
   //   }-*/;
   //
   //
   //   // Shader code
   //   public static native void jsLoadShaderCode() /*-{
   //		$wnd.fragmentShader = "//																								\n"
   //				+ "//  Shader.fsh																					\n"
   //				+ "//  Prueba Opengl iPad																			\n"
   //				+ "//																								\n"
   //				+ "//  Created by Agustin Trujillo Pino on 12/01/11.												\n"
   //				+ "//  Copyright 2011 Universidad de Las Palmas. All rights reserved.								\n"
   //				+ "//																								\n"
   //				+ "																								\n"
   //				+ "varying mediump vec2 TextureCoordOut;															\n"
   //				+ "precision mediump float;                                                                                                                      \n"
   //				+ "																								\n"
   //				+ "uniform sampler2D Sampler1,Sampler2;																		\n"
   //				+ "uniform bool EnableTexture, Multiple;																	\n"
   //				+ "uniform lowp vec4 FlatColor;																	\n"
   //				+ "																								\n"
   //				+ "void main()																					\n"
   //				+ "{																								\n"
   //				+ "	if (EnableTexture) {																			\n"
   //				+ "		if(Multiple==false) {                                                                                                                                            \n"
   //				+ "                    gl_FragColor = texture2D (Sampler1, TextureCoordOut);									\n"
   //				+ "           }                                                                                                                                                                              \n"
   //				+ "         else {                                                                                                                                                                              \n"
   //				+ "                vec4 t1= texture2D (Sampler1, TextureCoordOut);                                                                                                                                                                       \n"
   //				+ "                vec4 t2= texture2D (Sampler2, TextureCoordOut);                                                                                     \n"
   //				+ "                 gl_FragColor=mix(t2,t1,t1.a);                                                                                                                           \n"
   //				+ "         }                                                                                                                                                                               \n"
   //				+ "  }                                                                                                                                                                                      \n"
   //				+ "	else {																						\n"
   //				+ "		gl_FragColor = FlatColor;																\n"
   //				+ "   }                                                                                                                                                                                         \n"
   //				+ "}																								\n";
   //
   //		$wnd.vertexShader = "//																								\n"
   //				+ "//  Shader.vsh																					\n"
   //				+ "//  Prueba Opengl iPad																			\n"
   //				+ "//																								\n"
   //				+ "//  Created by Agustin Trujillo Pino on 12/01/11.												\n"
   //				+ "//  Copyright 2011 Universidad de Las Palmas. All rights reserved.								\n"
   //				+ "//																								\n"
   //				+ "																								\n"
   //				+ "attribute vec4 Position;																		\n"
   //				+ "varying vec4 realPosition;																	\n"
   //				+ "attribute vec2 TextureCoord;																	\n"
   //				+ "																								\n"
   //				+ "varying vec2 TextureCoordOut;																	\n"
   //				+ "																								\n"
   //				+ "uniform mat4 Projection;																		\n"
   //				+ "uniform mat4 Modelview;																		\n"
   //				+ "uniform bool Enable3D;                                             							\n"
   //				+ "uniform bool BBoard;                                            							\n"
   //				+ "uniform float ViewPortRatio;                                            						\n"
   //				+ "uniform float charWidth;																		\n"
   //				+ "uniform float charStride;																		\n"
   //				+ "varying float cornerNumber;																	\n"
   //				+ "varying float xCoord;																			\n"
   //				+ "varying float yCoord;																			\n"
   //				+ "																								\n"
   //				+ "																								\n"
   //				+ "void main()																					\n"
   //				+ "{																								\n"
   //				+ "   if(!BBoard) {                                                                                                                                                                                          \n"
   //				+ "		gl_Position = Projection * Modelview * Position;									\n"
   //				+ "	        TextureCoordOut = TextureCoord;																\n"
   //				+ "   }                                                                                                                                                                                          \n"
   //				+ "   else {                                                                                                                                                                                          \n"
   //				+ "      gl_Position = Projection * Modelview * Position;                                                                                                         \n"
   //				+ "      gl_Position.x += (-0.025 + TextureCoord.x * 0.05)* gl_Position.w ;                                                                          \n"
   //				+ "      gl_Position.y -= (-0.025+ TextureCoord.y *0.05)* gl_Position.w * ViewPortRatio;                                                                       \n"
   //				+ "      TextureCoordOut = TextureCoord;                                                                                                                                      \n"
   //				+ "   }                                                                                                                                                                                          \n"
   //				+ "}																								\n";
   //
   //   }-*/;
   //
   //
   //   // Shader compiler function
   //   public static native JavaScriptObject jsCompileShader(String shaderContent,
   //                                                         String shaderType) /*-{
   //
   //		var shader;
   //		if (shaderType == "x-shader/x-fragment") {
   //			shader = $wnd.gl.createShader($wnd.gl.FRAGMENT_SHADER);
   //		} else if (shaderType == "x-shader/x-vertex") {
   //			shader = $wnd.gl.createShader($wnd.gl.VERTEX_SHADER);
   //		} else {
   //			return null;
   //		}
   //
   //		$wnd.gl.shaderSource(shader, shaderContent);
   //		$wnd.gl.compileShader(shader);
   //
   //		if (!$wnd.gl.getShaderParameter(shader, $wnd.gl.COMPILE_STATUS)) {
   //			alert("Error: " + $wnd.gl.getShaderInfoLog(shader));
   //			return null;
   //		}
   //
   //		return shader;
   //   }-*/;
   //
   //
   //   // Global variables loading
   //   public static native void loadGlobalVars() /*-{
   //		$wnd.shaderProgram = null;
   //		$wnd.mvMatrix = $wnd.mat4.create();
   //		$wnd.mat4.identity($wnd.mvMatrix);
   //		$wnd.pMatrix = $wnd.mat4.create();
   //		$wnd.mvMatrixStack = [];
   //		$wnd.textureStack = [];
   //		$wnd.numPendingPetitions = 0;
   //		$wnd.maxGPUTextures = 400;
   //		$wnd.numFreeTexIds = $wnd.maxGPUTextures;
   //		for (i = 0; i < $wnd.maxGPUTextures; i++)
   //			$wnd.textureStack[i] = null;
   //
   //		$wnd.vertexBuffer = null, $wnd.texBuffer = null,
   //				$wnd.floatArrayV = null, $wnd.floatArrayT = null;
   //
   //		$wnd.vertexIndexBuffer = null; //Indices for drawElements 
   //
   //   }-*/;
   //
   //
   //   // Program and shader compiling, uniform variables, attributes and buffers
   //   // creation
   //   public static native JavaScriptObject jsCreateNewProgram(String fragmentShaderId,
   //                                                            String vertexShaderId) /*-{
   //
   //		@org.glob3.mobile.specific.WebGL::jsLoadShaderCode()();
   //		var fragmentShader = @org.glob3.mobile.specific.WebGL::jsCompileShader(Ljava/lang/String;Ljava/lang/String;)($wnd.fragmentShader,"x-shader/x-fragment");
   //		var vertexShader = @org.glob3.mobile.specific.WebGL::jsCompileShader(Ljava/lang/String;Ljava/lang/String;)($wnd.vertexShader,"x-shader/x-vertex");
   //
   //		var newProgram = $wnd.gl.createProgram();
   //
   //		$wnd.gl.attachShader(newProgram, vertexShader);
   //		$wnd.gl.attachShader(newProgram, fragmentShader);
   //		$wnd.gl.linkProgram(newProgram);
   //
   //		if (!$wnd.gl.getProgramParameter(newProgram, $wnd.gl.LINK_STATUS))
   //			return null;
   //
   //		//Creaci�n de punteros a las variables uniformes
   //		newProgram.Projection = $wnd.gl.getUniformLocation(newProgram,
   //				"Projection");
   //		newProgram.Modelview = $wnd.gl.getUniformLocation(newProgram,
   //				"Modelview");
   //		newProgram.FlatColor = $wnd.gl.getUniformLocation(newProgram,
   //				"FlatColor");
   //		newProgram.EnableTexture = $wnd.gl.getUniformLocation(newProgram,
   //				"EnableTexture");
   //		newProgram.Multiple = $wnd.gl
   //				.getUniformLocation(newProgram, "Multiple");
   //		newProgram.Sampler1 = $wnd.gl
   //				.getUniformLocation(newProgram, "Sampler1");
   //		newProgram.Sampler2 = $wnd.gl
   //				.getUniformLocation(newProgram, "Sampler2");
   //		newProgram.Enable3D = $wnd.gl
   //				.getUniformLocation(newProgram, "Enable3D");
   //		newProgram.BillBoard = $wnd.gl.getUniformLocation(newProgram, "BBoard");
   //		newProgram.ViewPortRatio = $wnd.gl.getUniformLocation(newProgram,
   //				"ViewPortRatio");
   //		newProgram.charWidth = $wnd.gl.getUniformLocation(newProgram,
   //				"charWidth");
   //		newProgram.charStride = $wnd.gl.getUniformLocation(newProgram,
   //				"charStride");
   //
   //		//Creaci�n de punteros a los atributos
   //		newProgram.Position = $wnd.gl.getAttribLocation(newProgram, "Position");
   //		newProgram.textureCoord = $wnd.gl.getAttribLocation(newProgram,
   //				"TextureCoord");
   //
   //		$wnd.shaderProgram = newProgram;
   //
   //		//Set default values:
   //		//$wnd.gl.uniform1i(newProgram.BillBoard, 0);
   //		$wnd.gl.uniform1i(newProgram.Enable3D, 1);
   //
   //		//Inicializacion de buferes
   //		$wnd.vertexBuffer = $wnd.gl.createBuffer();
   //		$wnd.texBuffer = $wnd.gl.createBuffer();
   //		$wnd.vertexIndexBuffer = $wnd.gl.createBuffer();
   //
   //		console.log("PROGRAM CREATED ");
   //		console.log(newProgram);
   //
   //		return newProgram;
   //
   //   }-*/;
   //
   //
   //   public static native void jsEnableVertices() /*-{
   //		$wnd.gl.enableVertexAttribArray($wnd.shaderProgram.Position);
   //   }-*/;
   //
   //
   //   public static native void jsEnableTextures()/*-{
   //		$wnd.gl.enableVertexAttribArray($wnd.shaderProgram.textureCoord);
   //   }-*/;
   //
   //
   //   public static native void jsEnable3D() /*-{
   //		$wnd.gl.uniform1i($wnd.shaderProgram.Enable3D, 1);
   //   }-*/;
   //
   //
   //   public static native void jsDisable3D() /*-{
   //		$wnd.gl.uniform1i($wnd.shaderProgram.Enable3D, 0);
   //   }-*/;
   //
   //
   //   public static native void jsEnableTexture2D() /*-{
   //		$wnd.gl.uniform1i($wnd.shaderProgram.EnableTexture, 1);
   //   }-*/;
   //
   //
   //   public static native void jsDisableTexture2D() /*-{
   //		$wnd.gl.uniform1i($wnd.shaderProgram.EnableTexture, 0);
   //   }-*/;
   //
   //
   //   public static native void jsDisableVertices() /*-{
   //		$wnd.gl.disableVertexAttribArray($wnd.shaderProgram.Position);
   //   }-*/;
   //
   //
   //   public static native void jsDisableTextures() /*-{
   //		$wnd.gl.disableVertexAttribArray($wnd.shaderProgram.textureCoord);
   //   }-*/;
   //
   //
   //   public static native void jsClearScreen(float r,
   //                                           float g,
   //                                           float b) /*-{
   //		$wnd.gl.clearColor(r, g, b, 1);
   //		$wnd.gl.clear($wnd.gl.COLOR_BUFFER_BIT | $wnd.gl.DEPTH_BUFFER_BIT);
   //   }-*/;
   //
   //
   //   public static native void jsColor(float r,
   //                                     float g,
   //                                     float b) /*-{
   //		$wnd.gl.uniform4f($wnd.shaderProgram.FlatColor, r, g, b, 1);
   //   }-*/;
   //
   //
   //   public static native void jsPushMatrix() /*-{
   //		var copy = $wnd.mat4.create();
   //		$wnd.mat4.set($wnd.mvMatrix, copy);
   //		$wnd.mvMatrixStack.push(copy);
   //   }-*/;
   //
   //
   //   public static native void jsPopMatrix() /*-{
   //		if ($wnd.mvMatrixStack.length == 0) {
   //			throw "Invalid popMatrix!";
   //		}
   //		$wnd.mvMatrix = $wnd.mvMatrixStack.pop();
   //		$wnd.gl.uniformMatrix4fv($wnd.shaderProgram.Modelview, false,
   //				$wnd.mvMatrix);
   //   }-*/;
   //
   //
   //   public static native boolean jsIsTexture(int texture) /*-{
   //		if (texture < 1 || texture > 32)
   //			return false;
   //		var glTexture = $wnd.textureStack[texture];
   //		return $wnd.gl.isTexture(glTexture);
   //   }-*/;
   //
   //
   //   public static native void jsLoadMatrixf(JsArrayNumber matrix) /*-{
   //		$wnd.gl.uniformMatrix4fv($wnd.shaderProgram.Modelview, false, matrix);
   //		$wnd.mat4.set(matrix, $wnd.mvMatrix);
   //   }-*/;
   //
   //
   //   public static native void jsBindTexture(int idTexture) /*-{
   //
   //		//Version texturas simples:
   //		//var texture = $wnd.textureStack[idTexture - 1];
   //		//$wnd.gl.bindTexture($wnd.gl.TEXTURE_2D, texture);
   //
   //		//Version texturas complejas:
   //		$wnd.gl.uniform1i($wnd.shaderProgram.Multiple, 0);
   //		var texture = $wnd.textureStack[idTexture - 1];
   //		if (texture.length < 2) {
   //			$wnd.gl.activeTexture($wnd.gl.TEXTURE0);
   //			$wnd.gl.bindTexture($wnd.gl.TEXTURE_2D, texture[0]);
   //			$wnd.gl.uniform1i($wnd.shaderProgram.Sampler1, 0);
   //			//console.log("una textura bind: "+texture[0].image.src);
   //		} else {
   //			$wnd.gl.uniform1i($wnd.shaderProgram.Multiple, 1);
   //			$wnd.textures = $wnd.textureStack[idTexture - 1];
   //			//console.log("dos texturas bind 1: "+$wnd.texture[0].image.src);
   //			//console.log("dos texturas bind 2: "+$wnd.texture[1].image.src);
   //			$wnd.texture1 = $wnd.textures[0];
   //			$wnd.texture2 = $wnd.textures[1];
   //
   //			$wnd.gl.activeTexture($wnd.gl.TEXTURE0);
   //			$wnd.gl.bindTexture($wnd.gl.TEXTURE_2D, $wnd.texture1);
   //			$wnd.gl.uniform1i($wnd.shaderProgram.Sampler1, 0);
   //
   //			//$wnd.gl.texImage2D($wnd.gl.TEXTURE_2D, 0, $wnd.gl.RGBA, $wnd.gl.RGBA, $wnd.gl.UNSIGNED_BYTE, $wnd.texture1.image);
   //			//$wnd.gl.texParameteri($wnd.gl.TEXTURE_2D, $wnd.gl.TEXTURE_MAG_FILTER, $wnd.gl.NEAREST);
   //			//$wnd.gl.texParameteri($wnd.gl.TEXTURE_2D, $wnd.gl.TEXTURE_MIN_FILTER, $wnd.gl.NEAREST);
   //
   //			$wnd.gl.activeTexture($wnd.gl.TEXTURE1);
   //			$wnd.gl.bindTexture($wnd.gl.TEXTURE_2D, $wnd.texture2);
   //			$wnd.gl.uniform1i($wnd.shaderProgram.Sampler2, 1);
   //
   //			//$wnd.gl.texImage2D($wnd.gl.TEXTURE_2D, 0, $wnd.gl.RGBA, $wnd.gl.RGBA, $wnd.gl.UNSIGNED_BYTE, $wnd.texture2.image);
   //			//$wnd.gl.texParameteri($wnd.gl.TEXTURE_2D, $wnd.gl.TEXTURE_MAG_FILTER, $wnd.gl.NEAREST);
   //			//$wnd.gl.texParameteri($wnd.gl.TEXTURE_2D, $wnd.gl.TEXTURE_MIN_FILTER, $wnd.gl.NEAREST);
   //		}
   //
   //   }-*/;
   //
   //
   //   public static native void jsVertexPointer(int size,
   //                                             int stride,
   //                                             JsArrayNumber vertex) /*-{
   //		$wnd.gl.bindBuffer($wnd.gl.ARRAY_BUFFER, $wnd.vertexBuffer);
   //		$wnd.gl.bufferData($wnd.gl.ARRAY_BUFFER, new Float32Array(vertex),
   //				$wnd.gl.STATIC_DRAW);
   //		$wnd.gl.vertexAttribPointer($wnd.shaderProgram.Position, size,
   //				$wnd.gl.FLOAT, false, stride, 0);
   //   }-*/;
   //
   //
   //   public static native void jsTexCoordPointer(int size,
   //                                               int stride,
   //                                               JsArrayNumber texcoord) /*-{
   //		$wnd.gl.bindBuffer($wnd.gl.ARRAY_BUFFER, $wnd.texBuffer);
   //		$wnd.gl.bufferData($wnd.gl.ARRAY_BUFFER, new Float32Array(texcoord),
   //				$wnd.gl.STATIC_DRAW);
   //		$wnd.gl.bindBuffer($wnd.gl.ARRAY_BUFFER, $wnd.texBuffer);
   //		$wnd.gl.vertexAttribPointer($wnd.shaderProgram.textureCoord, size,
   //				$wnd.gl.FLOAT, false, stride, 0);
   //   }-*/;
   //
   //
   //   public static native void jsDrawTriangleFan(int first,
   //                                               int count) /*-{
   //		$wnd.gl.drawArrays($wnd.gl.TRIANGLE_FAN, first, count);
   //   }-*/;
   //
   //
   //   public static native void jsDrawTriangleStrip(int first,
   //                                                 int count) /*-{
   //		$wnd.gl.drawArrays($wnd.gl.TRIANGLE_STRIP, first, count);
   //   }-*/;
   //
   //
   //   public static native float[] jsSetProjection(JsArrayNumber projectionMatrix) /*-{
   //		$wnd.gl.uniformMatrix4fv($wnd.shaderProgram.Projection, false,
   //				projectionMatrix);
   //   }-*/;
   //
   //
   //   public static native void jsUseProgram() /*-{
   //		$wnd.gl.useProgram($wnd.shaderProgram);
   //   }-*/;
   //
   //
   //   public static native void jsTranslate(float x,
   //                                         float y,
   //                                         float z) /*-{
   //		$wnd.mat4.translate($wnd.mvMatrix, [ x, y, z ]);
   //		$wnd.gl.uniformMatrix4fv($wnd.shaderProgram.Modelview, false,
   //				$wnd.mvMatrix);
   //   }-*/;
   //
   //
   //   public static native void jsMultiplyModelViewMatrix(JsArrayNumber matrix) /*-{
   //		var mvCopy = $wnd.mvMatrix;
   //		$wnd.mat4.multiply(mvCopy, matrix, $wnd.mvMatrix);
   //		$wnd.gl.uniformMatrix4fv($wnd.shaderProgram.Modelview, false,
   //				$wnd.mvMatrix);
   //   }-*/;
   //
   //
   //   public static native void jsIdentity() /*-{
   //		$wnd.mat4.identity($wnd.mvMatrix);
   //
   //		console.log($wnd.shaderProgram.Modelview);
   //
   //		$wnd.gl.uniformMatrix4fv($wnd.shaderProgram.Modelview, false,
   //				$wnd.mvMatrix);
   //   }-*/;
   //
   //
   //   public static native void jsDeleteTextures(int n) /*-{
   //		var texture = $wnd.textureStack[n];
   //		for (i = 0; i < texture.length; i++)
   //			$wnd.gl.deleteTexture(texture[i]);
   //		//delete texture.image;
   //		delete $wnd.textureStack[n];
   //		//texture.image = null;
   //		$wnd.textureStack[n] = null;
   //		$wnd.numFreeTexIds++;
   //   }-*/;
   //
   //
   //   public static native int jsGetNumFreeIdTextures() /*-{
   //		return $wnd.numFreeTexIds;
   //   }-*/;
   //
   //
   //   public static native void jsDrawIndexedMesh(int num_Indices,
   //                                               JsArrayInteger jsArray) /*-{
   //		$wnd.gl
   //				.bindBuffer($wnd.gl.ELEMENT_ARRAY_BUFFER,
   //						$wnd.vertexIndexBuffer);
   //		$wnd.gl.bufferData($wnd.gl.ELEMENT_ARRAY_BUFFER,
   //				new Uint8Array(jsArray), $wnd.gl.STATIC_DRAW);
   //		$wnd.gl
   //				.bindBuffer($wnd.gl.ELEMENT_ARRAY_BUFFER,
   //						$wnd.vertexIndexBuffer);
   //		$wnd.gl.drawElements($wnd.gl.TRIANGLE_STRIP, num_Indices,
   //				$wnd.gl.UNSIGNED_BYTE, 0);
   //   }-*/;
   //
   //
   //   public static native void jsDrawBillBoard(int tex,
   //                                             float x,
   //                                             float y,
   //                                             float z,
   //                                             float ratio) /*-{
   //		$wnd.gl.uniform1i($wnd.shaderProgram.BillBoard, 1);
   //		$wnd.gl.uniform1f($wnd.shaderProgram.ViewPortRatio, ratio);
   //
   //		var vertex = [ x, y, z, x, y, z, x, y, z, x, y, z ];
   //		var texcoord = [ 0, 0, 0, 1, 1, 0, 1, 1 ];
   //
   //		$wnd.gl.disable($wnd.gl.DEPTH_TEST);
   //
   //		$wnd.gl.uniform1i($wnd.shaderProgram.EnableTexture, 1);
   //		$wnd.gl.uniform4f($wnd.shaderProgram.FlatColor, 1.0, 0.0, 0.0, 1.0);
   //
   //		$wnd.gl.bindTexture($wnd.gl.TEXTURE_2D, $wnd.textureStack[tex - 1][0]);
   //
   //		$wnd.gl.bindBuffer($wnd.gl.ARRAY_BUFFER, $wnd.vertexBuffer);
   //		$wnd.gl.bufferData($wnd.gl.ARRAY_BUFFER, new Float32Array(vertex),
   //				$wnd.gl.STATIC_DRAW);
   //		$wnd.gl.vertexAttribPointer($wnd.shaderProgram.Position, 3,
   //				$wnd.gl.FLOAT, false, 0, 0);
   //
   //		$wnd.gl.bindBuffer($wnd.gl.ARRAY_BUFFER, $wnd.texBuffer);
   //		$wnd.gl.bufferData($wnd.gl.ARRAY_BUFFER, new Float32Array(texcoord),
   //				$wnd.gl.STATIC_DRAW);
   //		$wnd.gl.bindBuffer($wnd.gl.ARRAY_BUFFER, $wnd.texBuffer);
   //		$wnd.gl.vertexAttribPointer($wnd.shaderProgram.textureCoord, 2,
   //				$wnd.gl.FLOAT, false, 0, 0);
   //
   //		$wnd.gl.drawArrays($wnd.gl.TRIANGLE_STRIP, 0, 4);
   //		$wnd.gl.enable($wnd.gl.DEPTH_TEST);
   //
   //		$wnd.gl.uniform1i($wnd.shaderProgram.BillBoard, 0);
   //
   //   }-*/;
   //
   //
   //   public static native int jsUploadTexture(JavaScriptObject texture) /*-{
   //		debugger;
   //		var i = 0, idTexture = 0;
   //		var assignedPosition = false;
   //		for (i; i < $wnd.maxGPUTextures; i++) {
   //			if ($wnd.textureStack[i] == null) {
   //				$wnd.textureStack[i] = texture;
   //				idTexture = i + 1;
   //				$wnd.numFreeTexIds--;
   //				assignedPosition = true;
   //				break;
   //			}
   //		}
   //		if (assignedPosition == false)
   //			return 0;
   //		var magFilter = $wnd.gl.LINEAR;
   //		var minFilter = $wnd.gl.LINEAR;
   //		$wnd.gl.bindTexture($wnd.gl.TEXTURE_2D, texture);
   //
   //		$wnd.gl.texImage2D($wnd.gl.TEXTURE_2D, 0, $wnd.gl.RGBA, $wnd.gl.RGBA,
   //				$wnd.gl.UNSIGNED_BYTE, texture.image);
   //
   //		$wnd.gl.texParameteri($wnd.gl.TEXTURE_2D, $wnd.gl.TEXTURE_MAG_FILTER,
   //				magFilter);
   //		$wnd.gl.texParameteri($wnd.gl.TEXTURE_2D, $wnd.gl.TEXTURE_MIN_FILTER,
   //				minFilter);
   //		$wnd.gl.texParameteri($wnd.gl.TEXTURE_2D, $wnd.gl.TEXTURE_WRAP_S,
   //				$wnd.gl.CLAMP_TO_EDGE);
   //		$wnd.gl.texParameteri($wnd.gl.TEXTURE_2D, $wnd.gl.TEXTURE_WRAP_T,
   //				$wnd.gl.CLAMP_TO_EDGE);
   //		$wnd.gl.generateMipmap($wnd.gl.TEXTURE_2D);
   //		$wnd.gl.bindTexture($wnd.gl.TEXTURE_2D, null);
   //
   //		return idTexture;
   //
   //   }-*/;
   //
   //
   //   public static native int jsUploadMultipleTextures(JsArray<JavaScriptObject> images) /*-{
   //		var i = 0, idTexture = 0;
   //		var assignedPosition = false;
   //
   //		for (i = 0; i < images.length; i++) {
   //			var img = images[i].image;
   //			images[i] = $wnd.gl.createTexture();
   //			images[i].image = img;
   //		}
   //
   //		for (i; i < $wnd.maxGPUTextures; i++) {
   //			if ($wnd.textureStack[i] == null) {
   //				$wnd.textureStack[i] = images;
   //				idTexture = i + 1;
   //				$wnd.numFreeTexIds--;
   //				assignedPosition = true;
   //				break;
   //			}
   //		}
   //		if (assignedPosition == false)
   //			return 0;
   //		var magFilter = $wnd.gl.LINEAR;
   //		var minFilter = $wnd.gl.LINEAR;
   //
   //		if (images.length < 2) {
   //
   //			$wnd.gl.uniform1i($wnd.shaderProgram.Multiple, 0);
   //			var texture = images[0];
   //			$wnd.gl.bindTexture($wnd.gl.TEXTURE_2D, texture);
   //			$wnd.gl.texImage2D($wnd.gl.TEXTURE_2D, 0, $wnd.gl.RGBA,
   //					$wnd.gl.RGBA, $wnd.gl.UNSIGNED_BYTE, texture.image);
   //			$wnd.gl.texParameteri($wnd.gl.TEXTURE_2D,
   //					$wnd.gl.TEXTURE_MAG_FILTER, magFilter);
   //			$wnd.gl.texParameteri($wnd.gl.TEXTURE_2D,
   //					$wnd.gl.TEXTURE_MIN_FILTER, minFilter);
   //			$wnd.gl.texParameteri($wnd.gl.TEXTURE_2D, $wnd.gl.TEXTURE_WRAP_S,
   //					$wnd.gl.CLAMP_TO_EDGE);
   //			$wnd.gl.texParameteri($wnd.gl.TEXTURE_2D, $wnd.gl.TEXTURE_WRAP_T,
   //					$wnd.gl.CLAMP_TO_EDGE);
   //			$wnd.gl.generateMipmap($wnd.gl.TEXTURE_2D);
   //			$wnd.gl.bindTexture($wnd.gl.TEXTURE_2D, null);
   //		} else {
   //			var texture1 = images[0];
   //			var texture2 = images[1];
   //
   //			for (i = 0; i < images.length; i++) {
   //				texture = images[i];
   //				$wnd.gl.bindTexture($wnd.gl.TEXTURE_2D, texture);
   //
   //				$wnd.gl.texImage2D($wnd.gl.TEXTURE_2D, 0, $wnd.gl.RGBA,
   //						$wnd.gl.RGBA, $wnd.gl.UNSIGNED_BYTE, texture.image);
   //
   //				$wnd.gl.texParameteri($wnd.gl.TEXTURE_2D,
   //						$wnd.gl.TEXTURE_MAG_FILTER, magFilter);
   //				$wnd.gl.texParameteri($wnd.gl.TEXTURE_2D,
   //						$wnd.gl.TEXTURE_MIN_FILTER, minFilter);
   //				$wnd.gl.texParameteri($wnd.gl.TEXTURE_2D,
   //						$wnd.gl.TEXTURE_WRAP_S, $wnd.gl.CLAMP_TO_EDGE);
   //				$wnd.gl.texParameteri($wnd.gl.TEXTURE_2D,
   //						$wnd.gl.TEXTURE_WRAP_T, $wnd.gl.CLAMP_TO_EDGE);
   //				$wnd.gl.generateMipmap($wnd.gl.TEXTURE_2D);
   //				$wnd.gl.bindTexture($wnd.gl.TEXTURE_2D, null);
   //			}
   //
   //		}
   //		return idTexture;
   //
   //   }-*/;
   //
   //
   //   public static native int jsUploadBillboardTexture(JavaScriptObject images) /*-{
   //		var i = 0, idTexture = 0;
   //		var assignedPosition = false;
   //
   //		for (i = 0; i < images.length; i++) {
   //			var img = images[i].image;
   //			images[i] = $wnd.gl.createTexture();
   //			images[i].image = img;
   //		}
   //
   //		for (i; i < $wnd.maxGPUTextures; i++) {
   //			if ($wnd.textureStack[i] == null) {
   //				$wnd.textureStack[i] = images;
   //				idTexture = i + 1;
   //				$wnd.numFreeTexIds--;
   //				assignedPosition = true;
   //				break;
   //			}
   //		}
   //
   //		$wnd.gl.enable($wnd.gl.BLEND);
   //		$wnd.gl.blendFunc($wnd.gl.SRC_ALPHA, $wnd.gl.ONE_MINUS_SRC_ALPHA);
   //
   //		var texture = images[0];
   //
   //		$wnd.gl.bindTexture($wnd.gl.TEXTURE_2D, texture);
   //		$wnd.gl.texParameteri($wnd.gl.TEXTURE_2D, $wnd.gl.TEXTURE_MIN_FILTER,
   //				$wnd.gl.LINEAR);
   //		$wnd.gl.texParameteri($wnd.gl.TEXTURE_2D, $wnd.gl.TEXTURE_MAG_FILTER,
   //				$wnd.gl.LINEAR);
   //		$wnd.gl.texParameteri($wnd.gl.TEXTURE_2D, $wnd.gl.TEXTURE_WRAP_S,
   //				$wnd.gl.CLAMP_TO_EDGE);
   //		$wnd.gl.texParameteri($wnd.gl.TEXTURE_2D, $wnd.gl.TEXTURE_WRAP_T,
   //				$wnd.gl.CLAMP_TO_EDGE);
   //
   //		$wnd.gl.texImage2D($wnd.gl.TEXTURE_2D, 0, $wnd.gl.RGBA, $wnd.gl.RGBA,
   //				$wnd.gl.UNSIGNED_BYTE, texture.image);
   //
   //		$wnd.gl.bindTexture($wnd.gl.TEXTURE_2D, null);
   //
   //		return idTexture;
   //
   //   }-*/;
   //
   //
   //   public static native void jsDrawLines(int num_Indices,
   //                                         JsArrayInteger jsArray) /*-{
   //		$wnd.gl
   //				.bindBuffer($wnd.gl.ELEMENT_ARRAY_BUFFER,
   //						$wnd.vertexIndexBuffer);
   //		$wnd.gl.bufferData($wnd.gl.ELEMENT_ARRAY_BUFFER,
   //				new Uint8Array(jsArray), $wnd.gl.STATIC_DRAW);
   //		$wnd.gl
   //				.bindBuffer($wnd.gl.ELEMENT_ARRAY_BUFFER,
   //						$wnd.vertexIndexBuffer);
   //		$wnd.gl.drawElements($wnd.gl.LINES, num_Indices, $wnd.gl.UNSIGNED_BYTE,
   //				0);
   //   }-*/;
   //
   //
   //   public static native void jsDrawLineLoop(int num_Indices,
   //                                            JsArrayInteger jsArray) /*-{
   //		$wnd.gl
   //				.bindBuffer($wnd.gl.ELEMENT_ARRAY_BUFFER,
   //						$wnd.vertexIndexBuffer);
   //		$wnd.gl.bufferData($wnd.gl.ELEMENT_ARRAY_BUFFER,
   //				new Uint8Array(jsArray), $wnd.gl.STATIC_DRAW);
   //		$wnd.gl
   //				.bindBuffer($wnd.gl.ELEMENT_ARRAY_BUFFER,
   //						$wnd.vertexIndexBuffer);
   //		$wnd.gl.drawElements($wnd.gl.LINE_LOOP, num_Indices,
   //				$wnd.gl.UNSIGNED_BYTE, 0);
   //   }-*/;
   //
   //
   //   public static native void jsEnablePolygonOffset(float factor,
   //                                                   float units) /*-{
   //		$wnd.gl.enable($wnd.gl.POLYGON_OFFSET_FILL);
   //		$wnd.gl.polygonOffset(factor, units);
   //   }-*/;
   //
   //
   //   public static native void jsDisablePolygonOffset() /*-{
   //		$wnd.gl.disable($wnd.gl.POLYGON_OFFSET_FILL);
   //   }-*/;
   //
   //
   //   public static native void jsLineWidth(float width) /*-{
   //		$wnd.gl.lineWidth(width);
   //   }-*/;

}
