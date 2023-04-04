package com.montreal.gatewayapi.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.UnknownHostException;

@Tag(name = "Health")
@RequestMapping(value = "hostcheck", produces = MediaType.APPLICATION_JSON_VALUE)
public interface IHostCheckController {

    @GetMapping()
    String checkHost() throws UnknownHostException;

}
