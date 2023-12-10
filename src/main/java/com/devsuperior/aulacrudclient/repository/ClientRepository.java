package com.devsuperior.aulacrudclient.repository;

import com.devsuperior.aulacrudclient.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
