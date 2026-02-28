package top.tankimzeg.editorial_system.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public interface FileStorageService<T> {

    String store(MultipartFile file, T saved) throws IOException;

    default void store(List<MultipartFile> files, T saved) throws IOException {
        if (files == null) return;
        for (MultipartFile f : files) {
            if (f != null && !f.isEmpty()) {
                store(f, saved);
            }
        }
    }

    default byte[] load(String storagePath) throws IOException {
        return Files.readAllBytes(Paths.get(storagePath));
    }

}
