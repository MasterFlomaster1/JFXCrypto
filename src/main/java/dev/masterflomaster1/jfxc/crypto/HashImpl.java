package dev.masterflomaster1.jfxc.crypto;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.concurrent.CompletableFuture;

public final class HashImpl {

    private HashImpl() { }

    public static byte[] hash(String algorithm, byte[] value) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm, "BC");
            md.update(value);
            return md.digest();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    public static CompletableFuture<byte[]> asyncHash(String algorithm, String filePath) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();

        try {
            Path path = Paths.get(filePath);
            AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
            MessageDigest digest = MessageDigest.getInstance(algorithm, "BC");

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            long[] position = {0};

            CompletionHandler<Integer, ByteBuffer> handler = new CompletionHandler<>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    if (result == -1) {
                        byte[] hashBytes = digest.digest();
                        future.complete(hashBytes);
                        try {
                            fileChannel.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return;
                    }

                    buffer.flip();
                    digest.update(buffer);
                    buffer.clear();

                    position[0] += result;
                    fileChannel.read(buffer, position[0], buffer, this);
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    future.completeExceptionally(exc);
                    try {
                        fileChannel.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            };

            fileChannel.read(buffer, 0, buffer, handler);
        } catch (IOException | NoSuchProviderException | NoSuchAlgorithmException e) {
            future.completeExceptionally(e);
        }

        return future;
    }

}
