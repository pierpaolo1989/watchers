package com.soa.service;

import com.soa.model.Watch;
import com.soa.repository.WatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchService {

    @Autowired
    private WatchRepository watchRepository;

    public List<Watch> findAll() {
        return watchRepository.findAll();
    }

    public List<Watch> findAll(String email) {
        return watchRepository.findByUserEmail(email);
    }

    public void delete(Long id) {
        watchRepository.deleteById(id);
    }
}
