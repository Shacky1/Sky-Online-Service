package com.shacky.materialmanagement.service;

import com.shacky.materialmanagement.entity.Customer;
import com.shacky.materialmanagement.entity.ServiceOrder;
import com.shacky.materialmanagement.repository.ServiceOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceOrderService {
    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    public ServiceOrder saveOrder(ServiceOrder order) {
        return serviceOrderRepository.save(order);
    }
    public List<ServiceOrder> getOrdersByCustomer(Customer customer) {
        return serviceOrderRepository.findByCustomer(customer);
    }

    public List<ServiceOrder> findAll() {
        return serviceOrderRepository.findAll();
    }
    public boolean updateOrderStatus(Long orderId, String status) {
        Optional<ServiceOrder> optionalOrder = serviceOrderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            ServiceOrder order = optionalOrder.get();
            order.setStatus(status);
            serviceOrderRepository.save(order);
            return true;
        }
        return false;
    }
    public List<ServiceOrder> getFilteredOrders(String status, Long serviceId) {
        List<ServiceOrder> allOrders = serviceOrderRepository.findAll();

        return allOrders.stream()
                .filter(order -> (status == null || status.isEmpty() || order.getStatus().equalsIgnoreCase(status)))
                .filter(order -> (serviceId == null || order.getService().getId().equals(serviceId)))
                .toList();
    }
    public Optional<ServiceOrder> getOrderById(Long orderId) {
        return serviceOrderRepository.findById(orderId);
    }
    public List<ServiceOrder> getFilteredAndSortedOrders(String status, Long serviceId, String sortBy, String direction) {
        List<ServiceOrder> allOrders = serviceOrderRepository.findAll();

        // Filtering
        List<ServiceOrder> filtered = allOrders.stream()
                .filter(order -> (status == null || status.isEmpty() || order.getStatus().equalsIgnoreCase(status)))
                .filter(order -> (serviceId == null || order.getService().getId().equals(serviceId)))
                .toList();

        // Sorting
        Comparator<ServiceOrder> comparator;

        switch (sortBy) {
            case "datePlaced":
                comparator = Comparator.comparing(ServiceOrder::getDatePlaced);
                break;
            case "cost":
                comparator = Comparator.comparing(order -> order.getService().getCost());
                break;
            default:
                comparator = Comparator.comparing(ServiceOrder::getId);
                break;
        }

        if ("desc".equalsIgnoreCase(direction)) {
            comparator = comparator.reversed();
        }

        return filtered.stream().sorted(comparator).toList();
    }
    public void deleteOrder(ServiceOrder order) {
        serviceOrderRepository.delete(order);
    }


}
