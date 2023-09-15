package io.github.stealingdapenta.idletd.service.command.plot;

import io.github.stealingdapenta.idletd.plot.PlotHandler;
import io.github.stealingdapenta.idletd.service.utils.SchematicHandler;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class PlotCommand implements CommandExecutor {

    private final SchematicHandler schematicHandler = new SchematicHandler();
    private final PlotHandler plotHandler = new PlotHandler(schematicHandler);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CommandExecutor cmd = new GoToPlotCommand(plotHandler);

        // No arguments will display help.
        if (args.length == 0) {
            cmd.onCommand(sender, command, label, args);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "help" -> cmd = new PlotHelpCommand();
            case "new" -> cmd = new CreatePlotCommand(plotHandler);
        }

        cmd.onCommand(sender, command, label, args);
        return true;
    }

}
