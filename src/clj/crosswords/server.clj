(ns crosswords.server
  (:gen-class))

(defn require-and-resolve
  [sym]
  (require (symbol (namespace sym)))
  (resolve sym))

(defn -main
  [& args]
  (let [init  (require-and-resolve 'crosswords.system/new-system)
        start (require-and-resolve 'com.stuartsierra.component/start)]
    (start (init nil))))
