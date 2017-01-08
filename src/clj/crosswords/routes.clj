(ns crosswords.routes
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.interceptor :refer [interceptor]]
            [ring.util.response :as ring-resp]))

(def about-page
  (interceptor
   {:name ::about
    :enter (fn [ctx]
             (assoc ctx :response
                    (ring-resp/response (format "Clojure %s - served from %s"
                                                (clojure-version)
                                                (route/url-for ::about-page)))))}))

(def home-page
  (interceptor
   {:name ::home
    :enter (fn [ctx]
             (assoc ctx :response
                    (ring-resp/resource-response "index.html")))}))

(def common-interceptors [(body-params/body-params) http/html-body])

(defn init-routes
  [system]
  #{["/"      :get (conj common-interceptors home-page)]
    ["/about" :get (conj common-interceptors about-page)]})
