(ns freestuffly.notifier
  (:require [clj-http.client :as client])
  (:require [postmark.core :as p])
  (:require [freestuffly.scraper :as scraper]))

(def ^{:private true} my-email
  "emile.swarts123+heroku@gmail.com")

(def ^{:private true} subject
  "Free stuffly finds")

(def ^{:private true} postmark
  (p/postmark
    (System/getenv "POSTMARK_API_KEY")
    (System/getenv "POSTMARK_SENDER_SIGNATURE")))

(defn- send-email
  [content]
  (postmark {:to my-email
             :subject subject
             :text content}))

(defn listen
  []
  (send-email scraper/scraped-content))
