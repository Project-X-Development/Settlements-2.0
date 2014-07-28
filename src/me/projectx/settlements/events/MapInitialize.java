package me.projectx.settlements.events;

import me.projectx.settlements.managers.MapManager;
import me.projectx.settlements.utils.RenderMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.MapInitializeEvent;

public class MapInitialize implements Listener {

	@EventHandler
	public void onMap(MapInitializeEvent e){
		if(e.getMap().getId()==0){
			for(org.bukkit.map.MapRenderer r : e.getMap().getRenderers()){
				e.getMap().removeRenderer(r);
			}
			e.getMap().addRenderer(MapManager.getInstance().getRenderMap());
		}
	}
}
