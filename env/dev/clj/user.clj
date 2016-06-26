(ns user
  (:require [mount.core :as mount]
            freestuffly.core))

(defn start []
  (mount/start-without #'freestuffly.core/repl-server))

(defn stop []
  (mount/stop-except #'freestuffly.core/repl-server))

(defn restart []
  (stop)
  (start))


