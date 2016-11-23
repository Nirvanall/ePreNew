<?php

class Request {

    protected $_dependencyInjector;
    protected $_rawBody;
    protected $_filter;
    protected $_putCache;
    protected $_httpMethodParameterOverride = FALSE;
    protected $_strictHostCheck = FALSE;

    public function setDI($dependencyInjector) {
        $this->_dependencyInjector = $dependencyInjector;
    }

    public function getDI() {
        return $this->_dependencyInjector;
    }

    /**
     * Helper to get data from superglobals, applying filters if needed.
     * If no parameters are given the superglobal is returned.
     */
    protected function getHelper($source, $name=NULL, $filters=NULL,
            $defaultValue=NULL, $notAllowEmpty=FALSE, $noRecursive=FALSE) {

        if (is_null($name) {
            return $source;
        }

        if (!isset($source[$name])) {
            return $defaultValue;
        }

        if (!is_null($filters)) {
            if (!is_object($this->_filter)) {
                if (!is_object($dependencyInjector)) {
                    throw new Exception('A dependency injection object is required to access the "filter" service');
                }
                $this->_filter = $this->_dependencyInjector->getShared('filter');
            }

            $value = $filter->sanitize($value, $filters, $noRecursive);
        }

        if (empty($value) && $notAllowEmpty === TRUE) {
            return $defaultValue;
        }

        return $value;
    }

    /**
     * Gets variable from $_GET superglobal applying filters if needed
     * If no parameters are given the $_GET superglobal is returned
     *
     * <code>
     *   // Returns value from $_GET['id'] without sanitizing
     *   $id = $request->getQuery('id');
     *
     *   // Returns value from $_GET['id'] with sanitizing
     *   $id = $request->getQuery('id', 'int');
     *
     *   // Returns value from $_GET['id'] with a default value
     *   $id = $request->getQuery('id', NULL, 150);
     * </code>
     */
    public function getQuery($name=NULL, $filters=NULL, $defaultValue=NULL,
            $notAllowEmpty=FALSE, $noRecursive=FALSE) {
        return $this->getHelper($_GET, $name, $filters, $defaultValue,
                $notAllowEmpty, $noRecursive);
    }

    /**
     * Gets a variable from the $_POST superglobal applying filters if needed
     * If no parameters are given the $_POST superglobal is returned
     *
     * <code>
     *   //Returns value from $_POST["user_email"] without sanitizing
     *   $userEmail = $request->getPost("user_email");
     * 
     *   //Returns value from $_POST["user_email"] with sanitizing
     *   $userEmail = $request->getPost("user_email", "email");
     * </code>
     */
    public function getPost($name=NULL, $filters=NULL, $defaultValue=NULL,
            $notAllowEmpty=FALSE, $noRecursive=FALSE) {
        return $this->getHelper($_POST, $name, $filters, $defaultValue,
                $notAllowEmpty, $noRecursive);
    }

    /**
     * Gets a variable from put request
     *
     * <code>
     *   //Returns value from $_PUT["user_email"] without sanitizing
     *   $userEmail = $request->getPut("user_email");
     * 
     *   //Returns value from $_PUT["user_email"] with sanitizing
     *   $userEmail = $request->getPut("user_email", "email");
     * </code>
     */
    public function getPut($name=NULL, $filters=NULL, $defaultValue=NULL,
            $notAllowEmpty=FALSE, $noRecursive=FALSE) {
        $put = $this->_putCache;

        if (!is_array($put)) {
            parse_str($this->getRawBody(), $put);
            $this->_putCache = $put;
        }

        return $this->getHelper($put, $name, $filters, $defaultValue,
                $notAllowEmpty, $noRecursive);
    }

    /**
     * Gets a variable from the $_REQUEST superglobal applying filters if needed.
     * If no parameters are given the $_REQUEST superglobal is returned
     *
     * <code>
     *   //Returns value from $_REQUEST["user_email"] without sanitizing
     *   $userEmail = $request->get("user_email");
     * 
     *   //Returns value from $_REQUEST["user_email"] with sanitizing
     *   $userEmail = $request->get("user_email", "email");
     * </code>
     */
    public function get($name=NULL, $filters=NULL, $defaultValue=NULL,
            $notAllowEmpty=FALSE, $noRecursive=FALSE) {
        return $this->getHelper($_REQUEST, $name, $filters, $defaultValue,
                $notAllowEmpty, $noRecursive);
    }

    /**
     * Gets variable from $_SERVER superglobal
     */
    public function getServer($name) {
        if (isset($_SERVER[$name])) {
            return $_SERVER[$name];
        }
        return NULL;
    }

    /**
     * Checks whether $_GET superglobal has certain index
     */
    public function hasQuery($name) {
        return isset($_GET[$name]);
    }

    /**
     * Checks whether $_POST superglobal has certain index
     */
    public function hasPost($name) {
        return isset($_POST[$name]);
    }

    /**
     * Checks whether the PUT data has certain index
     */
    public function hasPut($name) {
        $put = $this->getPut();
        return isset($put[$name]);
    }

    /**
     *   * Checks whether $_REQUEST superglobal has certain index
     */
    public function has($name) {
        return isset($_REQUEST[$name]);
    }

    /**
     *   * Checks whether $_SERVER superglobal has certain index
     */
    public function hasServer($name) {
        return isset($_SERVER[$name]);
    }

    /**
     * Gets HTTP header from request data
     */
    public function getHeader($header) {
        $name = strtoupper(strtr(header, '-', '_'));

        if (isset($_SERVER[$name])) {
            return $_SERVER[$name];
        }

        if (isset($_SERVER['HTTP_' . $name])) {
            return $_SERVER['HTTP_' . $name];
        }

        return '';
    }

    /**
     * Gets HTTP schema (http/https)
     */
    public function getScheme() {
        $https = $this->getServer('HTTPS');
        return ($https && $https != 'off') ? 'https' : 'http';
    }

    /**
     * Checks whether request has been made using ajax
     */
    public function isAjax() {
        return isset($_SERVER['HTTP_X_REQUESTED_WITH']) &&
                'XMLHttpRequest' == $_SERVER['HTTP_X_REQUESTED_WITH'];
    }

    /**
     * Gets HTTP raw request body
     */
    public function getRawBody() {
        if (empty($this->_rawBody)) {
            $contents = file_get_contents('php://input');

            // We need store the read raw body because it can't be read again
            $this->_rawBody = $contents;
        }
        return $this->_rawBody;
    }

    /**
     * Gets decoded JSON HTTP raw request body
     */
    public function getJsonRawBody($associative=FALSE) {
        $rawBody = $this->getRawBody();
        if (!is_string($rawBody)) {
            return FALSE;
        }
        return json_decode($rawBody, $associative);
    }

    /**
     * Gets active server address IP
     */
    public function getServerAddress() {
        if (isset($_SERVER['SERVER_ADDR'])) {
            return $_SERVER['SERVER_ADDR'];
        }
        return gethostbyname('localhost');
    }

    /**
     * Gets active server name
     */
    public function getServerName() {
        if (isset($_SERVER['SERVER_NAME'])) {
            return $_SERVER['SERVER_NAME'];
        }
        return 'localhost';
    }

    /**
     * Gets host name used by the request.
     *
     * `Request::getHttpHost` trying to find host name in following order:
     *
     * - `$_SERVER['HTTP_HOST']`
     * - `$_SERVER['SERVER_NAME']`
     * - `$_SERVER['SERVER_ADDR']`
     *
     * Optionally `Request::getHttpHost` validates and clean host name.
     * The `Request::$_strictHostCheck` can be used to validate host name.
     *
     * Note: validation and cleaning have a negative performance impact because they use regular expressions.
     *
     * <code>
     * use Phalcon\Http\Request;
     *
     * $request = new Request;
     *
     * $_SERVER['HTTP_HOST'] = 'example.com';
     * $request->getHttpHost(); // example.com
     *
     * $_SERVER['HTTP_HOST'] = 'example.com:8080';
     * $request->getHttpHost(); // example.com:8080
     *
     * $request->setStrictHostCheck(TRUE);
     * $_SERVER['HTTP_HOST'] = 'ex=am~ple.com';
     * $request->getHttpHost(); // UnexpectedValueException
     *
     * $_SERVER['HTTP_HOST'] = 'ExAmPlE.com';
     * $request->getHttpHost(); // example.com
     * </code>
     */
    public function getHttpHost() {
        // Get the server name from _SERVER['HTTP_HOST']
        $host = $this->getServer('HTTP_HOST');
        if (!$host) {

            // Get the server name from _SERVER['SERVER_NAME']
            $host = $this->getServer('SERVER_NAME');
            if (!$host) {
                // Get the server address from _SERVER['SERVER_ADDR']
                $host = $this->getServer('SERVER_ADDR');
            }
        }

        if ($host && $this->_strictHostCheck) {
            // Cleanup. Force lowercase as per RFC 952/2181
            $host = strtolower(trim($host));
            if (strpos($host, ':') !== FALSE) {
                $host = preg_replace('/:[[:digit:]]+$/', '', $host);
            }

            /**
             * Host may contain only the ASCII letters 'a' through 'z'
             * (in a case-insensitive manner),
             * the digits '0' through '9', and the hyphen ('-') as per RFC 952/2181
             */
            if ('' != preg_replace('/[a-z0-9-]+\.?/', '', $host)) {
                throw new \UnexpectedValueException('Invalid host ' . $host);
            }
        }
        return $host;
    }

    /**
     * Sets if the `Request::getHttpHost` method must be use strict validation of host name or not
     */
    public function setStrictHostCheck($flag=TRUE) {
        $this->_strictHostCheck = $flag;
        return $this;
    }

    /**
     * Checks if the `Request::getHttpHost` method will be use strict validation of host name or not
     */
    public function isStrictHostCheck() {
        return $this->_strictHostCheck;
    }

    /**
     * Gets information about the port on which the request is made.
     */
    public function getPort() {
        // Get the server name from _SERVER['HTTP_HOST']
        $host = $this->getServer('HTTP_HOST');
        if ($host) {
            if (strpos($host, ':') !== FALSE) {
                $pos = strrpos($host, ':');

                if (FALSE !== $pos) {
                    return intval(substr($host, $pos + 1));
                }
            }

            return 'https' == $this->getScheme() ? 443 : 80;
        }

        return intval($this->getServer('SERVER_PORT'));
    }

    /**
     * Gets HTTP URI which request has been made
     */
    public function getURI() {
        if (isset($_SERVER['REQUEST_URI'])) {
            return $_SERVER['REQUEST_URI'];
        }
        return '';
    }

    /**
     * Gets most possible client IPv4 Address. This method search in _SERVER['REMOTE_ADDR'] and optionally in _SERVER['HTTP_X_FORWARDED_FOR']
     */
    public function getClientAddress($trustForwardedHeader=FALSE) {
        // Proxies uses this IP
        if ($trustForwardedHeader) {
            if (isset($_SERVER['HTTP_X_FORWARDED_FOR'])) {
                $address = $_SERVER['HTTP_X_FORWARDED_FOR'];
            } else if (isset($_SERVER['HTTP_CLIENT_IP'])) {
                $address = $_SERVER['HTTP_CLIENT_IP'];
            }
        }

        if (!isset($address)) {
            if (isset($_SERVER['REMOTE_ADDR'])) {
                $address = $_SERVER['REMOTE_ADDR'];
            }
        }

        if (is_string($address)) {
            if (strpos($address, ',') !== FALSE) {
                // The client address has multiples parts, only return the first part
                return explode(',', $address)[0];
            }
            return $address;
        }

        return FALSE;
    }

    /**
     * Gets HTTP user agent used to made the request
     */
    public function getUserAgent() {
        if (isset($_SERVER['HTTP_USER_AGENT'])) {
            return $_SERVER['HTTP_USER_AGENT'];
        }
        return '';
    }

    /**
     * Gets HTTP method which request has been made
     *
     * If the X-HTTP-Method-Override header is set, and if the method is a POST,
     * then it is used to determine the 'real' intended HTTP method.
     *
     * The _method request parameter can also be used to determine the HTTP method,
     * but only if setHttpMethodParameterOverride(true) has been called.
     *
     * The method is always an uppercased string.
     */
    public function getMethod() {
        $returnMethod = '';
        if (isset($_SERVER['REQUEST_METHOD'])) {
            $returnMethod = $_SERVER['REQUEST_METHOD'];
        }

        if ('POST' == $requestMethod) {
            $headers = $this->getHeaders();
            if (isset($headers['X-HTTP-METHOD-OVERRIDE'])) {
                $returnMethod = $headers['X-HTTP-METHOD-OVERRIDE'];
            } else if ($this->_httpMethodParameterOverride) {
                if (isset($_REQUEST['_method'])) {
                    $returnMethod = $_REQUEST['method'];
                }
            }
        }

        if (!$this->isValidHttpMethod($returnMethod)) {
            $returnMethod = 'GET';
        }
        return strtoupper($returnMethod);
    }

    /**
     * Checks if a method is a valid HTTP method
     */
    public function isValidHttpMethod($method) {
        switch (strtoupper($method)) {
            case 'GET':
            case 'POST':
            case 'PUT':
            case 'DELETE':
            case 'HEAD':
            case 'OPTIONS':
            case 'PATCH':
            case 'PURGE': // Squid and Varnish support
            case 'TRACE':
            case 'CONNECT':
                return TRUE;
        }
        return FALSE;
    }

    /**
     * Check if HTTP method match any of the passed methods
     * When strict is true it checks if validated methods are real HTTP methods
     */
    public function isMethod($methods, $strict=FALSE) {
        $httpMethod = $this->getMethod();

        if (is_string($methods)) {
            if ($strict && !$this->isValidHttpMethod($methods)) {
                throw new Exception('Invalid HTTP method: ' . $methods);
            }
            return $methods == $httpMethod;
        }

        if (is_array($methods)) {
            foreach ($methods as $method) {
                if ($this->isMethod($method, $strict)) {
                    return TRUE;
                }
            }
            return FALSE;
        }

        if ($strict) {
            throw new Exception('Invalid HTTP method: non-string');
        }
        return FALSE;
    }

    /**
     * Checks whether HTTP method is GET. if _SERVER['REQUEST_METHOD']==='GET'
     */
    public function isGet() {
        return 'GET' == $this->getMethod();
    }

    /**
     * Checks whether HTTP method is POST. if _SERVER['REQUEST_METHOD']==='POST'
     */
    public function isPost() {
        return 'POST' == $this->getMethod();
    }

    /**
     * Checks whether HTTP method is PUT. if _SERVER['REQUEST_METHOD']==='PUT'
     */
    public function isPut() {
        return 'PUT' == $this->getMethod();
    }

    /**
     * Checks whether HTTP method is DELETE. if _SERVER['REQUEST_METHOD']==='DELETE'
     */
    public function isDelete() {
        return 'DELETE' == $this->getMethod();
    }

    /**
     * Checks whether HTTP method is HEAD. if _SERVER['REQUEST_METHOD']==='HEAD'
     */
    public function isHead() {
        return 'HEAD' == $this->getMethod();
    }

    /**
     * Checks whether HTTP method is OPTIONS. if _SERVER['REQUEST_METHOD']==='OPTIONS'
     */
    public function isOptions() {
        return 'OPTIONS' == $this->getMethod();
    }

    /**
     * Checks whether HTTP method is PATCH. if _SERVER['REQUEST_METHOD']==='PATCH'
     */
    public function isPatch() {
        return 'PATCH' == $this->getMethod();
    }

    /**
     * Checks whether HTTP method is PURGE. if _SERVER['REQUEST_METHOD']==='PURGE'
     */
    public function isPurge() {
        return 'PURGE' == $this->getMethod();
    }

    /**
     * Checks whether HTTP method is TRACE. if _SERVER['REQUEST_METHOD']==='TRACE'
     */
    public function isTrace() {
        return 'TRACE' == $this->getMethod();
    }

    /**
     * Checks whether HTTP method is CONNECT. if _SERVER['REQUEST_METHOD']==='CONNECT'
     */
    public function isConnect() {
        return 'CONNECT' == $this->getMethod();
    }

}
