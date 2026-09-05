package com.abdo.JobManagement.repositories;

import com.abdo.JobManagement.entities.joboffer;
import com.abdo.JobManagement.entities.jobstatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class JobofferSpecification {

    public static Specification<joboffer> filter(String keyword, String location, jobstatus status, Long companyId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.trim().isEmpty()) {
                String pattern = "%" + keyword.trim().toLowerCase() + "%";
                Predicate titleMatch = cb.like(cb.lower(root.get("title")), pattern);
                Predicate descMatch = cb.like(cb.lower(root.get("description")), pattern);
                predicates.add(cb.or(titleMatch, descMatch));
            }

            if (location != null && !location.trim().isEmpty()) {
                String locPattern = "%" + location.trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("location")), locPattern));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (companyId != null) {
                predicates.add(cb.equal(root.get("company").get("id"), companyId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
