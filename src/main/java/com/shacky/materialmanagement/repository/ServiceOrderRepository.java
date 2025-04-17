package com.shacky.materialmanagement.repository;

import com.shacky.materialmanagement.entity.Customer;
import com.shacky.materialmanagement.entity.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {
    List<ServiceOrder> findByCustomer(Customer customer);

}
