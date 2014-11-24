package me.projectx.settlements.managers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.projectx.settlements.enums.MessageType;
import me.projectx.settlements.models.ClaimedChunk;
import me.projectx.settlements.models.Settlement;
import me.projectx.settlements.runtime.SettlementRuntime;
import me.projectx.settlements.utils.DatabaseUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class SettlementManager {

	public List<Settlement> settlements = new ArrayList<Settlement>();
	private Map<UUID, String> invitedPlayers = new HashMap<UUID, String>();
	private Map<Long, Long> allyInvites = new HashMap<Long, Long>();
	private static SettlementManager sm = new SettlementManager();
	private PreparedStatement selectnotdeleted, getcitizens, gethomes, createset, setdiscription, setdelete,
	deletesettlement, deletechunks, deletecitizens, deletehomes,
	removecitizen, addcitizen, setleader,
	addally, removeally,
	setrank,
	deletehome, sethome;

	private SettlementManager(){
		try{
			selectnotdeleted = DatabaseUtils.getConnection().prepareStatement("SELECT * FROM settlements WHERE deleted=false;");
			getcitizens = DatabaseUtils.getConnection().prepareStatement("SELECT * FROM citizens WHERE settlement=?;");
			gethomes = DatabaseUtils.getConnection().prepareStatement("SELECT * FROM sethomes WHERE id=?;");
			createset = DatabaseUtils.getConnection().prepareStatement("INSERT INTO settlements (id, name, leader)" + "VALUES (?,?,?);");
			setdiscription = DatabaseUtils.getConnection().prepareStatement("UPDATE settlements SET description=? WHERE id=?;");
			setdelete = DatabaseUtils.getConnection().prepareStatement("UPDATE settlements SET deleted=true WHERE id=?;");
			deletesettlement = DatabaseUtils.getConnection().prepareStatement("DELETE FROM settlements WHERE id=?;");
			deletechunks = DatabaseUtils.getConnection().prepareStatement("DELETE FROM chunks WHERE id=?;");
			deletecitizens = DatabaseUtils.getConnection().prepareStatement("DELETE FROM citizens WHERE id=?;");
			deletehomes = DatabaseUtils.getConnection().prepareStatement("DELETE FROM sethomes WHERE id=?;");
			removecitizen = DatabaseUtils.getConnection().prepareStatement("DELETE FROM citizens WHERE uuid=?;");
			addcitizen = DatabaseUtils.getConnection().prepareStatement("INSERT INTO citizens(uuid, settlement, rank) VALUES (?,?,?);");
			setleader = DatabaseUtils.getConnection().prepareStatement("UPDATE settlements SET leader=? WHERE name=?;");
			addally = DatabaseUtils.getConnection().prepareStatement("INSERT INTO alliances(main, ally) VALUES(?, ?);");
			removeally = DatabaseUtils.getConnection().prepareStatement("DELTE FROM alliances WHERE main=?, ally=?;");
			setrank = DatabaseUtils.getConnection().prepareStatement("UPDATE citizens SET rank=? WHERE uuid=?;");
			deletehome = DatabaseUtils.getConnection().prepareStatement("DELETE FROM sethomes WHERE id=?;");
			sethome = DatabaseUtils.getConnection().prepareStatement("INSERT INTO sethomes(id, world, x, y, z, yaw, pitch) VALUES(?,?,?,?,?,?,?);");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Get an instance of the SettlementManager class
	 *
	 * @return SettlementManager class instance
	 */
	public static SettlementManager getManager() {
		return sm;
	}

	/**
	 * Load settlements (Call onEnable)
	 *
	 * @return Returns true if successful, false otherwise
	 * @throws SQLException
	 */
	public void loadSettlmentsFromDB() throws SQLException {
		new Thread() {
			@Override
			public void run() { 
				try {
					//Load the Settlement
					ResultSet result = DatabaseUtils.queryIn(selectnotdeleted);
					while (result.next()) {
						String name = result.getString("name");
						Settlement set = new Settlement(name);
						set.setId(result.getLong("id"));
						set.setLeader(result.getString(("leader")));
						set.setDescription(result.getString("description"));
						set.setBalance(result.getDouble("balance"));
						
						//Load the Settlement members
						getcitizens.setString(1, set.getName());
						ResultSet citizens = DatabaseUtils.queryIn(getcitizens);
						while (citizens.next()) {
							UUID uuid = UUID.fromString(citizens.getString("uuid"));
							String rank = citizens.getString("rank");

							/* Rank 1 = Citizen || Rank 2 = Officer || Rank 3 = Leader */
							if (rank.equalsIgnoreCase("1")){
								set.getCitizens().add(uuid);
							}else if (rank.equalsIgnoreCase("2")) {
								set.getOfficers().add(uuid);
							}
							SettlementRuntime.getRuntime().sortMembers(set);
						}
						
						//Load the Settlement's home
						gethomes.setLong(1, set.getId());
						ResultSet homes = DatabaseUtils.queryIn(gethomes);
						while (homes.next()){
							set.setHome(new Location(Bukkit.getWorld(homes.getString("world")), homes.getDouble("x"), homes.getDouble("y"),
									homes.getDouble("z"), homes.getFloat("yaw"), homes.getFloat("pitch")));
						}
						
						//Load the Settlement's alliances
						gethomes.setLong(1, set.getId());
						ResultSet alliances = DatabaseUtils.queryIn(gethomes);
						while (alliances.next()){
							//ResultSet load = DatabaseUtils.queryIn("SELECT delete FROM settlements WHERE id=" + set.getId() + ";");//TODO
							set.getAllies().add(alliances.getLong("ally"));
						}
						settlements.add(set);
					}
					SettlementRuntime.getRuntime().sortSettlements();
					this.interrupt();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * Get the settlement of a player
	 *
	 * @param uuid: The UUID of the player to get the settlement for.
	 *<p>
	 *<b> UUID-based lookup is faster than name-based lookup! </b>
	 * @return The player's settlement. If they are not a member of a
	 *         settlement, this will return null
	 */
	public Settlement getPlayerSettlement(UUID uuid) {
		for (Settlement s : settlements) {
			if (s.hasMember(uuid)) {
				return s;
			}
		}
		return null;
	}

	/**
	 * Get the settlement of a player
	 *
	 * @param name: The name of the player to get the settlement for
	 * <p>
	 * <b> This is slower than UUID-based lookup! </b>
	 * @return The player's settlement. If they are not a member of a
	 *         settlement, this will return null
	 */
	public Settlement getPlayerSettlement(String name) {
		UUID id = Bukkit.getPlayer(name).getUniqueId();
		for (Settlement s : settlements) {
			if (s.hasMember(id)) {
				return s;
			}
		}
		return null;
	}

	/**
	 * Determine if a settlement exists
	 *
	 * @param name: The name of the settlement to check
	 * @return True if the settlement exists
	 */
	public boolean settlementExists(String name) {
		return settlements.contains(name);
	}

	/**
	 * Get a settlement by name
	 *
	 * @param name: The name of the settlement to get
	 * @return The designated settlement. Returns null if it doesn't exist
	 */
	public Settlement getSettlement(String name) {
		for (Settlement s : settlements) {
			if (s.getName().equalsIgnoreCase(name)) {
				return s;
			}
		}
		return null;
	}

	/**
	 * Get a settlement by id
	 *
	 * @param id: The id of the settlement to get
	 * @return The designated settlement. Returns null if it doesn't exist
	 */
	public Settlement getSettlement(long id) {
		for (Settlement s : settlements) {
			if (s.getId() == id) {
				return s;
			}
		}
		return null;
	}

	/**
	 * Create a new settlement. The settlement will only get created if the
	 * settlement doesn't already exist and if the sender isn't a member of a
	 * different settlement
	 *
	 * @param name: The name of the new settlement
	 * @param sender: Who issued the creation of the settlement
	 * @throws SQLException
	 */
	public void createSettlement(String name, CommandSender sender) throws SQLException {
		if (!settlementExists(name)) {
			Player p = (Player) sender;
			if (getPlayerSettlement(p.getUniqueId()) == null) {
				final Settlement s = new Settlement(name);
				s.setLeader(p.getUniqueId());
				settlements.add(s);
				List<ClaimedChunk> cc = new ArrayList<ClaimedChunk>();
				ChunkManager.getManager().setClaims.put(name, cc);
				createset.setLong(1, s.getId());
				createset.setString(2, s.getName());
				createset.setString(3, s.getLeader().toString());
				DatabaseUtils.queryOut(createset);
				String desc = "Default Settlement Description";
				s.setDescription(desc);
				setdiscription.setString(1, desc);
				setdiscription.setLong(2, s.getId());
				DatabaseUtils.queryOut(setdiscription);
				sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "Successfully created " + ChatColor.AQUA + s.getName());
				sender.sendMessage(ChatColor.GRAY + "You can now set a description by doing " + ChatColor.AQUA + "/s desc <description>");
				sender.sendMessage(ChatColor.GRAY + "For more things you can do, type " + ChatColor.AQUA + "/s");
				SettlementRuntime.getRuntime().sortSettlements();
			} else {
				sender.sendMessage(MessageType.CREATE_IN_SETTLEMENT.getMsg());
			}
		} else {
			sender.sendMessage(MessageType.SETTLEMENT_EXISTS.getMsg());
		}
	}

	/**
	 * Delete a settlement. Only works if the settlement exists and the sender
	 * is the leader of the settlement
	 *
	 * @param name: The name of the settlement to delete
	 * @param sender: Who issued the deletion of the settlement
	 * @throws SQLException
	 */
	public void deleteSettlement(final CommandSender sender) throws SQLException {
		Player p = (Player) sender;
		final UUID id = p.getUniqueId();
		final Settlement s = getPlayerSettlement(id);
		if (s != null) {
			if (s.isLeader(id)) {
				if (s.delete()){
					new Thread() {
						@Override
						public void run() {
							try {
								setdelete.setLong(1, s.getId());
								DatabaseUtils.queryOut(setdelete);
								settlements.remove(s);
								SettlementRuntime.getRuntime().sortSettlements();

								sender.sendMessage(MessageType.PREFIX.getMsg()
										+ ChatColor.GRAY + "Successfully deleted "
										+ ChatColor.AQUA + s.getName());
								this.interrupt();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					}.start();
				}else{
					sender.sendMessage(MessageType.SETTLEMENT_DELETE_CONFIRM.getMsg());
					s.setToDelete(true);
				}
			} else {
				sender.sendMessage(MessageType.DELETE_NOT_LEADER.getMsg());
			}
		} else {
			sender.sendMessage(MessageType.NOT_IN_SETTLEMENT.getMsg());
		}
	}

	/**
	 * Delete a settlement. Will work for any settlement, even if the sender
	 * isn't the leader
	 *
	 * @param name: The name of the settlement to delete
	 * @param sender: Who issued the deletion of the settlement
	 * @throws SQLException
	 */
	public void deleteSettlement(final CommandSender sender, final String name) throws SQLException {
		final Settlement s = getSettlement(name);
		if (s != null) {
			new Thread() {
				@Override
				public void run() {
					try {
						deletesettlement.setLong(1, s.getId());
						DatabaseUtils.queryOut(deletesettlement);
						deletechunks.setLong(1, s.getId());
						DatabaseUtils.queryOut(deletechunks);
						deletecitizens.setLong(1, s.getId());
						DatabaseUtils.queryOut(deletecitizens);
						deletehomes.setLong(1, s.getId());
						DatabaseUtils.queryOut(deletehomes);

						List<ClaimedChunk> cc = ChunkManager.getManager().getClaims(s);
						if (cc != null){
							for (ClaimedChunk c : cc){
								ChunkManager.getManager().claimedChunks.remove(c);
								ChunkManager.getManager().setClaims.remove(s.getName());
								c = null; //gc collection?
							}
						}

						if (invitedPlayers.containsValue(s)) {
							invitedPlayers.remove(s);
						}

						settlements.remove(s);
						SettlementRuntime.getRuntime().sortSettlements();

						sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "Successfully deleted "
								+ ChatColor.AQUA + s.getName());
						this.interrupt();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}.start();
		} else {
			sender.sendMessage(MessageType.SETTLEMENT_NOT_EXIST.getMsg());
		}
	}

	/**
	 * Invite a player to join the Settlement. The command sender must be an
	 * Officer or higher in the Settlement
	 *
	 * @param invite: The player to invite
	 * @param sender: Who issued the invite
	 */
	public void inviteCitizen(Player invite, CommandSender sender) {
		if (!invitedPlayers.containsKey(invite.getUniqueId())) {
			Settlement s = getPlayerSettlement(sender.getName());
			if (s != null) {
				if (!s.hasMember(invite.getUniqueId())) {
					Player send = (Player) sender;
					if (s.isOfficer(send.getUniqueId()) || s.isLeader(send.getUniqueId())) {
						invitedPlayers.put(invite.getUniqueId(), s.getName());
						s.sendSettlementMessage(MessageType.PREFIX.getMsg() + send.getName() + ChatColor.GRAY
								+ " invited " + ChatColor.AQUA + invite + ChatColor.GRAY + " to your Settlement");
						invite.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.AQUA + sender.getName()
								+ ChatColor.GRAY + " invited you to join " + ChatColor.AQUA + s.getName() + ". "
								+ ChatColor.GRAY + "Type " + ChatColor.AQUA + "/s accept " + ChatColor.GRAY + " to accept or "
								+ ChatColor.AQUA +  "/s decline" + ChatColor.GRAY + " to decline");
					} else {
						sender.sendMessage(MessageType.INVITE_NOT_RANK.getMsg());
					}
				} else {
					sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.AQUA + invite + ChatColor.GRAY + " is already in your Settlement!");
				}
			} else {
				sender.sendMessage(MessageType.NOT_IN_SETTLEMENT.getMsg());
			}
		}
	}

	/**
	 * Accept an invite to a Settlement. Will only work if the player has a
	 * pending invite
	 *
	 * @param player: The player who is accepting the invite
	 * @throws SQLException
	 */
	public void acceptInvite(final String player) throws SQLException {
		Player p = Bukkit.getPlayer(player);
		UUID id = p.getUniqueId();
		if (hasInvite(p)) {
			if (getPlayerSettlement(id) == null) {
				final Settlement s = getSettlement(invitedPlayers.get(id));
				s.giveCitizenship(id);
				invitedPlayers.remove(p.getUniqueId());
				removecitizen.setString(1, id.toString());
				DatabaseUtils.queryOut(removecitizen);
				addcitizen.setString(1, id.toString());
				addcitizen.setString(2, s.getName());
				addcitizen.setInt(3, 1);
				DatabaseUtils.queryOut(addcitizen);
				p.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "Successfully joined " + ChatColor.AQUA
						+ getPlayerSettlement(id).getName());
				s.sendSettlementMessage(MessageType.PREFIX.getMsg() + ChatColor.AQUA + player + ChatColor.GRAY + " joined the Settlement!");
				SettlementRuntime.getRuntime().sortMembers(s);
				s.setPower(s.getPower() + 1);
			} else {
				p.sendMessage(MessageType.CURRENTLY_IN_SETTLEMENT.getMsg());
			}
		} else {
			p.sendMessage(MessageType.NO_INVITE.getMsg());
		}
	}

	/**
	 * Determine if a player has a pending invite
	 *
	 * @param player: The player to check
	 * @return True if the player has an invite
	 */
	public boolean hasInvite(Player player) {
		return invitedPlayers.containsKey(player.getUniqueId());
	}

	/**
	 * Decline an invite to a Settlement. Player must have a valid invite
	 *
	 * @param player: The player who is declining the invite
	 */
	public void declineInvite(String player) {
		Player p = Bukkit.getPlayer(player);
		if (hasInvite(p)) {
			p.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "You declined an invite to join " + ChatColor.AQUA
					+ invitedPlayers.get(player));
			invitedPlayers.remove(p.getUniqueId());
		} else {
			p.sendMessage(MessageType.NO_INVITE.getMsg());
		}
	}

	/**
	 * Remove a player from a Settlement
	 *
	 * @param name: The player to remove from the Settlement
	 * @throws SQLException
	 */
	public void leaveSettlement(String name) throws SQLException {
		final Settlement s = getPlayerSettlement(name);
		Player p = Bukkit.getPlayer(name);
		UUID id = p.getUniqueId();
		if (s != null) {
			if (!s.isLeader(p.getUniqueId())) {
				p.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "Successfully left " + ChatColor.AQUA + s.getName());
				s.sendSettlementMessage(MessageType.PREFIX.getMsg() + ChatColor.AQUA + name + ChatColor.GRAY + " left the Settlement :(");
				s.revokeCitizenship(p.getUniqueId());
				removecitizen.setString(1, id.toString());
				DatabaseUtils.queryOut(removecitizen);
				if (s.getQueuedLeader() != null){
					s.setLeader(s.getQueuedLeader());
					setleader.setString(1, s.getQueuedLeader().toString());
					setleader.setString(2, s.getName());
					DatabaseUtils.queryOut(setleader);
					s.setQueuedLeader(null);
				}
				SettlementRuntime.getRuntime().sortMembers(s);
			}else{
				if (s.getQueuedLeader() == null){
					p.sendMessage(MessageType.MUST_APPOINT_NEW_LEADER.getMsg());
				}else{
					leaveSettlement(name);
				}
			}
		}else{
			p.sendMessage(MessageType.NOT_IN_SETTLEMENT.getMsg());
		}
	}

	/**
	 * Kick a player from a Settlement
	 *
	 * @param sender: The sender who issued the command
	 * @param name: The name of the player to kick
	 * @throws SQLException
	 */
	public void kickPlayer(CommandSender sender, String name) throws SQLException {
		final Settlement s = getPlayerSettlement(sender.getName());
		if (s != null) {
			OfflinePlayer p = Bukkit.getOfflinePlayer(name);
			UUID id = p.getUniqueId();
			if (s.hasMember(p.getUniqueId())) {
				if (!s.isLeader(p.getUniqueId())) {
					s.revokeCitizenship(p.getUniqueId());
					if (p.isOnline()) {
						((CommandSender) p).sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "You have been kicked from " + ChatColor.AQUA + s.getName());
					}
					s.sendSettlementMessage(MessageType.PREFIX.getMsg() + ChatColor.AQUA + sender.getName() + ChatColor.GRAY
							+ " kicked " + ChatColor.AQUA + name + ChatColor.GRAY + " from the Settlement!");
					removecitizen.setString(1, id.toString());
					DatabaseUtils.queryOut(removecitizen);
					SettlementRuntime.getRuntime().sortMembers(s);
				} else {
					sender.sendMessage(MessageType.KICK_NOT_LEADER.getMsg());
				}
			} else {
				sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.DARK_RED + "That player is not in your Settlement!");
			}
		} else {
			sender.sendMessage(MessageType.NOT_IN_SETTLEMENT.getMsg());
		}
	}

	/**
	 * Set the description for a Settlement
	 *
	 * @param sender: Who issued the command
	 * @param desc: The description for the Settlement
	 * @throws SQLException
	 */
	public void setDescription(CommandSender sender, final String desc) throws SQLException {
		final Settlement s = getPlayerSettlement(sender.getName());
		if (s != null) {
			Player p = (Player) sender;
			if (s.isOfficer(p.getUniqueId()) || s.isLeader(p.getUniqueId())) {
				s.setDescription(desc);
				sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "Set your Settlement's description to " + ChatColor.AQUA + desc);
				setdiscription.setString(1, desc);
				setdiscription.setLong(2, s.getId());
				DatabaseUtils.queryOut(setdiscription);
			} else {
				sender.sendMessage(MessageType.DESCRIPTION_NOT_RANK.getMsg());
			}
		} else {
			sender.sendMessage(MessageType.NOT_IN_SETTLEMENT.getMsg());
		}
	}

	/**
	 * Ally a Settlement
	 *
	 * @param s1: The Settlement that is issuing the request
	 * @param s2: The name of the Settlement that will be added to s1's allies
	 * @return True if successful
	 */
	public void allySettlement(Player sender, String s2) {
		Settlement s1 = getPlayerSettlement(sender.getUniqueId());
		Settlement s = getSettlement(s2);
		if (s != null) {
			if (s1 != null){
				//if (allyInvites.containsKey(s.getId()) && allyInvites.get(s.getId()).equals(s1.getId())){
				s1.addAlly(s);
				s1.sendSettlementMessage(MessageType.PREFIX.getMsg() + ChatColor.AQUA + s2 + ChatColor.GRAY
						+ " has been added to your Settlement's alliance!");
				s.sendSettlementMessage(MessageType.PREFIX.getMsg() + ChatColor.AQUA + s.getName() + ChatColor.GRAY
						+ " has been added to your Settlement's alliance!");
				allyInvites.remove(s.getId());
				try {
					addally.setLong(1, s1.getId());
					addally.setLong(2, s.getId());
					DatabaseUtils.queryOut(addally);
					addally.setLong(1, s.getId());
					addally.setLong(2, s1.getId());
					DatabaseUtils.queryOut(addally);
				} catch(SQLException e) {
					e.printStackTrace();
				}
				/*}else{
					sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.RED + "You don't have any pending alliance requests");
				}*/
			}else{
				sender.sendMessage(MessageType.NOT_IN_SETTLEMENT.getMsg());
			}
		}else{
			sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.RED + "That Settlement could not be found!");
		}
	}

	/**
	 * Remove a Settlement's ally
	 *
	 * @param s1: The Settlement issuing the request
	 * @param s2: The name of the Settlement that will be removed
	 * @return True if successful
	 */
	public void removeAlly(Settlement s1, String s2) {
		Settlement s = getSettlement(s2);
		if (s1.hasAlly(s)) {
			s1.removeAlly(s);
			try {
				removeally.setLong(1, s1.getId());
				removeally.setLong(2, s.getId());
				DatabaseUtils.queryOut(removeally);
				removeally.setLong(1, s.getId());
				removeally.setLong(2, s1.getId());
				DatabaseUtils.queryOut(removeally);
			} catch(SQLException e) {
				e.printStackTrace();
			}
			s1.sendSettlementMessage(MessageType.PREFIX.getMsg() + ChatColor.AQUA + s2 + ChatColor.GRAY
					+ " has been removed from your Settlement's alliance!");
			s.sendSettlementMessage(MessageType.PREFIX.getMsg() + ChatColor.AQUA + s.getName() + ChatColor.GRAY
					+ " has been removed from your Settlement's alliance!");
		}
	}

	/**
	 * Display the members of a Settlement in a GUI
	 *
	 * @param player: The player who issued the command & will view the GUI
	 * @param settlement: The Settlement who's members will be listed
	 */
	public void displayMembers(final Player player, String settlement) {
		final Settlement s = getSettlement(settlement);
		if (s != null) {
			Inventory inv = Bukkit.createInventory(null, getInventorySize(s.memberSize()), ChatColor.BLUE + "Members of " + s.getName());
			ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			SkullMeta sm = (SkullMeta) is.getItemMeta();

			for (int i = 0; i < s.memberSize(); i++) {
				Player mem = Bukkit.getPlayer(s.getPlayer(i));
				if (mem != null){
					sm.setOwner(mem.getName());
					sm.setDisplayName(mem.getDisplayName());
					sm.setLore(Arrays.asList(ChatColor.GREEN + "Rank: " + ChatColor.RED + s.getRank(mem),
							ChatColor.GRAY + "Status: " + ChatColor.AQUA + "Online"));
					is.setItemMeta(sm);
					inv.setItem(i, is);
				}else{
					OfflinePlayer omem = Bukkit.getOfflinePlayer(s.getPlayer(i));
					sm.setOwner(omem.getName());
					sm.setDisplayName(omem.getName());
					sm.setLore(Arrays.asList(ChatColor.GREEN + "Rank: " + ChatColor.RED + s.getRank(omem),
							ChatColor.GRAY + "Status: " + ChatColor.AQUA + "Offline"));
					is.setItemMeta(sm);
					inv.setItem(i, is);
				}
			}
			player.openInventory(inv);
		} else {
			player.sendMessage(MessageType.SETTLEMENT_NOT_EXIST.getMsg());
		}
	}

	/**
	 * Get the appropriate size of an inventory based on multiples of 9.
	 * <p>
	 * This ensures than no matter what the number is, a GUI's size will always
	 * be set to a multiple of 9.
	 *
	 * @param max: The maximum size of the inventory
	 * @return The size of the inventory, based on a multiple of 9
	 */
	public static int getInventorySize(int max) {
		if (max <= 0) {
			return 9;
		}
		int quotient = (int) Math.ceil(max / 9.0);
		return quotient > 5 ? 54 : quotient * 9;
	}

	/**
	 * List all of the existing Settlements in a GUI
	 *
	 * @param player: The player who issued the command and will view the GUI
	 */
	public void listSettlements(Player player) {
		Inventory inv = Bukkit.createInventory(null, getInventorySize(settlements.size()), ChatColor.BLUE + "All Current Settlements:");
		ItemStack is = new ItemStack(Material.DIAMOND);
		ItemMeta im = is.getItemMeta();

		for (int i = 0; i < settlements.size(); i++) {
			Settlement s = settlements.get(i);
			im.setDisplayName(ChatColor.AQUA + s.getName());
			im.setLore(Arrays.asList(ChatColor.DARK_AQUA + s.getDescription(), ChatColor.GOLD + "Owner: " + ChatColor.GREEN + Bukkit.getOfflinePlayer(s.getLeader()).getName(),
					ChatColor.DARK_GREEN + "Members: " + ChatColor.RED + s.memberSize(), ChatColor.LIGHT_PURPLE + "Power: " + ChatColor.BLUE + s.getPower(),
					ChatColor.GREEN + "Money: " + ChatColor.GOLD + "$" + s.getBalance()));
			is.setItemMeta(im);
			inv.setItem(i, is);
		}
		player.openInventory(inv);
	}

	/**
	 * Display all if the player's allied Settlements in a GUI
	 *
	 * @param player : The player who issued the command and will view the GUI
	 */
	public void listAllies(Player player){
		Settlement s = getPlayerSettlement(player.getUniqueId());
		int allySize = s.getAllies().size();
		Inventory inv = Bukkit.createInventory(null, getInventorySize(allySize), ChatColor.DARK_PURPLE + "Your Settlement's Allies");
		ItemStack is = new ItemStack(Material.INK_SACK, 1, (short)13);
		ItemMeta im = is.getItemMeta();

		for (int i = 0; i < allySize; i++){
			Settlement ally = settlements.get(i);
			im.setDisplayName(ChatColor.LIGHT_PURPLE + ally.getName());
			im.setLore(Arrays.asList(ChatColor.DARK_AQUA + ally.getDescription(), ChatColor.GOLD + "Owner: " + ChatColor.GREEN + Bukkit.getOfflinePlayer(ally.getLeader()).getName(),
					ChatColor.DARK_GREEN + "Members: " + ChatColor.RED + ally.memberSize(), ChatColor.LIGHT_PURPLE + "Power: " + ChatColor.BLUE + ally.getPower(),
					ChatColor.GREEN + "Money: " + ChatColor.GOLD + "$" + ally.getBalance()));
			is.setItemMeta(im);
			inv.setItem(i, is);
		}
		player.openInventory(inv);
	}

	public void promotePlayer(Player sender, OfflinePlayer player) { // untested
		UUID id = player.getUniqueId();
		Settlement s = getPlayerSettlement(id);
		if (s != null) {
			if (!s.isOfficer(id)){
				//s.getCitizens().remove(id);
				s.setOfficer(id);
				s.sendSettlementMessage(MessageType.PREFIX.getMsg() + ChatColor.AQUA + player.getName() +
						ChatColor.GRAY + " has been promoted to " + ChatColor.BLUE + "Officer");
				try {
					setrank.setInt(1, 2);
					setrank.setString(2, id.toString());
					DatabaseUtils.queryOut(setrank);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}else{
				sender.sendMessage(MessageType.PREFIX.getMsg() + player.getName() + ChatColor.GRAY + " is already an Officer!");
			}
		}else{
			sender.sendMessage(MessageType.SETTLEMENT_NO_MEMBER.getMsg());
		}
	}

	public void setHome(Player player){
		UUID id = player.getUniqueId();
		Settlement s = getPlayerSettlement(id);
		if (s != null){
			if (s.isLeader(id) || s.isOfficer(id)){
				Location loc = player.getLocation();
				s.sendSettlementMessage("New home location set!"); //temp msg
				try{
					if (s.hasHome()){
						deletehome.setLong(1, s.getId());
						DatabaseUtils.queryOut(deletehome); //shorter to simply DELETE than UPDATE
					}
					sethome.setLong(1, s.getId());
					sethome.setString(2, loc.getWorld().getName());
					sethome.setDouble(3, loc.getX());
					sethome.setDouble(4, loc.getY());
					sethome.setDouble(5, loc.getZ());
					sethome.setFloat(6, loc.getYaw());
					sethome.setFloat(7, loc.getPitch());
					DatabaseUtils.queryOut(sethome);
					s.setHome(loc);
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
	}

	public void teleportToHome(Player player){
		Settlement s = getPlayerSettlement(player.getUniqueId());
		if (s != null){
			if (s.hasHome()){
				player.teleport(s.getHome());
				player.sendMessage(MessageType.SETTLEMENT_HOME_TP.getMsg());
			}else{
				player.sendMessage(MessageType.SETTLEMENT_NO_HOME.getMsg());
			}
		}else{
			player.sendMessage(MessageType.NOT_IN_SETTLEMENT.getMsg());
		}
	}

	/*public void sendAllianceInvite(Player sender, Settlement invited){
		Settlement s = getPlayerSettlement(sender.getUniqueId());
		if (s.isOfficer(sender.getUniqueId()) || s.isLeader(sender.getUniqueId())){
			if (!allyInvites.containsKey(invited)){
				allyInvites.put(invited.getId(), s.getId());
				sender.sendMessage(MessageType.ALLIANCE_INVITE_SENT.getMsg().replace("<settlement>", invited.getName()));
			}else{
				sender.sendMessage(MessageType.ALLIANCE_INVITE_PENDING.getMsg().replace("<settlement>", invited.getName()));
			}
		}else{
			sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.DARK_RED + "You don't have the required rank to do that!");
		}
	}
	// invited, inviter
	public void declineAllianceInvite(Player sender, Settlement inviter){
		Settlement s = getPlayerSettlement(sender.getUniqueId());
		if (s.isOfficer(sender.getUniqueId()) || s.isLeader(sender.getUniqueId())){
			allyInvites.remove(s.getId());
			sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.GRAY + "Declined an Alliance invite from " + ChatColor.AQUA + inviter.getName());
			inviter.sendSettlementMessage(MessageType.PREFIX.getMsg() + ChatColor.AQUA + s.getName() + ChatColor.GRAY + " declined your Alliance request");
		}else{
			sender.sendMessage(MessageType.PREFIX.getMsg() + ChatColor.DARK_RED + "You don't have the required rank to do that!");
		}
	}*/
}