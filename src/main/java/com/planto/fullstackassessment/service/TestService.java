package com.planto.fullstackassessment.service;

import com.planto.fullstackassessment.model.TestEntity;
import com.planto.fullstackassessment.repository.TestRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;
    private final EntityManager entityManager;
    public TestEntity getTest() {
        List<TestEntity> tests =  this.testRepository.findAll();
        if (tests.size() > 0) {
            return tests.get(tests.size() - 1);
        }
        return null;
    }

    @Transactional
    public void addTest() {
        TestEntity testEntity = new TestEntity();
        testEntity.setValue(UUID.randomUUID().toString());
        this.entityManager.persist(testEntity);
    }
}
