<?php

mb_internal_encoding('UTF-8');

define('CODE_PATH', realpath('..') . '/');

require(CODE_PATH . 'Loader.php');
require(CODE_PATH . 'EventManager.php');
$loader = new Loader();
$loader->setEventsManager(new EventManager());
$loader->registerNamespaces(array(
    'Fyp\Controller\Message' => '../controller/message',
))->register();

// TODO: set di
// TODO: set routers
// TODO: register namespaces
(new Fyp\Controller\Message\AnnouncementController())->listAction();
