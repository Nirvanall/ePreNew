<?php
namespace Fyp\Controller\Message;

class AnnouncementController {

    public function listAction() {
        echo json_encode(array(
            'code' => 0,
            'timestamp' => time(),
            'message' => 'Success',
            'list' => array(),
        ));
    }
}
