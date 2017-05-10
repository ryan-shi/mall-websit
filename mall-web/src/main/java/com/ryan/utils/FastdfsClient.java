package com.ryan.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;

@Component
public class FastdfsClient {
	
	@Autowired
	protected FastFileStorageClient storageClient;

	public String uploadFile(MultipartFile file) {
		String fileType = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();
		StorePath path = null;
		try {
			path = storageClient.uploadFile(file.getInputStream(), file.getSize(), fileType, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (path != null)
			return path.getFullPath();
		else
			return null;
	}

	public String uploadFile(InputStream inputStream, Long size, String type) {
		StorePath path = null;
		try {
			path = storageClient.uploadFile(inputStream, size, type, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (path != null)
			return path.getFullPath();
		else
			return null;
	}

	public void deleteFile(String fullPath) {
		storageClient.deleteFile(fullPath);
	}
}
