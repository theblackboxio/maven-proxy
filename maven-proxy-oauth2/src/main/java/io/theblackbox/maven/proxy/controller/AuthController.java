package io.theblackbox.maven.proxy.controller;

import io.theblackbox.maven.proxy.service.AuthorizationFlowManager;
import io.theblackbox.maven.proxy.service.BackendIdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by guillermoblascojimenez on 18/04/15.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private BackendIdentityService backendIdentityService;

    @Autowired
    private AuthorizationFlowManager authorizationFlowManager;

    @RequestMapping(value="/status", method = RequestMethod.GET)
    public Map<String, ?> status() throws IOException {
        authorizationFlowManager.loadToken();
        if (!authorizationFlowManager.hasToken()) {
            Map<String, String> map = new HashMap<>();
            map.put("status", "LOGIN_REQUIRED");
            map.put("cause", "REFRESH_TOKEN_NOT_FOUND");
            return map;
        }
        try {
            String name = backendIdentityService.backendUser();
            Map<String, String> map = new HashMap<>();
            map.put("status", "OK");
            map.put("name", name);
            return map;
        } catch (IOException e) {
            Map<String, String> map = new HashMap<>();
            map.put("status", "LOGIN_REQUIRED");
            map.put("cause", "REFRESH_TOKEN_EXPIRED");
            map.put("rawError", e.getMessage());
            return map;
        }
    }

    @RequestMapping(method=RequestMethod.GET)
    public Map<String, ?> getUrl() {
        authorizationFlowManager.startFlow();
        String url = authorizationFlowManager.getUrl();
        return Collections.singletonMap("url", url);
    }

    @RequestMapping(method=RequestMethod.DELETE)
    public Map<String, ?> clearAuth() throws IOException {
        authorizationFlowManager.clearToken();
        return Collections.singletonMap("status", "OK");
    }

    @RequestMapping(method=RequestMethod.POST)
    public Map<String, ?> commitCode(@RequestBody String code) throws IOException {
        authorizationFlowManager.commitCode(code);
        return Collections.singletonMap("status", "OK");
    }
}
