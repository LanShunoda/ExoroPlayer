package com.plorial.plorialplayer.events;

/**
 * Created by plorial on 4/13/16.
 */
public class VideoStatusEvent {

    public static final String PAUSE = "Pause";
    public static final String READY_TO_START = "Ready to start";

    private String massage;

    public VideoStatusEvent(String massage) {
        this.massage = massage;
    }

    public String getMassage() {
        return massage;
    }
}
