package com.glob3mobile.pointcloud.octree.berkeleydb;

import com.glob3mobile.pointcloud.octree.Angle;
import com.glob3mobile.pointcloud.octree.Geodetic2D;
import com.glob3mobile.pointcloud.octree.Sector;
import com.glob3mobile.pointcloud.octree.Utils;

public class TileHeader {

	static final TileHeader ROOT_TILE_HEADER = new TileHeader(new byte[0],
			Sector.FULL_SPHERE);

	public static TileHeader deepestEnclosingTileHeader(
			final Sector targetSector) {
		return deepestEnclosingTile(ROOT_TILE_HEADER, targetSector);
	}

	private static TileHeader deepestEnclosingTile(final TileHeader candidate,
			final Sector targetSector) {
		final TileHeader[] children = candidate.createChildren();
		for (final TileHeader child : children) {
			if (child._sector.fullContains(targetSector)) {
				return deepestEnclosingTile(child, targetSector);
			}
		}
		return candidate;
	}

	public final byte[] _id;
	public final Sector _sector;

	public TileHeader(final byte[] id, final Sector sector) {
		_id = id;
		_sector = sector;
	}

	public TileHeader[] createChildren() {
		return new TileHeader[] { createChild((byte) 0), createChild((byte) 1),
				createChild((byte) 2), createChild((byte) 3),
				createChild((byte) 4), createChild((byte) 5),
				createChild((byte) 6), createChild((byte) 7) };
	}

	private static Sector createSectorForChild(final Sector sector,
			final byte index) {
		final Geodetic2D lower = sector._lower;
		final Geodetic2D upper = sector._upper;

		final Angle splitLatitude = BerkeleyDBOctreeNode
				.calculateSplitLatitude(lower._latitude, upper._latitude);
		final Angle splitLongitude = Angle.midAngle(lower._longitude,
				upper._longitude);
		final double splitHeight = sector._maxHeight / 2 + sector._minHeight
				/ 2;

		switch (index) {
		case 0:
			return new Sector(
					//
					splitLatitude,
					lower._longitude, //
					upper._latitude, splitLongitude, sector._minHeight,
					splitHeight);
		case 1:
			return new Sector(
					//
					splitLatitude,
					splitLongitude, //
					upper._latitude, upper._longitude, sector._minHeight,
					splitHeight);
		case 2:
			return new Sector(
					//
					lower._latitude,
					lower._longitude, //
					splitLatitude, splitLongitude, sector._minHeight,
					splitHeight);
		case 3:
			return new Sector(
					//
					lower._latitude,
					splitLongitude, //
					splitLatitude, upper._longitude, sector._minHeight,
					splitHeight);
		case 4:
			return new Sector(
					//
					splitLatitude,
					lower._longitude, //
					upper._latitude, splitLongitude, splitHeight,
					sector._maxHeight);
		case 5:
			return new Sector(
					//
					splitLatitude,
					splitLongitude, //
					upper._latitude, upper._longitude, splitHeight,
					sector._maxHeight);
		case 6:
			return new Sector(
					//
					lower._latitude,
					lower._longitude, //
					splitLatitude, splitLongitude, splitHeight,
					sector._maxHeight);
		case 7:
			return new Sector(
					//
					lower._latitude,
					splitLongitude, //
					splitLatitude, upper._longitude, splitHeight,
					sector._maxHeight);
		default:
			throw new RuntimeException("Invalid index=" + index);
		}
	}

	private TileHeader createChild(final byte index) {
		final int length = _id.length;
		final byte[] childId = new byte[length + 1];
		System.arraycopy(_id, 0, childId, 0, length);
		childId[length] = index;
		return new TileHeader(childId, createSectorForChild(_sector, index));
	}

	public static Sector sectorFor(final byte[] id) {
		Sector currentSector = Sector.FULL_SPHERE;
		for (final byte b : id) {
			currentSector = createSectorForChild(currentSector, b);
		}
		return currentSector;
	}

	int getLevel() {
		return _id.length;
	}

	@Override
	public String toString() {
		return "[TileHeader id=" + Utils.toIDString(_id) + ", sector="
				+ _sector + "]";
	}

}
