package gg.steve.mc.dazzer.mt.gui.action;

import gg.steve.mc.dazzer.mt.gui.action.types.*;
import gg.steve.mc.dazzer.mt.gui.exception.InventoryClickActionNotFoundException;
import gg.steve.mc.dazzer.mt.manager.AbstractManager;
import gg.steve.mc.dazzer.mt.manager.ManagerClass;

import java.util.HashMap;
import java.util.Map;

@ManagerClass
public class InventoryClickActionManager extends AbstractManager {
    private static InventoryClickActionManager instance;
    private Map<String, AbstractInventoryClickAction> clickActions;

    public InventoryClickActionManager() {
        instance = this;
        AbstractManager.registerManager(instance);
    }

    @Override
    public void onLoad() {
        // register all click actions
        this.registerInventoryClickAction(new BackInventoryClickAction());
        this.registerInventoryClickAction(new CloseInventoryClickAction());
        this.registerInventoryClickAction(new OpenInventoryClickAction());
        this.registerInventoryClickAction(new PageInventoryClickAction());
        this.registerInventoryClickAction(new PermissionInventoryClickAction());
    }

    @Override
    public void onShutdown() {
        if (this.clickActions != null && !this.clickActions.isEmpty()) this.clickActions.clear();
    }

    @Override
    protected String getManagerName() {
        return "InventoryActions";
    }

    public boolean registerInventoryClickAction(AbstractInventoryClickAction inventoryClickAction) {
        if (this.clickActions == null) this.clickActions = new HashMap<>();
        if (this.clickActions.containsKey(inventoryClickAction.getUniqueName())) return false;
        return this.clickActions.put(inventoryClickAction.getUniqueName(), inventoryClickAction) != null;
    }

    public boolean unregisterInventoryClickAction(String clickActionUniqueName) {
        if (this.clickActions == null || this.clickActions.isEmpty()) return true;
        if (!this.clickActions.containsKey(clickActionUniqueName)) return false;
        return this.clickActions.remove(clickActionUniqueName) != null;
    }

    public AbstractInventoryClickAction getClickActionByUniqueName(String clickActionUniqueName) throws InventoryClickActionNotFoundException {
        if (this.clickActions == null || this.clickActions.isEmpty() || !this.clickActions.containsKey(clickActionUniqueName)) throw new InventoryClickActionNotFoundException(clickActionUniqueName);
        return this.clickActions.get(clickActionUniqueName);
    }

    public static InventoryClickActionManager getInstance() {
        return instance;
    }
}
