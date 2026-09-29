package com.suma.carepoint.services.bed;

import com.suma.carepoint.entities.bed.Bed;
import com.suma.carepoint.entities.wardrooms.WardRoom;
import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.bed.BedResponse;
import com.suma.carepoint.models.bed.CreateBedRequest;
import com.suma.carepoint.repositories.bed.BedRepository;
import com.suma.carepoint.repositories.wardroom.WardRoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BedServiceImpl implements BedService {
    private final BedRepository bedRepository;
    private final WardRoomRepository wardRoomRepository;
    private final ModelMapper modelMapper;

    public BedServiceImpl(BedRepository bedRepository, WardRoomRepository wardRoomRepository,
            ModelMapper modelMapper) {

        this.bedRepository = bedRepository;
        this.wardRoomRepository = wardRoomRepository;
        this.modelMapper = modelMapper;
    }


    //Create
    @Override
    public ApiResponse createBed(CreateBedRequest createBedRequest) {

        WardRoom room = wardRoomRepository.findById(createBedRequest.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        Bed bed = modelMapper.map(createBedRequest, Bed.class);

        bed.setRoom(room);

        Bed savedBed = bedRepository.save(bed);

        BedResponse bedResponse = modelMapper.map(savedBed, BedResponse.class);

        bedResponse.setRoomId(savedBed.getRoom().getRoomId());

        return new ApiResponse(1, "Bed created successfully", bedResponse);
    }

    //Get
    @Override
    public ApiResponse getBed(Long bedId) {

        Bed bed = bedRepository.findById(bedId).orElseThrow(() -> new RuntimeException("Bed not found"));

        BedResponse bedResponse = modelMapper.map(bed, BedResponse.class);

        bedResponse.setRoomId(bed.getRoom().getRoomId());

        return new ApiResponse(1, "Bed fetched successfully", bedResponse
        );
    }
//GetAll
    @Override
    public ApiResponse getAllBeds() {

        List<Bed> beds = bedRepository.findAll();

        List<BedResponse> bedResponses = beds.stream()
                .map(bed -> {

                    BedResponse response = modelMapper.map(bed, BedResponse.class);

                    response.setRoomId(bed.getRoom().getRoomId());

                    return response;
                }).toList();

        return new ApiResponse(1, "Beds fetched successfully", bedResponses);
    }

    //Update
    @Override
    public ApiResponse updateBed(Long bedId, CreateBedRequest createBedRequest) {

        Bed existingBed = bedRepository.findById(bedId).orElseThrow(() -> new RuntimeException("Bed not found"));

        WardRoom room = wardRoomRepository.findById(createBedRequest.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        modelMapper.map(createBedRequest, existingBed);

        existingBed.setRoom(room);

        Bed updatedBed = bedRepository.save(existingBed);

        BedResponse bedResponse = modelMapper.map(updatedBed, BedResponse.class);

        bedResponse.setRoomId(updatedBed.getRoom().getRoomId());

        return new ApiResponse(1, "Bed updated successfully", bedResponse);
    }
//Delete
    @Override
    public ApiResponse deleteBed(Long bedId) {

        Bed bed = bedRepository.findById(bedId).orElseThrow(() -> new RuntimeException("Bed not found"));

        bedRepository.delete(bed);

        return new ApiResponse(1, "Bed deleted successfully", null);
    }
}

