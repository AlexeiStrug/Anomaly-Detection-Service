package com.example.demo.anomaly;

import com.example.demo.anomaly.model.Anomaly;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnomalyRepository extends JpaRepository<Anomaly, Long> {
    Page<Anomaly> findByThermometerId(String thermometerId, Pageable pageable);

    Page<Anomaly> findByRoomId(String roomId, Pageable pageable);

    @Query("""
            SELECT a.thermometerId, COUNT(a) 
            FROM Anomaly a 
            GROUP BY a.thermometerId 
            HAVING COUNT(a) > :threshold
            """)
    Page<Object[]> findThermometersWithHighAnomalyCount(@Param("threshold") Integer threshold, Pageable pageable);

}
