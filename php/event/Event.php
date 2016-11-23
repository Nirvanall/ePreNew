<?php

class Event {

    protected $_type;
    protected $_source;
    protected $_data;
    protected $_stopped = FALSE;
    protected $_cancelable = TRUE;

    public function __construct($type, $source, $data=NULL, $cancelable=TRUE) {
        $this->_type = $type;
        $this->_source = $source;
        if (!is_null($data)) $this->_data = $data;
        if ($cancelable !== TRUE) $this->_cancelable = $cancelable;
    }

    public function setData($data) {
        $this->_data = $data;
        return $this;
    }

    public function setType($type) {
        $this->_type = $type;
        return $this;
    }

    public function stop() {
        if (!$this->_cancelable) {
            throw new Exception('Trying to cancel a non-cancelable event');
        }
        $this->_stopped = TRUE;
        return $this;
    }

    public function isStopped() {
        return $this->_stopped;
    }

    public function isCancelable() {
        return $this->_cancelable;
    }
}
