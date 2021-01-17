package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

public interface ShipService {
    List<Ship> getAll(Specification<Ship> shipSpecification, Pageable pageable);
    Integer countShips(Specification<Ship> shipSpecification);
    Ship createShip(Ship ship);
    Ship updateShip(Long id, Ship ship);
    Ship getShip(Long id);
    void deleteShip(Long id);
    boolean isIdValid (Long id);
    boolean isIdExists (Long id);
    String validateCreateShip (Ship ship);
    String validateUpdateShip (Ship ship);
    String validateName(String name);
    String validatePlanet(String planet);
    String validateProdDate(Date prodDate);
    String validateIsUsed(Boolean isUsed);
    String validateSpeed(Double speed);
    String validateCrewSize(Integer crewSize);

    static Specification<Ship> getShipsByNameSpec(String name) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                if (name != null) {
                    return criteriaBuilder.like(root.get("name"), "%" + name + "%");
                } else return criteriaBuilder.conjunction();

            }
        };
    }

    static Specification<Ship> getShipsByPlanetSpec(String planet) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                if (planet != null) {
                    return criteriaBuilder.like(root.get("planet"), "%" + planet + "%");
                } else return criteriaBuilder.conjunction();
            }
        };
    }

    static Specification<Ship> getShipsByTypeSpec(ShipType shipType) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                if (shipType  != null) {
                    return criteriaBuilder.equal(root.get("shipType"), shipType);
                } else return criteriaBuilder.conjunction();
            }
        };
    }

    static Specification<Ship> getShipsByIsUsedSpec(Boolean isUsed) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                if (isUsed == null) {
                    return criteriaBuilder.conjunction();
                } else if (isUsed) {
                    return criteriaBuilder.isTrue(root.get("isUsed"));
                }
                return criteriaBuilder.isFalse(root.get("isUsed"));
            }
        };
    }

    static Specification<Ship> getShipsByProdDateSpec(Long before, Long after) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                if (before != null && after != null) {
                    return criteriaBuilder.between(root.get("prodDate"), new Date(after), new Date(before));
                } else if (after != null ) {
                    return criteriaBuilder.greaterThanOrEqualTo(root.get("prodDate"), new Date(after));
                } else if (before != null) {
                    return criteriaBuilder.lessThanOrEqualTo(root.get("prodDate"), new Date(before));

                } else {
                    return criteriaBuilder.conjunction();
                }
            }
        };
    }
    static Specification<Ship> getShipsBySpeedSpec(Double minSpeed, Double maxSpeed) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                if (minSpeed != null && maxSpeed != null) {
                    return criteriaBuilder.between(root.get("speed"), minSpeed, maxSpeed);
                } else if (minSpeed != null) {
                    return criteriaBuilder.greaterThanOrEqualTo(root.get("speed"), minSpeed);
                } else if (maxSpeed != null) {
                    return criteriaBuilder.lessThanOrEqualTo(root.get("speed"), maxSpeed);

                } else {
                    return criteriaBuilder.conjunction();
                }
            }
        };
    }

    static Specification<Ship> getShipsByCrewSizeSpec(Integer minCrewSize, Integer maxCrewSize) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                if (minCrewSize != null && maxCrewSize != null) {
                    return criteriaBuilder.between(root.get("crewSize"), minCrewSize, maxCrewSize);
                } else if (minCrewSize != null) {
                    return criteriaBuilder.greaterThanOrEqualTo(root.get("crewSize"), minCrewSize);
                } else if (maxCrewSize != null) {
                    return criteriaBuilder.lessThanOrEqualTo(root.get("crewSize"), maxCrewSize);

                } else {
                    return criteriaBuilder.conjunction();
                }
            }
        };
    }

    static Specification<Ship> getShipsByRatingSpec(Double minRating, Double maxRating) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                if (minRating != null && maxRating != null) {
                    return criteriaBuilder.between(root.get("rating"), minRating, maxRating);
                } else if (minRating != null) {
                    return criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), minRating);
                } else if (maxRating != null) {
                    return criteriaBuilder.lessThanOrEqualTo(root.get("rating"), maxRating);

                } else {
                    return criteriaBuilder.conjunction();
                }
            }
        };
    }

}
