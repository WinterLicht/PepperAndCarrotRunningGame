package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.peppercarrot.runninggame.PaCGame;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.Constants;

/**
 * WIP... Level is constructed of TMX-parts.
 * At the moment there are two TMX-parts loaded. This level parts occur
 * alterating in the level.
 * TODO: more level part to load, increasing scroll speed, load game entities from TMX ...
 * @author WinterLicht
 *
 */
public class Level {
	int activeMap = 1; /** Helper, indicates which level should be loaded next. */
	public float scrollSpeed = 10; /** Horizontal scroll speed in pixel of the level. */
	public boolean beginLevel = false; /** Set to true when player is ready. */

	public OrthographicCamera camera1;
	public TiledMap tiledMap1;
	TiledMapRenderer tiledMapRenderer1;

	public OrthographicCamera camera2;
	public TiledMap tiledMap2;
	TiledMapRenderer tiledMapRenderer2;

	//This stages contain only one Table that contains enemies.
	Stage enemies1; /** enemies of map part 1. */
	Stage enemies2; /** enemies of map part 2. */
	
	public Level(){
		this.camera1 = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera1.setToOrtho(false, Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT);
		tiledMap1 = new TmxMapLoader().load("startlevel.tmx");
		tiledMapRenderer1 = new OrthogonalTiledMapRenderer(tiledMap1);
		
		this.camera2 = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//the second camera is placed far on the left of level-part 2
		//so when it scolls a while (and the end of level part 1 is
		//about to show up in the screen) the part 2 is already
		//displayed
		camera2.setToOrtho(false, Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT);
		camera2.position.set(-getMapLength(1), 0, 0);
		tiledMap2 = new TmxMapLoader().load("startlevel.tmx");
		tiledMapRenderer2 = new OrthogonalTiledMapRenderer(tiledMap2);
		
		enemies1 = new Stage(PaCGame.getInstance().viewport);
		Table t1 = new Table();
		t1.setFillParent(true);
		enemies1.addActor(t1);
		enemies2 = new Stage(PaCGame.getInstance().viewport);
		Table t2 = new Table();
		t2.setFillParent(true);
		enemies2.addActor(t2);
	}

	/**
	 * Render background layers.
	 * It is assumed, that 0 and 1 are background
	 * TODO: recognize background/foreground from layer name 
	 */
	public void renderBackground(){
		int[] backgroundLayers = { 0, 1 };
		tiledMapRenderer1.render(backgroundLayers);
		tiledMapRenderer2.render(backgroundLayers);
	}

	/**
	 * Render foreground layers.
	 * It is assumed, that 2 is foreground
	 */
	public void renderForeground(){
		int[] foregroundLayers = { 2 };
		tiledMapRenderer1.render(foregroundLayers);
		tiledMapRenderer2.render(foregroundLayers);
	}
	
	/**
	 * Scroll enemies towards the player.
	 */
	public void updateEnemies() {
		for (int i = 0; i < ((Table) enemies1.getActors().get(0)).getChildren().size; i++) {
			Image enemy = getEnemy(1, i);
			enemy.setX((int)enemy.getX()-scrollSpeed);
		}
		for (int i = 0; i < ((Table) enemies2.getActors().get(0)).getChildren().size; i++) {
			Image enemy = getEnemy(2, i);
			enemy.setX((int)enemy.getX()-scrollSpeed);
		}
	}

	/**
	 * Update cameras.
	 */
	public void update(){
			if (boundsReached()) {
				if (beginLevel) { //game is started and player is ready
					resetMap(); //here are new level parts loaded
				} else { //loop two start levels without platforms
					resetCurrentTwoLevels();
				}
			}
		//Consider y-position of the main game camera.
		//Because this is updated depending of players jump.
		float offsetY = PaCGame.getInstance().camera.position.y;
		
		//Scroll both cameras according to level scroll speed.
		//Consider offset to the ground and the offset as above.
		this.camera1.position.set((int)scrollSpeed+camera1.position.x, offsetY-Constants.OFFSET_TO_GROUND, 0);
		this.camera1.update();
		tiledMapRenderer1.setView(this.camera1);
		
		this.camera2.position.set((int)scrollSpeed+camera2.position.x, offsetY-Constants.OFFSET_TO_GROUND, 0);
		this.camera2.update();
		tiledMapRenderer2.setView(this.camera2);
		updateEnemies();
	}

	/**
	 * Render and update enemies.
	 * @param delta
	 */
	public void renderEnemies(float delta){
		enemies1.act(delta);
		enemies1.draw();
		enemies2.act(delta);
		enemies2.draw();
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
		double random = Math.random();
		int mapsAmount = 2;
		for (int i = 1; i <= mapsAmount; i++) {
			if ((i-1)*(1f/mapsAmount) <= random && random < i*(1f/mapsAmount)){
				if(activeMap == 2) {
					tiledMap2 = new TmxMapLoader().load("level"+i+".tmx");
					tiledMapRenderer2 = new OrthogonalTiledMapRenderer(tiledMap2);
					camera2.position.set(-getMapLength(1), 0, 0);
					activeMap = 1;
					spawnEnemies(2);
				} else {
					tiledMap1 = new TmxMapLoader().load("level"+i+".tmx");
					tiledMapRenderer1 = new OrthogonalTiledMapRenderer(tiledMap1);
					camera1.position.set(-getMapLength(2), 0, 0);
					activeMap = 2;
					spawnEnemies(1);
				}
				System.out.println("level"+i+".tmx loaded");
			}
		}
	}
	
	/**
	 * To loop the start level without platforms.
	 */
	private void resetCurrentTwoLevels() {
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
	public Array<Integer> getWallsYPosNearPlayer(){
		Array<Integer> vectorArray = new Array<Integer>();
		if (activeMap == 1) {
			TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap1.getLayers().get("platforms");
			//Get players position in tile coordinates of the current map
			//Players position is on the left of screen with small offset
			float pos = (camera1.position.x - Constants.VIRTUAL_WIDTH / 2 + Constants.OFFSET_TO_EDGE) / tiledMap1.getProperties().get("tilewidth", Integer.class);
			//Round this coordinates to get two near columns
			int min = (int) Math.floor(pos);
			int max = (int) Math.ceil(pos);
			if (min == max) max += 1; //Take then the one further column
			if (layer != null) { //When layer named platforms exists
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
			}
		} else { //analog as above
			TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap2.getLayers().get("platforms");
			float pos = (camera2.position.x - Constants.VIRTUAL_WIDTH / 2 + Constants.OFFSET_TO_EDGE) / tiledMap2.getProperties().get("tilewidth", Integer.class);
			int min = (int) Math.floor(pos);
			int max = (int) Math.ceil(pos);
			if (min == max) max += 1;
			if (layer != null) {
				for (int column = min; column <= max; column++){
					for (int row = 0; row < layer.getHeight(); row++){
						Cell cell = layer.getCell(column, row);
						if (cell != null) {
							int vector = tiledMap2.getProperties().get("tilewidth", Integer.class) * (row+1);
							vectorArray.add(vector);
						}
					}
				}
			}
		}
		return vectorArray;
	}

	/**
	 * 
	 */
	private void spawnEnemies(int i){
		if (i == 1) {
			enemies1.getActors().get(0).clear();
			TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap1.getLayers().get("enemies");
			if (layer != null) {
				for (int column = 0; column < layer.getWidth(); column++){
					for (int row = 0; row < layer.getHeight(); row++){
						Cell cell = layer.getCell(column, row);
						if (cell != null) {
							int tileWidth = tiledMap1.getProperties().get("tilewidth", Integer.class);
							int tileHeight = tiledMap1.getProperties().get("tileheight", Integer.class);
							float posX = (column+0.5f)*tileWidth;
							float posY = (row+0.5f)*tileHeight;
							Image img = new Image(Assets.I.skin.getDrawable("enemy"));
							img.setOrigin(Align.center);
							//Not to forget the offset of camera center +width/2
							//and the tile width, so it is set on center
							img.setX(posX-camera1.position.x+Constants.VIRTUAL_WIDTH/2-tileWidth/2);
							img.setY(posY+Constants.OFFSET_TO_EDGE);
							((Table) enemies1.getActors().get(0)).addActor(img);
						}
					}
				}
			}
		} else {
			enemies2.getActors().get(0).clear();
			TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap2.getLayers().get("enemies");
			if (layer != null) {
				for (int column = 0; column < layer.getWidth(); column++){
					for (int row = 0; row < layer.getHeight(); row++){
						Cell cell = layer.getCell(column, row);
						if (cell != null) {
							int tileWidth = tiledMap2.getProperties().get("tilewidth", Integer.class);
							int tileHeight = tiledMap2.getProperties().get("tileheight", Integer.class);
							float posX = (column+0.5f)*tileWidth;
							float posY = (row+0.5f)*tileHeight;
							Image img = new Image(Assets.I.skin.getDrawable("enemy"));
							img.setOrigin(Align.center);
							img.setX(posX-camera2.position.x+Constants.VIRTUAL_WIDTH/2-tileWidth/2);
							img.setY(posY-Constants.OFFSET_TO_EDGE);
							((Table) enemies2.getActors().get(0)).addActor(img);
						}
					}
				}
			}
		}
	}

	/**
	 * Get all alive enemies of two level parts.
	 * @return array of enemies
	 */
	public Array<Image> getAllEnemies(){
		Array<Image> enemies = new Array<Image>();
		for (int i = 0; i < ((Table) enemies1.getActors().get(0)).getChildren().size; i++) {
			Image enemy = getEnemy(1, i);
			enemies.add(enemy);
		}
		for (int i = 0; i < ((Table) enemies2.getActors().get(0)).getChildren().size; i++) {
			Image enemy = getEnemy(2, i);
			enemies.add(enemy);
		}
		return enemies;
	}

	/**
	 * Enemy getter.
	 * @param i desired map part index, here 1 or 2
	 * @param index number of enemy
	 * @return
	 */
	public Image getEnemy(int i, int index) {
		if (i == 1){
			return (Image) ((Table) enemies1.getActors().get(0)).getChildren().get(index);
		} else {
			return (Image) ((Table) enemies2.getActors().get(0)).getChildren().get(index);
		}
	}
}
