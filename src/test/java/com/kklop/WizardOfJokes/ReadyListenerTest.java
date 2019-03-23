package com.kklop.WizardOfJokes;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.requests.restaction.MessageAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReadyListenerTest {

    private ReadyListener readyListener;

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private MessageReceivedEvent event;

    @Mock
    private Member member;

    @Mock
    private Resource resource;

    @Mock
    private MessageChannel messageChannel;

    @Mock
    private JDA jda;

    @Mock
    private SelfUser selfUser;

    @Mock
    private User user;

    @Mock
    private Message message;

    @Mock
    private MessageAction messageAction;

    private static final String S3_URL = "test";
    private static final String ID = "1";
    private static final String JOKES = "This is a joke.\nThis is a second joke.";


    @BeforeEach
    public void setUp() {
        readyListener = new ReadyListener(resourceLoader, S3_URL);
        when(event.getJDA()).thenReturn(jda);
        when(jda.getSelfUser()).thenReturn(selfUser);
        when(event.getJDA().getSelfUser().getId()).thenReturn(ID);
        when(member.getUser()).thenReturn(user);
        when(member.getUser().getId()).thenReturn(ID);
        when(event.getMessage()).thenReturn(message);
        when(event.getMessage().getMentionedMembers()).thenReturn(Collections.singletonList(member));
        when(event.getChannel()).thenReturn(messageChannel);
        when(messageChannel.sendMessage(anyString())).thenReturn(messageAction);
    }

    @Test
    public void receiveTellJokeAndSendJoke() throws IOException {
        when(resourceLoader.getResource(anyString())).thenReturn(resource);
        when(resource.getInputStream()).thenReturn(new ByteArrayInputStream(JOKES.getBytes()));
        when(event.getMessage().getContentDisplay()).thenReturn("/tellJoke");
        readyListener.onEvent(event);
        verify(messageChannel, times(1)).sendMessage(anyString());
    }

    @Test
    public void receiveHelpAndSendHelp() {
        when(event.getMessage().getContentDisplay()).thenReturn("/help");
        readyListener.onEvent(event);
        verify(messageChannel, times(1)).sendMessage(ReadyListener.HELP_TEXT);
    }

    @Test
    public void recieveUnkownMessageAndSendWarning() {
        when(event.getMessage().getContentDisplay()).thenReturn("sdfsdf");
        readyListener.onEvent(event);
        verify(messageChannel, times(1)).sendMessage(ReadyListener.UNKOWN_TEXT);
    }
}
