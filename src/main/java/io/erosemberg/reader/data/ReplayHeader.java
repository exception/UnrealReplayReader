package io.erosemberg.reader.data;

import io.erosemberg.reader.util.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.hugmanrique.jacobin.reader.LittleEndianDataReader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Represents the header of a replay (or demo) from the Unreal Engine.
 * There is no structure for it therefore it is placed inside this single class.
 * <p>
 * The defined variables can be seen here:
 *
 * @author Erik Rosemberg
 * @see <a href="https://github.com/EpicGames/UnrealEngine/blob/b70f31f6645d764bcb55829228918a6e3b571e0b/Engine/Source/Runtime/NetworkReplayStreaming/LocalFileNetworkReplayStreaming/Public/LocalFileNetworkReplayStreaming.h#L90-L100">Structure.</a>
 * @since 21/12/2018
 */
@Data
@AllArgsConstructor
public class ReplayHeader {

    private long magicNumber;
    private long fileVersion;
    private int lengthInMs;
    private long networkVersion;
    private long changeList;
    private String friendlyName;
    private boolean isLive;
    private Date timeStamp;
    private boolean compressed;

    static ReplayHeader readHeader(LittleEndianDataReader reader) throws IOException {
        long magicNumber = reader.readUInt32();
        long fileVersion = reader.readUInt32();
        int lengthInMs = reader.readInt32();
        long networkVersion = reader.readUInt32();
        long changeList = reader.readUInt32();
        int friendlyNameSize = adjustFriendlySizeName(reader.readInt32());

        byte[] buffer = new byte[friendlyNameSize];
        reader.read(buffer, 0, friendlyNameSize);
        String name = new String(buffer, StandardCharsets.UTF_8).trim().replaceAll("\u0000", "");
        boolean isLive = reader.readUInt32() != 0;

        // read timestamp as uint64 as per Unreal Engine specifications.
        // see https://api.unrealengine.com/INT/API/Runtime/Core/Misc/FDateTime/__ctor/2/index.html
        Date timestamp = TimeUtils.fromTicks(reader.readUInt64());

        boolean compressed = reader.readUInt32() != 0;

        return new ReplayHeader(
                magicNumber,
                fileVersion,
                lengthInMs,
                networkVersion,
                changeList,
                name,
                isLive,
                timestamp,
                compressed
        );
    }

    private static int adjustFriendlySizeName(int size) {
        return size < 0 ? -size * 2 : size;
    }
}
