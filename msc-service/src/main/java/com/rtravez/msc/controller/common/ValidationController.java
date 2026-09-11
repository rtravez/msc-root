package com.rtravez.msc.controller.common;

import com.rtravez.msc.dto.BaseResponseDto;
import com.rtravez.msc.service.common.ValidationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/validations")
@Validated
@Slf4j
@Tag(name = "Validaciones", description = "Validación de identificaciones y RUC")
public class ValidationController {

	private final ValidationServiceImpl service;

	public ValidationController(ValidationServiceImpl service) {
		this.service = service;
	}

	@GetMapping(path = "identification/{identification}")
	@Operation(summary = "Validar identificación", security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponse(responseCode = "200", description = "Identificación validada correctamente")
	@ApiResponse(responseCode = "401", description = "Token ausente o inválido")
	public ResponseEntity<BaseResponseDto<Object>> validationIdentification(
			@Parameter(description = "Número de identificación a validar", required = true, example = "1712345678")
			@PathVariable String identification) {
		return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.builder().status(HttpStatus.OK.value())
				.data(service.validationIdentification(identification)).detail("Identificación validada con éxito").build());
	}

	@GetMapping(path = "ruc/{ruc}")
	@Operation(summary = "Validar RUC", security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponse(responseCode = "200", description = "RUC validado correctamente")
	@ApiResponse(responseCode = "401", description = "Token ausente o inválido")
	public ResponseEntity<BaseResponseDto<Object>> validationRuc(
			@Parameter(description = "RUC a validar", required = true, example = "1790012345001")
			@PathVariable String ruc) {
		return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.builder().status(HttpStatus.OK.value()).data(service.validationRuc(ruc))
				.detail("RUC validado con éxito").build());
	}

}
