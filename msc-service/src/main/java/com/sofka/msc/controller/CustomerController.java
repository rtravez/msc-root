package com.sofka.msc.controller;

import com.sofka.msc.dto.BaseResponseDto;
import com.sofka.msc.dto.request.CustomerRequest;
import com.sofka.msc.dto.response.CustomerResponse;
import com.sofka.msc.exception.ExceptionManager;
import com.sofka.msc.service.ICustomerService;
import com.sofka.msc.service.IPersonService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * CustomerController
 */
@RestController()
@RequestMapping("/api/customers")
@Validated
@Slf4j
public class CustomerController {

    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IPersonService personService;

    /**
     * Find customer all
     *
     * @return
     */
    @GetMapping
    @Operation(summary = "Find Customer")
    public ResponseEntity<BaseResponseDto<Object>> findCustomerAll() {
        try {
            List<CustomerResponse> customerResponses = customerService.findCustomerAll();
            if (customerResponses.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.builder().code(HttpStatus.OK.value()).message("No existen clientes").build());
            }

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.builder().code(HttpStatus.OK.value())
                    .data(customerResponses).message("Clientes encontrados con \u00E9xito").build());
        } catch (ExceptionManager e) {
            log.error("findCustomerAll", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponseDto.builder().message(e.getMessage()).code(HttpStatus.INTERNAL_SERVER_ERROR.value()).build());
        }
    }

    /**
     * Find customer by identification
     *
     * @param request
     * @return
     */
    @PostMapping(path = "findCustomerByIdentification")
    @Operation(summary = "Find customer by identification")
    public ResponseEntity<CustomerResponse> findCustomerByIdentification(@RequestBody CustomerRequest request) {
        try {
            return ResponseEntity.ok(this.customerService.findCustomerByIdentification(request));
        } catch (ExceptionManager e) {
            log.error("findCustomerByIdentification", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Save customer
     *
     * @param request
     * @return
     */
    @Secured({"ROLE_ADMIN"})
    @PostMapping
    @Operation(summary = "Create Customer")
    public ResponseEntity<BaseResponseDto<Object>> save(@Valid @RequestBody CustomerRequest request) {
        try {
            if (Boolean.TRUE.equals(this.personService.exist(request.getIdentification()))) {
                return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.builder().code(HttpStatus.OK.value()).message("El cliente ya existe").build());
            }

            CustomerResponse response = customerService.processSaveCustomer(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseDto.builder().code(HttpStatus.CREATED.value()).data(response).message("Cliente creado con \u00E9xito").build());
        } catch (ExceptionManager e) {
            log.error("save: {0}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponseDto.builder().message(e.getMessage()).code(HttpStatus.INTERNAL_SERVER_ERROR.value()).build());
        }
    }

    /**
     * Update customer
     *
     * @param request
     * @return
     */
    @Secured({"ROLE_ADMIN"})
    @PutMapping
    @Operation(summary = "Update Customer")
    public ResponseEntity<BaseResponseDto<Object>> update(@Valid @RequestBody CustomerRequest request) {
        try {
            CustomerResponse response = customerService.processUpdateCustomer(request);
            if (response == null) {
                return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.builder().code(HttpStatus.OK.value()).message("El cliente no existe").build());
            }

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.builder().code(HttpStatus.OK.value()).data(response).message("Cliente actualizado con \u00E9xito").build());
        } catch (ExceptionManager e) {
            log.error("update: {0}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponseDto.builder().message(e.getMessage()).code(HttpStatus.INTERNAL_SERVER_ERROR.value()).build());
        }
    }

    /**
     * Delete customer by id
     *
     * @param id
     * @return
     */
    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Delete Customer")
    public ResponseEntity<BaseResponseDto<Object>> deleteById(@PathVariable Long id) {
        try {
            if (this.customerService.deleteCustomerById(id) >= 1) {
                return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.builder().code(HttpStatus.OK.value()).message("Cliente eliminado con \u00E9xito").build());
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.builder().code(HttpStatus.OK.value()).message("El cliente no existe").build());
            }
        } catch (ExceptionManager e) {
            log.error("deleteById", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponseDto.builder().message(e.getMessage()).code(HttpStatus.INTERNAL_SERVER_ERROR.value()).build());
        }
    }
}
