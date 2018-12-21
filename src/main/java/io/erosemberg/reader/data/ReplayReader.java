package io.erosemberg.reader.data;

import com.google.common.primitives.UnsignedInteger;
import me.hugmanrique.jacobin.reader.ByteStreamReader;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static io.erosemberg.reader.util.ByteUtils.adjustLength;

/**
 * @author Erik Rosemberg
 * @since 21/12/2018
 */
public class ReplayReader {

    private final int INDEX_NONE = -1;

    private ByteStreamReader reader;

    /**
     * Creates a new instance of the ReplayReader.
     *
     * @param reader the ByteStreamReader from which we will be reading the data.
     */
    public ReplayReader(ByteStreamReader reader) {
        this.reader = reader;
    }

    @SuppressWarnings("all")
    public ReplayInfo read() throws IOException {
        ReplayInfo.ReplayInfoBuilder builder = ReplayInfo.builder();

        ReplayHeader header = ReplayHeader.readHeader(this.reader);

        System.out.println("Read replay header...");
        System.out.println("========= header =========");
        System.out.println(header.toString());
        System.out.println("======= end header =======");
        builder.header(header);

        int totalSize = reader.available();
        int chunkIndex = 0;
        int checkpointIndex = 0;
        int replayDataIndex = 0;
        int eventIndex = 0;
        int chunkHeader = INDEX_NONE;

        LinkedList<Chunk> chunks = new LinkedList<>();
        LinkedList<Event> checkpoints = new LinkedList<>();
        LinkedList<Event> events = new LinkedList<>();
        LinkedList<ReplayData> dataChunks = new LinkedList<>();

        while (reader.available() > 0) {
            // TODO: Figure out all of this. -> https://github.com/EpicGames/UnrealEngine/blob/master/Engine/Source/Runtime/NetworkReplayStreaming/LocalFileNetworkReplayStreaming/Private/LocalFileNetworkReplayStreaming.cpp#L243
            long typeOffset = reader.getOffset(); // Same as FArchive.Tell()

            // Parses ELocalFileChunkType from reader.
            long uint = reader.readUInt32(); // unsigned int32 according to https://github.com/EpicGames/UnrealEngine/blob/b70f31f6645d764bcb55829228918a6e3b571e0b/Engine/Source/Runtime/NetworkReplayStreaming/LocalFileNetworkReplayStreaming/Public/LocalFileNetworkReplayStreaming.h#L19
            ChunkType type = ChunkType.from(uint);

            int sizeInBytes = reader.readInt32();
            long dataOffset = reader.getOffset();

            Chunk chunk = new Chunk(type, sizeInBytes, typeOffset, dataOffset);
            chunks.add(chunk);
            chunkIndex += 1;

            switch (type) {
                case HEADER:
                    if (chunkHeader == INDEX_NONE) {
                        chunkHeader = chunkIndex;
                    } else {
                        throw new IllegalStateException("more than one header chunk found!");
                    }
                    break;
                case CHECKPOINT: {
                    int idLength = reader.readInt32();
                    String id = reader.readUTF8String(0, idLength).trim();
                    int groupLength = reader.readInt32();
                    String group = reader.readUTF8String(0, groupLength).trim();
                    int metadataLength = reader.readInt32();
                    String metadata = reader.readUTF8String(0, metadataLength).trim();

                    long time1 = reader.readUInt32();
                    long time2 = reader.readUInt32();
                    int size = reader.readInt32();

                    long eventDataOffset = reader.getOffset();

                    Event checkpoint = new Event(checkpointIndex, id, group, metadata, time1, time2, size, eventDataOffset);
                    checkpoints.add(checkpoint);
                    checkpointIndex += 1;
                    break;
                }
                case REPLAY_DATA: {
                    long time1 = reader.readUInt32();
                    long time2 = reader.readUInt32();
                    int size = reader.readInt32();
                    long replayDataOffset = reader.getOffset();

                    ReplayData data = new ReplayData(replayDataIndex, time1, time2, size, replayDataOffset);
                    dataChunks.add(data);
                    replayDataIndex += 1;
                    break;
                }
                case EVENT: {
                    int idLength = reader.readInt32();
                    String id = reader.readUTF8String(0, idLength).trim();
                    int groupLength = reader.readInt32();
                    String group = reader.readUTF8String(0, groupLength).trim();
                    int metadataLength = reader.readInt32();
                    String metadata = reader.readUTF8String(0, metadataLength).trim();

                    long time1 = reader.readUInt32();
                    long time2 = reader.readUInt32();
                    int size = reader.readInt32();

                    if (group.equalsIgnoreCase("playerElim")) {
                        // here
                    }

                    long eventDataOffset = reader.getOffset();

                    Event event = new Event(eventIndex, id, group, metadata, time1, time2, size, eventDataOffset);
                    events.add(event);
                    eventIndex += 1;
                    break;
                }
                case UNKNOWN:
                    System.out.println("Encountered unknown type.");
                    break;
            }

            // https://github.com/EpicGames/UnrealEngine/blob/master/Engine/Source/Runtime/NetworkReplayStreaming/LocalFileNetworkReplayStreaming/Private/LocalFileNetworkReplayStreaming.cpp#L401
            reader.setOffset(dataOffset + sizeInBytes); // Same as FArchive.Seek()
        }

        builder.chunks(chunks);
        builder.checkpoints(checkpoints);
        builder.dataChunks(dataChunks);
        builder.events(events);

        return builder.build();
    }
}
