package com.soa.service;

import com.soa.model.Producer;
import com.soa.repository.ProducerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProducerService {

    @Autowired
    private ProducerRepository producerRepository;

    public List<Producer> findAll() {
        return producerRepository.findAll();
    }

    public List<Producer> findByName(String name) {
        return producerRepository.findByName(name);
    }

}
