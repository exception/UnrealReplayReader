package io.erosemberg.reader.util;

import com.sun.jna.Library;

public interface OodleLib extends Library {

    int OodleLZ_Decompress(byte[] buffer, long bufferSize, byte[] outputBuffer, long outputBufferSize,
                           int a, int b, int c, int d, int e, int f, int g, int h, int i, int threadModule);

    boolean DecompressBuffer(byte[] dataBuffer, byte[] output);
}
