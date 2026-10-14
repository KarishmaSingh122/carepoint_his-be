package com.suma.carepoint.repositories.bed;

import com.suma.carepoint.entities.bed.Bed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BedRepository extends JpaRepository<Bed,Long> {

    List<Bed> findAllByRoomRoomIdAndStatus(Long roomId, String available);

    List<Bed> findAllByRoomRoomId(Long roomId);
}
