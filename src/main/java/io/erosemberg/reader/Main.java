package io.erosemberg.reader;

import io.erosemberg.reader.data.ReplayInfo;
import io.erosemberg.reader.data.ReplayReader;
import io.erosemberg.reader.gamedata.fortnite.FortniteGameData;
import io.erosemberg.reader.parsing.ParserOptions;
import io.erosemberg.reader.parsing.StandardParsers;
import me.hugmanrique.jacobin.reader.ByteStreamReader;
import me.hugmanrique.jacobin.reader.ByteStreamReaderBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * @author Erik Rosemberg
 * @since 21/12/2018
 */
public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter the path to the replay: ");
        String replayPath = scanner.next();

        // This is just for testing purposes.
        Path path = Paths.get(replayPath);
        FileInputStream stream = new FileInputStream(path.toFile());
        ByteStreamReader reader = new ByteStreamReaderBuilder()
                .stream(stream)
                .order(ByteOrder.LITTLE_ENDIAN)
                .build();

        ReplayReader<FortniteGameData> replayReader = new ReplayReader<>(reader, StandardParsers.FORTNITE_PARSER, ParserOptions.builder().printUnknownWeapons(true).build());
        ReplayInfo<FortniteGameData> info = replayReader.read();
        FortniteGameData gameData = info.getGameData();
        System.out.println("Finished Reading Replay!");
        System.out.println("*** HEADER DATA:");
        System.out.println("    Name: " + info.getHeader().getFriendlyName());
        System.out.println("    Changelist Size: " + info.getHeader().getChangeList());
        System.out.println("    File Version: " + info.getHeader().getFileVersion());
        System.out.println("    Network Version: " + info.getHeader().getNetworkVersion());
        System.out.println("    Duration: " + info.getHeader().getLengthInMs());
        System.out.println("    Magic Number: " + info.getHeader().getMagicNumber());
        System.out.println("*** BREAKDOWN...");
        System.out.println("    Found " + info.getEvents().size() + " events.");
        System.out.println("    Found " + info.getDataChunks().size() + " data chunks.");
        System.out.println("    Found " + info.getCheckpoints().size() + " checkpoints.");
        System.out.println("    Found " + gameData.getKills().size() + " kills.");
        System.out.println("    Found " + gameData.getPlayers().size() + " players.");
        System.out.println(gameData.toString());
    }

}
