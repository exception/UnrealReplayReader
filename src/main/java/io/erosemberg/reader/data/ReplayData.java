package io.erosemberg.reader.data;

import com.google.common.primitives.UnsignedInteger;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents the FLocalFileReplayDataInfo from Unreal Engine.
 *
 * @see <a href="https://github.com/EpicGames/UnrealEngine/blob/b70f31f6645d764bcb55829228918a6e3b571e0b/Engine/Source/Runtime/NetworkReplayStreaming/LocalFileNetworkReplayStreaming/Public/LocalFileNetworkReplayStreaming.h#L45">FLocalFileReplayDataInfo</a>
 *
 * @author Erik Rosemberg
 * @since 21/12/2018
 * */
@Data
@AllArgsConstructor
class ReplayData {

    private int chunkIndex;
    private UnsignedInteger time1;
    private UnsignedInteger time2;
    private int sizeInBytes;
    private long replayDataOffset;
    private long streamOffset;

}
