(ns zombie.core
  (:require [clj-time.core :as time]))

(defmacro is-like [description & disparity]
  "Produce a new piece of data relaltive to description."
  `(-> ~description ~@disparity))

(defmacro but-it [description & disparities]
  "Articulate the differences between the two pieces of data."
  `(-> ~description ~@disparities))

(defmacro but-he [description & disparities]
  `(but-it ~description ~@disparities))

(defmacro but-she [description & disparities]
  `(but-it ~description ~@disparities))

(defprotocol Morph
  "Protocol for how to birth new values from old ones"
  (morph [this description attribute])
  (identity-element [this description attribute]))

(extend-type Number
  Morph
  (morph
   [this description attribute]
   (assoc description attribute (inc (attribute description))))
  (identity-element
   [this description attribute]
   (assoc description attribute 0)))

(extend-type String
  Morph
  (morph
   [this description attribute]
   (assoc description attribute (str (attribute description) "x")))
  (identity-element
   [this description attribute]
   (assoc description attribute "")))

(extend-type clojure.lang.PersistentVector
  Morph
  (identity-element
   [this description attribute]
   (assoc description attribute []))) 

(defn has-a-different [description attribute]
  "Create a new piece of data with a different attribute. Accepts Numbers and Strings."
  (morph (attribute description) description attribute))

(defn has-a-smaller [description attribute]
  "Create a new piece of data with a smaller numeric value for attribute."
  (assoc description attribute (+
                                (Integer/MIN_VALUE)
                                (int (* (rand)
                                        (- (attribute description)
                                           (Integer/MIN_VALUE)))))))

(defn has-a-lesser [description attribute]
  (has-a-smaller description attribute))

(defn has-no [description attribute]
  "Create a new piece of data with an empty value for attribute. Empty string for strings,
   zero for numbers, and [] for vectors."
  (identity-element (attribute description) description attribute))

(defn has-a-nil [description attribute]
  "Create a new piece of data with nil for the attribute."
  (assoc description attribute nil))

(defn has-a [description attribute value]
  "Create a new piece of data with value for attribute."
  (assoc description attribute value))

(defn birth-with-new-time [description attribute f units]
  (assoc description attribute (f (attribute description) (units 1))))

(defn has-an-earlier [description attribute]
  "Create a new piece of data with an arbitrarily earlier DateTime for attribute (further in past)."
  (birth-with-new-time description attribute time/minus time/months))

(defn has-a-later [description attribute]
  "Create a new piece of data with an arbitrarily later DateTime for attribute (further in future)."
  (birth-with-new-time description attribute time/plus time/months))

(defn has-one-day-later [description attribute]
  "Create a new piece of data with attribute one day in the future than it currently is."
  (birth-with-new-time description attribute time/plus time/days))

(defn has-one-week-later [description attribute]
  "Create a new piece of data with attribute one week in the future than it currently is."
  (birth-with-new-time description attribute time/plus time/weeks))

(defn generated-exprs [exprs]
  (vec (concat exprs (vec ['all (into [] (take-nth 2 (drop 1 exprs)))]))))

(defmacro spawn
  "Given a vector of pairs ([a b c d]), gives access to a var called 'all'. Useful for
   handling anonymously named pieces of data, often called '_'."
  [exprs & body]
  `(let ~(generated-exprs exprs) ~@body))

(defmacro mass-spawn [{:keys [n] :or {n 1} :as options} exprs & body]
  `(dotimes [_# ~n]
     (spawn ~exprs ~@body)))
  