package ru.galuzin.spring.ioc.domain;

import java.util.Collection;

public class EventServer {

    private final Collection<EventHandler> eventHandlers;

    public EventServer(Collection<EventHandler> eventHandlers){
        this.eventHandlers = eventHandlers;
    }

    public void register(){
        eventHandlers.forEach(eh -> eh.onEvent() );
    }
}
