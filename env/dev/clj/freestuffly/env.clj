(ns freestuffly.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [freestuffly.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[freestuffly started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[freestuffly has shut down successfully]=-"))
   :middleware wrap-dev})
