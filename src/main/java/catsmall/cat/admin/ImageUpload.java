package catsmall.cat.admin;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageUpload {
    private MultipartFile file;
    private String url;
}
