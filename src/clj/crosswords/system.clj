(ns crosswords.system
  (:require [com.stuartsierra.component :as component]
            [crosswords.pedestal :refer [new-pedestal]]
            [crosswords.routes :refer [init-routes]]
            [io.pedestal.http :as http]))

(defn service-map
  [config]
  {
   ;; You can bring your own non-default interceptors. Make
   ;; sure you include routing and set it up right for
   ;; dev-mode. If you do, many other keys for configuring
   ;; default interceptors will be ignored.
   ;; ::http/interceptors []

   ;; Uncomment next line to enable CORS support, add
   ;; string(s) specifying scheme, host and port for
   ;; allowed source(s):
   ;;
   ;; "http://localhost:8080"
   ;;
   ;;::http/allowed-origins ["scheme://host:port"]

   :env (get config :environment :dev)

   ::http/port (get config :http-port 8080)

   ;; Root for resource interceptor that is available by default.
   ::http/resource-path "/public"

   ;; Either :jetty, :immutant or :tomcat (see comments in project.clj)
   ::http/type :jetty
   ;;::http/host "localhost"
   ;; Options to pass to the container (Jetty)
   ::http/container-options {:h2c? true
                             :h2? false
                                        ;:keystore "test/hp/keystore.jks"
                                        ;:key-password "password"
                                        ;:ssl-port 8443
                             :ssl? false}})

(def default-config
  {:environment :prod
   :http-port 8080})

(defn new-system
  [config]
  (let [config (merge default-config config)]
    (component/system-map
     :config       config
     :service-map (service-map config)
     :pedestal    (component/using
                   (new-pedestal init-routes)
                   [:config :service-map]))))
