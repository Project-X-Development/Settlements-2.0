package me.projectx.Settlements.Managers;

import me.projectx.Settlements.Models.Settlement;
import me.projectx.Settlements.Models.War;

public class WarManager {

	public static WarManager instance;

	public WarManager(){
		instance = this;
	}

	public War getWar(long id){
		for (War w : War.instances){
			if (w.getId() == id){
				return w;
			}
		}
		return null;
	}

	public War getWar(Settlement set){
		for (War w : War.instances){
			if (w.getStarter() == set || w.getAccepter() == set){
				return w;
			}
		}
		return null;
	}



}
