package gg.steve.mc.dazzer.mt.data;

import gg.steve.mc.dazzer.mt.SPlugin;
import gg.steve.mc.dazzer.mt.data.exception.VoteDataYmlFileNotFoundException;
import gg.steve.mc.dazzer.mt.file.types.DataPluginFile;
import gg.steve.mc.dazzer.mt.manager.AbstractManager;
import gg.steve.mc.dazzer.mt.manager.ManagerClass;
import gg.steve.mc.dazzer.mt.vote.MediaVote;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ManagerClass
public class VoteDataYmlManager extends AbstractManager {
    private static VoteDataYmlManager instance;
    private final SPlugin sPlugin;
    private Map<UUID, DataPluginFile> loadedVoteDataFiles;

    public VoteDataYmlManager(SPlugin sPlugin) {
        instance = this;
        this.sPlugin = sPlugin;
        this.loadedVoteDataFiles = new HashMap<>();
        AbstractManager.registerManager(instance);
    }

    @Override
    protected String getManagerName() {
        return "Vote Yml Data";
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onShutdown() {
        if (this.loadedVoteDataFiles != null && !this.loadedVoteDataFiles.isEmpty()) this.loadedVoteDataFiles.clear();
    }

    public static VoteDataYmlManager getInstance() {
        return instance;
    }

    public boolean doesVoteDataYmlFileExist(UUID ymlFileId) {
        if (loadedVoteDataFiles.containsKey(ymlFileId)) return true;
        return new File(this.sPlugin.getPlugin().getDataFolder() + File.separator + "data", String.valueOf(ymlFileId) + ".yml").exists();
    }

    public DataPluginFile loadYmlFileById(UUID ymlFileId) throws VoteDataYmlFileNotFoundException {
        if (this.loadedVoteDataFiles.containsKey(ymlFileId)) return this.loadedVoteDataFiles.get(ymlFileId);
        if (!doesVoteDataYmlFileExist(ymlFileId)) throw new VoteDataYmlFileNotFoundException(ymlFileId);
        DataPluginFile file = new DataPluginFile(String.valueOf(ymlFileId),
                new File(this.sPlugin.getPlugin().getDataFolder() + File.separator + "data", String.valueOf(ymlFileId) + ".yml"),
                this.sPlugin);
        return this.loadedVoteDataFiles.put(ymlFileId, file);
    }

    public DataPluginFile saveVoteDataAsYml(MediaVote vote) {
        DataPluginFile file = null;
        if (this.doesVoteDataYmlFileExist(vote.getVoteId())) {
            // overwrite existing file
            try {
                file = this.loadYmlFileById(vote.getVoteId());
            } catch (VoteDataYmlFileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            File raw = new File(this.sPlugin.getPlugin().getDataFolder() + File.separator + "data", String.valueOf(vote.getVoteId()) + ".yml");
            if (!raw.exists()) {
                try {
                    raw.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            file = new DataPluginFile(String.valueOf(vote.getVoteId()), raw, this.sPlugin);
        }
        // save logic here
        return file;
    }
}
