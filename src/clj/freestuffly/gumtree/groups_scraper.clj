(ns freestuffly.gumtree.groups-scraper
  (:require [clj-yaml.core :as yaml])
  (:require [clj-http.client :as client])
  (:require [clojure.string :as string])
  (:require [freestuffly.gumtree.result-parser :as result-parser]))

(def ^:private config (yaml/parse-string (slurp "config/gumtree.yml")))

(def ^:private freecycle-groups (:groups config))

(def ^:private freecycle-results-per-page (:per_page config))

(defn to-url
  [group]
  (str "https://groups.freecycle.org/group/"
       group
       "/posts/offer?page=1&resultsperpage="
       freecycle-results-per-page
       "&showall=off&include_offers=off&include_wanteds=off&include_receiveds=off&include_takens=off"))

(def my-group-urls
  (map to-url freecycle-groups))

(defn groups-content
  []
  (map
    (fn [group-url]
      (println (str "processing" group-url))
      (result-parser/parsed (:body (client/get group-url))))
    my-group-urls))

(defn results [] (string/join " \n "  (groups-content)))
