package gg.steve.mc.dazzer.mt.gui;

import gg.steve.mc.dazzer.mt.SPlugin;
import gg.steve.mc.dazzer.mt.file.types.GuiPluginFile;
import gg.steve.mc.dazzer.mt.gui.exception.AbstractGuiNotFoundException;
import gg.steve.mc.dazzer.mt.gui.exception.InvalidConfigurationFileTypeException;
import gg.steve.mc.dazzer.mt.manager.AbstractManager;
import gg.steve.mc.dazzer.mt.manager.ManagerClass;
import org.bukkit.entity.Player;

import java.util.*;

@ManagerClass
public class GuiManager extends AbstractManager {
    private static GuiManager instance;
    private Map<String, AbstractGui> guis;
    private Map<UUID, List<AbstractGui>> playerGuis;

    public GuiManager() {
        instance = this;
        AbstractManager.registerManager(instance);
    }

    @Override
    public void onLoad() {
        this.playerGuis = new HashMap<>();
    }

    @Override
    public void onShutdown() {
        if (this.guis != null && !this.guis.isEmpty()) {
            this.guis.forEach((s, abstractGui) -> abstractGui.onShutdown());
            this.guis.clear();
        }
        if (this.playerGuis != null && !this.playerGuis.isEmpty()) this.playerGuis.clear();
    }

    @Override
    protected String getManagerName() {
        return "Gui";
    }

    public boolean guiExists(String guiUniqueName) {
        if (guis == null || guis.isEmpty()) return false;
        return guis.containsKey(guiUniqueName);
    }

    public boolean registerGuiFromFile(GuiPluginFile file) {
        String unique = file.get().getString("unique-name");
        AbstractGui gui = null;
        try {
            gui = new SGui(unique, file, SPlugin.getSPluginInstance());
        } catch (InvalidConfigurationFileTypeException e) {
            e.printStackTrace();
            return false;
        }
        return this.registerGui(gui);
    }

    public boolean registerGui(AbstractGui gui) {
        if (this.guis == null) this.guis = new HashMap<>();
        if (this.guis.containsKey(gui.getGuiUniqueName())) return false;
        return this.guis.put(gui.getGuiUniqueName(), gui) != null;
    }

    public boolean unregisterGui(String guiUniqueName) {
        if (this.guis == null || this.guis.isEmpty()) return true;
        if (!this.guis.containsKey(guiUniqueName)) return false;
        return this.guis.remove(guiUniqueName) != null;
    }

    public boolean removePlayerFromGuiMap(Player player) {
        if (this.playerGuis == null || this.playerGuis.isEmpty()) return true;
        return this.playerGuis.remove(player.getUniqueId()) != null;
    }

    public void openGui(Player player, String guiUniqueName) throws AbstractGuiNotFoundException {
        if (!this.guiExists(guiUniqueName)) throw new AbstractGuiNotFoundException(guiUniqueName);
        if (this.playerGuis.containsKey(player.getUniqueId())) {
            for (AbstractGui gui : this.playerGuis.get(player.getUniqueId())) {
                if (gui.getGuiUniqueName().equalsIgnoreCase(guiUniqueName)) {
                    gui.open();
                    return;
                }
            }
        } else {
            this.playerGuis.put(player.getUniqueId(), new ArrayList<>());
        }
        AbstractGui gui = this.guis.get(guiUniqueName).createDuplicateGui();
        gui.setOwner(player);
        this.playerGuis.get(player.getUniqueId()).add(gui);
        gui.open();
    }

    public static GuiManager getInstance() {
        return instance;
    }
}
