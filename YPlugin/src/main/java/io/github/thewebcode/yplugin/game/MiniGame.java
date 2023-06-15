package io.github.thewebcode.yplugin.game;


import io.github.thewebcode.yplugin.game.clause.ServerShutdownClause;
import io.github.thewebcode.yplugin.game.listener.UserManagerListener;
import io.github.thewebcode.yplugin.game.players.UserManager;
import io.github.thewebcode.yplugin.game.world.Arena;
import io.github.thewebcode.yplugin.game.world.ArenaManager;
import io.github.thewebcode.yplugin.player.Players;
import io.github.thewebcode.yplugin.reflection.ReflectionUtilities;
import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public abstract class MiniGame<T extends UserManager> extends CraftGame {

	private Set<ServerShutdownClause> shutdownClauses = new HashSet<>();

	private boolean gameOver = false;

	private boolean autoSave = true;

	private ArenaManager arenaManager;

	private UserManagerListener userManagerListener = null;

	private Class<? extends UserManager> userManagerClass = null;

	private T userManager = null;

	public MiniGame() {

	}

	@Deprecated
	public MiniGame(Class<? extends UserManager> userManager) {
		registerUserManager(userManager);
	}

	@Override
	public void onEnable() {
		initLogger();

		super.onEnable();

		for (Player player : Players.allPlayers()) {
			userManager.addUser(player);
		}

		if (userManager == null) {
			boolean hasClass = userManagerClass == null;
			debug(String.format("User manager class %s", hasClass ? "is nul; - fix this. " : "isn't null"));

			if (hasClass) {
				Constructor constructor = ReflectionUtilities.getConstructor(userManagerClass);
				this.userManager = ReflectionUtilities.invokeConstructor(constructor);
				debug("Created usermanager of class " + userManagerClass.getCanonicalName());
			} else {
				debug("Unable to locate the usermanager class");
			}
		}
		if (userManager != null) {
			userManager.setParent(this);
		}
		if (userManagerListener == null) {
			userManagerListener = new UserManagerListener(this, userManager);
		}
		registerListeners(userManagerListener);
	}

	@Override
	public void onDisable() {
		super.onDisable();

		shutdown();

		userManager.disposeAll();
		userManager = null;
	}

	@Override
	public void update() {
		super.update();

		if (!shutdownClauses.isEmpty() && doShutdown()) {
			onDisable();
		}
	}

	protected boolean doShutdown() {
		for (ServerShutdownClause clause : shutdownClauses) {
			if (clause.shutdown()) {
				return true;
			}
		}
		return false;
	}


	public void registerShutdownClauses(ServerShutdownClause... clauses) {
		Collections.addAll(shutdownClauses, clauses);
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public File getArenaFolder() {
		return new File("plugins/" + getName() + "/Arenas/");
	}

	public void loadArenas() {
		Logger logger = getLogger();
		File arenaFolder = getArenaFolder();
		if (!arenaFolder.exists()) {
			arenaFolder.mkdirs();
		}

		Collection<File> arenas = FileUtils.listFiles(arenaFolder, null, false);

		if (arenas.isEmpty()) {
			logger.info("No arenas loaded for " + getName());
			return;
		}

		for (File file : arenas) {
			try {
				Arena arena = new Arena(file);
				arena.load();
				arenaManager.addArena(arena);
				logger.info(("Loaded arena " + arena.toString()));
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("Unable to load arena from file: " + file.getName());
			}
		}

	}

	public ArenaManager getArenaManager() {
		return arenaManager;
	}

	public Arena getActiveArena() {
		return arenaManager.getActiveArena();
	}

	public void saveArena(Arena arena) {
		File arenaFile = new File(getArenaFolder(), arena.getWorldName() + ".yml");
		try {
			arena.save(arenaFile);
			debug("Saved " + arena.toString() + " to " + arenaFile.getPath());
		} catch (Exception e) {
			e.printStackTrace();
			debug("Unable to save arena " + arena.getArenaName() + " to file " + arenaFile.getName());
		}
	}

	public void saveArenas() {
		getArenaManager().getArenas().forEach(this::saveArena);
	}

	public boolean autoSave() {
		return autoSave;
	}

	public void setAutoSave(boolean autoSave) {
		this.autoSave = autoSave;
	}

	public void registerUserManager(Class<? extends UserManager> userManagerClass) {
		this.userManagerClass = userManagerClass;
		Constructor constructor = ReflectionUtilities.getConstructor(userManagerClass);
		this.userManager = ReflectionUtilities.invokeConstructor(constructor);
	}

	public void setUserManagerListener(UserManagerListener listener) {
		userManagerListener = listener;
	}

	public T getUserManager() {
		return userManager;
	}

	public abstract void startup();

	public abstract void shutdown();

	public abstract String getAuthor();

	public abstract void initConfig();
}
