package net.countercraft.movecraft.warfare.siege;

import net.countercraft.movecraft.config.Settings;
import net.countercraft.movecraft.localisation.I18nSupport;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SiegePreparationTask extends SiegeTask {


    public SiegePreparationTask(Siege siege) {
        super(siege);
    }

    @Override
    public void run() {
        siege.getProgressBar().setVisible(true);
        addPlayersToProgressBar();
        int timePassed = ((int)(System.currentTimeMillis() - siege.getStartTime())); //time passed in milliseconds
        int timePassedInSeconds = timePassed / 1000;
        double progress = (double) timePassedInSeconds / (double) siege.getDelayBeforeStart();
        siege.getProgressBar().setProgress(Math.min(progress, 1.0));
        if (timePassedInSeconds >= siege.getDelayBeforeStart()){
            siege.getProgressBar().setProgress(0.0);
            siege.setJustCommenced(true);
            siege.setStage(SiegeStage.IN_PROGRESS);
            return;
        }
        if ((siege.getDelayBeforeStart() - timePassedInSeconds) % Settings.SiegeTaskSeconds != 0 || timePassed < 3000){
             return;
        }
        int timeLeft = siege.getDelayBeforeStart() - timePassedInSeconds;
        broadcastSiegePreparation(Bukkit.getPlayer(siege.getPlayerUUID()), siege.getName(), timeLeft);
    }

    private void broadcastSiegePreparation(Player player, String siegeName, int timeLeft){
        String playerName = "";
        if (player != null) {
            playerName = player.getDisplayName();
        }

        Bukkit.getServer().broadcastMessage(String.format(I18nSupport.getInternationalisedString("Siege - Siege About To Begin"), playerName, siegeName) + formatMinutes(timeLeft));
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 0);
        }
    }
}
