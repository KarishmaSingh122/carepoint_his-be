package com.suma.carepoint.repositories.wardroom;


import com.suma.carepoint.entities.wardrooms.WardRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WardRoomRepository extends JpaRepository<WardRoom,Long> {

    Optional<WardRoom> findByWardWardId(Long wardId);

    List<WardRoom> findAllByWardWardId(Long wardId);
}
