"""
Converts the Yale Bright Star Catalogue (BSC5) into the stars file G3M's StarsRenderer reads.

Input:  bsc5.json from https://github.com/brettonw/YaleBrightStarCatalog (MIT), a JSON
        conversion of BSC5 (Hoffleit & Warren 1991, http://tdc-www.harvard.edu/catalogs/bsc5.html)
Output: a flat JSON array [x, y, z, red, green, blue, ...], one star per 6 numbers:
        x, y, z = unit direction, J2000 equatorial frame laid on G3M's ECEF axes
                  (x towards RA 0h, z towards the north celestial pole)
        red, green, blue = colour from B-V, scaled by brightness from V magnitude

usage: python3 make_stars_json.py bsc5.json stars.json
"""
import json, math, re, sys

def angleInDegrees(text, isRightAscension):
    numbers = [float(n) for n in re.findall(r"[\d.]+", text)]
    degrees = numbers[0] + numbers[1] / 60 + numbers[2] / 3600
    if isRightAscension:
        return degrees * 15  # hours to degrees
    return -degrees if text.strip().startswith("-") else degrees

def direction(star):
    rightAscension = math.radians(angleInDegrees(star["RA"], True))
    declination = math.radians(angleInDegrees(star["Dec"], False))
    return (math.cos(declination) * math.cos(rightAscension),
            math.cos(declination) * math.sin(rightAscension),
            math.sin(declination))

# Ballesteros 2012, black body temperature from B-V
def temperature(bMinusV):
    return 4600 * (1 / (0.92 * bMinusV + 1.7) + 1 / (0.92 * bMinusV + 0.62))

# Tanner Helland's fit of the black body colour, 1000 K to 40000 K
def blackBodyColour(kelvin):
    t = min(max(kelvin, 1000), 40000) / 100
    red = 255 if t <= 66 else 329.698727446 * (t - 60) ** -0.1332047592
    green = 99.4708025861 * math.log(t) - 161.1195681661 if t <= 66 else 288.1221695283 * (t - 60) ** -0.0755148492
    blue = 255 if t >= 66 else (0 if t <= 19 else 138.5177312231 * math.log(t - 10) - 305.0447927307)
    colour = [min(max(c, 0), 255) / 255 for c in (red, green, blue)]
    brightest = max(colour)
    return [c / brightest for c in colour]

def main(inputPath, outputPath):
    stars = [s for s in json.load(open(inputPath)) if "Vmag" in s and "RA" in s and "Dec" in s]
    magnitudes = [float(s["Vmag"]) for s in stars]
    brightestMagnitude, faintestMagnitude = min(magnitudes), max(magnitudes)
    numbers = []
    for star, magnitude in zip(stars, magnitudes):
        # linear in magnitude (the eye's scale); the faintest star keeps one magnitude of brightness
        brightness = (faintestMagnitude + 1 - magnitude) / (faintestMagnitude + 1 - brightestMagnitude)
        colour = blackBodyColour(temperature(float(star["B-V"]))) if "B-V" in star else [1, 1, 1]
        numbers += [round(c, 4) for c in direction(star)]
        numbers += [round(c * brightness, 2) for c in colour]
    with open(outputPath, "w") as output:
        json.dump(numbers, output, separators=(",", ":"))
    print(f"{len(stars)} stars, magnitudes {brightestMagnitude} to {faintestMagnitude}")

if __name__ == "__main__":
    main(sys.argv[1], sys.argv[2])
