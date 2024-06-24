package com.example.apidemo.cloudinary;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

    @Autowired
    Cloudinary cloudinary;

    public String uploadFile(MultipartFile multipartFile) throws IOException {
        return cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                        Map.of("public_id", UUID.randomUUID().toString(), "folder", "products"))
                .get("url")
                .toString();
    }

    public Boolean deleteFile(String url) throws IOException {
        var arrS = url.split("/", 0); // split url then arrS[8] is {{uuid}}.jpg
        var uuid = arrS[8].substring(0, 36); // substring to remove .jpg and get only uuid
        var result = cloudinary.uploader().destroy("products/" + uuid, null);
        return true;
    }
}