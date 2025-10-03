package com.senibo.e_commerce_api.repository;

import com.senibo.e_commerce_api.model.auth.User;
import com.senibo.e_commerce_api.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {

    List<Order> findByUser(User user);

    Optional<Order> findByPaymentReference(String reference);
}
