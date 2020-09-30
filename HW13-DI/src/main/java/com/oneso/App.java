package com.oneso;

import com.oneso.appcontainer.AppComponentsContainerImpl;
import com.oneso.appcontainer.api.AppComponentsContainer;
import com.oneso.services.GameProcessor;

public class App {

    public static void main(String[] args) throws Exception {
        AppComponentsContainer container = new AppComponentsContainerImpl(false, "com.oneso.config");
        GameProcessor gameProcessor = container.getAppComponent(GameProcessor.class);
        gameProcessor.startGame();
    }
}
