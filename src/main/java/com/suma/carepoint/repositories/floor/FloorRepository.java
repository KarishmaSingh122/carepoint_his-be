package com.suma.carepoint.repositories.floor;

import com.suma.carepoint.entities.floor.Floor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FloorRepository extends JpaRepository<Floor,Long> {

    List<Floor> findByActiveTrue();

    boolean existsByFloorNumber(Long floorNumber);

}
