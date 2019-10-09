package GUI;

import javafx.event.Event;
import javafx.event.EventType;

public abstract class TurnPlayedEvent extends Event {
    public static final EventType<TurnPlayedEvent> CUSTOM_EVENT_TYPE = new EventType(ANY);

    public TurnPlayedEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public abstract void invokeHandler(TurnPlayedEventHandler handler);

}