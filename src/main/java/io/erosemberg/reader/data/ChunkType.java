package io.erosemberg.reader.data;

import lombok.AllArgsConstructor;

import java.util.stream.Stream;

/**
 * Represents the ELocalFileChunkType from Unreal Engine.
 * All identifiers should be unsigned integers.
 *
 * @author Erik Rosemberg
 * @see <a href="https://github.com/EpicGames/UnrealEngine/blob/b70f31f6645d764bcb55829228918a6e3b571e0b/Engine/Source/Runtime/NetworkReplayStreaming/LocalFileNetworkReplayStreaming/Public/LocalFileNetworkReplayStreaming.h#L19">ELocalFileChunkType</a>
 * @since 21/12/2018
 */
@AllArgsConstructor
public enum ChunkType {
    HEADER(0),
    REPLAY_DATA(1),
    CHECKPOINT(2),
    EVENT(3),
    UNKNOWN(0xFFFFFFFF);

    long identifier;

    public static ChunkType from(long identifier) {
        return Stream.of(values()).filter(type -> type.identifier == identifier).findAny().orElse(UNKNOWN);
    }
}
