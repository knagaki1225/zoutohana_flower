package com.example.zoutohanafansite.entity.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AdminCreateProjectImageForm {
    private long id;
    private MultipartFile image;
}
