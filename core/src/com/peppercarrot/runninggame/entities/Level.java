package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.peppercarrot.runninggame.PaCGame;
import com.peppercarrot.runninggame.utils.Constants;

/**
 * WIP... Level is constructed of TMX-parts.
 * At the moment there are two TMX-parts loaded. This level parts occur
 * alterating in the level.
 * TODO: more level part to load, random occurrence of parts inside the
 * level, increasing scroll speed, load game entities from TMX ...
 * @author WinterLicht
 *
 */
public class Level {
	int offset; /** Offset in pixel when a new level part should be loaded. */
	int activeMap = 1; /** Helper, indicates which level should be loaded next. */
	float scrollSpeed = 10; /** Horizontal scroll speed in pixel of the level. */

	public OrthographicCamera camera1;
	public TiledMap tiledMap1;
	TiledMapRenderer tiledMapRenderer1;

	public OrthographicCamera camera2;
	public TiledMap tiledMap2;
	TiledMapRenderer tiledMapRenderer2;

	public Level(){
		this.offset = Constants.VIRTUAL_WIDTH / 2;
		
		this.camera1 = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		tiledMap1 = new TmxMapLoader().load("level.tmx");
		tiledMapRenderer1 = new OrthogonalTiledMapRenderer(tiledMap1);
		
		this.camera2 = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//the second camera is placed far on the left of level-part 2
		//so when it scolls a while (and the end of level part 1 is
		//about to show up in the screen) the part 2 is already
		//displayed
		camera2.position.set(-getMapLength(1), 0, 0);
		tiledMap2 = new TmxMapLoader().load("level2.tmx");
		tiledMapRenderer2 = new OrthogonalTiledMapRenderer(tiledMap2);
	}

	public void render(){
		if (boundsReached()) {
			resetMap();
		}
		//Consider y-position of the main game camera.
		//Because this is updated depending of players jump.
		float offsetY = PaCGame.getInstance().camera.position.y;
		
		//Scroll both cameras according to level scroll speed.
		//Consider offset to the ground and the offset as above.

		this.camera1.position.set(scrollSpeed+camera1.position.x, offsetY-Constants.OFFSET_TO_GROUND, 0);
		this.camera1.update();
		tiledMapRenderer1.setView(this.camera1);
		tiledMapRenderer1.render();
		
		this.camera2.position.set(scrollSpeed+camera2.position.x, offsetY-Constants.OFFSET_TO_GROUND, 0);
		this.camera2.update();
		tiledMapRenderer2.setView(this.camera2);
		tiledMapRenderer2.render();
	}

	/**
	 * Get length of a Level part in pixel.
	 * @param i desired map, here 1 or 2
	 * @return
	 */
	private int getMapLength(int i){
		int tileWidth; //in pixel
		int mapLengthT; //in amount of tiles
		if (i == 1) {
			tileWidth = tiledMap1.getProperties().get("tilewidth", Integer.class); 
			mapLengthT = tiledMap1.getProperties().get("width", Integer.class);
		} else {
			tileWidth = tiledMap2.getProperties().get("tilewidth", Integer.class); 
			mapLengthT = tiledMap2.getProperties().get("width", Integer.class);
		}
		return (tileWidth * mapLengthT);
	}

	/**
	 * Is the current displayed level part near its end.
	 * @return
	 */
	private boolean boundsReached(){
		//Check if the left camera bound is about to pass level length
		//If it do, obviously the map part will be not visible
		if(activeMap == 1) {
			return (getMapLength(1)) <= camera1.position.x-Constants.VIRTUAL_WIDTH / 2;
		}else{
			return (getMapLength(2)) <= camera2.position.x-Constants.VIRTUAL_WIDTH / 2;
		}
	}

	/**
	 * Resets cameras for each level part so it is rendered seemless
	 */
	private void resetMap(){
		//TODO: here to load other map
		if(activeMap == 2) {
			camera2.position.set(-getMapLength(1), 0, 0);
			activeMap = 1;
		} else {
			camera1.position.set(-getMapLength(2), 0, 0);
			activeMap = 2;
		}
	}

	/**
	 * Used to check players collision with platforms.
	 * @return array with integer y-values of all platform tops near the player in pixel.
	 */
	public Array<Integer> getPlatforms(){
		Array<Integer> vectorArray = new Array<Integer>();
		if (activeMap == 1) {
			//TODO: special layer "platforms" should be hear loaded
			TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap1.getLayers().get(0);
			//Get players position in tile coordinates of the current map
			//Players position is on the left of screen with small offset
			float pos = (camera1.position.x - Constants.VIRTUAL_WIDTH / 2 + Constants.OFFSET_TO_EDGE) / tiledMap1.getProperties().get("tilewidth", Integer.class);
			//Round this coordinates to get two near columns
			int min = (int) Math.floor(pos);
			int max = (int) Math.ceil(pos);
			if (min == max) max += 1; //Take then the one further column
			for (int column = min; column <= max; column++){
				for (int row = 0; row < layer.getHeight(); row++){
					//Iterate all cells of this two columns
					Cell cell = layer.getCell(column, row);
					if (cell != null) {
						//If the cell contains an element, it is a platform
						//Store top y-coordinates of it
						int vector = tiledMap1.getProperties().get("tilewidth", Integer.class) * (row+1);
						vectorArray.add(vector);
					}
				}
			}
		} else { //analog as above
			TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap2.getLayers().get(0);
			float pos = (camera2.position.x - Constants.VIRTUAL_WIDTH / 2 + Constants.OFFSET_TO_EDGE) / tiledMap2.getProperties().get("tilewidth", Integer.class);
			int min = (int) Math.floor(pos);
			int max = (int) Math.ceil(pos);
			if (min == max) max += 1;
			for (int column = min; column <= max; column++){
				for (int row = 0; row < layer.getHeight(); row++){
					//TODO store only topmost!
					Cell cell = layer.getCell(column, row);
					if (cell != null) {
						int vector = tiledMap2.getProperties().get("tilewidth", Integer.class) * (row+1);
						vectorArray.add(vector);
					}
				}
			}
		}
		return vectorArray;
	}

}




