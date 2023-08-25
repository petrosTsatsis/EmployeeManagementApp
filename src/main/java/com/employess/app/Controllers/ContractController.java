package com.employess.app.Controllers;

import com.employess.app.Entities.Contract;
import com.employess.app.Repositories.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contracts")
public class ContractController {

    @Autowired
    private ContractRepository contractRepository;

    // get all contracts
    @GetMapping("")
    List<Contract> getAllContracts(){
        return contractRepository.findAll();
    }

    // get contract by id
    @GetMapping("/{contract_id}")
    Optional<Contract> get(@PathVariable int contract_id){
        return contractRepository.findById(contract_id);
    }

    // delete contract by id
    @DeleteMapping("/{contract_id}")
    public void delete(@PathVariable int contract_id) {
        contractRepository.deleteById(contract_id);
    }

    //update a contract
    @PutMapping("/{contract_id}/update-contract")
    public ResponseEntity<Contract> updateContract(@PathVariable int contract_id, @RequestBody Contract contractDetails){

        Optional<Contract> optionalContract = contractRepository.findById(contract_id);

        // checks if the contract exists
        if(optionalContract.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Contract not found"
            );
        }

        // modify the optional type to Contract type
        Contract updateContract = optionalContract.get();

        //update contract
        updateContract.setHireDate(contractDetails.getHireDate());
        updateContract.setEndDate(contractDetails.getEndDate());
        updateContract.setJobTitle(contractDetails.getJobTitle());
        updateContract.setMonthlySalary(contractDetails.getMonthlySalary());

        contractRepository.save(updateContract);

        return ResponseEntity.ok(updateContract);
    }


}
