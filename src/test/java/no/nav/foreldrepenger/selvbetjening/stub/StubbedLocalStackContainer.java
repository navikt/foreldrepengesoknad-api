package no.nav.foreldrepenger.selvbetjening.stub;

import org.testcontainers.containers.localstack.LocalStackContainer;

public class StubbedLocalStackContainer extends LocalStackContainer {

    public  StubbedLocalStackContainer(){ }

    @Override
    public StubbedLocalStackContainer withServices(Service... services) {
        super.withServices(services);
        try {
            super.before();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
        return this;
    }


    public void stopContainer(){
        super.after();
    }
}
