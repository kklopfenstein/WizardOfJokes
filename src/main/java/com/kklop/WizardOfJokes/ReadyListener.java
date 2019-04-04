package com.kklop.WizardOfJokes;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ReadyListener implements EventListener {

    private Logger LOGGER = LoggerFactory.getLogger(ReadyListener.class);

    private ResourceLoader resourceLoader;
    private String s3Url;

    static final String HELP_TEXT = "commands: /tellJoke";
    static final String UNKOWN_TEXT = "I don't understand that. Use `/help` for help.";

    public ReadyListener(ResourceLoader resourceLoader, String s3Url) {
        this.resourceLoader = resourceLoader;
        this.s3Url = s3Url;
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof MessageReceivedEvent) {
            String id = event.getJDA().getSelfUser().getId();
            MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
            System.out.println("message received");
            MessageChannel channel = messageReceivedEvent.getChannel();
            if (messageReceivedEvent.getMessage().getMentionedMembers().stream()
                    .anyMatch(user -> user.getUser().getId().equals(id))) {
                String msg = messageReceivedEvent.getMessage().getContentDisplay();
                if (msg.contains("/tellJoke")) {
                    String message = randomJoke(getJokes());
                    System.out.println("Sending message: \"" + message + "\"");
                    channel.sendMessage(message).queue();
                } else if (msg.contains("/help")) {
                    channel.sendMessage(HELP_TEXT).queue();
                } else {
                    channel.sendMessage(UNKOWN_TEXT).queue();
                }
            }
        }
    }

    private String randomJoke(List<String> jokes) {
        return jokes.get(ThreadLocalRandom.current().nextInt(0, jokes.size() - 1));
    }

    private List<String> getJokes() {
        List<String> jokes = new ArrayList<>();
        Resource resource = this.resourceLoader.getResource(s3Url);

        try(InputStream is = resource.getInputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = br.readLine()) != null) {
                jokes.add(line);
            }
        } catch (IOException e) {
            LOGGER.error("Error getting jokes.", e);
        }

        return jokes;
    }
}
