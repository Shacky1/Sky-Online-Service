package com.shacky.materialmanagement.service;

import com.shacky.materialmanagement.entity.OnlineService;
import com.shacky.materialmanagement.repository.OnlineServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OnlineServiceService {

    @Autowired
    private OnlineServiceRepository onlineServiceRepository;

    public List<OnlineService> getAllServices() {
        return onlineServiceRepository.findAll();
    }

    public void saveService(OnlineService service) {
        onlineServiceRepository.save(service);
    }

    public void deleteService(Long id) {
        onlineServiceRepository.deleteById(id);
    }

    public OnlineService getService(Long id) {
        return onlineServiceRepository.findById(id).orElse(null);
    }
}
