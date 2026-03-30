package com.epam.learn.resource.parcer;

import com.epam.learn.resource.exception.InvalidMp3Exception;
import com.epam.learn.resource.model.Mp3Metadata;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;

@Service
public class Mp3MetadataParser {

    public Mp3Metadata extractMp3Metadata(byte[] audioData) {
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        InputStream inputStream = new ByteArrayInputStream(audioData);
        ParseContext context = new ParseContext();

        AutoDetectParser mp3Parser = new AutoDetectParser();
        try {
            mp3Parser.parse(inputStream, handler, metadata, context);
        } catch (IOException | SAXException | TikaException e) {
            throw new InvalidMp3Exception(e);
        }

        return new Mp3Metadata(
                metadata.get(TikaCoreProperties.TITLE),
                metadata.get("xmpDM:artist"),
                metadata.get("xmpDM:album"),
                metadata.get("xmpDM:genre"),
                formatDuration(metadata.get("xmpDM:duration")),
                parseYear(metadata.get("xmpDM:releaseDate")));
    }

    private String formatDuration(String durationInSeconds) {
        if (durationInSeconds == null) {
            return null;
        }
        Duration duration = Duration.ofSeconds((long) Double.parseDouble(durationInSeconds));

        return String.format("%02d:%02d", duration.toMinutes(), duration.toSecondsPart());
    }

    private String parseYear(String releaseDate) {
        if (releaseDate == null || releaseDate.length() < 4) {
            return null;
        }
        try {
            return Integer.valueOf(releaseDate.substring(0, 4)).toString();
        } catch (NumberFormatException _) {
            return null;
        }
    }
}
