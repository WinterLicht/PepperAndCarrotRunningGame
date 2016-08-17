package com.peppercarrot.runninggame.utils;

import java.io.IOException;
import java.io.Writer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Account data is stored in account.txt
 * 
 * @author WinterLicht
 *
 */
public enum Account {
	I; // Singleton

	public int progress = -1;
	public int completeLevels = 0;
	public int huntEnemies = 0;
	public int usedSkills = 0;
	public int jumps = 0;
	public int collectedPotions = 0;
	public int died = 0;
	public int levelsWithoutKilling = 0;
	public int levelsWithoutHealthLost = 0;

	//Helper 
	public int startedLvlID = -1;
	public boolean pacifist = true; //false when at least 1 enemy is killed during a game session
	public boolean nimbleness = true; //false when at least 1 heart is lost during a game session

	Account() {
		load();
	}

	public void load() {
		String newLine = System.getProperty("line.separator");
		try{
			FileHandle handle = Gdx.files.local("account.txt");
			String inputFile = handle.readString();
			String[] lines = inputFile.split(newLine);
			
			for(String line: lines){
				String[] words = line.split(" ");
				switch (words[0]) {
					case "Progress":
						progress = Integer.parseInt(words[1]);
						break;
					case "CompleteLevels":
						completeLevels = Integer.parseInt(words[1]);
						break;
					case "HuntEnemies":
						huntEnemies = Integer.parseInt(words[1]);
						break;
					case "UsedSkills":
						usedSkills = Integer.parseInt(words[1]);
						break;
					case "Jumps":
						jumps = Integer.parseInt(words[1]);
						break;
					case "CollectedPotions":
						collectedPotions = Integer.parseInt(words[1]);
						break;
					case "Died":
						died = Integer.parseInt(words[1]);
						break;
					case "LevelsWithoutKilling":
						levelsWithoutKilling = Integer.parseInt(words[1]);
					case "LevelsWithoutHealthLost":
						levelsWithoutHealthLost = Integer.parseInt(words[1]);
						break;
					default:
						break;
				}
				
			}
		} catch(Exception e) {
			System.out.println("Error in Account:loadData()");
		}
	}

	public void saveData(){
		try {
			FileHandle file = Gdx.files.local("account.txt");
			boolean isLocalDirAvailable = Gdx.files.isLocalStorageAvailable();

			if(isLocalDirAvailable){
				Writer wr = file.writer(false);
				wr.write(this.toString());
				wr.close();
			}else{
				System.out.println("No Storage available to save account");
			}
			
		} catch (IOException e) {
			System.out.println("Error in Account:saveData()");
			e.printStackTrace();
		}
	}

	/**
	 * returns all relevant account information as String.
	 */
	public String toString(){
		String newLine = System.getProperty("line.separator");
		String output;
		output= ("Progress" + " " + progress + newLine);
		output = output.concat("CompleteLevels"+ " " + completeLevels + newLine);
		output = output.concat("HuntEnemies"+ " " + huntEnemies + newLine);
		output = output.concat("UsedSkills" + " "+ usedSkills + newLine);
		output = output.concat("Jumps" + " "+ jumps + newLine);
		output = output.concat("CollectedPotions" + " "+ collectedPotions + newLine);
		output = output.concat("Died" + " "+ died + newLine);
		output = output.concat("LevelsWithoutKilling" + " "+ levelsWithoutKilling + newLine);
		output = output.concat("LevelsWithoutHealthLost" + " "+ levelsWithoutHealthLost + newLine);
		return output;
	}

	public void reset() {
		progress = -1;
		completeLevels = 0;
		huntEnemies = 0;
		usedSkills = 0;
		jumps = 0;
		collectedPotions = 0;
		died = 0;
		levelsWithoutKilling = 0;
		levelsWithoutHealthLost = 0;
		saveData();
	}

	public void exit() {
		saveData();
		Gdx.app.exit();
	}

	/**
	 * Is called, when a new game session is started.
	 */
	public void resetHelper() {
		pacifist = true;
		nimbleness = true;
	}
}
