package io.erosemberg.reader.data;

import com.google.common.base.Charsets;
import io.erosemberg.reader.util.ByteUtils;
import io.erosemberg.reader.util.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.hugmanrique.jacobin.reader.ByteStreamReader;

import java.io.IOException;
import java.util.Date;

/**
 * Represents the header of a replay (or demo) from the Unreal Engine.
 * There is no structure for it therefore it is placed inside this single class.
 *
 * The defined variables can be seen here:
 * @see <a href="https://github.com/EpicGames/UnrealEngine/blob/b70f31f6645d764bcb55829228918a6e3b571e0b/Engine/Source/Runtime/NetworkReplayStreaming/LocalFileNetworkReplayStreaming/Public/LocalFileNetworkReplayStreaming.h#L90-L100">Structure.</a>
 *
 * @author Erik Rosemberg
 * @since 21/12/2018
 */
@Data
@AllArgsConstructor
class ReplayHeader {

    private int magicNumber;
    private int fileVersion;
    private int lengthInMs;
    private int networkVersion;
    private int changeList;
    private String friendlyName;
    private boolean isLive;
    private Date timeStamp;
    private boolean compressed;

    static ReplayHeader readHeader(ByteStreamReader reader) throws IOException {
        int magicNumber = reader.readInt32();
        int fileVersion = reader.readInt32();
        int lengthInMs = reader.readInt32();
        int networkVersion = reader.readInt32();
        int changeList = reader.readInt32();
        int friendlyNameSize = adjustFriendlySizeName(reader.readInt32());

        System.out.println("friendlyNameSize = " + friendlyNameSize);

        byte[] buffer = new byte[friendlyNameSize];
        reader.read(buffer, 0, friendlyNameSize);
        String name = new String(buffer, Charsets.UTF_8).trim();
        boolean isLive = reader.readInt32() != 0;

        // read timestamp as uint64 as per Unreal Engine specifications.
        // see https://api.unrealengine.com/INT/API/Runtime/Core/Misc/FDateTime/__ctor/2/index.html
        Date timestamp = TimeUtils.fromTicks(reader.readUInt64());

        boolean compressed = reader.readInt32() != 0;

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
