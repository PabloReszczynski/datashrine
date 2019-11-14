(ns castle.fortune
  (:require [clj-http.client :as http]
            [datahike.api :as d]
            [clojure.edn :as edn]
            [clojure.data.json :as json]
            [twitter.oauth :as oauth]
            [twitter.api.search :refer [def-twitter-search-method]]
            [clojure.string :as string])
  (:import (java.text SimpleDateFormat)
           (java.util Locale)))

(def twitter-keys
  (-> (slurp "config.edn")
      (edn/read-string)
      :twitter))

(def-twitter-search-method
  followers
  :get "followers/list.json")

(defn get-followers
  [creds cursor]
  (:body
   (followers
    :oauth-creds creds
    :params {:screen-name "papertanuki"
             :cursor      cursor})))

(defn get-shit
  []
  (let [creds (oauth/make-oauth-creds
               (:key twitter-keys)
               (:secret twitter-keys)
               (:token twitter-keys)
               (:token-secret twitter-keys))]
    (loop [cursor -1
           users []]
      (let [response (get-followers creds cursor)]
        (if (empty? (:users response))
          users
          (recur (:next_cursor response)
                 (concat users (:users response))))))))

(def datahike-uri "datahike:mem://example")

(defn convert-key
  [base k]
  (-> k
      name
      (string/replace \_ \-)
      (#(str base "/" %))
      keyword))

(def fmt "EEE MMM dd HH:mm:ss ZZZZZ yyyy")

(defn parse-inst
  [inst]
  (let [sf (SimpleDateFormat. fmt Locale/ENGLISH)]
    (.parse sf inst)))

(defn mapkeys
  [f m]
  (into {} (map (fn [[k v]] [(f k) v]) m)))

(defn removevals
  [f m]
  (into {} (remove (fn [[_ v]] (f v)) m)))

(defn convert-keys
  [m base]
  (mapkeys (partial convert-key base) m))

(defn process-user
  [user]
  (-> user
      (convert-keys "user")
      (update-in [:user/created-at] parse-inst)
      (#(removevals nil? %))
      (dissoc :user/status :user/entities)))

(def user-schema
  [{:db/ident :user/id
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity}
   {:db/ident :user/id-str
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/screen-name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/location
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/description
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/url
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/protected
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/followers-count
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/friend-count
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/listed-count
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/created-at
    :db/valueType :db.type/instant
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/favourites-count
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/utc-offset
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/time-zone
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/geo-enabled
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/verified
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/statuses-count
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/lang
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/contributors-enabled
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/is-translator
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/is-translation-enabled
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/profile-background-color
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/profile-background-image-url
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/profile-background-image-url-https
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/profile-background-tile
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/profile-image-url
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/profile-image-url-https
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/profile-banner-url
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/profile-link-color
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/profile-sidebar-border-color
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/profile-sidebar-fill-color
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/profile-text-color
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/profile-use-background-image
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/has-extended-profile
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/default-profile
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/default-profile-image
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/following
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/follow-request-send
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/notifications
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/translator-type
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/can-media-tag
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/live-following
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/blocked-by
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/blocking
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/followed-by
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/follow-request-sent
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/muting
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one}
   {:db/ident :user/friends-count
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one}])

(defn create-db []
  (do (d/create-database datahike-uri)
      (let [conn (d/connect datahike-uri)]
        (d/transact conn user-schema))))

(defn simple-query
  [conn]
  (d/q '[:find ?e ?sn ?d ?n
         :where [?e :user/name ?n]
         [?e :user/screen-name ?sn]
         [?e :user/created-at ?d]]
       @conn))
