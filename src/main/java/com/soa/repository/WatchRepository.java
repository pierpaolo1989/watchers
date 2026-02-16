package com.soa.repository;

import com.soa.model.Watch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchRepository extends JpaRepository<Watch, Long> {
    List<Watch> findByUserEmail(String email);
}
