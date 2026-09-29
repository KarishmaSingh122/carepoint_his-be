package com.suma.carepoint.repositories.wardroom;


import com.suma.carepoint.entities.wardrooms.WardRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WardRoomRepository extends JpaRepository<WardRoom,Long> {

}
