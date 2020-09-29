package com.oneso.config;

import com.oneso.appcontainer.api.AppComponent;
import com.oneso.appcontainer.api.AppComponentsContainerConfig;
import com.oneso.services.IOService;
import com.oneso.services.IOServiceConsole;

@AppComponentsContainerConfig(order = 0)
public class AppConfigIO {

    @AppComponent(order = 0, name = "ioService")
    public IOService ioService() {
        return new IOServiceConsole(System.out, System.in);
    }
}
