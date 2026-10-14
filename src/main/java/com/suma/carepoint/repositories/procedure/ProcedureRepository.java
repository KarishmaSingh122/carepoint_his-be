package com.suma.carepoint.repositories.procedure;

import com.suma.carepoint.entities.procedure.Procedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcedureRepository  extends JpaRepository<Procedure,Long> {

    Optional<Procedure> findByProcedureCodeIgnoreCase(
            String procedureCode
    );

    Optional<Procedure> findByProcedureNameIgnoreCase(
            String procedureName
    );

    boolean existsByProcedureCodeIgnoreCase(
            String procedureCode
    );

    boolean existsByProcedureNameIgnoreCase(
            String procedureName
    );

    List<Procedure> findByActiveTrue();

    List<Procedure> findByProcedureNameContainingIgnoreCaseAndActiveTrue(
            String procedureName
    );

    List<Procedure> findByProcedureCodeContainingIgnoreCaseAndActiveTrue(
            String procedureCode
    );

    boolean existsByProcedureCodeIgnoreCaseAndProcedureIdNot(String procedureCode, Long procedureId);

    boolean existsByProcedureNameIgnoreCaseAndProcedureIdNot(String procedureName, Long procedureId);

    List<Procedure> findByProcedureNameContainingIgnoreCaseOrProcedureCodeContainingIgnoreCaseAndActiveTrue(String searchKeyword, String searchKeyword1);
}
