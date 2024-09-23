package com.hotel.roomBooker.service;

import com.hotel.roomBooker.DTO.RoomFilterDTO;
import com.hotel.roomBooker.exception.InvalidInputException;
import com.hotel.roomBooker.model.Booking;
import com.hotel.roomBooker.model.Room;
import com.hotel.roomBooker.model.UnavailableDate;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class RoomSpecification {

    public static Specification<Room> createSpecification(RoomFilterDTO filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("id"), filter.getId()));
            }

            if (filter.getTitle() != null && !filter.getTitle().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + filter.getTitle() + "%"));
            }

            if (filter.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), filter.getMinPrice()));
            }

            if (filter.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), filter.getMaxPrice()));
            }

            if (filter.getMaxPeople() != null) {
                predicates.add(criteriaBuilder.equal(root.get("maxPeople"), filter.getMaxPeople()));
            }

            if (filter.getHotelId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("hotel").get("id"), filter.getHotelId()));
            }

            if (filter.getCheckInDate() != null && filter.getCheckOutDate() != null) {
                Predicate availablePredicate = isRoomAvailable(root, query, criteriaBuilder, filter.getCheckInDate(), filter.getCheckOutDate());
                if (availablePredicate != null) {
                    predicates.add(availablePredicate);
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Predicate isRoomAvailable(Root<Room> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, LocalDate checkInDate, LocalDate checkOutDate) {
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<UnavailableDate> unavailableDateRoot = subquery.from(UnavailableDate.class);
        subquery.select(unavailableDateRoot.get("id"))
                .where(
                        criteriaBuilder.equal(unavailableDateRoot.join("rooms").get("id"), root.get("id")),
                        criteriaBuilder.between(unavailableDateRoot.get("date"), checkInDate, checkOutDate)
                );
        return criteriaBuilder.not(criteriaBuilder.exists(subquery));
    }
}
