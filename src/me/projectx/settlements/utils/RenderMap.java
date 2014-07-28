package me.projectx.settlements.utils;

import java.util.ArrayList;
import java.util.List;

import me.projectx.settlements.enums.ClaimType;
import me.projectx.settlements.managers.ChunkManager;
import me.projectx.settlements.managers.PlayerManager;
import me.projectx.settlements.managers.SettlementManager;
import me.projectx.settlements.models.ClaimedChunk;
import me.projectx.settlements.models.Players;
import me.projectx.settlements.models.Settlement;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
@SuppressWarnings("deprecation")
public class RenderMap extends MapRenderer {

	private List<String> players = new ArrayList<String>();

	@Override
	public void render(MapView view, MapCanvas canvas, Player p) {
		if(contains(p)){
			return;
		}
		add(p);
		for(int j = 0; j < 128; j++) {
			for(int i = 0; i < 128; i++) {
				canvas.setPixel(i, j, MapPalette.WHITE);
			} 
		}
		ChunkManager cm = ChunkManager.getManager();
		Settlement set = SettlementManager.getManager().getPlayerSettlement(p.getName());
		int pchunkx = p.getLocation().getChunk().getX();
		int pchunkz = p.getLocation().getChunk().getZ();
		World w = p.getWorld();
		int i = getZoom(PlayerManager.getInstance().getPlayer(p));
		switch(i){
		case 1:
			zoom1(view, canvas, cm, set, pchunkx, pchunkz, w);
			break;
		case 3:
			zoom3(view, canvas, cm, set, pchunkx, pchunkz, w);
			break;
		case 5:
			zoom5(view, canvas, cm, set, pchunkx, pchunkz, w);
			break;
		case 7:
			zoom7(view, canvas, cm, set, pchunkx, pchunkz, w);
			break;
		case 9:
			zoom9(view, canvas, cm, set, pchunkx, pchunkz, w);
			break;
		case 11:
			zoom11(view, canvas, cm, set, pchunkx, pchunkz, w);
			break;
		case 13:
			zoom13(view, canvas, cm, set, pchunkx, pchunkz, w);
			break;
		case 17:
			zoom17(view, canvas, cm, set, pchunkx, pchunkz, w);
			break;
		case 25:
			zoom25(view, canvas, cm, set, pchunkx, pchunkz, w);
			break;
		case 41:
			zoom41(view, canvas, cm, set, pchunkx, pchunkz, w);
			break;
		case 125:
			zoom125(view, canvas, cm, set, pchunkx, pchunkz, w);
			break;
		}
		p.sendMap(view);
	}

	public void add(Player pl){
		this.players.add(pl.getUniqueId().toString());
	}

	public void remove(Player pl){
		String uuid = pl.getUniqueId().toString();
		if(this.players.contains(uuid)){
			this.players.remove(uuid);
		}
	}

	public boolean contains(Player pl){
		String uuid = pl.getUniqueId().toString();
		if(this.players.contains(uuid)){
			return true;
		}
		return false;
	}

	public int getZoom(Players pl){
		return pl.getInt("map");
	}

	public void zoom1(MapView view, MapCanvas canvas, ChunkManager cm, Settlement set, int pchunkx, int pchunkz, World w){
		int column = 2;
		int chunkx = pchunkx;
		int row = 2;
		int chunkz = pchunkz;
		ClaimedChunk c = cm.getChunk(chunkx, chunkz, w);
		if(cm.isClaimed(chunkx, chunkz, w)){
			if(c.getType() == ClaimType.SAFEZONE){
				for(int a = 0; a<124; a++){
					for(int b = 0; b<124; b++){
						canvas.setPixel(column + a, row + b, MapPalette.BLUE);
					}
				}
			}else{
				if(c.getSettlement()==set){
					for(int a = 0; a<124; a++){
						for(int b = 0; b<124; b++){
							canvas.setPixel(column + a, row + b, MapPalette.LIGHT_GREEN);
						}
					}
				}else{
					for(int a = 0; a<124; a++){
						for(int b = 0; b<124; b++){
							canvas.setPixel(column + a, row + b, MapPalette.RED);
						}
					}
				}
			}
		}else{
			for(int a = 0; a<124; a++){
				for(int b = 0; b<124; b++){
					canvas.setPixel(column + a, row + b, MapPalette.LIGHT_GRAY);
				}
			}
		}
		for(int a = 61; a<=62; a++){
			for(int b = 61; b<=62; b++){
				canvas.setPixel(column + a, row + b, MapPalette.LIGHT_BROWN);
			}
		}
	}

	public void zoom3(MapView view, MapCanvas canvas, ChunkManager cm, Settlement set, int pchunkx, int pchunkz, World w){
		for(int x = 1; x<=3; x++){
			int column = (40*x)-37+(x-1);
			int chunkx = pchunkx+(x-2);
			for(int z = 1; z<=3; z++){
				int row = (40*z)-37+(z-1);
				int chunkz = pchunkz+(z-2);
				ClaimedChunk c = cm.getChunk(chunkx, chunkz, w);
				if(cm.isClaimed(chunkx, chunkz, w)){
					if(c.getType() == ClaimType.SAFEZONE){
						for(int a = 0; a<40; a++){
							for(int b = 0; b<40; b++){
								canvas.setPixel(column + a, row + b, MapPalette.BLUE);
							}
						}
					}else{
						if(c.getSettlement()==set){
							for(int a = 0; a<40; a++){
								for(int b = 0; b<40; b++){
									canvas.setPixel(column + a, row + b, MapPalette.LIGHT_GREEN);
								}
							}
						}else{
							for(int a = 0; a<40; a++){
								for(int b = 0; b<40; b++){
									canvas.setPixel(column + a, row + b, MapPalette.RED);
								}
							}
						}
					}
				}else{
					for(int a = 0; a<40; a++){
						for(int b = 0; b<40; b++){
							canvas.setPixel(column + a, row + b, MapPalette.LIGHT_GRAY);
						}
					}
				}
				if(x==2&&z==2){
					for(int a = 19; a<=20; a++){
						for(int b = 19; b<=20; b++){
							canvas.setPixel(column + a, row + b, MapPalette.LIGHT_BROWN);
						}	
					}
				}
			}
		}
	}

	public void zoom5(MapView view, MapCanvas canvas, ChunkManager cm, Settlement set, int pchunkx, int pchunkz, World w){
		for(int x = 1; x<=5; x++){
			int column = (24*x)-22+(x-1);
			int chunkx = pchunkx+(x-3);
			for(int z = 1; z<=5; z++){
				int row = (24*z)-22+(z-1);
				int chunkz = pchunkz+(z-3);
				ClaimedChunk c = cm.getChunk(chunkx, chunkz, w);
				if(cm.isClaimed(chunkx, chunkz, w)){
					if(c.getType() == ClaimType.SAFEZONE){
						for(int a = 0; a<24; a++){
							for(int b = 0; b<24; b++){
								canvas.setPixel(column + a, row + b, MapPalette.BLUE);
							}
						}
					}else{
						if(c.getSettlement()==set){
							for(int a = 0; a<24; a++){
								for(int b = 0; b<24; b++){
									canvas.setPixel(column + a, row + b, MapPalette.LIGHT_GREEN);
								}
							}
						}else{
							for(int a = 0; a<24; a++){
								for(int b = 0; b<24; b++){
									canvas.setPixel(column + a, row + b, MapPalette.RED);
								}
							}
						}
					}
				}else{
					for(int a = 0; a<24; a++){
						for(int b = 0; b<24; b++){
							canvas.setPixel(column + a, row + b, MapPalette.LIGHT_GRAY);
						}
					}
				}
				if(x==3&&z==3){
					for(int a = 0; a<24; a++){
						for(int b = 0; b<24; b++){
							if((a==11&&b==11)||(a==12&&b==11)||(a==11&&b==12)||(a==12&&b==12)){
								canvas.setPixel(column + a, row + b, MapPalette.LIGHT_BROWN);
							}
						}	
					}
				}
			}
		}
	}

	public void zoom7(MapView view, MapCanvas canvas, ChunkManager cm, Settlement set, int pchunkx, int pchunkz, World w){
		for(int x = 1; x<=7; x++){
			int column = (16*x)-11+(x-1);
			int chunkx = pchunkx+(x-4);
			for(int z = 1; z<=7; z++){
				int row = (16*z)-11+(z-1);
				int chunkz = pchunkz+(z-4);
				ClaimedChunk c = cm.getChunk(chunkx, chunkz, w);
				if(cm.isClaimed(chunkx, chunkz, w)){
					if(c.getType() == ClaimType.SAFEZONE){
						for(int a = 0; a<16; a++){
							for(int b = 0; b<16; b++){
								canvas.setPixel(column + a, row + b, MapPalette.BLUE);
							}
						}
					}else{
						if(c.getSettlement()==set){
							for(int a = 0; a<16; a++){
								for(int b = 0; b<16; b++){
									canvas.setPixel(column + a, row + b, MapPalette.LIGHT_GREEN);
								}
							}
						}else{
							for(int a = 0; a<16; a++){
								for(int b = 0; b<16; b++){
									canvas.setPixel(column + a, row + b, MapPalette.RED);
								}
							}
						}
					}
				}else{
					for(int a = 0; a<16; a++){
						for(int b = 0; b<16; b++){
							canvas.setPixel(column + a, row + b, MapPalette.LIGHT_GRAY);
						}
					}
				}
				if(x==4&&z==4){
					for(int a = 7; a<=8; a++){
						for(int b = 7; b<=8; b++){
							canvas.setPixel(column + a, row + b, MapPalette.LIGHT_BROWN);
						}	
					}
				}
			}
		}
	}

	public void zoom9(MapView view, MapCanvas canvas, ChunkManager cm, Settlement set, int pchunkx, int pchunkz, World w){
		for(int x = 1; x<=9; x++){
			int column = (12*x)-6+(x-1);
			int chunkx = pchunkx+(x-5);
			for(int z = 1; z<=9; z++){
				int row = (12*z)-6+(z-1);
				int chunkz = pchunkz+(z-5);
				ClaimedChunk c = cm.getChunk(chunkx, chunkz, w);
				if(cm.isClaimed(chunkx, chunkz, w)){
					if(c.getType() == ClaimType.SAFEZONE){
						for(int a = 0; a<12; a++){
							for(int b = 0; b<12; b++){
								canvas.setPixel(column + a, row + b, MapPalette.BLUE);
							}
						}
					}else{
						if(c.getSettlement()==set){
							for(int a = 0; a<12; a++){
								for(int b = 0; b<12; b++){
									canvas.setPixel(column + a, row + b, MapPalette.LIGHT_GREEN);
								}
							}
						}else{
							for(int a = 0; a<12; a++){
								for(int b = 0; b<12; b++){
									canvas.setPixel(column + a, row + b, MapPalette.RED);
								}
							}
						}
					}
				}else{
					for(int a = 0; a<12; a++){
						for(int b = 0; b<12; b++){
							canvas.setPixel(column + a, row + b, MapPalette.LIGHT_GRAY);
						}
					}
				}
				if(x==5&&z==5){
					for(int a = 5; a<=6; a++){
						for(int b = 5; b<=6; b++){
							canvas.setPixel(column + a, row + b, MapPalette.LIGHT_BROWN);
						}	
					}
				}
			}
		}
	}

	public void zoom11(MapView view, MapCanvas canvas, ChunkManager cm, Settlement set, int pchunkx, int pchunkz, World w){
		for(int x = 1; x<=11; x++){
			int column = (10*x)-6+(x-1);
			int chunkx = pchunkx+(x-6);
			for(int z = 1; z<=11; z++){
				int row = (10*z)-6+(z-1);
				int chunkz = pchunkz+(z-6);
				ClaimedChunk c = cm.getChunk(chunkx, chunkz, w);
				if(cm.isClaimed(chunkx, chunkz, w)){
					if(c.getType() == ClaimType.SAFEZONE){
						for(int a = 0; a<10; a++){
							for(int b = 0; b<10; b++){
								canvas.setPixel(column + a, row + b, MapPalette.BLUE);
							}
						}
					}else{
						if(c.getSettlement()==set){
							for(int a = 0; a<10; a++){
								for(int b = 0; b<10; b++){
									canvas.setPixel(column + a, row + b, MapPalette.LIGHT_GREEN);
								}
							}
						}else{
							for(int a = 0; a<10; a++){
								for(int b = 0; b<10; b++){
									canvas.setPixel(column + a, row + b, MapPalette.RED);
								}
							}
						}
					}
				}else{
					for(int a = 0; a<10; a++){
						for(int b = 0; b<10; b++){
							canvas.setPixel(column + a, row + b, MapPalette.LIGHT_GRAY);
						}
					}
				}
				if(x==6&&z==6){
					for(int a = 4; a<=5; a++){
						for(int b = 4; b<=5; b++){
							canvas.setPixel(column + a, row + b, MapPalette.LIGHT_BROWN);
						}	
					}
				}
			}
		}
	}

	public void zoom13(MapView view, MapCanvas canvas, ChunkManager cm, Settlement set, int pchunkx, int pchunkz, World w){
		for(int x = 1; x<=13; x++){
			int column = (8*x)-2+(x-1);
			int chunkx = pchunkx+(x-7);
			for(int z = 1; z<=13; z++){
				int row = (8*z)-2+(z-1);
				int chunkz = pchunkz+(z-7);
				ClaimedChunk c = cm.getChunk(chunkx, chunkz, w);
				if(cm.isClaimed(chunkx, chunkz, w)){
					if(c.getType() == ClaimType.SAFEZONE){
						for(int a = 0; a<8; a++){
							for(int b = 0; b<8; b++){
								canvas.setPixel(column + a, row + b, MapPalette.BLUE);
							}
						}
					}else{
						if(c.getSettlement()==set){
							for(int a = 0; a<8; a++){
								for(int b = 0; b<8; b++){
									canvas.setPixel(column + a, row + b, MapPalette.LIGHT_GREEN);
								}
							}
						}else{
							for(int a = 0; a<8; a++){
								for(int b = 0; b<8; b++){
									canvas.setPixel(column + a, row + b, MapPalette.RED);
								}
							}
						}
					}
				}else{
					for(int a = 0; a<8; a++){
						for(int b = 0; b<8; b++){
							canvas.setPixel(column + a, row + b, MapPalette.LIGHT_GRAY);
						}
					}
				}
				if(x==7&&z==7){
					for(int a = 3; a<=4; a++){
						for(int b = 3; b<=4; b++){
							canvas.setPixel(column + a, row + b, MapPalette.LIGHT_BROWN);
						}	
					}
				}
			}
		}
	}

	public void zoom17(MapView view, MapCanvas canvas, ChunkManager cm, Settlement set, int pchunkx, int pchunkz, World w){
		for(int x = 1; x<=17; x++){
			int column = (6*x)-1+(x-1);
			int chunkx = pchunkx+(x-9);
			for(int z = 1; z<=17; z++){
				int row = (6*z)-1+(z-1);
				int chunkz = pchunkz+(z-9);
				ClaimedChunk c = cm.getChunk(chunkx, chunkz, w);
				if(cm.isClaimed(chunkx, chunkz, w)){
					if(c.getType() == ClaimType.SAFEZONE){
						for(int a = 0; a<6; a++){
							for(int b = 0; b<6; b++){
								canvas.setPixel(column + a, row + b, MapPalette.BLUE);
							}
						}
					}else{
						if(c.getSettlement()==set){
							for(int a = 0; a<6; a++){
								for(int b = 0; b<6; b++){
									canvas.setPixel(column + a, row + b, MapPalette.LIGHT_GREEN);
								}
							}
						}else{
							for(int a = 0; a<6; a++){
								for(int b = 0; b<6; b++){
									canvas.setPixel(column + a, row + b, MapPalette.RED);
								}
							}
						}
					}
				}else{
					for(int a = 0; a<6; a++){
						for(int b = 0; b<6; b++){
							canvas.setPixel(column + a, row + b, MapPalette.LIGHT_GRAY);
						}
					}
				}
				if(x==9&&z==9){
					for(int a = 2; a<=3; a++){
						for(int b = 2; b<=3; b++){
							canvas.setPixel(column + a, row + b, MapPalette.LIGHT_BROWN);
						}	
					}
				}
			}
		}
	}

	public void zoom25(MapView view, MapCanvas canvas, ChunkManager cm, Settlement set, int pchunkx, int pchunkz, World w){
		for(int x = 1; x<=25; x++){
			int column = (4*x)-2+(x-1);
			int chunkx = pchunkx+(x-13);
			for(int z = 1; z<=25; z++){
				int row = (4*z)-2+(z-1);
				int chunkz = pchunkz+(z-13);
				ClaimedChunk c = cm.getChunk(chunkx, chunkz, w);
				if(cm.isClaimed(chunkx, chunkz, w)){
					if(c.getType() == ClaimType.SAFEZONE){
						for(int a = 0; a<4; a++){
							for(int b = 0; b<4; b++){
								canvas.setPixel(column + a, row + b, MapPalette.BLUE);
							}
						}
					}else{
						if(c.getSettlement()==set){
							for(int a = 0; a<4; a++){
								for(int b = 0; b<4; b++){
									canvas.setPixel(column + a, row + b, MapPalette.LIGHT_GREEN);
								}
							}
						}else{
							for(int a = 0; a<4; a++){
								for(int b = 0; b<4; b++){
									canvas.setPixel(column + a, row + b, MapPalette.RED);
								}
							}
						}
					}
				}else{
					for(int a = 0; a<4; a++){
						for(int b = 0; b<4; b++){
							canvas.setPixel(column + a, row + b, MapPalette.LIGHT_GRAY);
						}
					}
				}
				if(x==13&&z==13){
					for(int a = 1; a<=2; a++){
						for(int b = 1; b<=2; b++){
							canvas.setPixel(column + a, row + b, MapPalette.LIGHT_BROWN);
						}	
					}
				}
			}
		}
	}

	public void zoom41(MapView view, MapCanvas canvas, ChunkManager cm, Settlement set, int pchunkx, int pchunkz, World w){
		for(int x = 1; x<=41; x++){
			int column = 1+(2*x)+(x-1);
			int chunkx = pchunkx+(x-21);
			for(int z = 1; z<=41; z++){
				int row = 1+(2*z)+(z-1);
				int chunkz = pchunkz+(z-21);
				ClaimedChunk c = cm.getChunk(chunkx, chunkz, w);
				if(cm.isClaimed(chunkx, chunkz, w)){
					if(c.getType() == ClaimType.SAFEZONE){
						for(int a = 0; a<2; a++){
							for(int b = 0; b<2; b++){
								canvas.setPixel(column + a, row + b, MapPalette.BLUE);
							}
						}
					}else{
						if(c.getSettlement()==set){
							for(int a = 0; a<2; a++){
								for(int b = 0; b<2; b++){
									canvas.setPixel(column + a, row + b, MapPalette.LIGHT_GREEN);
								}
							}
						}else{
							for(int a = 0; a<2; a++){
								for(int b = 0; b<2; b++){
									canvas.setPixel(column + a, row + b, MapPalette.RED);
								}
							}
						}
					}
				}else{
					for(int a = 0; a<2; a++){
						for(int b = 0; b<2; b++){
							canvas.setPixel(column + a, row + b, MapPalette.LIGHT_GRAY);
						}
					}
				}
				if(x==21&&z==21){
					for(int a = 0; a<=1; a++){
						for(int b = 0; b<=1; b++){
							canvas.setPixel(column + a, row + b, MapPalette.LIGHT_BROWN);
						}	
					}
				}
			}
		}
	}

	public void zoom125(MapView view, MapCanvas canvas, ChunkManager cm, Settlement set, int pchunkx, int pchunkz, World w){
		for(int x = 1; x<=63; x++){
			int column = x+(x-1);
			int chunkx = pchunkx+(x-32);
			for(int z = 1; z<=63; z++){
				int row = z+(z-1);;
				int chunkz = pchunkz+(z-32);
				ClaimedChunk c = cm.getChunk(chunkx, chunkz, w);
				if(cm.isClaimed(chunkx, chunkz, w)){
					if(c.getType() == ClaimType.SAFEZONE){
						canvas.setPixel(column, row, MapPalette.BLUE);
					}else{
						if(c.getSettlement()==set){
							canvas.setPixel(column, row, MapPalette.LIGHT_GREEN);
						}else{
							canvas.setPixel(column, row, MapPalette.RED);
						}
					}
				}else{
					canvas.setPixel(column, row, MapPalette.LIGHT_GRAY);
				}
				if(x==32&&z==32){
					for(int a = 0; a<=0; a++){
						for(int b = 0; b<=0; b++){
							canvas.setPixel(column + a, row + b, MapPalette.LIGHT_BROWN);
						}	
					}
				}
			}
		}
	}
}
