package com.ppm.sgs.services;

import java.util.List;

import com.ppm.sgs.models.Batch;
import com.ppm.sgs.models.BatchDto;
import com.ppm.sgs.models.Course;

public interface BatchService {
    void save(Batch batch);
    List<Batch> getAllBatches();
    List<Batch> getActiveBatchesByCourse(Course course);
    List<Batch> getArchivedBatchesByCourse(Course course);
    Batch convertDtoToEntity(BatchDto batchDto);
}
