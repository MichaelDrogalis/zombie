(ns zombie.core
  (:require [clj-time.core :as time]))

(defmacro is-like [description & disparity]
  `(-> ~description ~@disparity))

(defmacro but-it [description & disparities]
  `(-> ~description ~@disparities))

(defprotocol Differentiate
  (birth-a-different [this description attribute])
  (zero-out [this description attribute]))

(extend-type Number
  Differentiate
  (birth-a-different
   [this description attribute]
   (assoc description attribute (inc (attribute description))))
  (zero-out
   [this description attribute]
   (assoc description attribute 0)))

(extend-type String
  Differentiate
  (birth-a-different
   [this description attribute]
   (assoc description attribute (str (attribute description) "x")))
  (zero-out
   [this description attribute]
   (assoc description attribute "")))

(extend-type clojure.lang.PersistentVector
  Differentiate
  (zero-out
   [this description attribute]
   (assoc description attribute []))) 

(defn has-a-different [description attribute]
  (birth-a-different (attribute description) description attribute))

(defn has-no [description attribute]
  (zero-out (attribute description) description attribute))

(defn has-a-nil [description attribute]
  (assoc description attribute nil))

(defn has-a [description attribute value]
  (assoc description attribute value))

(defn has-an-earlier [description attribute]
  (assoc description attribute
         (time/minus (attribute description) (time/days 1))))

(defn has-a-later [description attribute]
  (assoc description attribute
         (time/plus (attribute description) (time/days 1))))

