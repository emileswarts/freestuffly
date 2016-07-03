(ns freestuffly.notifier
  (:require [clj-http.client :as client])
  (:require [postmark.core :as p])
  (:require [freestuffly.scraper :as scraper]))

(def pm (p/postmark (System/getenv "POSTMARK_API_KEY") (System/getenv "POSTMARK_SENDER_SIGNATURE")))

(defn send-email
  [content]
  (pm {:to "emile.swarts123+heroku@gmail.com"
       :subject "Free stuffly"
       :text content}))

(defn listen
  []
  (send-email scraper/scraped-content))
