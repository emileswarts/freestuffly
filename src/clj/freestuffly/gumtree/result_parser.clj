(ns freestuffly.gumtree.result-parser
  (:require [hickory.core :as h]
            [clojure.set :as cset]
            [clojure.string :as string]
            [selmer.parser :as parser]
            [clj-yaml.core :as yaml]
            [hickory.select :as s]))

(def ^:private config (yaml/parse-string (slurp "config/gumtree.yml")))

(defn- interesting-keywords [] (:keywords config))

(defn- interesting-keywords-regex [] (re-pattern (str "(?i)" (string/join "|" (interesting-keywords)))))

(defn- interesting-finds
  [results]
  (cset/select
    (fn [result]
      (re-find
        (re-matcher (interesting-keywords-regex)
                    (first (:content (into {} result))))))
    (set results)))

(defn- site-tree
  [scraped-html]
  (h/as-hickory (h/parse scraped-html)))

(defn- parsed-html
  [html]
  (-> (s/select
        (s/descendant
          (s/id "group_posts_table")
          (s/and (s/tag :tr))
          (s/and (s/tag :td) (s/nth-child 2))
          (s/and (s/tag :a) (s/nth-child 1)))
      html)))

(defn- interesting-value?
  [content-values]
  (not= (content-values :content) ["See details"]))

(defn- content-for
  [html-vector]
  (filter interesting-value? (map #(select-keys % [:attrs :content]) html-vector)))

(defn- presentable
  [results]
  (str "FOUND\n\n\n\n"
    (parser/render-file "/email/gumtree/results.html" {:results (string/join "\n\n\n" (map vals results))})))

(defn parsed
  [scraped-html]
  (presentable
    (interesting-finds
      (content-for
        (parsed-html
          (site-tree scraped-html))))))
