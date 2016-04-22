package com.plorial.exoroplayer.model;

import android.support.annotation.Nullable;

/**
 * Created by plorial on 4/22/16.
 */
public class SubtitlesUtil {
    public enum SubtitlesType{
        SRT(".srt"),
        ASS(".ass"),
        SCC(".scc"),
        STL(".stl"),
        TTML(".ttml"),
        UNKNOWN("");

        private String extension;

        SubtitlesType(String extension) {
            this.extension = extension;
        }

        public String getExtension() {
            return extension;
        }
    }

    private SubtitlesUtil() {
    }

    public static SubtitlesType getSubtitlesType(String subtitlesUri) {
        String extension = getExtension(subtitlesUri);
        if (extension == null) {
            return SubtitlesType.UNKNOWN;
        }

        for (SubtitlesType type : SubtitlesType.values()) {
            if (type.getExtension().equals(extension)) {
                return type;
            }
        }

        return SubtitlesType.UNKNOWN;
    }

    @Nullable
    private static String getExtension(String subtitlesUri) {
        if (subtitlesUri == null || subtitlesUri.trim().isEmpty()) {
            return null;
        }

        int periodIndex = subtitlesUri.lastIndexOf('.');
        if (periodIndex == -1 || periodIndex >= subtitlesUri.length()) {
            return null;
        }

        String rawExtension = subtitlesUri.substring(periodIndex);
        return rawExtension.toLowerCase();
    }
}
