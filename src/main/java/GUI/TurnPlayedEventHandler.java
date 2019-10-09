package GUI;

import javafx.event.EventHandler;

public abstract class TurnPlayedEventHandler implements EventHandler<TurnPlayedEvent> {

    public abstract void turnSwitched();

    @Override
    public void handle(TurnPlayedEvent event) {
        event.invokeHandler(this);
    }
}
