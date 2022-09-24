package me.allan;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.FunctionalResultHandler;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.List;

public class OlhaEleAeBot extends ListenerAdapter
{
    static String owner = "";
    VoiceChannel channel = null;
    AudioPlayer player = null;
    static AudioTrack audio = null;
    static AudioPlayerManager playerManager = new DefaultAudioPlayerManager();


    public static void main(String[] args)
            throws IllegalArgumentException, LoginException
    {
        if(args.length < 3) {
            System.out.println("Favor preencher todos os argumentos corretamente!");
            System.exit(0);
        }
        owner = args[2];
        JDABuilder.createDefault(args[0])
                .addEventListeners(new OlhaEleAeBot())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();
        AudioSourceManagers.registerLocalSource(playerManager);
        playerManager.loadItem(new File(args[1]).getAbsolutePath(), new FunctionalResultHandler(track -> {
            System.out.println(track.getIdentifier());
            audio = track;
        }, null, () -> System.out.println("Empty") , e -> {
            System.out.println("Error");
            e.printStackTrace();
        }));
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (!event.isFromGuild()) return;
        if (!event.getAuthor().getId().equals(owner)) return;
        if (event.getMessage().getContentRaw().startsWith("!stop")) {
            event.getJDA().shutdown();
            return;
        }
        if (!event.getMessage().getContentRaw().startsWith("!start")) return;
        Guild guild = event.getGuild();
        List<VoiceChannel> channels = guild.getVoiceChannelsByName(event.getMessage().getContentRaw().replace("!start", "").trim(), true);
        if (channels.size() == 0) {
            event.getChannel().sendMessage("Channel not found!").queue();
        }
        channel = channels.get(0);
        AudioManager manager = guild.getAudioManager();
        player = playerManager.createPlayer();
        manager.setAutoReconnect(true);
        manager.setSendingHandler(new AudioPlayerSendHandler(player));
        manager.openAudioConnection(channel);
    }

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        if(channel == null) return;
        if(event.getChannelJoined() != channel) return;
        if(event.getEntity().getUser().isBot()) return;
        player.playTrack(audio.makeClone());
    }

    public static class AudioPlayerSendHandler implements AudioSendHandler {
        private final AudioPlayer audioPlayer;
        private AudioFrame lastFrame;

        public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
            this.audioPlayer = audioPlayer;
        }

        @Override
        public boolean canProvide() {
            lastFrame = audioPlayer.provide();
            return lastFrame != null;
        }

        @Override
        public ByteBuffer provide20MsAudio() {
            return ByteBuffer.wrap(lastFrame.getData());
        }

        @Override
        public boolean isOpus() {
            return true;
        }
    }
}

