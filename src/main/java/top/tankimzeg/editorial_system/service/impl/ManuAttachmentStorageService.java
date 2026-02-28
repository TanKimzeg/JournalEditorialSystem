package top.tankimzeg.editorial_system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import top.tankimzeg.editorial_system.entity.Attachment;
import top.tankimzeg.editorial_system.entity.Manuscript;
import top.tankimzeg.editorial_system.entity.ManuscriptAttachment;
import top.tankimzeg.editorial_system.repository.ManuscriptAttachmentRepo;
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
 * @date 2026/2/27 22:54
 * @description 稿件附件存取服务实现类
 */
@Service
public class ManuAttachmentStorageService implements FileStorageService<Manuscript> {

    @Value("${storage.attachments.upload-dir:uploads}")
    private String uploadDir;

    @Value("${storage.attachments.max-size-bytes:10485760}") // default 10MB
    private long maxSizeBytes;

    @Value("${storage.attachments.allowed-types:*}")
    private String allowedTypes; // comma separated, e.g. application/pdf,image/png,*


    @Autowired
    private ManuscriptAttachmentRepo manuscriptAttachmentRepo;


    @Override
    public String store(MultipartFile file, Manuscript saved) throws IOException {
        validateFile(file);
        String storePath = createFile(file, saved.getId());
        ManuscriptAttachment att = new ManuscriptAttachment();
        att.setManuscript(saved);
        att.setAttachment(createAttachment(file, storePath));
        manuscriptAttachmentRepo.save(att);
        return storePath;
    }

    private Path init() throws IOException {
        Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(dir);
        return dir;
    }


    private Attachment createAttachment(MultipartFile file, String storagePath) {
        Attachment attachment = new Attachment();
        attachment.setFilename(file.getOriginalFilename());
        attachment.setContentType(file.getContentType());
        attachment.setSize(file.getSize());
        attachment.setStoragePath(storagePath);
        return attachment;
    }

    private String createFile(MultipartFile file, Long id) throws IOException {
        Path base = init();
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String storedName = id + timestamp + "_" + originalFilename;
        Path target = base.resolve(storedName);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return target.toString();
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
