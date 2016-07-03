(ns freestuffly.scraper
  (:require [clj-http.client :as client])
  (:require [hickory.core :as h])
  (:require [clojure.string :as string])
  (:require [clojure.set :as cset])
  (:require [hickory.select :as s]))

(def interesting-keywords
  #"(?i)paint|playstation|camping|emulsion|lamp|ladder|roller|xbox|mac|fish|marine|kitchen")

(defn interesting-finds
  [results]
  (cset/select
    (fn [result]
      (re-find (re-matcher interesting-keywords (first (:content (into {} result))))))
    (set results)))

(def my-group-urls "https://groups.freecycle.org/group/southwark-freecycle/posts/offer?page=1&resultsperpage=50&showall=off&include_offers=off&include_wanteds=off&include_receiveds=off&include_takens=off")

(def site-tree
  (-> (client/get my-group-urls) :body h/parse h/as-hickory))

(defn parsed-html
  [html]
  (-> (s/select
        (s/descendant
          (s/id "group_posts_table")
          (s/and (s/tag :tr))
          (s/and (s/tag :td) (s/nth-child 2))
          (s/and (s/tag :a) (s/nth-child 1)))
      html)))

(defn rubbish-value?
  [content-values]
  (not= (content-values :content) ["See details"]))

(defn content-for
  [html-vector]
  (filter rubbish-value? (map #(select-keys % [:attrs :content]) html-vector)))

(defn presentable
  [results]
  (string/join "\n\n" results))

(def scraped-content (presentable (interesting-finds (content-for (parsed-html site-tree)))))
