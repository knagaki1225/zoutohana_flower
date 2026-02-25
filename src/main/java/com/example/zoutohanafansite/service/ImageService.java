package com.example.zoutohanafansite.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.UUID;

@Service
public class ImageService {
    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Value("${image.webp.quality:0.85f}")
    private float webpQuality;

    /**
     * 画像保存
     * 自動で.webpファイルへ変換
     *
     * @param file MultipartFile型の画像
     * @return String ファイル名
     */
    public String saveImage(MultipartFile file) throws IOException {
        // アップロードディレクトリの作成
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 画像ファイルかチェック
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IOException("画像ファイルではありません");
        }

        // 元の画像を読み込み
        BufferedImage originalImage;
        try {
            originalImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
            if (originalImage == null) {
                throw new IOException("画像の読み込みに失敗しました");
            }
        } catch (Exception e) {
            throw new IOException("画像の読み込みエラー: " + e.getMessage(), e);
        }

        // ユニークなファイル名を生成（拡張子は.webp）
        String uniqueFileName = UUID.randomUUID().toString() + ".webp";
        Path filePath = uploadPath.resolve(uniqueFileName);

        // WebP形式で保存
        convertToWebP(originalImage, filePath);

        return uniqueFileName;
    }

    private void convertToWebP(BufferedImage image, Path outputPath) throws IOException {
        // WebP用のImageWriterを取得
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByMIMEType("image/webp");
        if (!writers.hasNext()) {
            throw new IOException("WebPエンコーダーが見つかりません。webp-imageioライブラリを確認してください。");
        }

        ImageWriter writer = writers.next();

        try (ImageOutputStream ios = ImageIO.createImageOutputStream(Files.newOutputStream(outputPath))) {
            writer.setOutput(ios);

            // 圧縮品質の設定
            ImageWriteParam writeParam = writer.getDefaultWriteParam();
            if (writeParam.canWriteCompressed()) {
                writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                // 圧縮タイプを設定してから品質を設定する
                String[] compressionTypes = writeParam.getCompressionTypes();
                if (compressionTypes != null && compressionTypes.length > 0) {
                    writeParam.setCompressionType(compressionTypes[0]);
                }
                writeParam.setCompressionQuality(webpQuality);
            }

            // 画像を書き込み
            writer.write(null, new IIOImage(image, null, null), writeParam);
        } finally {
            writer.dispose();
        }
    }

    public byte[] getImage(String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        if (!Files.exists(filePath)) {
            throw new IOException("ファイルが見つかりません: " + fileName);
        }
        return Files.readAllBytes(filePath);
    }

    public String getContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "webp":
                return "image/webp";
            default:
                return "image/*";
        }
    }
}
