package io.github.stealingdapenta.idletd.service.utils;


import static io.github.stealingdapenta.idletd.Idletd.LOGGER;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import io.github.stealingdapenta.idletd.Idletd;
import io.github.stealingdapenta.idletd.utils.ANSIColor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

@RequiredArgsConstructor
public class SchematicHandler {

    public void pasteSchematic(String schematicFileName, Location location) {
        File schematic = this.loadSchematicFile(schematicFileName);
        try {
            Clipboard clipboardWithSchematic = this.loadSchematic(schematic);
            this.pasteSchematic(clipboardWithSchematic, location);
        } catch (IOException | WorldEditException e) {
            LOGGER.warning("Error while pasting schematic: " + e.getMessage());
        }
    }

    private Clipboard loadSchematic(File schematicFile) throws IOException {
        ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
        try (ClipboardReader reader = format.getReader(new FileInputStream(schematicFile))) {
            return reader.read();
        }
    }

    private void pasteSchematic(Clipboard clipboardWithSchematic, Location location) throws WorldEditException {
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(location.getWorld()))) {
            Operation operation = new ClipboardHolder(clipboardWithSchematic)
                    .createPaste(editSession)
                    .to(BlockVector3.at(location.x(), location.y(), location.z()))
                    .ignoreAirBlocks(false)
                    .build();
            Operations.complete(operation);
        }
    }

    public File loadSchematicFile(String filename) {
        File schematicFile = new File(Idletd.getInstance().getIdleTdFolder(), "schematics" + File.separator + filename);
        LOGGER.info(ANSIColor.WHITE, "Constructed File Path: " + schematicFile.getAbsolutePath());
        return schematicFile;
    }

}
