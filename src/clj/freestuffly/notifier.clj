(ns freestuffly.notifier
  (:require [clj-http.client :as client]))

(def my-group-urls "https://groups.freecycle.org/group/southwark-freecycle/posts/offer?page=1&resultsperpage=100&showall=off&include_offers=off&include_wanteds=off&include_receiveds=off&include_takens=off")

(defn listen
  []
(println (client/get my-group-urls)))
