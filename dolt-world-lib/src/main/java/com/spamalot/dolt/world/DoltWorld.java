package com.spamalot.dolt.world;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Range;

/**
 * Hold the map. Idea is a World is just the terrain, not the political
 * boundaries.
 *
 * @author gej
 *
 */
public class DoltWorld<T extends MapTileFeatures> {

  /** A range check object for the height of the map. */
  private Range<Integer> heightRange;
  /** Actual height of the Map. */
  private final int mapHeight;

  /** The MapTiles that make up this Map. */
  private WorldTile<T>[][] mapTiles;
  /** Actual width of the Map. */
  private final int mapWidth;

  /** A range check object for the width of the map. */
  private Range<Integer> widthRange;

  /**
   * Construct a Map Object.
   *
   * @param width  Width of the Map
   * @param height Height of the Map
   */
  public DoltWorld(final int width, final int height) {
    this.mapWidth = width;
    this.mapHeight = height;

    this.widthRange = Range.closedOpen(0, this.mapWidth);
    this.heightRange = Range.closedOpen(0, this.mapHeight);

    this.mapTiles = new WorldTile[width][height];

    // Initialize all the tiles to be water tiles.
    initMapTiles(width, height, WorldTileType.WATER);
    // Link the tiles orthogonally to their neighbors
    linkTiles();
  }

  /**
   * Get the map tile.
   *
   * @param i Horizontal coordinate
   * @param j Vertical coordinate
   * @return The map tile at those coordinates
   */
  public final WorldTile<T> getMapTile(final int i, final int j) {
    if (isOnMap(i, j)) {
      return this.mapTiles[i][j];
    }
    return null;
  }

  /**
   * Get a map tile in a direction.
   *
   * @param i   Horizontal coordinate
   * @param j   Vertical coordinate
   * @param dir Direction to get map tile
   * @return The map tile in that direction
   */
  private WorldTile<T> getMapTileInDirection(final int i, final int j, final Direction dir) {
    checkNotNull(dir);

    return getMapTile(i + dir.gethDiff(), j + dir.getvDiff());
  }

  /**
   * Create the map, with all the tiles being of type.
   *
   * @param width  Width of the map
   * @param height Height of the map
   * @param type   Type of tile
   */
  private void initMapTiles(final int width, final int height, final WorldTileType type) {
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        this.mapTiles[i][j] = new WorldTile<>();
        this.mapTiles[i][j].setType(type);
      }
    }
  }

  /**
   * Check if coordinate is on map.
   *
   * @param x The X ordinate
   * @param y The Y ordinate
   * @return true if this Coordinate is on the Map
   */
  private boolean isOnMap(final int x, final int y) {
    return this.widthRange.contains(x) && this.heightRange.contains(y);
  }

  /**
   * Link the tiles together.
   */
  private void linkTiles() {
    for (int i = 0; i < this.mapWidth; i++) {
      for (int j = 0; j < this.mapHeight; j++) {
        WorldTile<T> left = getMapTileInDirection(i, j, Direction.LEFT);
        WorldTile<T> right = getMapTileInDirection(i, j, Direction.RIGHT);
        WorldTile<T> up = getMapTileInDirection(i, j, Direction.UP);
        WorldTile<T> down = getMapTileInDirection(i, j, Direction.DOWN);

        WorldTile<T> cur = getMapTile(i, j);

        cur.linkTileInDirection(Direction.LEFT, left);
        cur.linkTileInDirection(Direction.RIGHT, right);
        cur.linkTileInDirection(Direction.UP, up);
        cur.linkTileInDirection(Direction.DOWN, down);
      }
    }
  }

  @Override
  public final String toString() {

    final StringBuilder sb = new StringBuilder();
    sb.append('+');
    for (int x = 0; x < this.mapWidth; x++) {
      sb.append("-+");
    }
    sb.append('\n');
    for (int y = 0; y < this.mapHeight; y++) {
      sb.append('|');
      for (int x = 0; x < this.mapWidth; x++) {
        sb.append(this.mapTiles[x][y]);
        // if
        // (this.mapTiles[x][y].isInSameTerritory(this.mapTiles[x][y].get(Direction.RIGHT)))
        // {
        // sb.append('#');
        // } else {
        sb.append('|');
        // }
      }
      sb.append("\n+");
      for (int x = 0; x < this.mapWidth; x++) {
        // if
        // (this.mapTiles[x][y].isInSameTerritory(this.mapTiles[x][y].get(Direction.DOWN)))
        // {
        // sb.append("#+");
        // } else {
        sb.append("-+");
        // }
      }
      sb.append('\n');
    }
    return sb.toString();
  }

}