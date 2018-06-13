package com.example.Uploader.repository;

import com.example.Uploader.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByMd5Hash(String md5Hash);
}
