package com.example.optimatefleet.repository;

import com.example.optimatefleet.model.RentContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RentContractRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void createRentContract(RentContract rentContract){
        String sqlCheckCity = "SELECT COUNT(*) FROM city WHERE zip_code = ?";
        int cityCount = jdbcTemplate.queryForObject(sqlCheckCity, Integer.class, rentContract.getZip_code());

        if (cityCount == 0) {
            String sqlCity = "INSERT INTO city(zip_code, city_name) VALUES (?, ?)";
            jdbcTemplate.update(sqlCity, rentContract.getZip_code(), rentContract.getCity_name());
        }

        String sqlAddress = "INSERT INTO address(zip_code, street_name, street_number) VALUES (?, ?, ?)";

        jdbcTemplate.update(sqlAddress, rentContract.getZip_code(), rentContract.getStreet_name(), rentContract.getStreet_number());

        String getAddressId = "SELECT address_id FROM address WHERE zip_code = ? AND street_name = ? AND street_number = ?";

        int addressID = jdbcTemplate.queryForObject(getAddressId, new Object[]{rentContract.getZip_code(),
                        rentContract.getStreet_name(), rentContract.getStreet_number()}, Integer.class
        );

        String sqlRenter = "INSERT INTO renter (drivers_license_number, renter_first_name, renter_last_name," +
                " address_id, renter_phonenumber, email, date_of_birth) VALUES (?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sqlRenter, rentContract.getDrivers_license_number(), rentContract.getRenter_first_name(), rentContract.getRenter_last_name(),
                addressID, rentContract.getRenter_phonenumber(), rentContract.getEmail(), rentContract.getDate_of_birth());

        String sqlRentContract = "INSERT INTO rent_contract (drivers_license_number, license_plate, rental_start_date, " +
                "rental_end_date, pickup_location, return_location, max_km) VALUES (?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sqlRentContract,rentContract.getDrivers_license_number(), rentContract.getLicense_plate(), rentContract.getRental_start_date(), rentContract.getRental_end_date(),
                rentContract.getPickup_location(), rentContract.getReturn_location(), rentContract.getMax_km());
    }

    public List<RentContract> fetchAllRentContracts(){
        String getRentContractsSql = "SELECT \n" +
                "    rent_contract.contract_id, \n" +
                "    rent_contract.license_plate, \n" +
                "    rent_contract.rental_start_date, \n" +
                "    rent_contract.rental_end_date, \n" +
                "    rent_contract.pickup_location, \n" +
                "    rent_contract.return_location, \n" +
                "    rent_contract.max_km,\n" +
                "    renter.renter_first_name,\n" +
                "    renter.renter_last_name,\n" +
                "    renter.renter_phonenumber,\n" +
                "    renter.email,\n" +
                "    renter.date_of_birth,\n" +
                "    address.street_name,\n" +
                "    address.street_number,\n" +
                "    city.city_name,\n" +
                "    address.zip_code\n" +
                "FROM rent_contract\n" +
                "INNER JOIN renter ON rent_contract.drivers_license_number = renter.drivers_license_number\n" +
                "INNER JOIN address ON renter.address_id = address.address_id\n" +
                "INNER JOIN city ON address.zip_code = city.zip_code;\n";

        RowMapper rowMapper = new BeanPropertyRowMapper(RentContract.class);

        return jdbcTemplate.query(getRentContractsSql, rowMapper);

    }

}