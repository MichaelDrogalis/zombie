(ns zombie.core
  (:require [clj-time.core :as time]))

(defmacro is-like [description & disparity]
  "Produce a new piece of data relaltive to description."
  `(-> ~description ~@disparity))

(defmacro but-it [description & disparities]
  "Articulate the differences between the two pieces of data."
  `(-> ~description ~@disparities))

(defprotocol Differentiate
  "Protocol for how to birth new values from old ones"
  (birth-a-different [this description attribute])
  (empty-value-for [this description attribute]))

(extend-type Number
  Differentiate
  (birth-a-different
   [this description attribute]
   (assoc description attribute (inc (attribute description))))
  (empty-value-for
   [this description attribute]
   (assoc description attribute 0)))

(extend-type String
  Differentiate
  (birth-a-different
   [this description attribute]
   (assoc description attribute (str (attribute description) "x")))
  (empty-value-for
   [this description attribute]
   (assoc description attribute "")))

(extend-type clojure.lang.PersistentVector
  Differentiate
  (empty-value-for
   [this description attribute]
   (assoc description attribute []))) 

(defn has-a-different [description attribute]
  "Create a new piece of data with a different attribute. Accepts Numbers and Strings."
  (birth-a-different (attribute description) description attribute))

(defn has-a-smaller [description attribute]
  "Create a new piece of data with a smaller numeric value for attribute."
  (assoc description attribute (dec (attribute description))))

(defn has-no [description attribute]
  "Create a new piece of data with an empty value for attribute. Empty string for strings,
   zero for numbers, and [] for vectors."
  (empty-value-for (attribute description) description attribute))

(defn has-a-nil [description attribute]
  "Create a new piece of data with nil for the attribute."
  (assoc description attribute nil))

(defn has-a [description attribute value]
  "Create a new piece of data with value for attribute."
  (assoc description attribute value))

(defn birth-with-new-time [description attribute f]
  (assoc description attribute
         (f (attribute description) (time/days 1))))

(defn has-an-earlier [description attribute]
  "Create a new piece of data with earlier DateTime for attribute (further in past)."
  (birth-with-new-time description attribute time/minus))

(defn has-a-later [description attribute]
  "Create a new piece of data with later DateTime for attribute (further in future)."
  (birth-with-new-time description attribute time/plus))

(defn generated-exprs [exprs]
  (vec (concat exprs (vec ['all (into [] (take-nth 2 (drop 1 exprs)))]))))

(defmacro specified-by [exprs & body]
  "Given a vector of pairs ([a b c d]), gives access to a var called 'all'. Useful for
   handling anonymously named pieces of data, often called '_'."
  `(let ~(generated-exprs exprs) ~@body))

