<?php

class EventManager {

    protected $_events = NULL;
    protected $_collect = FALSE;
    protected $_enablePriorities = FALSE;
    protected $_responses;

    public function fire($eventType, $source, $data=NULL, $cancelable=TRUE) {
        if (!is_array($this->_events)) {
            return NULL;
        }

        // All valid events must have a colon seperator
        if (strpos($eventType, ':') === FALSE) {
            throw new Exception('Invalid event type' . $eventType);
        }

        $eventParts = explode(':', $eventType);
        $type = $eventParts[0];
        $eventName = $eventParts[1];
        $status = NULL;

        if ($this->_collect) $this->_responses = NULL;
        $event = NULL;

        // Check if events are grouped by type
        if (isset($this->_events[$type])) {
            $fireEvents = $this->_events[$type];
            if (is_object($fireEvents) || is_array($fireEvents)) {
                // Create the event context
                $event = new Event($eventName, $source, $data, $cancelable);

                // Call the events queue
                $status = $this->fireQueue($fireEvents, $event);
            }
        }

        // Check if there are listeners for the event type itself
        if (isset($this->_events[$eventType])) {
            $fireEvents = $this->_events[$eventType];
            if (is_object($fireEvents) || is_array($fireEvents)) {
                // Create the event context
                $event = new Event($eventName, $source, $data, $cancelable);

                // Call the events queue
                $status = $this->fireQueue($fireEvents, $event);
            }
        }

