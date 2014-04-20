package me.projectx.Settlements.Scoreboard;

import me.projectx.Settlements.Models.Settlement;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class NameBoard {

	public static NameBoard instance;
	ScoreboardManager manager = Bukkit.getScoreboardManager();
	Scoreboard board = manager.getNewScoreboard();

	public NameBoard(){
		instance = this;
	}

	public static NameBoard getInstance(){
		return instance;
	}

	public Team requestTeam(Settlement set){
		Team team = board.registerNewTeam(set.getId() + "");
		team.setPrefix(ChatColor.WHITE + "[" + set.getTag() + "]");
		return team;
	}

	public void changeTag(Settlement set){
		set.getTeam().setPrefix(ChatColor.WHITE + "[" + set.getTag() + "]");
	}

}
