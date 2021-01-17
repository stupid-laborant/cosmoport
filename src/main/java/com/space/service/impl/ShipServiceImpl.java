package com.space.service.impl;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class ShipServiceImpl implements ShipService {
    @Autowired
    private ShipRepository shipRepository;

    public void calculateRating(Ship ship) {
        int CURRENT_YEAR=3019;
        GregorianCalendar prodDate = new GregorianCalendar();
        prodDate.setTime(ship.getProdDate());
        int prodYear = prodDate.get(Calendar.YEAR);
        double k = ship.getUsed()? 0.5 : 1;
        double shipSpeed = ship.getSpeed();
        Double rating = 80*shipSpeed*k/(CURRENT_YEAR-prodYear+1);
        rating = Math.round(rating*100)/100.0;
        ship.setRating(rating);
    }

    @Override
    public List<Ship> getAll(Specification<Ship> shipSpecification, Pageable pageable) {

        return shipRepository.findAll(shipSpecification, pageable).getContent();
    }

    @Override
    public Integer countShips(Specification<Ship> shipSpecification) {

        return shipRepository.findAll(shipSpecification).size();
    }

    @Override
    public Ship createShip(Ship ship) {
        Boolean isUsed = ship.getUsed();
        if (isUsed == null) {
            ship.setUsed(false);
        }
        calculateRating(ship);
        Ship savedShip = shipRepository.saveAndFlush(ship);
        return savedShip;
    }

    @Override
    public Ship updateShip(Long id, Ship ship) {

        Optional<Ship> foundShip = shipRepository.findById(id);

            Ship toBeUpdated = foundShip.get();
            String name = ship.getName();
            if (name != null) {
                toBeUpdated.setName(name);
            }
            String planet = ship.getPlanet();
            if (planet != null) {
                toBeUpdated.setPlanet(planet);
            }
            ShipType shipType = ship.getShipType();
            if (shipType != null) {
                toBeUpdated.setShipType(shipType);
            }
            Date prodDate = ship.getProdDate();
            if (prodDate != null) {
                toBeUpdated.setProdDate(prodDate);
            }
            Boolean isUsed = ship.getUsed();
            if (isUsed != null) {
                toBeUpdated.setUsed(isUsed);
            }
            Double speed = ship.getSpeed();
            if (speed != null) {
                toBeUpdated.setSpeed(speed);
            }
            Integer crewSize = ship.getCrewSize();
            if (crewSize != null) {
                toBeUpdated.setCrewSize(crewSize);
            }
            calculateRating(toBeUpdated);
            shipRepository.saveAndFlush(toBeUpdated);

            return toBeUpdated;

    }

    @Override
    public Ship getShip(Long id) {
        return shipRepository.findById(id).get();
    }

    @Override
    public void deleteShip(Long id) {
        shipRepository.deleteById(id);
    }

    @Override
    public boolean isIdValid(Long id) {
        return id > 0;
    }

    @Override
    public boolean isIdExists(Long id) {
        return shipRepository.existsById(id);
    }

    @Override
    public String validateCreateShip(Ship ship) {

        String errorMessage = new String();
        errorMessage = errorMessage.concat(validateName(ship.getName()));
        errorMessage = errorMessage.concat(validatePlanet(ship.getPlanet()));
        errorMessage = errorMessage.concat(validateProdDate(ship.getProdDate()));
        errorMessage = errorMessage.concat(validateSpeed(ship.getSpeed()));
        errorMessage = errorMessage.concat(validateCrewSize(ship.getCrewSize()));
        return errorMessage;
    }

    @Override
    public String validateUpdateShip(Ship ship) {
        String errorMessage = new String();
        String name = ship.getName();
        if (ship.getName() != null && (name.isEmpty() || name.length() > 50)) {
            errorMessage = errorMessage.concat("Name should be not empty and shorter than 50 symbols. ");
        }
        String planet = ship.getPlanet();
        if (planet != null && planet.length() > 50) {
            errorMessage = errorMessage.concat("Planet should be not empty and shorter than 50 symbols. ");
        }
        Date prodDate = ship.getProdDate();
        if (prodDate != null) {
            Calendar shipProdDate = new GregorianCalendar();
            shipProdDate.setTime(prodDate);
            Integer yearOfProd = shipProdDate.get(Calendar.YEAR);
            if (yearOfProd > 3019 || yearOfProd < 2800) {
                errorMessage = errorMessage.concat("Prod year should be less than 3019 and greater than 2800. ");
            }
        }
        Integer crewSize = ship.getCrewSize();
        if (crewSize != null && (crewSize < 1 || crewSize > 9999))  {
            errorMessage = errorMessage.concat("Crew size should be between 1 and 9999. ");
        }
        Double speed = ship.getSpeed();
        if (speed != null && (speed < 0.01d || speed > 0.99d)) {
            errorMessage = errorMessage.concat("Speed should be between 0.01 and 0.99. ");
        }
        return errorMessage;
    }

    @Override
    public String validateName(String name) {
        if ( name == null || name.isEmpty() || name.length() > 50) {
            return "Name is too long or empty. ";
        }
        return "";
    }

    @Override
    public String validatePlanet(String planet) {
        if ( planet == null || planet.length() > 50) {
            return "Planet name is too long or empty. ";
        }
        return "";
    }

    @Override
    public String validateProdDate(Date prodDate) {
        if (prodDate == null) {
            return "Ship prod date is null";
        }
        Calendar shipProdDate = new GregorianCalendar();

            shipProdDate.setTime(prodDate);
            Integer yearOfProd = shipProdDate.get(Calendar.YEAR);
            if (yearOfProd > 3019 || yearOfProd < 2800) {
                return "Year of production should be between 2800 and 3019. ";
            }
        return "";
    }

    @Override
    public String validateIsUsed(Boolean isUsed) {
        if (isUsed == null) {
            return "IsUsed flag is empty";
        }
        return "";
    }

    @Override
    public String validateSpeed(Double speed) {
        if ( speed == null || speed < 0.01d || speed > 0.99d) {
            return "Speed should be between 0.01 and 0.99. ";
        }
        return "";
    }

    @Override
    public String validateCrewSize(Integer crewSize) {
        if ( crewSize == null || crewSize < 1 || crewSize > 9999) {
            return "Crew size should be between 1 and 9999. ";
        }
        return "";
    }


}
