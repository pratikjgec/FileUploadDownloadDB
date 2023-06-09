package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entiry.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
	
}

