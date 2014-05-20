package me.projectx.Settlements.Utils;


import me.projectx.Settlements.Managers.ChunkManager;
import me.projectx.Settlements.Managers.PlayerManager;
import me.projectx.Settlements.Managers.SettlementManager;
import me.projectx.Settlements.Models.ClaimedChunk;
import me.projectx.Settlements.Models.Players;
import me.projectx.Settlements.Models.Settlement;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class RenderMap extends MapRenderer {

	@SuppressWarnings("deprecation")
	@Override
	public void render(MapView view, MapCanvas canvas, Player p) {
		Players pl = PlayerManager.getInstance().getPlayer(p);
		if(pl.getBoolean("map")){
			return;
		}
		pl.setBoolean("map", true);
		for(int j = 0; j < 128; j++) {
			for(int i = 0; i < 128; i++) {
				canvas.setPixel(i, j, MapPalette.WHITE);
			}
		}
		ChunkManager cm = ChunkManager.getInstance();
		Settlement set = SettlementManager.getManager().getPlayerSettlement(p.getName());
		for(int x = 1; x<=25; x++){
			for(int z = 1; z<=25; z++){
				int column = 1+(4*x)-3+(x-1);
				int row = 1+(4*z)-3+(z-1);
				int chunkx = p.getLocation().getChunk().getX()-(x-13);
				int chunkz = p.getLocation().getChunk().getZ()-(z-13);
				for(int a = 0; a<4; a++){
					for(int b = 0; b<4; b++){
						ClaimedChunk c = cm.getChunk(chunkx, chunkx);
						if(cm.isClaimed(chunkx, chunkz)&&c.getSettlement()==set){//If chunk x,z is claimed by own settlement
							canvas.setPixel(column + a, row + b, MapPalette.LIGHT_GREEN);
						}else if(cm.isClaimed(chunkx, chunkz)&&c.getSettlement()!=set){//If chunk x,z is claimed by other settlement
							canvas.setPixel(column + a, row + b, MapPalette.RED);
						}else{
							canvas.setPixel(column + a, row + b, MapPalette.LIGHT_GRAY);
						}
						if(x==13&&z==13){
							if((a==1&&b==1)||(a==1&&b==2)||(a==2&&b==1)||(a==2&&b==2)){
								canvas.setPixel(column + a, row + b, MapPalette.PALE_BLUE);
							}
						}
					}	
				}
			}
		}
		p.sendMap(view);
	}
}
