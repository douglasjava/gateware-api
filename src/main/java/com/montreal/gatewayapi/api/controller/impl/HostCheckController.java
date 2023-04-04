package com.montreal.gatewayapi.api.controller.impl;


import com.montreal.gatewayapi.api.controller.IHostCheckController;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

@RestController
public class HostCheckController implements IHostCheckController {

    @Override
    public String checkHost() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress()
                + " - " + LocalDateTime.now()
                + " - " + InetAddress.getLocalHost().getHostName();
    }

}