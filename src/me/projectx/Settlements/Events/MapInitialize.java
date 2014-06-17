package me.projectx.Settlements.Events;

import me.projectx.Settlements.Managers.MapManager;
import me.projectx.Settlements.Utils.RenderMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.MapInitializeEvent;

public class MapInitialize implements Listener {

	@EventHandler
	public void onMap(MapInitializeEvent e){
		if(e.getMap().getId()==1){
			for(org.bukkit.map.MapRenderer r : e.getMap().getRenderers()){
				e.getMap().removeRenderer(r);
			}
			e.getMap().addRenderer(MapManager.getInstance().getRenderMap());
		}
	}
}
