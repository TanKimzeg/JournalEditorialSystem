package top.tankimzeg.editorial_system.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import top.tankimzeg.editorial_system.service.FileStorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * @author Kim
 * @date 2026/2/27 23:49
 * @description 媒体文件存取实现类
 */
@Service
public class MediaStorageService implements FileStorageService<Object> {
    @Value("${storage.picture.upload-dir:uploads}")
    private String uploadDir;

    @Value("${storage.picture.max-size-bytes:10485760}") // default 10MB
    private long maxSizeBytes;

    @Value("${storage.picture.allowed-types:*}")
    private String allowedTypes; // comma separated, e.g. application/pdf,image/png,*

    @Override
    public String store(MultipartFile file, Object saved) throws IOException {
        validateFile(file);
        Path base = init();
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Path target = base.resolve(originalFilename + timestamp);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return target.toString();
    }

    public String store(MultipartFile media) {
        try {
            return store(media, null);
        } catch (IOException e) {
            throw new RuntimeException("文件保存失败：" + e.getMessage());
        }
    }


    private Path init() throws IOException {
        Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(dir);
        return dir;
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("上传文件为空");
        }
        if (file.getSize() > maxSizeBytes) {
            throw new RuntimeException("文件大小超过限制: " + maxSizeBytes + " bytes");
        }
        String contentType = file.getContentType();
        if (!isAllowedType(contentType)) {
            throw new RuntimeException("不支持的文件类型: " + contentType);
        }
    }

    private boolean isAllowedType(String contentType) {
        if (allowedTypes == null || allowedTypes.trim().isEmpty() || allowedTypes.trim().equals("*")) {
            return true;
        }
        List<String> allowed = Arrays.stream(allowedTypes.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
        if (allowed.contains("*")) return true;
        return contentType != null && allowed.stream().anyMatch(t -> t.equalsIgnoreCase(contentType));
    }

}
