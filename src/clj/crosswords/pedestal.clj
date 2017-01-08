(ns crosswords.pedestal
  (:require [com.stuartsierra.component :as component]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]))

(defmulti start* :env)

(defmethod start* :test
  [service-map]
  (-> service-map
      http/default-interceptors
      http/create-server))

(defmethod start* :dev
  [service-map]
  (-> service-map
      (merge {;; do not block thread that starts web server
              ::http/join? false
              ;; all origins are allowed in dev mode
              ::http/allowed-origins {:creds true :allowed-origins (constantly true)}})
      http/default-interceptors
      http/dev-interceptors
      http/create-server
      http/start))

(defmethod start* :default
  [service-map]
  (-> service-map
      http/default-interceptors
      http/create-server
      http/start))

(defrecord Pedestal [init-routes service-map service]

  component/Lifecycle
  (start [this]
    (if service
      this
      (let [service-map (assoc service-map ::http/routes (init-routes this))]
        (assoc this :service-map service-map :service (start* service-map)))))

  (stop [this]
    (when (and service (not= :test (:env service-map)))
      (http/stop service))
    (assoc this :service nil)))

(defn new-pedestal
  [init-routes-fn]
  (map->Pedestal {:init-routes init-routes-fn}))
