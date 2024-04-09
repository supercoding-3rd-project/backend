package com.github.devsns.domain.chat.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService {
    // 파일이 저장될 기본 경로 설정
    private final Path rootLocation = Paths.get("uploads");

    // 파일을 저장하고 저장된 파일명을 반환하는 메소드
    public String storeFile(MultipartFile file) throws IOException {
        // 지정된 경로에 디렉토리가 없으면 생성
        Files.createDirectories(rootLocation);
        // 파일명에 UUID를 추가하여 고유성을 보장하고, 이를 통해 파일명 충돌을 방지
        String uniqueFilename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        // 최종적으로 파일을 저장할 경로를 생성
        Path destinationFile = rootLocation.resolve(Paths.get(uniqueFilename)).normalize().toAbsolutePath();
        // 파일을 최종 경로에 저장
        file.transferTo(destinationFile);

        // 파일 저장 성공 로그 기록
        log.info("파일 저장 성공: {}", destinationFile);

        // 저장된 파일의 이름을 반환 (UUID를 포함한 전체 파일명)
        return uniqueFilename;
    }
}


