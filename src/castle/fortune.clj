(ns castle.fortune
  (:require [clj-http.client :as http]
            [datahike.api :as d]))

(def twitter-api-url "")
(def datahike-uri "datahike:mem://example")

(def user-schema
  [{:db/ident :user/id
    :db/valueType :db.type/long
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
   {:db/ident :user/get-enabled
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
    :db/cardinality :db.cardinality/one}])

(defn create-db []
  (do (d/create-database datahike-uri)
      (let [conn (d/connect datahike-uri)]
        (d/transact conn user-schema))))

(defn get-data
  [url]
  [http/get url])

