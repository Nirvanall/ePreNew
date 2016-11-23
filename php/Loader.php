<?php

class Loader {

    protected $_eventManager = NULL;
    protected $_foundPath = NULL;
    protected $_checkedPath = NULL;
    protected $_classes = NULL;
    protected $_extensions = array('php');
    protected $_namespaces = NULL;
    protected $_directories = NULL;
    protected $_files = NULL;
    protected $_registered = FALSE;

    public function setEventsManager($eventsManager) {
        $this->_eventManager = $eventsManager;
    }

    public function getEventsManager() {
        return $this->_eventManager;
    }

    /**
     * Sets an array of file extensions that the loader must try in each
     * attempt to locate the file
     */
    public function setExtensions($extensions) {
        $this->_extensions = $extensions;
    }

    /**
     * Returns the file extensions registered in the loader
     */
    public function getExtension() {
        return $this->_extensions;
    }

    /**
     * Registers namespaces and their related directories
     */
    public function registerNamespaces($namespaces, $merge=FALSE) {
        $preparedNamespaces = $this->prepareNamespace($namespaces);
        if ($merge && is_array($this->_namespaces)) {
            foreach ($preparedNamespaces as $name => $paths) {
                if (!isset($this->_namespaces[$name])) {
                    $this->_namespaces[$name] = array();
                }
            }
            $this->_namespaces[$name] = array_merge(
                    $this->_namespaces[$name], $paths);
        } else {
            $this->_namespaces = $preparedNamespaces;
        }
        return $this;
    }

    protected function prepareNamespace($namespaces) {
        $prepared = array();
        foreach ($namespaces as $name => $paths) {
            if (is_array($paths)) {
                $prepared[$name] = $paths;
            } else {
                $prepared[$name] = array($paths);
            }
        }
        return $prepared;
    }

    /**
     * Returns the namespaces currently registered in the autoloader
     */
    public function getNamespaces() {
        return $this->_namespaces;
    }

    /**
     * Registers directories in which "not found" classes could be found
     */
    public function registerDirs($directories, $merge=FALSE) {
        if ($merge && is_array($this->_directories)) {
            $this->_directories = array_merge($this->_directories, $directories);
        } else {
            $this->_directories = $directories;
        }
        return $this;
    }

    /**
     * Returns the directories currently registered in the autoloader
     */
    public function getDirs() {
        return $this->_directories;
    }

    /**
     * Registers files that are "non-classes" hence need a "require"
     * This is very useful for including files that only have functions
     */
    public function registerFiles($files, $merge=FALSE) {
        if ($merge && is_array($this->_files)) {
            $this->_files = array_merge($this->_files, $files);
        } else {
            $this->_files = $files;
        }
        return $this;
    }

    /**
     * Returns the files currently registered in the autoloader
     */
    public function getFiles() {
        return $this->_files;
    }

    /**
     * Registers classes and their locations
     */
    public function registerClasses($classes, $merge=FALSE) {
        if ($merge && is_array($this->_classes)) {
            $this->_classes = array_merge($this->_classes, $classes);
        } else {
            $this->_classes = $classes;
        }
        return $this;
    }

    /**
     * Returns the class map currently registered in the autoloader
     */
    public function getClasses() {
        return $this->classes;
    }

    /**
     * Registers the autoload method
     */
    public function register() {
        if ($this->_registered === FALSE) {
            // Loads individual files added using Loader->registerFiles()
            $this->loadFiles();

            // Registers directories and namespaces to PHP's autoload
            spl_autoload_register(array($this, 'autoload'));

            $this->_registered = TRUE;
        }
        return $this;
    }

    /**
     * Unregister the autoload method
     */
    public function unregister() {
        if ($this->_registered === TRUE) {
            spl_autoload_unregister(array($this, 'autoload'));
            $this->_registered = FALSE;
        }
        return $this;
    }

    /**
     * Checks if a file exists and then adds the file by doing virtual require
     */
    public function loadFiles() {
        if (is_array($this->_files)) {
            foreach ($this->_files as $filePath) {
                if (is_object($this->_eventManager)) {
                    $this->_checkedPath = $filePath;
                    $this->_eventManager->fire('loader:beforeCheckPath',
                            $this, $filePath);
                }

                // Check if the file specified exists
                if (is_file($filePath)) {
                    if (is_object($this->_eventManager)) {
                        $this->_foundPath = $filePath;
                        $this->_eventManager->fire('loader:pathFound',
                                $this, $filePath);
                    }
                    return $filePath;
                }
            }
        }
    }

    /**
     * Autoloads the registered classes
     */
    public function autoload($className) {
        if (is_object($this->_eventManager)) {
            $this->_eventManager->fire('loader:beforeCheckClass', $this, $className);
        }

        // First we check for static paths for classes
        if (is_array($this->_classes)) {
            if (isset($this->_classes[$className])) {
                $filePath = $this->_classes[$className];
                if (is_object($this->_eventManager)) {
                    $this->_foundPath = $filePath;
                    $this->_eventManager->fire('loader:pathFound',
                            $this, $filePath);
                }
                require $filePath;
                return TRUE;
            }
        }

        $ds = DIRECTORY_SEPARATOR;
        $ns = '\\';
        if (is_array($this->_namespaces)) {
            foreach ($this->_namespaces as $nsPrefix => $directories) {
                // The class name must start with the current namespace
                if (strncmp($className, $nsPrefix, strlen($nsPrefix))) {
                    continue;
                }

                // Append the namespace seperator to the prefix
                $fileName = substr($className, strlen($nsPrefix . $ns));
                $fileName = str_replace($ns, $ds, $fileName);
                if (!$fileName) continue;

                foreach ($directories as $directory) {
                    // Add a trailing directory seperator if the user forgets to do that
                    $fixedDirectory = rtrim($directory, $ds) . $ds;
                    foreach ($this->_extensions as $extension) {
                        $filePath = $fixedDirectory . $fileName . '.' . $extension;
                        if (is_object($this->_eventManager)) {
                            $this->_checkedPath = $filePath;
                            $this->_eventManager->fire('loader:beforeCheckPath',
                                    $this, $filePath);
                        }
                        if (is_file($filePath)) {
                            if (is_object($this->_eventManager)) {
                                $this->_foundPath = $filePath;
                                $this->_eventManager->fire('loader:pathFound',
                                        $this, $filePath);
                            }
                            require $filePath;
                            return TRUE;
                        }
                    } // foreach extensions
                } // foreach directories
            } // foreach namespaces
        }

        // Change the namespace seperator by directory seperator too
        $nsClassName = str_replace('\\', $ds, $className);

        // Check in the directories
        if (is_array($this->_directories)) {
            foreach ($this->_directories as $directory) {
                // Add a trailing directory seperator if the user forgets to do that
                $fixedDirectory = rtrim($directory, $ds) . $ds;
                foreach ($this->_extensions as $extension) {
                    $filePath = $fixedDirectory . $nsClassName . '.' . $extension;
                    if (is_object($this->_eventManager)) {
                        $this->_checkedPath = $filePath;
                        $this->_eventManager->fire('loader:beforeCheckPath',
                                $this, $filePath);
                    }
                    if (is_file($filePath)) {
                        if (is_object($this->_eventManager)) {
                            $this->_foundPath = $filePath;
                            $this->_eventManager->fire('loader:pathFound',
                                    $this, $filePath);
                        }
                        require $filePath;
                        return TRUE;
                    }
                } // foreach extensions
            } // foreach directories
        }

        if (is_object($this->_eventManager)) {
            $this->_eventManager->fire('loader:afterCheckClass', $this, $className);
        }

        // Cannot find the class
        return FALSE;
    }

    /**
     * Gets the path when a class was found
     */
    public function getFoundPath() {
        return $this->_foundPath;
    }

    /**
     * Gets th path the loader is checking for a path
     */
    public function getCheckedPath() {
        return $this->_checkedPath;
    }
}
