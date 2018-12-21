package io.erosemberg.reader.data;

import com.google.common.base.Charsets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 *
 * Represents the FLocalFileReplayInfo from Unreal Engine.
 *
 * @see <a href="https://github.com/EpicGames/UnrealEngine/blob/b70f31f6645d764bcb55829228918a6e3b571e0b/Engine/Source/Runtime/NetworkReplayStreaming/LocalFileNetworkReplayStreaming/Public/LocalFileNetworkReplayStreaming.h#L88">FLocalFileReplayInfo</a>
 *
 * @author Erik Rosemberg
 * @since 21/12/2018
 */
@Data
@Builder
public class ReplayInfo {

    /**
     * The header is structured into it's own object to simplify this class.
     */
    private ReplayHeader header;
    private int headerChunkIndex;

    private LinkedList<Chunk> chunks;
    private LinkedList<Event> checkpoints;
    private LinkedList<Event> events;
    private LinkedList<ReplayData> dataChunks;

    private Set<String> players;
    private LinkedList<Kill> kills;

    public void dumpToFile(Path path) {
        JsonObject json = new JsonObject();

        json.add("chunks", new JsonPrimitive(chunks.size()));
        json.add("events", new JsonPrimitive(events.size()));
        json.add("checkpoints", new JsonPrimitive(checkpoints.size()));
        json.add("dataChunks", new JsonPrimitive(dataChunks.size()));
        json.add("headerChunkIndex", new JsonPrimitive(headerChunkIndex));

        json.add("fileVersion", new JsonPrimitive(header.getFileVersion()));
        json.add("magicNumber", new JsonPrimitive(header.getMagicNumber()));
        json.add("lengthInMs", new JsonPrimitive(header.getLengthInMs()));
        json.add("friendlyName", new JsonPrimitive(header.getFriendlyName()));

        JsonArray array = new JsonArray();
        players.forEach(array::add);
        json.add("players", array);

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path.toFile()), Charsets.UTF_8))) {
            writer.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Data
    @AllArgsConstructor
    static class Kill {
        String killer;
        String killed;
    }
}
