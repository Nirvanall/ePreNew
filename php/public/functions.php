<?php

/**
 * For compatible with Zephir
 */
function typeof($something) {
    return gettype($something);
}
function fetch(&$variable, $arrayOrObject, $key) {
    if (is_array($arrayOrObject) && array_key_exists($key, $arrayOrObject)) {
        return $variable = $arrayOrObject[$key];
    } else if (isset($arrayOrObject->$key)) {
        return $variable = $arrayOrObject->$key;
    }
    return $variable = NULL;
}


/**
 * The first second of the day
 * @param  Integer  $timestamp  a Unix timestamp
 * @return Integer  a Unix timestamp
 */
function dayBegin($timestamp=NULL) {
    if (!is_int($timestamp)) $timestamp = time();
    return mktime(0, 0, 0, date('n', $timestamp),
            date('j', $timestamp), date('Y', $timestamp));
}

/**
 * The last second of the day
 * @param  Integer  $timestamp  a Unix timestamp
 * @return Integer  a Unix timestamp
 */
function dayEnd($timestamp=NULL) {
    return dayBegin($timestamp) + 24 * 3600 - 1;
}

