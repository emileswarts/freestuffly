(ns freestuffly.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[freestuffly started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[freestuffly has shut down successfully]=-"))
   :middleware identity})
