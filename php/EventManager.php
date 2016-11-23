<?php

class EventManager {

    public function fire($eventType, $source, $data) {
        echo '[' . $eventType . '] ' . $data . PHP_EOL;
    }
}
