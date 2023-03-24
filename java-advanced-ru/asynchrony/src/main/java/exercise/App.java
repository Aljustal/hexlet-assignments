package exercise;

import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CompletableFuture;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;

class App {

    // BEGIN
    public static CompletableFuture<String> unionFiles(String file1Path, String file2Path, String destPath) {
        Path dest = Paths.get(destPath);
        // create the destination file if it does not exist
        if (!Files.exists(dest)) {
            try {
                Files.createFile(dest);
            } catch (Exception ex) {
                return CompletableFuture.failedFuture(ex);
            }
        }

        // read the contents of the source files
        CompletableFuture<String> content1 = CompletableFuture.supplyAsync(() -> {
            Path file1 = Paths.get(file1Path);
            try {
                return new String(Files.readAllBytes(file1), StandardCharsets.UTF_8);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }).exceptionally(ex -> {
            System.out.println("Oops! We have an exception - " + ex.getMessage());
            return null;
        });

        CompletableFuture<String> content2 = CompletableFuture.supplyAsync(() -> {
            Path file2 = Paths.get(file2Path);
            try {
                return new String(Files.readAllBytes(file2), StandardCharsets.UTF_8);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }).exceptionally(ex -> {
            System.out.println("Oops! We have an exception - " + ex.getMessage());
            return null;
        });

        // combine the contents of the source files and write to the destination file
        CompletableFuture<String> result = content1.thenCombine(content2, (c1, c2) -> c1 + c2)
                .thenComposeAsync(content -> {
                    try {
                        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
                        Files.write(dest, bytes, StandardOpenOption.TRUNCATE_EXISTING);
                        return CompletableFuture.completedFuture("Successfully written to file");
                    } catch (Exception ex) {
                        return CompletableFuture.failedFuture(ex);
                    }
                });

        return result;
    }
    // END

    public static void main(String[] args) throws Exception {
        // BEGIN
        var file1 = "src/main/resources/file1.txt";
        var file2 = "src/main/resources/file2.txt";
        var file3 = "src/main/resources/file3.txt";
        CompletableFuture<String> result = App.unionFiles(file1, file2, file3);
        // END
    }
}

