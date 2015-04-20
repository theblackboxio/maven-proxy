package io.theblackbox.maven.proxy.service;

/**
 * Created by guillermoblascojimenez on 18/04/15.
 */
public abstract class AbstractAuthrizatinFlowManager implements AuthorizationFlowManager {

    private State state;

    protected AbstractAuthrizatinFlowManager() {
        state = State.READY;
    }

    protected void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

}
