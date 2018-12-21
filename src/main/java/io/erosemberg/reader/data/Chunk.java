package io.erosemberg.reader.data;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents the FLocalFileChunkInfo from Unreal Engine.
 *
 * @see <a href="https://github.com/EpicGames/UnrealEngine/blob/b70f31f6645d764bcb55829228918a6e3b571e0b/Engine/Source/Runtime/NetworkReplayStreaming/LocalFileNetworkReplayStreaming/Public/LocalFileNetworkReplayStreaming.h#L29">FLocalFileChunkInfo</a>
 *
 * @author Erik Rosemberg
 * @since 21/12/2018
 * */
@Data
@AllArgsConstructor
public class Chunk {

    private ChunkType chunkType;
    private int sizeInBytes;
    private long typeOffset;
    private long dataOffset;

}
