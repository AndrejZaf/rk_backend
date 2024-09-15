package com.rarekickz.rk_inventory_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Sneaker not found")
public class SneakerNotFoundException extends RuntimeException {
}
