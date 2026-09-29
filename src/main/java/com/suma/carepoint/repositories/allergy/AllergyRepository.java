package com.suma.carepoint.repositories.allergy;

import com.suma.carepoint.entities.allergy.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllergyRepository extends JpaRepository<Allergy,Long> {
}
