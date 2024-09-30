package com.hotel.roomBooker.service;

import com.hotel.roomBooker.DTO.HotelFilterDTO;
import com.hotel.roomBooker.model.Hotel;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class HotelSpecification {

    public static Specification<Hotel> createSpecification(HotelFilterDTO filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("id"), filter.getId()));
            }

            if (filter.getName() != null && !filter.getName().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + filter.getName() + "%"));
            }

            if (filter.getAnnouncementTitle() != null && !filter.getAnnouncementTitle().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("announcementTitle"), "%" + filter.getAnnouncementTitle() + "%"));
            }

            if (filter.getCity() != null && !filter.getCity().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("city"), "%" + filter.getCity() + "%"));
            }

            if (filter.getAddress() != null && !filter.getAddress().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("address"), "%" + filter.getAddress() + "%"));
            }

            if (filter.getDistanceFromCityCenter() != null) {
                predicates.add(criteriaBuilder.equal(root.get("distanceFromCityCenter"), filter.getDistanceFromCityCenter()));
            }

            if (filter.getRating() != null) {
                predicates.add(criteriaBuilder.equal(root.get("rating"), filter.getRating()));
            }

            if (filter.getNumberOfRatings() != null) {
                predicates.add(criteriaBuilder.equal(root.get("numberOfRatings"), filter.getNumberOfRatings()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
