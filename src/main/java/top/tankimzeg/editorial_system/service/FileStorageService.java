package top.tankimzeg.editorial_system.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Generic file storage contract.
 *
 * <p>Design goals:
 * <ul>
 *     <li>Only expose logical storage keys to callers (no raw file-system paths).</li>
 *     <li>All implementations are responsible for choosing and managing a fixed base directory.</li>
 *     <li>Prevent path traversal by normalizing and constraining to the configured base.</li>
 * </ul>
 *
 * @param <T> domain type the file is associated with (e.g. Manuscript/Revision/Review), or {@code Void}
 *           when there is no associated owner entity.
 */
public interface FileStorageService<T> {

    /**
     * Store a single file associated with the given domain object.
     *
     * @param file  multipart file from client
     * @param owner associated domain object, or {@code null} if not applicable
     * @return storage key (relative path or opaque id) that can be later used to load the file
     */
    String store(MultipartFile file, T owner) throws IOException;

    /**
     * Convenience method to store a collection of files.
     */
    default void store(List<MultipartFile> files, T owner) throws IOException {
        if (files == null || files.isEmpty()) {
            return;
        }
        for (MultipartFile f : files) {
            if (f != null && !f.isEmpty()) {
                store(f, owner);
            }
        }
    }

    /**
     * Load raw file bytes by previously issued storage key.
     *
     * <p>Implementations MUST ensure that the storage key cannot escape their configured
     * base directory (e.g. by normalizing paths and validating that the resolved path
     * starts with the base directory).</p>
     *
     * @param storageKey key returned by {@link #store(MultipartFile, Object)}
     * @return file contents
     */
    byte[] load(String storageKey) throws IOException;
}
