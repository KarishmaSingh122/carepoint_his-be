package com.suma.carepoint.repositories.emr;

import com.suma.carepoint.entities.emr.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllergyRepositiry extends JpaRepository<Allergy,Long> {
}
