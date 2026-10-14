package com.suma.carepoint.repositories.discharge;

import com.suma.carepoint.entities.discharge.Discharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DischargeRepository extends JpaRepository<Discharge,Long> {
}
