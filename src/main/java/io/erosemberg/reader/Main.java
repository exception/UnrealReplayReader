package io.erosemberg.reader;

import io.erosemberg.reader.data.ReplayInfo;
import io.erosemberg.reader.data.ReplayReader;
import me.hugmanrique.jacobin.reader.ByteStreamReader;
import me.hugmanrique.jacobin.reader.ByteStreamReaderBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Erik Rosemberg
 * @since 21/12/2018
 */
public class Main {

    public static void main(String[] args) throws IOException {
        // This is just for testing purposes.
        Path path = Paths.get("C:\\Users\\Usuario\\AppData\\Local\\FortniteGame\\Saved\\Demos\\UnsavedReplay-2018.12.18-23.41.38.replay");
        FileInputStream stream = new FileInputStream(path.toFile());
        ByteStreamReader reader = new ByteStreamReaderBuilder()
                .stream(stream)
                .order(ByteOrder.LITTLE_ENDIAN)
                .build();
        
        ReplayReader replayReader = new ReplayReader(reader);
        ReplayInfo info = replayReader.read();
        System.out.println("info = " + info);
    }

}
