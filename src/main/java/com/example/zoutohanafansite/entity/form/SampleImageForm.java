package com.example.zoutohanafansite.entity.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SampleImageForm {
    private MultipartFile image;
}
