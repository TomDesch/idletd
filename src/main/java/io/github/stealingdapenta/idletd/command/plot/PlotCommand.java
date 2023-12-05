package io.github.stealingdapenta.idletd.command.plot;

import io.github.stealingdapenta.idletd.plot.PlotService;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class PlotCommand implements CommandExecutor {

    private final PlotService plotService;

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             String[] args) {
        // Default command : teleport player to his plot
        CommandExecutor cmd = new GoToPlotCommand(plotService);

        // No arguments will display help.
        if (args.length == 0) {
            return cmd.onCommand(sender, command, label, args);
        }

        String subcommand = args[0].toLowerCase();

        switch (subcommand) {
            case "help" -> cmd = new PlotHelpCommand();
            case "new" -> cmd = new CreatePlotCommand(plotService);
            default -> cmd = new GoToPlotCommand(plotService);
        }

        return cmd.onCommand(sender, command, label, args);
    }

}
