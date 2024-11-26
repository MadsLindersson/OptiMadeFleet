package com.example.optimatefleet.controller;

import com.example.optimatefleet.model.RentContract;
import com.example.optimatefleet.service.RentContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RentContractController {

    @Autowired
    RentContractService rentContractService;

    @GetMapping("/RentContract")
    public String createRentContract(){
        return "RentContract";
    }

    @PostMapping("/RentContract")
    public String createRentContract(@ModelAttribute RentContract rentContract){
        rentContractService.createRentContract(rentContract);
        return "RentContract";
    }

    @GetMapping("/DataRegister")
    public String showAllRentalContracts(Model model){
        List<RentContract> rentContractList = rentContractService.showAllRentContracts();
        model.addAttribute("rentContracts", rentContractList);
        return "DataRegister";
    }
}
