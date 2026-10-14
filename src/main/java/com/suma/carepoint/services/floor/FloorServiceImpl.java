package com.suma.carepoint.services.floor;

import com.suma.carepoint.entities.floor.Floor;
import com.suma.carepoint.models.ApiResponse;
import com.suma.carepoint.models.floor.CreateFloorRequest;
import com.suma.carepoint.models.floor.FloorResponse;
import com.suma.carepoint.repositories.floor.FloorRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Slf4j
@Service
public class FloorServiceImpl implements FloorService{
    @Autowired
    private FloorRepository floorRepository ;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public ApiResponse createFloor(CreateFloorRequest request) {

        Floor floor = modelMapper.map(request, Floor.class);

        Floor savedFloor = floorRepository.save(floor);

        FloorResponse response = modelMapper.map(savedFloor, FloorResponse.class);

        return new ApiResponse(1, "Floor created successfully", response);
    }


        @Override
        public ApiResponse getAllFloors() {

            List<Floor> floors = floorRepository.findAll();

            List<FloorResponse> responses = floors.stream()
                    .map(floor -> modelMapper.map(floor, FloorResponse.class)).toList();

            return new ApiResponse(1, "", responses, (long) responses.size());
        }



        @Override
        public ApiResponse getFloorById(Long floorId) {

            Floor floor = floorRepository.findById(floorId)
              .orElseThrow(() -> new RuntimeException("Floor not found"));

            FloorResponse response = modelMapper.map(floor, FloorResponse.class);

            return new ApiResponse(1, "", response);
}


    @Override
        public ApiResponse updateFloor(Long floorId, CreateFloorRequest request) {

            Floor floor = floorRepository.findById(floorId).orElseThrow(() -> new RuntimeException("Floor not found"));

            modelMapper.map(request, floor);

            Floor updatedFloor = floorRepository.save(floor);

            FloorResponse response = modelMapper.map(updatedFloor, FloorResponse.class);

            return new ApiResponse(1, "Floor updated successfully", response);
        }


    @Override
    public ApiResponse deleteFloor(Long floorId) {

        Floor floor = floorRepository.findById(floorId).orElseThrow(() -> new RuntimeException("Floor not found"));

        floorRepository.delete(floor);

        return new ApiResponse(1, "Floor deleted successfully", null);
    }
}
